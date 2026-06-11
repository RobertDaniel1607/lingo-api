package com.robertradulescu.lingo.card;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // POST /api/cards -> creo una card. Devuelvo 201 Created (no 200) porque he creado un recurso.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse create(@Valid @RequestBody CardCreateRequest request) {
        return cardService.create(request);
    }

    // GET /api/cards/{id} -> una card concreta.
    @GetMapping("/{id}")
    public CardResponse getById(@PathVariable UUID id) {
        return cardService.findById(id);
    }

    // GET /api/cards -> todas las cards.
    @GetMapping
    public List<CardResponse> getAll() {
        return cardService.findAll();
    }

    // PUT /api/cards/{id} -> actualizo una card existente.
    @PutMapping("/{id}")
    public CardResponse update(@PathVariable UUID id,
                               @Valid @RequestBody CardUpdateRequest request) {
        return cardService.update(id, request);
    }

    // DELETE /api/cards/{id} -> borro. Devuelvo 204 No Content: éxito sin cuerpo.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        cardService.delete(id);
    }
}