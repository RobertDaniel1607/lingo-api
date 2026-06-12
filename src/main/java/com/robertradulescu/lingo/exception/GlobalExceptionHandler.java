package com.robertradulescu.lingo.exception;

import com.robertradulescu.lingo.translation.TranslationUnavailableException;
import com.robertradulescu.lingo.translation.UnsupportedLanguageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.robertradulescu.lingo.card.CardNotFoundException;
import com.robertradulescu.lingo.deck.DeckNotFoundException;
import com.robertradulescu.lingo.auth.EmailAlreadyUsedException;
import com.robertradulescu.lingo.auth.InvalidCredentialsException;

// Con @RestControllerAdvice centralizo aquí el manejo de errores de TODA la API.
// Así mis controladores quedan limpios y todas las respuestas de error tienen el mismo formato.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Si LibreTranslate está caído devuelvo 503, no un 500 genérico,
    // para que el cliente sepa que es un problema temporal de un servicio externo.
    @ExceptionHandler(TranslationUnavailableException.class)
    public ProblemDetail handleUnavailable(TranslationUnavailableException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        problem.setTitle("Servicio de traducción no disponible");
        return problem;
    }

    // Petición inválida hacia el traductor (p. ej. idioma no soportado): 422.
    @ExceptionHandler(UnsupportedLanguageException.class)
    public ProblemDetail handleUnsupported(UnsupportedLanguageException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Petición de traducción inválida");
        return problem;
    }

    // Errores de validación de @Valid (texto vacío, etc.): 400 con el detalle de los campos.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Petición inválida");
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Error de validación");
        return problem;
    }

    // Cuando piden una card que no existe: 404, no un 500 genérico.
    @ExceptionHandler(CardNotFoundException.class)
    public ProblemDetail handleCardNotFound(CardNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso no encontrado");
        return problem;
    }

    // Lo mismo para los decks: si no existe, 404.
    @ExceptionHandler(DeckNotFoundException.class)
    public ProblemDetail handleDeckNotFound(DeckNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso no encontrado");
        return problem;
    }

    // Email ya registrado: 409 Conflict.
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ProblemDetail handleEmailUsed(EmailAlreadyUsedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Conflicto");
        return problem;
    }

    // Login fallido: 401 Unauthorized.
    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleBadCredentials(InvalidCredentialsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("No autorizado");
        return problem;
    }

}