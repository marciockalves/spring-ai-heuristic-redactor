package com.marciockalves.springaiheuristicredactor.infrastructure.persistence;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.domain.port.RedactorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedactorAdapter  implements RedactorPort {

    private final Redactor repository;
    private final RedactorRepository redactorRepository;

    @Override
    public List<Redactor> findAll() {
        return List.of();
    }

    @Override
    public List<Redactor> findById(UUID id) {
        return List.of();
    }

    @Override
    public List<Redactor> findByUserName(String userName) {
        return List.of();
    }

    @Override
    public List<Redactor> findByUserNameAndTitle(String userName, String title) {
        return List.of();
    }

    @Override
    public List<Redactor> findByUserNameAndModelRedactor(String userName, ModelRedactor modelRedactor) {
        return List.of();
    }

    @Override
    public Redactor save(Redactor redactor){return redactorRepository.save(redactor);
    }



}
