package com.robertradulescu.lingo.deck;

import com.robertradulescu.lingo.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;

    // Inyecto el repositorio y el mapper por constructor: Spring me los pasa montados.
    public DeckService(DeckRepository deckRepository, DeckMapper deckMapper) {
        this.deckRepository = deckRepository;
        this.deckMapper = deckMapper;
    }

    // Al crear, marco el deck como propiedad del usuario logueado.
    @Transactional
    public DeckResponse create(DeckCreateRequest request) {
        Deck deck = deckMapper.toEntity(request);
        deck.setUserId(SecurityUtils.getCurrentUserId());
        return deckMapper.toResponse(deckRepository.save(deck));
    }

    // Filtro por dueño ya en la consulta: si el deck no es tuyo, para ti "no existe" (404).
    @Transactional(readOnly = true)
    public DeckResponse findById(UUID id) {
        Deck deck = deckRepository.findByIdAndUserId(id, SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new DeckNotFoundException(id));
        return deckMapper.toResponse(deck);
    }

    // Solo los decks del usuario actual.
    @Transactional(readOnly = true)
    public List<DeckResponse> findAll() {
        return deckRepository.findByUserId(SecurityUtils.getCurrentUserId()).stream()
                .map(deckMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        // Solo puedo borrar un deck si existe Y es mío.
        if (!deckRepository.existsByIdAndUserId(id, SecurityUtils.getCurrentUserId())) {
            throw new DeckNotFoundException(id);
        }
        deckRepository.deleteById(id);
    }
}
