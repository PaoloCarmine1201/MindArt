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
     *  Provvede ad accedere al database per l'entità Terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     * Metodo per validare una password rispetto a determinati criteri.
     * @param password la password da validare
     * @return true se la password è valida, false altrimenti.
     */
    private boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        // Regex per verificare i criteri
        String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!?_.,:;@#$%^&*]).{8,}$";
        return password.matches(passwordPattern);
    }

    /**
     *  Provvede a criptare la password.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     *  Provvede a mappare l'entità Terapeuta con TerapeutaDTO.
     * @param request richiesta
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

        Terapeuta terapeuta = terapeutaRepository.
                findByEmail(principal).orElseThrow();

        if (passwordEncoder.matches(
                request.getOldPassword(),
                terapeuta.getPassword())) {
            if (!isPasswordValid(request.getNewPassword())){
                throw new IllegalArgumentException("Password non conforme");
            }
            // Hash the password
            String hashedPassword = passwordEncoder.encode(request
                    .getNewPassword());
            terapeuta.setPassword(hashedPassword);
            terapeutaRepository.save(terapeuta);
            return ResponseEntity.ok("SUCCESS");
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
