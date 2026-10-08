package com.marciockalves.springaiheuristicredactor.domain.entity;


import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import com.marciockalves.springaiheuristicredactor.domain.enums.ModelTarget;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "redactors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Redactor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "model_redactor", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelRedactor modelRedactor;

    @Column(name = "model_target", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelTarget modelTarget;

    @Column(name = "text_redacted", columnDefinition = "TEXT",  nullable = false)
    private String textRedacted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name ="updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
