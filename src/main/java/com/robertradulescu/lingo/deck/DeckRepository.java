package com.robertradulescu.lingo.deck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Igual que con las cards, extiendo JpaRepository y me ahorro escribir el CRUD:
// Spring Data me genera save, findById, findAll, deleteById, existsById, etc.
public interface DeckRepository extends JpaRepository<Deck, UUID> {

    // Consultas filtradas por dueño: cada usuario solo ve y toca sus propios decks.
    List<Deck> findByUserId(UUID userId);

    Optional<Deck> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
