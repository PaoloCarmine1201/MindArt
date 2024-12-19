package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/sessione")
public class SessioneController {

    @Autowired
    private SessioneService sessioneService;

    @PostMapping("/create")
    public ResponseEntity<SessioneDTO> create(@Valid @RequestBody SessioneDTO sessioneDTO){
        sessioneService.creaSessione(sessioneDTO);
        return ResponseEntity.ok(sessioneDTO);
    }
}
