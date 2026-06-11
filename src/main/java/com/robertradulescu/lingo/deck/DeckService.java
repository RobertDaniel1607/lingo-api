package com.robertradulescu.lingo.deck;

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

    // Operación de escritura: la envuelvo en una transacción.
    @Transactional
    public DeckResponse create(DeckCreateRequest request) {
        Deck saved = deckRepository.save(deckMapper.toEntity(request));
        return deckMapper.toResponse(saved);
    }

    // Solo lectura: readOnly deja claro que no modifico nada y permite optimizar.
    @Transactional(readOnly = true)
    public DeckResponse findById(UUID id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new DeckNotFoundException(id));
        return deckMapper.toResponse(deck);
    }

    @Transactional(readOnly = true)
    public List<DeckResponse> findAll() {
        return deckRepository.findAll().stream()
                .map(deckMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID id) {
        // Compruebo que existe antes de borrar, para devolver un 404 claro
        // en vez de fallar en silencio si el id no existe.
        if (!deckRepository.existsById(id)) {
            throw new DeckNotFoundException(id);
        }
        deckRepository.deleteById(id);
    }
}
