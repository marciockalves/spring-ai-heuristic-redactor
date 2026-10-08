package com.marciockalves.springaiheuristicredactor.infrastructure.tool;

import com.marciockalves.springaiheuristicredactor.infrastructure.strategy.SaveRedactorStrategy;
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

    @Tool(description = "Salva o texto redigido no banco de dados quando o usuário expressar aprovação final ou pedir para finalizar.")
    public String saveRedactedText(
            @ToolParam(description = "O texto completo e final que foi redigido.")
            String finalContent,
            ToolContext toolContext) {

        log.info("🛠️ [TOOL] Repassando texto recebido da IA para a Strategy...");


        String userName = (String) toolContext.getContext().get("userName");
        String title = (String) toolContext.getContext().get("title");
        String category = (String) toolContext.getContext().get("category");
        String modelRedactorStr = (String) toolContext.getContext().get("modelRedactorStr");


        saveRedactorStrategy.execute(finalContent, category, userName, title, modelRedactorStr);

        return "Texto salvo com sucesso no banco de dados!";
    }
}