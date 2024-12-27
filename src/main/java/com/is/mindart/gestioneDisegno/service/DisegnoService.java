
package com.is.mindart.gestioneDisegno.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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
     */
    @Transactional
    public DisegnoResponseDTO updateDisegnoViaSocket( final DisegnoMessage drawingMessage) {
        try {

            System.out.println("Aggiornamento disegno via socket con messaggio: " + drawingMessage);

            Disegno disegno = disegnoRepository.findBySessioneId(drawingMessage.getSessionId())
                    .orElseThrow(() -> new NoSuchElementException("Disegno non trovato per la sessione con ID: " + drawingMessage.getSessionId()));

            // Gestione dei punti
            List<Integer> points = drawingMessage.getPoints();
            if (points != null && !points.isEmpty()) {
                if (points.size() % 2 != 0) {
                    throw new IllegalArgumentException("La lista dei punti deve contenere un numero pari di elementi.");
                }

                for (int i = 0; i < points.size(); i += 2) {
                    int x = points.get(i);
                    int y = points.get(i + 1);

                    PointData newPoint = new PointData();
                    newPoint.setPoints(Arrays.asList(x, y));
                    newPoint.setColor(drawingMessage.getColor());

                    System.out.println("Aggiungo nuovo punto: " + newPoint);
                    disegno.getDisegno().getStrokes().add(newPoint);
                }
            } else {
                System.out.println("Nessun punto da aggiungere.");
            }

            Disegno savedDisegno = disegnoRepository.save(disegno);
            DisegnoResponseDTO responseDTO = mapToResponseDTO(savedDisegno);

            System.out.println("Disegno salvato e mappato al DTO: " + responseDTO);
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
            PointDataDTO strokeDTO = new PointDataDTO();
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
    public DisegnoResponseDTO getDisegnoBySessioneId(Long sessioneId)  {
        Disegno disegno = disegnoRepository.findBySessioneId(sessioneId)
                .orElseThrow(() -> new NoSuchElementException("Disegno non trovato per la sessione con ID: " + sessioneId));

        DisegnoResponseDTO responseDTO = mapToResponseDTO(disegno);
        return responseDTO;
    }


}
