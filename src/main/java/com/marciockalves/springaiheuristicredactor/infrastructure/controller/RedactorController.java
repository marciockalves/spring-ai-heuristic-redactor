package com.marciockalves.springaiheuristicredactor.infrastructure.controller;

import com.marciockalves.springaiheuristicredactor.application.dto.RedactorRequestDTO;
import com.marciockalves.springaiheuristicredactor.application.usecase.RedactorUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/redactor")
@RequiredArgsConstructor
public class RedactorController {

    private final RedactorUseCase redactorUseCase;

    @PostMapping(
            value = "/stream", // Adicionamos explicitamente o value igual na sua outra aplicação bem-sucedida
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> processRedaction(@Valid @RequestBody RedactorRequestDTO request) {
        // Executa o use case repassando o DTO e retorna o stream reativo para o client
        return redactorUseCase.execute(request);
    }
}