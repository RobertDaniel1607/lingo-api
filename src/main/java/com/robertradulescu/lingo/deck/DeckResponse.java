package com.robertradulescu.lingo.deck;

import java.time.Instant;
import java.util.UUID;

// Lo que devuelvo al cliente. No incluyo la lista de cards aquí a propósito:
// si la metiera, al listar decks Hibernate cargaría las cards de cada uno y
// provocaría el problema N+1. Las cards de un deck las sirvo en su propio endpoint.
public record DeckResponse(
        UUID id,
        String name,
        String description,
        Instant createdAt
) {}
