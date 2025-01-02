package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOSimple;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOStat;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/terapeuta")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class TerapeutaController {

    /**
     * Servizio per la gestione del terapeuta.
     */
    private final TerapeutaService terapeutaService;

    /**
     * Restituisce le informazioni del terapeuta loggato.
     * @return TerapeutaDTOStat
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/get")
    public ResponseEntity<TerapeutaDTOStat> getTerapeuta() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getPrincipal().toString();
        TerapeutaDTOStat terapeuta = terapeutaService.getTerapeuta(email);
        return ResponseEntity.ok(terapeuta);
    }
    /**
     * Aggiorna le informazioni del terapeuta.
     * @param terapeutaDTO TerapeutaDTOSimple
     * @return TerapeutaDTOSimple
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/update")
    public ResponseEntity<TerapeutaDTOSimple> updateTerapeuta(
            @RequestBody final TerapeutaDTOSimple terapeutaDTO
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDTOSimple terapeuta =
                terapeutaService.updateTerapeuta(
                        terapeutaDTO, (String) authentication.getPrincipal()
                );
        return ResponseEntity.ok(terapeuta);
    }
}
