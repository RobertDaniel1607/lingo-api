package com.robertradulescu.lingo.auth;

// La lanzo cuando el login falla. Mensaje genérico a propósito: no revelo si el fallo
// fue por email inexistente o contraseña incorrecta. La mapearé a 401 Unauthorized.
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Email o contraseña incorrectos");
    }
}
