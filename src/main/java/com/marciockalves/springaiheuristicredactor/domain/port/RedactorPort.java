package com.marciockalves.springaiheuristicredactor.domain.port;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedactorPort{

    List<Redactor> findAll();
    List<Redactor> findById(UUID id);
    List<Redactor> findByUserName(String userName);
    List<Redactor> findByUserNameAndTitle(String userName, String title);
    List<Redactor> findByUserNameAndModelRedactor(String userName, ModelRedactor modelRedactor);
    Redactor save(Redactor redactor);

}
