package com.robertradulescu.lingo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

// Helper para saber QUIÉN está haciendo la petición. El id del usuario lo metió
// mi JwtAuthenticationFilter como "principal" al validar el token.
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) auth.getPrincipal();
    }
}
