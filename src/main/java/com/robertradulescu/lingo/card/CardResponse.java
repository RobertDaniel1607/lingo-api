package com.robertradulescu.lingo.card;

import java.time.Instant;
import java.util.UUID;

// Lo que devuelvo al cliente. Incluye id y fechas, que él no envía sino que genero yo.
// También el deckId, para que se sepa a qué deck pertenece la card (o null si está suelta).
public record CardResponse(
        UUID id,
        String sourceText,
        String translatedText,
        String sourceLang,
        String targetLang,
        UUID deckId,
        Instant createdAt,
        Instant updatedAt
) {}