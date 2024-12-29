package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
     * Repository del bambino.
     */
    private final BambinoRepository bambinoRepository;


    /**
     * Creazione della sessione.
     * Mappa {@link SessioneDTO} a {@link Sessione} e
     * aggiorna la lista delle sessioni per ogni Bambino
     * @param sessioneDto - proveniente dall'endpoint di creazione
     * @param terapeuta terapeuta che desidera creare la sessione
     */
    @Transactional
    public void creaSessione(final SessioneDTO sessioneDto,
                             final String terapeuta) {
        Optional<Terapeuta> t = terapeutaRepository.findByEmail(terapeuta);
        Sessione sessione = sessioneMapper.toEntity(sessioneDto);
        sessione.setTerapeuta(t.get());
        if (sessione.getBambini() != null) {
            sessione.getBambini().forEach(
                    bambino -> bambino.getSessioni().add(sessione));
        }
        repository.save(sessione);
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
     * @throws EntityNotFoundException se l'id non viene trovato
     */
    @Transactional
    public void consegnaDisegno(final String codice)
            throws EntityNotFoundException {
        Sessione sessione = sessioneRepository
                .findByTerminataFalseAndBambini_CodiceOrderByDataAsc(codice)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Bambino con codice " + codice + " non trovato"));
        sessioneRepository.terminaSessione(sessione.getId());
    }
}
