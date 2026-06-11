-- Borro la tabla dummy que creé solo para probar Flyway en la Fase 1.
DROP TABLE IF EXISTS dummy;

-- Mi tabla principal de flashcards.
CREATE TABLE cards (
                       id              UUID PRIMARY KEY,
                       source_text     TEXT NOT NULL,
                       translated_text TEXT NOT NULL,
                       source_lang     VARCHAR(10) NOT NULL,
                       target_lang     VARCHAR(10) NOT NULL,
                       created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                       updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);