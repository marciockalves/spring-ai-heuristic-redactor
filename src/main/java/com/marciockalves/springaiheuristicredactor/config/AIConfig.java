package com.marciockalves.springaiheuristicredactor.config;

import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class AIConfig {

    @Value("classpath:prompts/literary.st")
    private Resource literaryPromptResource;

    @Value("classpath:prompts/technical.st")
    private Resource technicalPromptResource;

    @Value("classpath:prompts/commercial.st")
    private Resource commercialPromptResource;

    @Value("classpath:prompts/scientific.st")
    private Resource scientificPromptResource;

    /**
     * Strategy Pattern: Mapeia cada Enum de estilo ao seu respectivo Resource de System Prompt.
     * Isso elimina qualquer switch-case engessado na camada de serviço/use case.
     */
    @Bean
    public Map<ModelRedactor, Resource> promptTemplateStrategy() {
        Map<ModelRedactor, Resource> strategyMap = new EnumMap<>(ModelRedactor.class);
        strategyMap.put(ModelRedactor.LITERARY, literaryPromptResource);
        strategyMap.put(ModelRedactor.TECHNICAL, technicalPromptResource);
        strategyMap.put(ModelRedactor.COMMERCIAL, commercialPromptResource);
        strategyMap.put(ModelRedactor.SCIENTIFIC, scientificPromptResource);
        return strategyMap;
    }
}