
package com.is.mindart.gestioneDisegno.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneDisegno.model.DrawingData;
import com.is.mindart.gestioneDisegno.model.PointData;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DisegnoService {
    private final DisegnoRepository disegnoRepository;
    private final ObjectMapper objectMapper; // Per convertire da/verso JSON



    // Aggiunge lo stroke alla lista di stroke su Disegno
    public void addStrokeToDisegno(Long disegnoId, StrokeDTO newStroke) {
        Disegno disegno = disegnoRepository.findById(disegnoId)
                .orElseThrow(() -> new RuntimeException("Disegno non trovato con id " + disegnoId));

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


    // Converte List<StrokeDTO> -> JSON
    private String serializeStrokes(List<StrokeDTO> strokes) {
        try {
            return objectMapper.writeValueAsString(strokes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore durante la scrittura del JSON dei strokes", e);
        }
    }


    public DisegnoDTO getDisegnoById(Long disegnoId) {
        Disegno disegno = disegnoRepository.findById(disegnoId)
                .orElseThrow(() -> new NoSuchElementException("Disegno non trovato con id " + disegnoId));

        List<StrokeDTO> strokes = deserializeStrokes(disegno.getStrokesJson());

        return new DisegnoDTO(disegno.getId(), strokes);
    }
    public DisegnoDTO getDisegnoBySessioneId(Long sessioneId) {
        Disegno disegno = disegnoRepository.findBySessioneId(sessioneId)
                .orElseThrow(() -> new NoSuchElementException("Disegno non trovato per la sessione con id " + sessioneId));

        List<StrokeDTO> strokes = deserializeStrokes(disegno.getStrokesJson());

        return new DisegnoDTO(disegno.getId(), strokes);
    }

    /**
     * Converte la stringa JSON dei tratti in una lista di StrokeDTO.
     *
     * @param strokesJson JSON dei tratti
     * @return Lista di StrokeDTO
     */
    private List<StrokeDTO> deserializeStrokes(String strokesJson) {
        if (strokesJson == null || strokesJson.isBlank()) {
            return new ArrayList<>();
        }
        try {
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, StrokeDTO.class);
            return objectMapper.readValue(strokesJson, type);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la deserializzazione dei tratti", e);
        }
    }


}
