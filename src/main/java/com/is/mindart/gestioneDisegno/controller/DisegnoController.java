
package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.service.DisegnoDTO;
import com.is.mindart.gestioneDisegno.service.DisegnoDTOResponse;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

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
                .getFirst().getId();
        DisegnoDTO disegnoResponseDTO = disegnoService
                .getDisegnoBySessioneId(sessioneId);
        return ResponseEntity.ok(disegnoResponseDTO);
    }

    /**
     * Permette al terapeuta di votare un disegno.
     * @param disegnoId id del disegno.
     * @param valutazione voto da assegnare.
     * @return 200 OK
     */
    @PostMapping("terapeuta/disegno/{disegnoId}/valutazione")
    public ResponseEntity<Void> vota(
            @PathVariable final long disegnoId,
            @RequestBody final Map<String, String> valutazione) {
        disegnoService
                .vota(disegnoId, Integer
                        .parseInt(valutazione.get("valutazione")));
        return ResponseEntity.ok().build();
    }


    /**
     * Restituisce i disegni di un bambino.
     * @param id id del bambino
     * @return 200 OK
     */
    @GetMapping("terapeuta/bambino/{id}/disegni/")
    public ResponseEntity<List<DisegnoDTOResponse>> getDisegniByBambinoId(
            @PathVariable final Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        List<DisegnoDTOResponse> disegnoResponseDTO = disegnoService
                .getDisegniByBambinoId(id);
        return ResponseEntity.ok(disegnoResponseDTO);
    }

    /**
     * Restituisce i disegni di un bambino.
     * @param id id del bambino
     * @return 200 OK
     */
    @GetMapping("terapeuta/disegno/{id}")
    public ResponseEntity<DisegnoDTO> getDisegnoById(
            @PathVariable final Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        DisegnoDTO disegnoResponseDTO = disegnoService.getDisegnoById(id);
        return ResponseEntity.ok(disegnoResponseDTO);
    }
}
