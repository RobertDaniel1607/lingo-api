package com.robertradulescu.lingo.deck;

import org.springframework.stereotype.Component;

// Convierto entre la entidad Deck y sus DTOs a mano, igual que hice con las cards.
// Lo marco @Component para que Spring lo gestione y poder inyectarlo en el servicio.
@Component
public class DeckMapper {

    // De la petición de creación a una entidad nueva. El id y la fecha los pone Hibernate.
    public Deck toEntity(DeckCreateRequest request) {
        Deck deck = new Deck();
        deck.setName(request.name());
        deck.setDescription(request.description());
        return deck;
    }

    // De la entidad al DTO que ve el cliente.
    public DeckResponse toResponse(Deck deck) {
        return new DeckResponse(
                deck.getId(),
                deck.getName(),
                deck.getDescription(),
                deck.getCreatedAt()
        );
    }
}
