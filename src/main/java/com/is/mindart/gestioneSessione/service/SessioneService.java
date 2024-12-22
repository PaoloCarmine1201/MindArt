package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
     * Costruttore.
     * @param repository
     * @param sessioneMapper
     * @param sessioneRepository
     */
    @Autowired
    public SessioneService(final SessioneRepository repository,
                           final SessioneMapper sessioneMapper,
                           final SessioneRepository sessioneRepository) {
        this.repository = repository;
        this.sessioneMapper = sessioneMapper;
        this.sessioneRepository = sessioneRepository;
    }

    /**
     * Creazione della sessione.
     * Mappa {@link SessioneDTO} a {@link Sessione} e
     * aggiorna la lista delle sessioni per ogni Bambino
     * @param sessioneDto - proveniente dall'endpoint di creazione
     */
    @Transactional
    public void creaSessione(final SessioneDTO sessioneDto, final Terapeuta terapeuta) {
        Sessione sessione = sessioneMapper.toEntity(sessioneDto);
        sessione.setTerapeuta(terapeuta);
        if (sessione.getBambini() != null) {
            sessione.getBambini().forEach(
                    bambino -> bambino.getSessioni().add(sessione));
        }
        repository.save(sessione);
    }

    /**
     * Terminazione della sessione.
     * @param id id sessione
     * @throws EntityNotFoundException se l'id non viene trovato
     */
    @Transactional
    public void terminaSessione(final long id, final Bambino bambino)
            throws EntityNotFoundException {
        //se la sessione appartiene al bambino può terminarla
        if(bambino.getSessioni().stream().noneMatch(sessione -> sessione.getId() == id)) {
            throw new EntityNotFoundException(
                    "Sessione con id " + id + " non trovato");
        }
        if (sessioneRepository.terminaSessione(id) == 0) {
            throw new EntityNotFoundException(
                    "Sessione con id " + id + " non trovato");
        }
    }
}
