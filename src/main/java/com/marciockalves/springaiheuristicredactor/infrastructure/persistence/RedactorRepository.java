package com.marciockalves.springaiheuristicredactor.infrastructure.persistence;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RedactorRepository extends JpaRepository<Redactor , UUID> {

    Redactor save(Redactor redactor);
}
