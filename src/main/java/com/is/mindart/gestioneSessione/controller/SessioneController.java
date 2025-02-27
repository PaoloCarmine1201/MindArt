package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SessioneController {
    /**
     * Servizio per la gestione delle sessioni.
     */
    private final SessioneService sessioneService;
    /**
     * Repository per la gestione delle sessioni.
     */
    private final SessioneRepository sessioneRepository;

    /**
     * Endpoint per la creazione di una sessione.
     * @param sessioneDTO DTO proveniente dal client
     * @return 200 OK
     */
    @PostMapping("/terapeuta/sessione/create")
    public ResponseEntity<Void> create(
            @Valid @RequestBody final SessioneDTO sessioneDTO) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        sessioneService.creaSessione(sessioneDTO, principal);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint per la terminazione di una sessione.
     * @return 200 OK oppure 404 Not Found
     */
    @PostMapping("/terapeuta/sessione/termina")
    public ResponseEntity<Void> terminaSessione() {
        try {
            Authentication authentication = SecurityContextHolder
                    .getContext().getAuthentication();
            String principal = (String) authentication.getPrincipal();
            sessioneService.terminaSessione(principal);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Endpoint per la consegna di un disegno.
     * @param immagine che verrà valutatada IA
     * @return 200 OK oppure 404 Not Found
     */
    @PostMapping("/bambino/sessione/consegna")
    public ResponseEntity<Void> consegnaDisegno(
            @RequestParam("image") final MultipartFile immagine
    ) {
        try {
            Authentication authentication = SecurityContextHolder
                    .getContext().getAuthentication();
            String principal = (String) authentication.getPrincipal();
            sessioneService.consegnaDisegno(principal, immagine.getBytes());
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint per la visualizzazione della sessione attiva.
     * @return 200 OK
     */
    @GetMapping("/terapeuta/sessione/")
    public ResponseEntity<SessioneDTO> getSessione() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        sessioneRepository
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(principal)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Terapeuta con email " + principal + " non trovato"));
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint che restiuisce la descrizione della sessione del bambino.
     * @return 200 OK
     */
    @GetMapping("/bambino/sessione/")
    public ResponseEntity<SessioneDTO> getSessioneBambino() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        return ResponseEntity.ok(sessioneService.getSessioneBambino(principal));

    }
}
