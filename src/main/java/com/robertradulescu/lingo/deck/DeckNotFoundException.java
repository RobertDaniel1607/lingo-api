package com.robertradulescu.lingo.deck;

import java.util.UUID;

// La lanzo cuando me piden un deck que no existe. Mi GlobalExceptionHandler
// la convierte en un 404, igual que hace con CardNotFoundException.
public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(UUID id) {
        super("No existe ningún deck con id " + id);
    }
}
