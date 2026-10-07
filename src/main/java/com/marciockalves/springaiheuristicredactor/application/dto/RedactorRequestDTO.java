package com.marciockalves.springaiheuristicredactor.application.dto;

import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // Necessário para o Jackson conseguir instanciar via desserialização JSON
@AllArgsConstructor
public class RedactorRequestDTO {

    @NotBlank(message = "O username é obrigatório para o rastreio no cookbook.")
    private String username;

    @NotBlank(message = "O título da redação é obrigatório.")
    private String title;

    @NotBlank(message = "O texto original é obrigatório.")
    private String contentText;

    @NotNull(message = "O estilo de redação (ModelRedactor) deve ser informado.")
    private ModelRedactor modelRedactor;

    // Se preenchido, define automaticamente o modo ORIENTED. Se vazio/nulo, atua como DRAFT.
    private String orientation;
}