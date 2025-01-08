package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaCambioPasswordDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOSimple;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOStat;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/terapeuta")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class TerapeutaController {

    /**
     * Il servizio per la gestione dei terapeuti.
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
     * Gestisce le richieste per il cambio della password.
     * @param request richiesta in forma di {@link TerapeutaCambioPasswordDTO}
     * @return ResponseEntity
     */
    @PostMapping("/cambia-password")
    public ResponseEntity<String> cambiaPassword(
            @RequestBody final TerapeutaCambioPasswordDTO request) {
        // Estrae l'autenticazione dall'oggetto SecurityContextHolder
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String principal = (String) authentication.getPrincipal();
        // Chiamata al service per cambiare la password
        // Se la password non è valida, ritorna BadRequest
        return
                terapeutaService.changePassword(
                        principal,
                        request.getOldPassword(),
                        request.getNewPassword()
                )
                        ?
                ResponseEntity.ok().build()
                        :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Aggiorna le informazioni del terapeuta.
     * @param terapeutaDTO in forma di {@link TerapeutaDTOSimple}
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
