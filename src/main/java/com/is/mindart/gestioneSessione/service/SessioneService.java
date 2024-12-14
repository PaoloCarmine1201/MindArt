package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessioneService {

    private final SessioneRepository repository;
    private final SessioneMapper sessioneMapper;

    @Autowired
    public SessioneService(SessioneRepository repository, SessioneMapper sessioneMapper) {
        this.repository = repository;
        this.sessioneMapper = sessioneMapper;
    }

    @Transactional
    public void creaSessione(SessioneDTO sessioneDto) {
        Sessione sessione = sessioneMapper.toEntity(sessioneDto);
        if (sessione.getBambini() != null) {
            sessione.getBambini().forEach(bambino -> bambino.getSessioni().add(sessione));
        }
        repository.save(sessione);
    }
}
