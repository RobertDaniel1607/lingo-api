package com.robertradulescu.lingo.translation;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
public class TranslationService {

    private final RestClient libreTranslateClient;

    public TranslationService(RestClient libreTranslateClient) {
        this.libreTranslateClient = libreTranslateClient;
    }

    public TranslationResponse translate(TranslationRequest request) {
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