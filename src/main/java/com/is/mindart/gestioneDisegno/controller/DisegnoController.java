package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.service.DisegnoResponseDTO;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller per gestire le operazioni relative ai Disegni.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DisegnoController {
    /**
     * Servizio per la gestione dei disegni.
     */
    private final DisegnoService disegnoService;

    /**
     * Restituisce il disegno associato ad una sessione.
     * @param sessioneId id della sessione
     * @return 200 OK
     */
    @GetMapping("/bambino/sessione/{sessioneId}")
    public ResponseEntity<DisegnoResponseDTO> getDisegnoBySessioneId(@PathVariable Long sessioneId) {
        DisegnoResponseDTO disegnoResponseDTO = disegnoService.getDisegnoBySessioneId(sessioneId);
        return ResponseEntity.ok(disegnoResponseDTO);
    }
}
