package com.robertradulescu.lingo.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CardUpdateRequest(
        @NotBlank @Size(max = 5000) String sourceText,
        @NotBlank @Size(max = 5000) String translatedText,
        @NotBlank @Size(max = 10) String sourceLang,
        @NotBlank @Size(max = 10) String targetLang
) {}