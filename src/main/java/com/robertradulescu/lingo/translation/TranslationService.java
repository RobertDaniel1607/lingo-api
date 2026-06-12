package com.robertradulescu.lingo.translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
public class TranslationService {

    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);

    private final RestClient libreTranslateClient;

    public TranslationService(RestClient libreTranslateClient) {
        this.libreTranslateClient = libreTranslateClient;
    }

    // La clave combina texto + idioma origen + idioma destino: traducciones idénticas
    // comparten entrada de caché. Si ya está cacheada, Spring NO ejecuta este método.
    @Cacheable(value = "translations",
            key = "#request.text() + '|' + #request.sourceLang() + '|' + #request.targetLang()")
    public TranslationResponse translate(TranslationRequest request) {
        // Este log SOLO aparece en un cache MISS. Si la traducción ya estaba cacheada,
        // este método no se ejecuta y no verás esta línea: así se demuestra que la caché funciona.
        log.info("Cache MISS: traduciendo '{}' de {} a {}",
                request.text(), request.sourceLang(), request.targetLang());

        // Transformo mi DTO al formato que espera LibreTranslate (su campo de texto se llama "q").
        LibreTranslateRequest ltRequest = new LibreTranslateRequest(
                request.text(),
                request.sourceLang(),
                request.targetLang(),
                "text"
        );

        LibreTranslateResponse ltResponse;
        try {
            ltResponse = libreTranslateClient.post()
                    .uri("/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ltRequest)
                    .retrieve()
                    .body(LibreTranslateResponse.class);
        } catch (HttpClientErrorException e) {
            // LibreTranslate respondió con un 4xx: la petición es inválida
            // (normalmente un idioma no soportado). La culpa es del cliente.
            throw new UnsupportedLanguageException(
                    "Idioma no soportado o petición inválida ("
                            + request.sourceLang() + " → " + request.targetLang() + ")");
        } catch (ResourceAccessException e) {
            // Ni siquiera pude conectar con LibreTranslate: está caído o inaccesible.
            // No es culpa del cliente, lo trato como 503.
            throw new TranslationUnavailableException(
                    "El servicio de traducción no está disponible ahora mismo", e);
        }

        // Devuelvo mi propio DTO, no el de LibreTranslate, para no acoplar mi API a la suya.
        return new TranslationResponse(
                ltResponse.translatedText(),
                request.sourceLang(),
                request.targetLang()
        );
    }
}