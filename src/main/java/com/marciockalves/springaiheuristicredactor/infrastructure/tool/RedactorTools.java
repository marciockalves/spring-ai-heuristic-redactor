package com.marciockalves.springaiheuristicredactor.infrastructure.tool;

import com.marciockalves.springaiheuristicredactor.infrastructure.strategy.SaveRedactorStrategy;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RedactorTools {

    private static final Logger log = LoggerFactory.getLogger(RedactorTools.class);
    private final SaveRedactorStrategy saveRedactorStrategy;

    public RedactorTools(SaveRedactorStrategy saveRedactorStrategy) {
        this.saveRedactorStrategy = saveRedactorStrategy;
    }


    public record SaveRequest(
            @JsonProperty("finalContent") String finalContent
    ) {}

    @Tool(description = "Salva o texto redigido no banco de dados. Você DEVE passar o texto completo gerado no parâmetro finalContent.")
    public String saveRedactedText(
            @ToolParam(description = "O texto completo e final que foi redigido pela IA.")
            SaveRequest request,
            ToolContext toolContext) {

        log.info("🛠️ [TOOL] Objeto SaveRequest recebido da IA -> finalContent: [{}]",
                request != null ? request.finalContent() : "REQUEST ESTÁ NULO!");

        if (request == null || request.finalContent() == null || request.finalContent().isBlank()) {
            throw new IllegalArgumentException("O conteúdo redigido (finalContent) não pode ser nulo ou vazio!");
        }

        String userName = (String) toolContext.getContext().get("userName");
        String title = (String) toolContext.getContext().get("title");
        String category = (String) toolContext.getContext().get("category");
        String modelRedactorStr = (String) toolContext.getContext().get("modelRedactorStr");

        saveRedactorStrategy.execute(request.finalContent(), category, userName, title, modelRedactorStr);

        return "Texto salvo com sucesso no banco de dados!";
    }
}