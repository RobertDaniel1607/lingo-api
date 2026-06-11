package com.robertradulescu.lingo.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Extiendo JpaRepository y obtengo gratis save, findById, findAll, deleteById...
// No escribo SQL para el CRUD básico: Spring Data genera la implementación por mí.
public interface CardRepository extends JpaRepository<Card, UUID> {

    // Spring Data lee el nombre del método y genera la consulta sola:
    // "findByDeckId" -> SELECT * FROM cards WHERE deck_id = ?. Una sola consulta, sin N+1.
    List<Card> findByDeckId(UUID deckId);
}