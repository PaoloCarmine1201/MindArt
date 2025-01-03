
package com.is.mindart.security.controller;

import com.is.mindart.gestioneBambino.service.BambinoService;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    /**
     * Il servizio per la gestione dei terapeuti.
     */
    private final TerapeutaService terapeutaService;

    /**
     * Il servizio per la gestione dei bambini.
     */
    private final BambinoService bambinoService;

    /**
     * Questo metodo gestisce la richiesta di login per un terapeuta.
     * @param request La richiesta di login
     * @return Il token JWT
     */
    @PostMapping("/terapeuta/login")
    public ResponseEntity<String> loginTerapeuta(
            @RequestBody final TerapeutaLoginRequest request)
            throws AuthenticationException {
           String token = terapeutaService.loginTerapeuta(
                   request.getEmail(),
                   request.getPassword());
              if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
              }
                return ResponseEntity.ok(token);
    }

    /**
     * Questo metodo gestisce la richiesta di login per un bambino.
     * @param request La richiesta di login
     * @return Il token JWT
     */
    @PostMapping("/bambino/login")
    public ResponseEntity<String> loginBambino(
            @RequestBody final BambinoLoginRequest request) {
        String token = bambinoService.loginBambino(request.getCodice());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);

    }

    /**
     * Questo metodo gestisce la richiesta di registrazione per un terapeuta.
     * @param terapeutaDto Il terapeuta da registrare
     * @return Il terapeuta registrato
     */
    @PostMapping("/terapeuta/register")
    public ResponseEntity<TerapeutaDTO> registerTerapeuta(
            @Valid @RequestBody final TerapeutaDTO terapeutaDto) {
        terapeutaService.registerTerapeuta(terapeutaDto);
        return ResponseEntity.ok(terapeutaDto);
    }
}
