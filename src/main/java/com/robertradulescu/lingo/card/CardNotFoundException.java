package com.robertradulescu.lingo.card;

import java.util.UUID;

// La lanzo cuando me piden una card que no existe. La mapearé a un 404.
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(UUID id) {
        super("No existe ninguna card con id " + id);
    }
}