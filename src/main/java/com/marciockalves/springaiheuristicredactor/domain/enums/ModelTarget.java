package com.marciockalves.springaiheuristicredactor.domain.enums;

public enum ModelTarget {
    DRAFT,
    ORIENTED;

    public static ModelTarget safeValueOf(String value) {
        if (value == null || value.isBlank()) {
            return DRAFT;
        }

        for (ModelTarget model : ModelTarget.values()) {
            if (model.name().equalsIgnoreCase(value.trim())) {
                return model;
            }
        }

        throw new IllegalArgumentException("Modelo de target inválido: " + value);
    }
}


