package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/api/terapeuta/sessione")
@RequiredArgsConstructor
public class SessioneController {

    /**
     * Servizio per la gestione delle sessioni.
     */
    private final SessioneService sessioneService;

    /**
     * Endpoint per la creazione di una sessione.
     * @param sessioneDTO DTO proveniente dal client
     * @return 200 OK
     */
    @PostMapping("terapeuta/sessione/create")
    public ResponseEntity<SessioneDTO> create(
            @Valid @RequestBody final SessioneDTO sessioneDTO) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        sessioneService.creaSessione(sessioneDTO, principal);
        return ResponseEntity.ok(sessioneDTO);
    }

    /**
     * Endpoint per la terminazione di una sessione.
     * @param id id sessione
     * @return 200 OK oppure 404 Not Found
     */
    @PatchMapping("terapeuta/sessione/{id}/termina")
    public ResponseEntity<Void> terminaSessione(@PathVariable final long id) {
        try {
            Authentication authentication = SecurityContextHolder
                    .getContext().getAuthentication();
            String principal = (String) authentication.getPrincipal();
            sessioneService.terminaSessione(id, principal);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
