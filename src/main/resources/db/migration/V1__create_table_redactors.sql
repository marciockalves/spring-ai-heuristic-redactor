CREATE TABLE redactors (
                           id UUID PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           username VARCHAR(255) NOT NULL,
                           version INT NOT NULL,
                           model_redactor VARCHAR(50) NOT NULL,
                           model_target VARCHAR(50) NOT NULL,
                           text_redacted TEXT NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice opcional para buscas rápidas por username e título que definimos no repositório
CREATE INDEX idx_redactors_username_title ON redactors(username, title);
CREATE INDEX idx_redactors_username_model ON redactors(username, model_redactor);