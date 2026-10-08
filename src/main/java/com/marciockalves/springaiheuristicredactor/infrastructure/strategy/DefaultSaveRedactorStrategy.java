package com.marciockalves.springaiheuristicredactor.infrastructure.strategy;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelTarget;
import com.marciockalves.springaiheuristicredactor.domain.port.RedactorPort;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultSaveRedactorStrategy implements SaveRedactorStrategy {
    private final RedactorPort redactorPort;

    public DefaultSaveRedactorStrategy(RedactorPort redactorPort) {
        this.redactorPort = redactorPort;
    }

    @Override
    @Transactional
    public void execute(String finalContent, String category, String userName, String title, String modelRedactorStr) {
        try {

            ModelRedactor modelRedactor = ModelRedactor.safeValueOf(modelRedactorStr);
            ModelTarget modelTarget = ModelTarget.safeValueOf(category);


            log.info("Persistindo no banco -> Usuário: [{}] | Título: [{}] | ModelRedactor: [{}] | ModelTarget: [{}]",
                    userName, title, modelRedactor, modelTarget);

            Redactor redactor = Redactor.builder()
                    .textRedacted(finalContent)
                    .userName(userName)
                    .title(title)
                    .modelRedactor(modelRedactor)
                    .modelTarget(modelTarget)
                    .build();


            redactorPort.save(redactor);

            log.info("Texto redigido salvo com sucesso no banco de dados!");

        } catch (Exception e) {
            log.error("Erro ao persistir o texto redigido: ", e);
            throw new RuntimeException("Erro ao persistir o texto redigido: " + e.getMessage(), e);
        }
    }
}