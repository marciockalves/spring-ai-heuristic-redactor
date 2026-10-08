package com.marciockalves.springaiheuristicredactor.domain.port;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedactorPort{

    void save(Redactor redactor);


}
