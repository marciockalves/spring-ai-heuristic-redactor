package com.marciockalves.springaiheuristicredactor.infrastructure.strategy;

import com.marciockalves.springaiheuristicredactor.domain.entity.Redactor;
import com.marciockalves.springaiheuristicredactor.domain.port.RedactorPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultSaveRedactorStrategyTest {

    private RedactorPort redactorPort;
    private DefaultSaveRedactorStrategy strategy;

    @BeforeEach
    void setUp() {
        redactorPort = mock(RedactorPort.class);
        strategy = new DefaultSaveRedactorStrategy(redactorPort);
    }

    @Test
    void shouldSaveRedactorSuccessfully() {
        // Arrange
        String finalContent = "Texto redigido de teste";
        String category = "ORIENTED";
        String userName = "marcio.alves";
        String title = "Título de Teste";
        String modelRedactorStr = "COMMERCIAL";

        // Act
        strategy.execute(finalContent, category, userName, title, modelRedactorStr);

        // Assert
        ArgumentCaptor<Redactor> captor = ArgumentCaptor.forClass(Redactor.class);
        verify(redactorPort, times(1)).save(captor.capture());

        Redactor savedRedactor = captor.getValue();
        assertEquals(finalContent, savedRedactor.getTextRedacted());
        assertEquals(userName, savedRedactor.getUserName());
        assertEquals(title, savedRedactor.getTitle());
        assertNotNull(savedRedactor.getModelRedactor());
        assertNotNull(savedRedactor.getModelTarget());
    }

    @Test
    void shouldThrowExceptionWhenContentIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            strategy.execute(null, "COMMERCIAL", "marcio.alves", "Título", "COMMERCIAL");
        });

        assertEquals("O conteúdo redigido não pode ser nulo ou vazio para salvamento.", exception.getMessage());
        verify(redactorPort, never()).save(any());
    }
}