package com.marciockalves.springaiheuristicredactor.application.usecase;

import com.marciockalves.springaiheuristicredactor.application.dto.RedactorRequestDTO;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class RedactorUseCase {

    private final Map<ModelRedactor, Resource> promptTemplateStrategy;
    private final ChatClient chatClient;

    public RedactorUseCase(Map<ModelRedactor, Resource> promptTemplateStrategy, ChatClient.Builder chatClientBuilder) {
        this.promptTemplateStrategy = promptTemplateStrategy;
        // Constrói o ChatClient padrão configurado pelo Spring AI Ollama no application.yaml
        this.chatClient = chatClientBuilder.build();
    }

    public String execute(RedactorRequestDTO request) {
        log.info("==================================================");
        log.info(" [COOKBOOK] Iniciando processamento de redação com IA...");
        log.info("--------------------------------------------------");
        log.info(" Autor / Usuário     : {}", request.getUsername());
        log.info(" Título              : {}", request.getTitle());
        log.info(" Estilo Selecionado  : {}", request.getModelRedactor());

        boolean hasOrientation = Optional.ofNullable(request.getOrientation())
                .map(s -> !s.isBlank())
                .orElse(false);

        ModelTarget targetMode = hasOrientation ? ModelTarget.ORIENTED : ModelTarget.DRAFT;
        log.info(" Modo Detectado      : {}", targetMode);

        String orientationValue = hasOrientation ? request.getOrientation() : "Nenhuma orientação extra fornecida. Atue em modo rascunho livre.";

        // 1. Recupera o Resource do Strategy Pattern
        Resource promptResource = promptTemplateStrategy.get(request.getModelRedactor());
        if (promptResource == null) {
            throw new IllegalArgumentException("Nenhum template encontrado para o estilo: " + request.getModelRedactor());
        }

        log.info(" Template Carregado  : filename = {}", promptResource.getFilename());
        log.info(" Texto Original      : {}", request.getContentText());

        // 2. Cria o PromptTemplate do Spring AI combinando o arquivo .st externo e as variáveis
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        Prompt prompt = promptTemplate.create(Map.of(
                "text", request.getContentText(),
                "orientation", orientationValue
        ));

        log.info(" Enviando prompt para o modelo Ollama...");

        // 3. Executa a chamada ao LLM e captura o texto gerado
        String redactedContent = chatClient.prompt(prompt)
                .call()
                .content();

        log.info(" Resposta gerada com sucesso pela IA!");
        log.info("==================================================");

        return redactedContent;
    }
}