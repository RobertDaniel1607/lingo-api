package com.robertradulescu.lingo.translation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TranslationRequest(
        @NotBlank @Size(max = 5000) String text,
        @NotBlank String sourceLang,
        @NotBlank String targetLang
) {}