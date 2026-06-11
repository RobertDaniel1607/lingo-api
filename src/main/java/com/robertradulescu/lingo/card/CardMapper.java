package com.robertradulescu.lingo.card;

import org.springframework.stereotype.Component;

import java.util.UUID;

// Hago la conversión entidad <-> DTO a mano (sin MapStruct todavía) para entender
// qué pasa por dentro. Lo marco @Component para poder inyectarlo donde lo necesite.
@Component
public class CardMapper {

    // De la petición de creación a una entidad nueva.
    // No pongo id ni fechas: de eso se encargan Hibernate y la BD.
    public Card toEntity(CardCreateRequest request) {
        Card card = new Card();
        card.setSourceText(request.sourceText());
        card.setTranslatedText(request.translatedText());
        card.setSourceLang(request.sourceLang());
        card.setTargetLang(request.targetLang());
        return card;
    }

    // De la entidad al DTO que ve el cliente.
    public CardResponse toResponse(Card card) {
        // Saco el id del deck solo si la card tiene uno. Llamar a getId() sobre la
        // asociación LAZY no dispara consulta extra: Hibernate ya conoce el id por la FK.
        UUID deckId = card.getDeck() != null ? card.getDeck().getId() : null;
        return new CardResponse(
                card.getId(),
                card.getSourceText(),
                card.getTranslatedText(),
                card.getSourceLang(),
                card.getTargetLang(),
                deckId,
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}