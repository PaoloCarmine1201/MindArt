package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaCambioPasswordDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOSimple;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOStat;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * Aggiorna le informazioni del terapeuta.
     * @param terapeutaDTO TerapeutaDTOSimple
     * @return TerapeutaDTOSimple
     */
    /**
     *  Provvede ad accedere al database per l'entità Terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     *  Provvede a criptare la password.
     */
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/cambia-password")
    public ResponseEntity<String> cambiaPassword(
            @RequestBody final TerapeutaCambioPasswordDTO request) {
        // Estrae l'autenticazione dall'oggetto SecurityContextHolder
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String principal = (String) authentication.getPrincipal();

        Terapeuta terapeuta = terapeutaRepository.findById(request.getId()).orElseThrow();

        if (passwordEncoder.matches(request.getOldPassword(), terapeuta.getPassword())) {
            // Hash the password
            String hashedPassword = passwordEncoder.encode(request
                    .getNewPassword());
            terapeuta.setPassword(hashedPassword);
            terapeutaRepository.save(terapeuta);
            return ResponseEntity.ok("SUCCESS");
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

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
