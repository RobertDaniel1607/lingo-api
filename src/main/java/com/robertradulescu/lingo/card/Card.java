package com.robertradulescu.lingo.card;

import com.robertradulescu.lingo.deck.Deck;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

// @Entity le dice a JPA que esta clase se corresponde con una tabla de la BD.
// Es mi modelo INTERNO: nunca lo expongo directamente en la API, para eso uso DTOs.
@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card {

    // Uso UUID en vez de un Long autoincremental: no revela cuántas cards hay,
    // es difícil de adivinar, y puedo generarlo en la app sin pedírselo a la BD.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "source_text", nullable = false)
    private String sourceText;

    @Column(name = "translated_text", nullable = false)
    private String translatedText;

    @Column(name = "source_lang", nullable = false)
    private String sourceLang;

    @Column(name = "target_lang", nullable = false)
    private String targetLang;

    // Muchas cards pueden apuntar a un mismo deck. La columna deck_id está en MI tabla.
    // LAZY para no traer el deck entero cada vez que cargo una card.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    // Hibernate rellena esta fecha automáticamente al crear la fila.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Y esta la actualiza solo cada vez que modifico la card.
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}