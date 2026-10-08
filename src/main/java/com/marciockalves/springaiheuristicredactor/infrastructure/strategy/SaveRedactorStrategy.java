package com.marciockalves.springaiheuristicredactor.infrastructure.strategy;

import com.marciockalves.springaiheuristicredactor.domain.enums.ModelRedactor;

public interface SaveRedactorStrategy {
    void execute(String finalContent, String category, String userName, String title, String modelRedactor);
}