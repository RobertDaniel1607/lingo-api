package com.robertradulescu.lingo.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Lo que el cliente me manda para crear una card. Valido aquí para rechazar
// datos basura antes de que lleguen a mi lógica.
public record CardCreateRequest(
        @NotBlank @Size(max = 5000) String sourceText,
        @NotBlank @Size(max = 5000) String translatedText,
        @NotBlank @Size(max = 10) String sourceLang,
        @NotBlank @Size(max = 10) String targetLang
) {}