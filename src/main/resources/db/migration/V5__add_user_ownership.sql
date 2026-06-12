-- Cada deck y cada card pertenecen a un usuario (su "owner").
-- Lo dejo nullable para no romper las filas de prueba que ya existen; la app
-- siempre rellenará el owner al crear registros nuevos.
ALTER TABLE decks ADD COLUMN user_id UUID REFERENCES users(id);
ALTER TABLE cards ADD COLUMN user_id UUID REFERENCES users(id);
