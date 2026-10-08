package com.marciockalves.springaiheuristicredactor.application.usecase;

import com.marciockalves.springaiheuristicredactor.application.dto.RedactorRequestDTO;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.infrastructure.tool.RedactorTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedactorUseCaseTest {

    private ChatClient.Builder chatClientBuilder;
    private ChatClient chatClient;
    private ChatClient.ChatClientRequestSpec requestSpec;
    private ChatClient.StreamResponseSpec streamResponseSpec;
    private Map<ModelRedactor, Resource> promptTemplateStrategy;
    private RedactorTools redactorTools;
    private ChatMemory chatMemory;
    private Resource resourceMock;

    private RedactorUseCase redactorUseCase;

    @BeforeEach
    void setUp() {
        chatClientBuilder = mock(ChatClient.Builder.class);
        chatClient = mock(ChatClient.class);
        requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        streamResponseSpec = mock(ChatClient.StreamResponseSpec.class);
        chatMemory = mock(ChatMemory.class); // Corrigido para referenciar ChatMemory corretamente
        resourceMock = mock(Resource.class);
        redactorTools = mock(RedactorTools.class);

        // Resolve a ambiguidade do defaultAdvisors tipando o argumento com o Advisor (ou usando varargs aceitando qualquer Advisor)
        when(chatClientBuilder.defaultAdvisors(any(org.springframework.ai.chat.client.advisor.api.Advisor[].class)))
                .thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Configura o encadeamento fluente do ChatClient
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(any(java.util.function.Consumer.class))).thenReturn(requestSpec);
        when(requestSpec.user(any(String.class))).thenReturn(requestSpec);
        when(requestSpec.tools(any())).thenReturn(requestSpec);
        when(requestSpec.toolContext(any())).thenReturn(requestSpec);
        when(requestSpec.advisors(any(java.util.function.Consumer.class))).thenReturn(requestSpec);

        when(requestSpec.stream()).thenReturn(streamResponseSpec);
        when(streamResponseSpec.content()).thenReturn(Flux.just("Texto ", "processado ", "com sucesso"));

        // Preenche o mapa de estratégias de templates de prompt
        promptTemplateStrategy = Map.of(ModelRedactor.COMMERCIAL, resourceMock);

        // Instancia o Use Case com o construtor real
        redactorUseCase = new RedactorUseCase(chatClientBuilder, promptTemplateStrategy, redactorTools, chatMemory);
    }

    @Test
    void shouldExecuteRedactionSuccessfully() {
        RedactorRequestDTO request = new RedactorRequestDTO();
        request.setUsername("marcio.alves");
        request.setTitle("Relatório Comercial");
        request.setContentText("Conteúdo bruto para redação");
        request.setModelRedactor(ModelRedactor.COMMERCIAL);

        Flux<String> result = redactorUseCase.execute(request);

        assertNotNull(result);
        StepVerifier.create(result)
                .expectNext("Texto ")
                .expectNext("processado ")
                .expectNext("com sucesso")
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenContentAndOrientationAreEmpty() {
        RedactorRequestDTO request = new RedactorRequestDTO();
        request.setUsername("marcio.alves");
        request.setModelRedactor(ModelRedactor.COMMERCIAL);

        Flux<String> result = redactorUseCase.execute(request);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Você precisa fornecer um texto"))
                .verify();
    }

    @Test
    void shouldReturnErrorWhenModelNotMapped() {
        RedactorUseCase unmappedUseCase = new RedactorUseCase(
                chatClientBuilder, Map.of(), redactorTools, chatMemory
        );

        RedactorRequestDTO request = new RedactorRequestDTO();
        request.setUsername("marcio.alves");
        request.setContentText("Texto válido");
        request.setModelRedactor(ModelRedactor.COMMERCIAL);

        Flux<String> result = unmappedUseCase.execute(request);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Modelo de redator não mapeado"))
                .verify();
    }
}