package com.marciockalves.springaiheuristicredactor.domain.enums;

public enum ModelRedactor {
    LITERARY,
    TECHNICAL,
    COMMERCIAL,
    SCIENTIFIC;

    public static ModelRedactor safeValueOf(String value) {
        if (value == null || value.isBlank()) {
            return COMMERCIAL; // ou lançar uma exceção de negócio se for obrigatório
        }

        for (ModelRedactor model : ModelRedactor.values()) {
            if (model.name().equalsIgnoreCase(value.trim())) {
                return model;
            }
        }

        throw new IllegalArgumentException("Modelo de redator inválido: " + value);
    }
}
