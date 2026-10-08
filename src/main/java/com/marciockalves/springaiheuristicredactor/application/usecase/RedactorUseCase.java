package com.marciockalves.springaiheuristicredactor.application.usecase;

import com.marciockalves.springaiheuristicredactor.application.dto.RedactorRequestDTO;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.infrastructure.tool.RedactorTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Service
public class RedactorUseCase {

    private static final Logger log = LoggerFactory.getLogger(RedactorUseCase.class);

    private final ChatClient chatClient;
    private final Map<ModelRedactor, Resource> promptTemplateStrategy;
    private final RedactorTools redactorTools;

    public RedactorUseCase(ChatClient.Builder chatClientBuilder,
                           Map<ModelRedactor, Resource> promptTemplateStrategy,
                           RedactorTools redactorTools,
                           ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.promptTemplateStrategy = promptTemplateStrategy;
        this.redactorTools = redactorTools;
    }

    public Flux<String> execute(RedactorRequestDTO request) {
        log.info("Iniciando processo de redação para o usuário: {} usando o modelo: {}",
                request.getUsername(), request.getModelRedactor());

        boolean hasText = request.getContentText() != null && !request.getContentText().isBlank();
        boolean hasOrientation = request.getOrientation() != null && !request.getOrientation().isBlank();

        if (!hasText && !hasOrientation) {
            log.warn("Tentativa de requisição rejeitada: tanto o texto quanto a orientação estão vazios.");
            return Flux.error(new IllegalArgumentException("Você precisa fornecer um texto (contentText) ou uma orientação (orientation)."));
        }

        Resource templateResource = promptTemplateStrategy.get(request.getModelRedactor());

        if (templateResource == null) {
            log.error("Modelo de redator não mapeado: {}", request.getModelRedactor());
            return Flux.error(new IllegalArgumentException("Modelo de redator não mapeado: " + request.getModelRedactor()));
        }

        String chatId = request.getUsername() != null ? request.getUsername() : "default-user";

        String contentToProcess = hasText ? request.getContentText() : "[Usar contexto anterior da memória]";
        String orientation = hasOrientation ? request.getOrientation() : "Padrão";
        String userMessage = hasOrientation ? request.getOrientation() : "Aplique as modificações necessárias.";
        String determinedCategory = hasOrientation
                ? "ORIENTED"
                : "DRAFT";

        Map<String, Object> toolContextMap = Map.of(
                "userName", request.getUsername() != null ? request.getUsername() : "default-user",
                "title", request.getTitle() != null ? request.getTitle() : "Sem Título",
                "category", determinedCategory,
                "modelRedactorStr", request.getModelRedactor().name()
        );


        return chatClient.prompt()
                .system(promptSpec -> promptSpec
                        .text(templateResource)
                        .param("contentText", contentToProcess)
                        .param("orientation", orientation)
                )
                .user(userMessage)
                .tools(redactorTools)
                // Força ou instrui o modelo a utilizar a ferramenta de salvamento quando concluir
                .toolContext(toolContextMap)
                .advisors(advisorSpec -> advisorSpec.param("chat_memory_conversation_id", chatId))
                .stream()
                .content()
                .doOnSubscribe(subscription -> log.info("🔌 Conexão com o provedor de IA estabelecida."))
                .collectList()
                .doOnSuccess(chunksList -> {
                    String respostaCompleta = String.join("", chunksList);
                    log.info("📝 [RESPOSTA COMPLETA DA IA]:\n--------------------\n{}\n--------------------", respostaCompleta);
                })
                .flatMapMany(Flux::fromIterable)
                .doOnError(error -> log.error("❌ Erro no stream da IA: ", error))
                .subscribeOn(Schedulers.boundedElastic());
    }
}