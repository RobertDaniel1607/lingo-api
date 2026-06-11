package com.robertradulescu.lingo.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// Extiendo JpaRepository y obtengo gratis save, findById, findAll, deleteById...
// No escribo SQL para el CRUD básico: Spring Data genera la implementación por mí.
public interface CardRepository extends JpaRepository<Card, UUID> {
}