-- Tabla de usuarios. Guardo el HASH de la contraseña, nunca la contraseña en claro.
CREATE TABLE users (
                       id            UUID PRIMARY KEY,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role          VARCHAR(50) NOT NULL DEFAULT 'USER',
                       created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);