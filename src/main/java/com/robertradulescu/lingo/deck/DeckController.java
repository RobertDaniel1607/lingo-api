package com.robertradulescu.lingo.deck;

import com.robertradulescu.lingo.card.CardResponse;
import com.robertradulescu.lingo.card.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;
    // Inyecto también el CardService para poder servir las cards de un deck.
    private final CardService cardService;

    public DeckController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    // POST /api/decks -> creo un deck. Devuelvo 201 Created porque he creado un recurso.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeckResponse create(@Valid @RequestBody DeckCreateRequest request) {
        return deckService.create(request);
    }

    // GET /api/decks/{id} -> un deck concreto.
    @GetMapping("/{id}")
    public DeckResponse getById(@PathVariable UUID id) {
        return deckService.findById(id);
    }

    // GET /api/decks -> todos los decks.
    @GetMapping
    public List<DeckResponse> getAll() {
        return deckService.findAll();
    }

    // GET /api/decks/{id}/cards -> las cards de un deck.
    // Primero compruebo que el deck existe (findById lanza 404 si no), y luego
    // devuelvo sus cards con una única consulta.
    @GetMapping("/{id}/cards")
    public List<CardResponse> getCards(@PathVariable UUID id) {
        deckService.findById(id);
        return cardService.findByDeckId(id);
    }

    // DELETE /api/decks/{id} -> borro. 204 No Content: éxito sin cuerpo que devolver.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deckService.delete(id);
    }
}
