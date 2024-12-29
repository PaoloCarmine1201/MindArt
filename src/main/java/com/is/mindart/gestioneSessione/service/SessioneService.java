package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
     */
    @Transactional
    public void creaSessione(final SessioneDTO sessioneDto, final String terapeuta) {
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
     * @param id id sessione
     * @throws EntityNotFoundException se il bambino o la sessione non vengono
     * trovati nel database
     */
    @Transactional
    public void terminaSessione(final long id, final String codice)
            throws EntityNotFoundException {
        Bambino bambino = bambinoRepository.findByCodice(codice)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Bambino con codice " + codice + " non trovato"));
        //se la sessione appartiene al bambino può terminarla
        if (bambino.getSessioni().stream().noneMatch(sessione -> sessione.getId() == id)) {
            throw new EntityNotFoundException(
                    "Sessione con id " + id + " non trovato");
        }
        sessioneRepository.terminaSessione(id);
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
}
