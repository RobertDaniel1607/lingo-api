package com.robertradulescu.lingo.auth;

// Lo que devuelvo tras registro/login: el token, su tipo (Bearer) y cuánto dura en segundos.
public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds
) {}
