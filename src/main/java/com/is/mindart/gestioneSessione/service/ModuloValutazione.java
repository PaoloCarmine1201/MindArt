package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModuloValutazione {

    /**
     * Logger per la classe.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ModuloValutazione.class);

    /**
     * RestTemplate per effettuare richieste HTTP.
     */
    private final RestTemplate restTemplate = new RestTemplate();
    /**
     * Repository del disegno.
     */
    private final DisegnoRepository disegnoRepository;

    /**
     * URL dell'API per la valutazione emotiva.
     */
    private static final String API_URL
            = "http://localhost:6000/valutadisegno";
    /**
     * Metodo pianificato che esegue una richiesta API ogni 5 minuti.
     */
    @Scheduled(fixedRate = 40000) // 300000 ms = 5 minuti
    public void triggerApiRequest() {
        List<Disegno> disegni =
                disegnoRepository
                    .findAllByValutazioneEmotivaIsNullAndSessione_TerminataTrue();

        if (disegni.isEmpty()) {
            logger.info("Nessun disegno da valutare.");
            return;
        }

        List<DisegnoRequest> disegniRequest = disegni.stream()
                .filter(disegno -> disegno.getImmagine() != null)
                .map(disegno -> new DisegnoRequest(
                        disegno.getId(),
                        Base64.getEncoder()
                                .encodeToString(disegno.getImmagine())
                ))
                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<DisegnoRequest>> requestEntity =
                new HttpEntity<>(disegniRequest, headers);

        try {
            ResponseEntity<List<ValutazioneEmotivaResponse>> response =
                    restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    requestEntity,
                            new ParameterizedTypeReference<>() {
                            });

            if (response.getStatusCode().is2xxSuccessful()
                    && response.getBody() != null) {
                List<ValutazioneEmotivaResponse> valutazioni
                        = response.getBody();

                for (ValutazioneEmotivaResponse valutazione : valutazioni) {
                    Disegno disegno = disegnoRepository.findById(
                            valutazione.getDisegnoId())
                            .orElse(null);

                    if (disegno != null) {
                        try {
                            ValutazioneEmotiva valutazioneEnum =
                                    ValutazioneEmotiva.valueOf(
                                            valutazione.getValutazioneEmotiva()
                                                    .toUpperCase());
                            disegno.setValutazioneEmotiva(valutazioneEnum);
                            disegnoRepository.save(disegno);
                            logger.info("Disegno ID {} valutato con successo.",
                                    disegno.getId());
                        } catch (IllegalArgumentException e) {
                            logger.error("ValutazioneEmotiva non valida "
                                           + "per Disegno ID {}: {}",
                                    disegno.getId(),
                                    valutazione.getValutazioneEmotiva());
                        }
                    } else {
                        logger.warn("Disegno con ID {} non trovato.",
                                valutazione.getDisegnoId());
                    }
                }
            } else {
                logger.error("Errore nella richiesta API: {}",
                        response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Eccezione durante la richiesta API: {}",
                    e.getMessage());
        }

    }

    /**
     * Classe interna per rappresentare la richiesta al servizio esterno.
     */
    @Data
    @AllArgsConstructor
    private static class DisegnoRequest {
        /**
         * Identificativo del disegno.
         */
        private Long disegnoId;
        /**
         * Immagine del disegno codificata in Base64.
         */
        private String immagine;
    }

    /**
     * Classe interna per rappresentare la risposta del servizio esterno.
     */
    @Data
    @AllArgsConstructor
    private static class ValutazioneEmotivaResponse {
        /**
         * Identificativo del disegno.
         */
        private Long disegnoId;
        /**
         * Valutazione emotiva del disegno.
         */
        private String valutazioneEmotiva;
    }
}
