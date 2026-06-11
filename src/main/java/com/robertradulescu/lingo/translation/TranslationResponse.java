package com.robertradulescu.lingo.translation;

public record TranslationResponse(
        String translatedText,
        String sourceLang,
        String targetLang
) {}