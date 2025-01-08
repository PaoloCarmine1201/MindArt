package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import com.is.mindart.gestioneBambino.model.Bambino;


@Service
@AllArgsConstructor
public class SessioneService {

    /**
     * Repository della sessione.
     */
    private final SessioneRepository repository;
    /**
     * Custom mapper della sessione.
     */
    private final SessioneMapper sessioneMapper;
    /**
     * Repository della sessione.
     */
    private final SessioneRepository sessioneRepository;

    /**
     * Repository del terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;
    /**
     * Repository del disegno.
     */
    private final DisegnoRepository disegnoRepository;

    /**
     * Repository del bambino.
     */
    private final BambinoRepository bambinoRepository;


    /**
     * Creazione della sessione.
     * Mappa {@link SessioneDTO} a {@link Sessione} e
     * aggiorna la lista delle sessioni per ogni Bambino
     * @param sessioneDto - proveniente dall'endpoint di creazione
     */
    @Transactional
    public void creaSessione(final SessioneDTO sessioneDto,
                             final String terapeutaEmail) {
        // Validate no active session exists
        if (!sessioneRepository.findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail).isEmpty()) {
            throw new IllegalArgumentException("Terapeuta ha già una sessione attiva");
        }
        // Fetch terapeuta
        Terapeuta terapeuta = terapeutaRepository.findByEmail(terapeutaEmail).orElseThrow(() ->
                new IllegalArgumentException("Terapeuta not found"));

        // Map DTO to entity
        Sessione sessione = sessioneMapper.toEntity(sessioneDto);
        sessione.setTerapeuta(terapeuta);

        // Persist Bambini if necessary
        if (sessione.getBambini() != null) {
            sessione.setBambini(sessione.getBambini().stream().map(bambino ->
                            bambinoRepository.findById(bambino.getId()).orElseThrow(() ->
                                            new IllegalArgumentException("Bambino not found")))
                            .collect(Collectors.toList())
            );
        }


        // Handle Disegno for DISEGNO sessions
        if (sessioneDto.getTipoSessione().equals(TipoSessione.DISEGNO)
                || sessioneDto.getTipoSessione().equals(TipoSessione.COLORE)) {
            Disegno disegno = new Disegno();
            disegno.setSessione(sessione);
            disegno.setTerapeuta(terapeuta);
            disegno.setBambini(sessione.getBambini());
            disegnoRepository.save(disegno);
        }

        // Persist Sessione
        sessioneRepository.save(sessione);
    }


    /**
     * Terminazione della sessione.
     * @param email email del terapeuta
     * @throws EntityNotFoundException se l'id non viene trovato
     */
    @Transactional
    public void terminaSessione(final String email)
            throws EntityNotFoundException {
        Sessione sessione = sessioneRepository
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Terapeuta con email " + email + " non trovato"));
        sessioneRepository.terminaSessione(sessione.getId());
    }
    /**
     * Terminazione della sessione.
     * @param codice codice del bambino
     * @param immagine immmagine che verrà valutat da IA
     * @throws EntityNotFoundException se l'id non viene trovato
     */
    @Transactional
    public void consegnaDisegno(final String codice, final byte[] immagine)
            throws EntityNotFoundException {
        Sessione sessione = sessioneRepository
                .findByTerminataFalseAndBambini_CodiceOrderByDataAsc(codice)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Bambino con codice " + codice + " non trovato"));
        Disegno disegno = disegnoRepository.findBySessioneId(sessione.getId())
                .orElseThrow(NoSuchElementException::new);

        disegno.setImmagine(immagine);

        sessioneRepository.terminaSessione(sessione.getId());
    }

    /**
     * Getter della sessione attiva del bambino
     * @param codice codice del bambino
     * @throws EntityNotFoundException se l'id non viene trovato
     */
    public SessioneDTO getSessioneBambino(final String codice)
            throws EntityNotFoundException {
        Sessione sessione = trovaSessioneDaCodiceBambino(codice);
        return new SessioneDTO(
                sessione.getId(),
                sessione.getTipo(),
                sessione.getTerapeuta().getId(),
                sessione.getTemaAssegnato(),
                sessione.getMateriale() != null ? sessione.getMateriale().getId() : null,
                sessione.getBambini().stream().map(Bambino::getId).toList()
        );
    }

    /**
     * Getter del Materiale
     * @param id id sessione
     * @throws EntityNotFoundException se la sessione non viene trovata
     */
    @Transactional
    public Materiale getMaterialeFromSessione(final long id)
            throws EntityNotFoundException {
        Sessione sessione = sessioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Sessione con codice " + id + " non trovato"));
        return sessione.getMateriale();

    }

    public Sessione trovaSessioneDaCodiceBambino(String codice) {
        return sessioneRepository
                .findByTerminataFalseAndBambini_CodiceOrderByDataAsc(codice)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Bambino con codice " + codice + " non trovato " +
                                "o sessione non valida"));

    }

}
