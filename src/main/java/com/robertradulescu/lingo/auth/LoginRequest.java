package com.robertradulescu.lingo.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Datos para iniciar sesión.
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {}
