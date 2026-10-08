package com.marciockalves.springaiheuristicredactor.infrastructure.persistence;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.domain.port.RedactorPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RedactorAdapter implements RedactorPort {

    private final RedactorRepository redactorRepository;
    private static final Logger log = LoggerFactory.getLogger(RedactorAdapter.class);

    public RedactorAdapter(RedactorRepository redactorRepository) {
        this.redactorRepository = redactorRepository;
    }

    @Override
    public void save(Redactor redactor) {
        log.info("🔌 [ADAPTER] Tentando salvar no banco de dados via JPA...");
        redactorRepository.saveAndFlush(redactor); // Trocado para saveAndFlush para forçar a escrita
        log.info("✅ [ADAPTER] Registro salvo e flush executado com sucesso!");
    }
}