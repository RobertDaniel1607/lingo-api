package com.robertradulescu.lingo.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Extiendo JpaRepository y obtengo gratis save, findById, findAll, deleteById...
// No escribo SQL para el CRUD básico: Spring Data genera la implementación por mí.
public interface CardRepository extends JpaRepository<Card, UUID> {

    // Consultas filtradas por dueño: cada usuario solo ve y toca sus propias cards.
    List<Card> findByUserId(UUID userId);

    Optional<Card> findByIdAndUserId(UUID id, UUID userId);

    List<Card> findByDeckIdAndUserId(UUID deckId, UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}