
package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.service.DisegnoDTO;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller per gestire le operazioni relative ai Disegni.
 */
@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class DisegnoController {
    /**
     * Servizio per la gestione dei disegni.
     */
    private final DisegnoService disegnoService;
    /**
     * Repository per la gestione delle sessioni.
     */
    private final SessioneRepository sessioneRepository;

    /**
     * Restituisce il disegno associato ad una sessione per il bambino.
     * @return 200 OK
     */
    @GetMapping("bambino/sessione/disegno/")
    public ResponseEntity<DisegnoDTO> getDisegnoBySessioneId() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        Long sessioneId = sessioneRepository
                .findByTerminataFalseAndBambini_CodiceOrderByDataAsc(principal)
                .getFirst().getId();

        DisegnoDTO disegnoResponseDTO = disegnoService
                .getDisegnoBySessioneId(sessioneId);
        return ResponseEntity.ok(disegnoResponseDTO);
    }

    /**
     * Restituisce il disegno associato ad una sessione per il terapeuta.
     * @return 200 OK
     */
    @GetMapping("terapeuta/sessione/disegno/")
    public ResponseEntity<DisegnoDTO> getDisegnoTerapeutaBySessioneId() {
       Authentication authentication = SecurityContextHolder
               .getContext().getAuthentication();
         String principal = (String) authentication.getPrincipal();
        Long sessioneId = sessioneRepository
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(principal)
                .get(0).getId();
            DisegnoDTO disegnoResponseDTO = disegnoService
                    .getDisegnoBySessioneId(sessioneId);
        return ResponseEntity.ok(disegnoResponseDTO);
    }

}
