package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessioneService {

    @Autowired
    private SessioneRepository repository;

    @Autowired
    private SessioneMapper sessioneMapper;

    public void creaSessione(SessioneDTO sessioneDto) {
        Sessione sessione = sessioneMapper.toEntity(sessioneDto);
        repository.save(sessione);
    }

}
