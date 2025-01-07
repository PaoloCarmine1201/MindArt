
package com.is.mindart.security.controller;

import com.is.mindart.gestioneBambino.service.BambinoService;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import com.is.mindart.security.jwt.FileBasedTokenBlacklist;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    /**
     * Il servizio per la gestione dei token JWT expired.
     */
    @Autowired
    private final FileBasedTokenBlacklist tokenBlacklist;
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

    /**
     * Questo metodo gestisce la richiesta di logout.
     * @param authHeader L'header Authorization
     * @return Il messaggio di logout
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") final String authHeader) {
        final int bearerLength = 7;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(bearerLength);
            tokenBlacklist.addToken(token);
            return ResponseEntity.ok("Logout successful");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
