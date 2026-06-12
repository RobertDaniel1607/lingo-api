package com.robertradulescu.lingo.auth;

// La lanzo si alguien intenta registrarse con un email que ya existe. La mapearé a 409 Conflict.
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("Ya existe una cuenta con el email " + email);
    }
}
