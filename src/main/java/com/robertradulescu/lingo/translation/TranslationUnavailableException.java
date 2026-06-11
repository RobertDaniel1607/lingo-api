package com.robertradulescu.lingo.translation;

// Lanzo esta excepción cuando LibreTranslate no responde (está caído o hay un
// problema de red). Luego la traduzco a un 503, porque el fallo no es culpa
// del cliente, sino de un servicio externo del que dependo.
public class TranslationUnavailableException extends RuntimeException {
    public TranslationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}