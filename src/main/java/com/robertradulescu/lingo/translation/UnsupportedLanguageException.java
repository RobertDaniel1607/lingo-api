package com.robertradulescu.lingo.translation;

// Lanzo esta cuando LibreTranslate rechaza la petición (p. ej. un idioma que
// no soporta). Es un error del cliente, así que lo mapearé a un 422.
public class UnsupportedLanguageException extends RuntimeException {
    public UnsupportedLanguageException(String message) {
        super(message);
    }
}