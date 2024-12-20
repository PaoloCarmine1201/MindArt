package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class SessioneService {
    private final SessioneRepository sessioneRepository;
    private final BambinoRepository bambinoRepository;

    @Autowired
    public SessioneService(SessioneRepository sessioneRepository, BambinoRepository bambinoRepository) {
        this.sessioneRepository = sessioneRepository;
        this.bambinoRepository = bambinoRepository;
    }
}
