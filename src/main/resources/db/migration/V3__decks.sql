-- Tabla de mazos (decks). Una card pertenecerá a un deck.
CREATE TABLE decks (
                       id          UUID PRIMARY KEY,
                       name        VARCHAR(255) NOT NULL,
                       description TEXT,
                       created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Añado la clave foránea en cards. La dejo nullable: permito cards sin deck por ahora.
ALTER TABLE cards ADD COLUMN deck_id UUID REFERENCES decks(id);