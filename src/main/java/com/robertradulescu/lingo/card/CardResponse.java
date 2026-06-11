package com.robertradulescu.lingo.card;

import java.time.Instant;
import java.util.UUID;

// Lo que devuelvo al cliente. Incluye id y fechas, que él no envía sino que genero yo.
public record CardResponse(
        UUID id,
        String sourceText,
        String translatedText,
        String sourceLang,
        String targetLang,
        Instant createdAt,
        Instant updatedAt
) {}