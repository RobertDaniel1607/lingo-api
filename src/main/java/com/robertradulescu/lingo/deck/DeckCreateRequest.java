package com.robertradulescu.lingo.deck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Lo que el cliente me envía para crear un deck. El nombre es obligatorio;
// la descripción la dejo opcional, pero limito su longitud por seguridad.
public record DeckCreateRequest(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 2000) String description
) {}
