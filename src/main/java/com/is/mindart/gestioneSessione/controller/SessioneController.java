package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneBambino.controller.BambinoController;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import com.is.mindart.security.model.BambinoDetails;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@Controller
@RequestMapping("/api/terapeuta/sessione")
@RequiredArgsConstructor
public class SessioneController {

    /**
     * Servizio per la gestione delle sessioni.
     */
    private SessioneService sessioneService;

    /**
     * Endpoint per la creazione di una sessione.
     * @param sessioneDTO DTO proveniente dal client
     * @return 200 OK
     */
    @PostMapping("/create")
    public ResponseEntity<SessioneDTO> create(@Valid @RequestBody SessioneDTO sessioneDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        sessioneDTO.setIdTerapeuta(principal.getTerapeuta().getId());

        sessioneService.creaSessione(sessioneDTO);
        return ResponseEntity.ok(sessioneDTO);
    }

    /**
     * Endpoint per la terminazione di una sessione.
     * @param id id sessione
     * @return 200 OK oppure 404 Not Found
     */
    @PatchMapping("/sessioni/{id}/termina")
    public ResponseEntity<Void> terminaSessione (@PathVariable long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BambinoDetails principal = (BambinoDetails) authentication.getPrincipal();

            sessioneService.terminaSessione(id, principal.getBambino());
            return ResponseEntity.ok().build();
        } catch(EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
