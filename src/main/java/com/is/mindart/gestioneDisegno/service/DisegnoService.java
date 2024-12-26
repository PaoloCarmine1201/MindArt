
package com.is.mindart.gestioneDisegno.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneDisegno.model.DrawingData;
import com.is.mindart.gestioneDisegno.model.StrokeData;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DisegnoService {

    @Autowired
    private DisegnoRepository disegnoRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private SessioneRepository sessioneRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BambinoRepository bambinoRepository;

    public DisegnoDTO createDisegno(final Long terapeutaId, final DisegnoDTO disegnoDTO) {
        // Recupera Terapeuta
        Terapeuta terapeuta = terapeutaRepository.findById(terapeutaId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Terapeuta non trovato con ID: "
                        + terapeutaId));

        // Recupera Sessione
        Sessione sessione = sessioneRepository.findById(
                disegnoDTO.getSessioneId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Sessione non trovata con ID: "
                                        + disegnoDTO.getSessioneId())
                );

        // Recupera Bambini
        List<Bambino> bambini = bambinoRepository
                .findAllById(disegnoDTO.getBambinoIds());
        if (bambini.size() != disegnoDTO.getBambinoIds().size()) {
            throw new NoSuchElementException("Alcuni Bambino IDs non sono validi");
        }

        // Crea Disegno
        Disegno disegno = new Disegno();
        disegno.setTerapeuta(terapeuta);
        disegno.setSessione(sessione);
        disegno.setBambini(bambini);

        // Salva nel database
        Disegno savedDisegno = disegnoRepository.save(disegno);

        disegnoDTO.setId(savedDisegno.getId());
        return disegnoDTO;
    }
    

    /**
     * Aggiorna il campo 'disegno' di un record Disegno esistente per una sessione.
     * Questo metodo è destinato esclusivamente alle operazioni WebSocket.
     *
     * @param drawingMessage I dati del disegno da aggiungere.
     * @return DisegnoResponseDTO del disegno aggiornato.
     * @throws Exception In caso di errori durante l'aggiornamento.
     */
    @Transactional
    public DisegnoResponseDTO updateDisegnoViaSocket(DisegnoMessage drawingMessage) throws Exception {
        try {


            // Trova il Disegno esistente per la sessione
            Disegno disegno = disegnoRepository.findBySessioneId(drawingMessage.getSessionId())
                    .orElseThrow(() -> new NoSuchElementException("Disegno non trovato per la sessione con ID: " + drawingMessage.getSessionId()));

            // Crea un nuovo StrokeDataDTO
            StrokeData newStroke = new StrokeData(
                    drawingMessage.getPoints(),
                    drawingMessage.getColor()
            );

            // Aggiungi il nuovo tratto alla lista dei tratti esistenti
            disegno.getDisegno().getStrokes().add(newStroke);

            // Salva il Disegno aggiornato nel database
            Disegno savedDisegno = disegnoRepository.save(disegno);

            // Mappa l'entità salvata al DTO di risposta
            DisegnoResponseDTO responseDTO = mapToResponseDTO(savedDisegno);
            return responseDTO;
        } catch (ObjectOptimisticLockingFailureException e) {
            // Gestione dell'eccezione di ottimistic locking
            throw new RuntimeException("Conflitto durante l'aggiornamento del Disegno. Riprova.", e);
        }
    }

    /**
     * Mappa un'entità Disegno a DisegnoResponseDTO.
     *
     * @param disegno L'entità Disegno.
     * @return DisegnoResponseDTO.
     */
    private DisegnoResponseDTO mapToResponseDTO(Disegno disegno) {
        DisegnoResponseDTO dto = new DisegnoResponseDTO();
        dto.setId(disegno.getId());
        dto.setDisegno(mapDrawingDataToDTO(disegno.getDisegno()));
        return dto;
    }

    /**
     * Mappa un'entità DrawingData a DrawingDataDTO.
     *
     * @param drawingData L'entità DrawingData.
     * @return DrawingDataDTO.
     */
    private DrawingDataDTO mapDrawingDataToDTO(DrawingData drawingData) {
        DrawingDataDTO dto = new DrawingDataDTO();
        drawingData.getStrokes().forEach(stroke -> {
            StrokeDataDTO strokeDTO = new StrokeDataDTO();
            strokeDTO.setPoints(stroke.getPoints());
            strokeDTO.setColor(stroke.getColor());
            dto.getStrokes().add(strokeDTO);
        });
        return dto;
    }

    /**
     * Recupera il disegno associato a una sessione.
     *
     * @param sessioneId L'ID della sessione.
     * @return DisegnoResponseDTO del disegno.
     * @throws Exception In caso di disegno non trovato o altri errori.
     */
    @Transactional
    public DisegnoResponseDTO getDisegnoBySessioneId(Long sessioneId) throws Exception {
        Disegno disegno = disegnoRepository.findBySessioneId(sessioneId)
                .orElseThrow(() -> new NoSuchElementException("Disegno non trovato per la sessione con ID: " + sessioneId));

        DisegnoResponseDTO responseDTO = mapToResponseDTO(disegno);
        return responseDTO;
    }


}
