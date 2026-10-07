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


    List<Redactor> findAll();

    List<Redactor> findByUserName(String userName);

    @Query("""
        SELECT r FROM Redactor r
        WHERE r.userName = :userName
        AND r.title = :title
        ORDER By r.updatedAt ASC
""")
    List<Redactor> findByUserNameAndTitle(
            @Param("userName") String userName,
            @Param("title") String title
    );

    @Query("""
        SELECT r FROM Redactor r
        WHERE r.userName = :userName
        AND r.modelRedactor = :modelRedactor
        ORDER By r.updatedAt ASC
""")
    List<Redactor> findByUserNameAndModelRedactor(
            @Param("userName") String userName,
            @Param("modelReactor") ModelRedactor modelRedactor
    );

    Redactor save(Redactor redactor);
}
