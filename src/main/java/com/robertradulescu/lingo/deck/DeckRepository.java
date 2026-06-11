package com.robertradulescu.lingo.deck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Igual que con las cards, extiendo JpaRepository y me ahorro escribir el CRUD:
// Spring Data me genera save, findById, findAll, deleteById, existsById, etc.
public interface DeckRepository extends JpaRepository<Deck, UUID> {
}
