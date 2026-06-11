package com.robertradulescu.lingo.card;

import com.robertradulescu.lingo.deck.Deck;
import com.robertradulescu.lingo.deck.DeckNotFoundException;
import com.robertradulescu.lingo.deck.DeckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    // Necesito el repositorio de decks para asociar una card a su deck al crearla.
    private final DeckRepository deckRepository;

    public CardService(CardRepository cardRepository,
                       CardMapper cardMapper,
                       DeckRepository deckRepository) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.deckRepository = deckRepository;
    }

    // Las operaciones que escriben van con @Transactional: si algo falla a mitad,
    // se deshace todo. Lo pongo en el servicio, no en el controlador.
    @Transactional
    public CardResponse create(CardCreateRequest request) {
        Card card = cardMapper.toEntity(request);
        // Si el cliente indicó un deck, lo busco y lo asocio. Si no existe, devuelvo 404.
        if (request.deckId() != null) {
            Deck deck = deckRepository.findById(request.deckId())
                    .orElseThrow(() -> new DeckNotFoundException(request.deckId()));
            card.setDeck(deck);
        }
        Card saved = cardRepository.save(card);
        return cardMapper.toResponse(saved);
    }

    // Devuelvo todas las cards de un deck. Una sola consulta gracias a findByDeckId.
    @Transactional(readOnly = true)
    public List<CardResponse> findByDeckId(UUID deckId) {
        return cardRepository.findByDeckId(deckId).stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    // Las de solo lectura las marco readOnly: optimiza y deja claro que no modifican nada.
    @Transactional(readOnly = true)
    public CardResponse findById(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toResponse(card);
    }

    @Transactional(readOnly = true)
    public List<CardResponse> findAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    @Transactional
    public CardResponse update(UUID id, CardUpdateRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        card.setSourceText(request.sourceText());
        card.setTranslatedText(request.translatedText());
        card.setSourceLang(request.sourceLang());
        card.setTargetLang(request.targetLang());
        // No llamo a save(): dentro de la transacción, Hibernate detecta los cambios
        // de una entidad gestionada y los guarda solo al hacer commit (dirty checking).
        return cardMapper.toResponse(card);
    }

    @Transactional
    public void delete(UUID id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }
        cardRepository.deleteById(id);
    }
}