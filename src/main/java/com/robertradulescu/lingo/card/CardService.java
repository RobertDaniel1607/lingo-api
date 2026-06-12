package com.robertradulescu.lingo.card;

import com.robertradulescu.lingo.deck.Deck;
import com.robertradulescu.lingo.deck.DeckNotFoundException;
import com.robertradulescu.lingo.deck.DeckRepository;
import com.robertradulescu.lingo.security.SecurityUtils;
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

    @Transactional
    public CardResponse create(CardCreateRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Card card = cardMapper.toEntity(request);
        // Marco la card como propiedad del usuario logueado.
        card.setUserId(userId);
        // Si indicó un deck, debe existir Y ser suyo (no puedo meter mi card en el deck de otro).
        if (request.deckId() != null) {
            Deck deck = deckRepository.findByIdAndUserId(request.deckId(), userId)
                    .orElseThrow(() -> new DeckNotFoundException(request.deckId()));
            card.setDeck(deck);
        }
        return cardMapper.toResponse(cardRepository.save(card));
    }

    // Solo las cards del usuario que pertenecen al deck indicado.
    @Transactional(readOnly = true)
    public List<CardResponse> findByDeckId(UUID deckId) {
        return cardRepository.findByDeckIdAndUserId(deckId, SecurityUtils.getCurrentUserId())
                .stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardResponse findById(UUID id) {
        Card card = cardRepository.findByIdAndUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toResponse(card);
    }

    @Transactional(readOnly = true)
    public List<CardResponse> findAll() {
        return cardRepository.findByUserId(SecurityUtils.getCurrentUserId()).stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    @Transactional
    public CardResponse update(UUID id, CardUpdateRequest request) {
        Card card = cardRepository.findByIdAndUserId(id, SecurityUtils.getCurrentUserId())
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
        // Solo puedo borrar una card si existe Y es mía.
        if (!cardRepository.existsByIdAndUserId(id, SecurityUtils.getCurrentUserId())) {
            throw new CardNotFoundException(id);
        }
        cardRepository.deleteById(id);
    }
}
