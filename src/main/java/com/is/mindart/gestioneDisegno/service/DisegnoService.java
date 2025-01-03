
package com.is.mindart.gestioneDisegno.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DisegnoService {

    /**
     * Repository per i disegni.
     */
    private final DisegnoRepository disegnoRepository;
    /**
     * ObjectMapper per la conversione di oggetti in JSON.
     */
    private final ObjectMapper objectMapper;

    /**
     * Aggiunge un tratto al disegno sovrascivendo il precedente.
     * @param disegnoId id del disegno
     * @param newStroke tratto da aggiungere
     */
    public void addStrokeToDisegno(final Long disegnoId,
                                   final StrokeDTO newStroke) {
        Disegno disegno = disegnoRepository.findById(disegnoId)
                .orElseThrow(() -> new RuntimeException(
                        "Disegno non trovato con id " + disegnoId));

        // Leggo la stringa JSON e la converto in lista esistente di strokes
        List<StrokeDTO> strokes = deserializeStrokes(disegno.getStrokesJson());

        // Aggiungo il nuovo stroke
        strokes.add(newStroke);

        // Converto nuovamente in JSON
        String updatedJson = serializeStrokes(strokes);

        // Aggiorno il campo in Disegno
        disegno.setStrokesJson(updatedJson);

        // Salvo su DB
        disegnoRepository.save(disegno);
    }


    /**
     * Converte la lista di StrokeDTO in una stringa JSON.
     *
     * @param strokes lista di StrokeDTO
     * @return stringa JSON
     */
    private String serializeStrokes(final List<StrokeDTO> strokes) {
        try {
            return objectMapper.writeValueAsString(strokes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "Errore durante la scrittura del JSON dei strokes", e);
        }
    }

    /**
     * Restituisce il disegno associato ad un id.
     * @param disegnoId id del disegno
     * @return DisegnoDTO con i tratti
     */
    public DisegnoDTO getDisegnoById(final Long disegnoId) {
        Disegno disegno = disegnoRepository.findById(disegnoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Disegno non trovato con id " + disegnoId));

        List<StrokeDTO> strokes = deserializeStrokes(disegno.getStrokesJson());

        return new DisegnoDTO(disegno.getId(), strokes);
    }

    /**
     * Restituisce il disegno associato ad una sessione.
     * @param sessioneId id della sessione
     * @return DisegnoDTO con i tratti
     */
    public DisegnoDTO getDisegnoBySessioneId(final Long sessioneId) {
        Disegno disegno = disegnoRepository.findBySessioneId(sessioneId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Disegno non trovato per la sessione con id "
                                + sessioneId));

        List<StrokeDTO> strokes = deserializeStrokes(disegno.getStrokesJson());

        return new DisegnoDTO(disegno.getId(), strokes);
    }

    /**
     * Converte la stringa JSON dei tratti in una lista di StrokeDTO.
     *
     * @param strokesJson JSON dei tratti
     * @return Lista di StrokeDTO
     */
    private List<StrokeDTO> deserializeStrokes(final String strokesJson) {
        if (strokesJson == null || strokesJson.isBlank()) {
            return new ArrayList<>();
        }
        try {
            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, StrokeDTO.class);
            return objectMapper.readValue(strokesJson, type);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Errore durante la deserializzazione dei tratti", e);
        }
    }

    /**
     * Metodo che restituisce i disegni di un bambino.
     * @param bambinoId id del bambino
     * @return lista di disegni
     */
    public List<DisegnoDTOResponse> getDisegniByBambinoId(
            final Long bambinoId) {
        List<Disegno> disegni = disegnoRepository.findByBambini_Id(bambinoId);
        if (disegni.isEmpty()) {
            throw new NoSuchElementException(
                    "Nessun disegno trovato per il bambino con id "
                            + bambinoId);
        }
        return disegni.stream()
                .map(disegno -> new DisegnoDTOResponse(disegno.getId(),
                        disegno.getSessione().getTemaAssegnato(),
                        disegno.getData(), disegno.getVoto(),
                        disegno.getValutazioneEmotiva()))
                .toList();
    }

    /**
     * Metodo che permette di votare un disegno.
     * @param disegnoId id del disegno
     * @param voto voto da assegnare
     */
    public void vota(final Long disegnoId, final int voto) {
        Disegno disegno = disegnoRepository.findById(disegnoId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Disegno non trovato con id " + disegnoId));
        disegno.setVoto(voto);
        disegnoRepository.save(disegno);
    }
}
