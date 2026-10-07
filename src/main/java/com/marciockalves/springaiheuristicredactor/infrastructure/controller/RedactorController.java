package com.marciockalves.springaiheuristicredactor.infrastructure.controller;

import com.marciockalves.springaiheuristicredactor.application.dto.RedactorRequestDTO;
import com.marciockalves.springaiheuristicredactor.application.usecase.RedactorUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redactor")
@RequiredArgsConstructor
public class RedactorController {

    private final RedactorUseCase redactorUseCase;

    @PostMapping
    public ResponseEntity<String> processRedaction(@Valid @RequestBody RedactorRequestDTO request) {
        // Executa o use case e captura a redação lapidada pela IA
        String redactedResult = redactorUseCase.execute(request);

        return ResponseEntity.ok(redactedResult);
    }
}