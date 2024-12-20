package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/sessione")
public class SessioneController {

    @Autowired
    private SessioneService sessioneService;

    /**
     * Endpoint per la creazione di una sessione.
     * @param sessioneDTO DTO proveniente dal client
     * @return 200 OK
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/create")
    public ResponseEntity<SessioneDTO> create(@Valid @RequestBody SessioneDTO sessioneDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        sessioneService.creaSessione(sessioneDTO, principal.getTerapeuta());
        return ResponseEntity.ok(sessioneDTO);
    }

    /**
     * Endpoint per la terminazione di una sessione.
     * @param id id sessione
     * @return 200 OK oppure 404 Not Found
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PatchMapping("/sessioni/{id}/termina")
    public ResponseEntity<Void> terminaSessione (@PathVariable long id){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

            sessioneService.terminaSessione(id);
            return ResponseEntity.ok().build();
        } catch(EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
