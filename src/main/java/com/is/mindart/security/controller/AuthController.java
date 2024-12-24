
package com.is.mindart.security.controller;

import com.is.mindart.gestioneBambino.service.BambinoService;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import com.is.mindart.security.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * L'oggetto AuthenticationManager è fornito da Spring Security e viene utilizzato
     * per autenticare un utente.
     */
    private final AuthenticationManager authenticationManager;


    /**
     * Il repository delle sessioni viene utilizzato per verificare se esiste almeno una sessione
     */
   private final SessioneRepository sessioneRepository;

    /**
     * L'oggetto JwtUtil è fornito da Spring Security e viene utilizzato
     * per generare il token JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Il servizio per la gestione dei terapeuti.
     */
    private final TerapeutaService terapeutaService;

    /**
     * Il servizio per la gestione dei bambini.
     */
    private final BambinoService bambinoService;


    static class BambinoLoginRequest {
        public String codice;
    }

    /**
     * Questo metodo gestisce la richiesta di login per un terapeuta.
     * @param request La richiesta di login
     * @return Il token JWT
     */
    @PostMapping("/terapeuta/login")
    public ResponseEntity<String> loginTerapeuta(@RequestBody final TerapeutaLoginRequest request) throws AuthenticationException {
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
    public ResponseEntity<String> loginBambino(@RequestBody final BambinoLoginRequest request) {
        String token = bambinoService.loginBambino(request.codice);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);

    }

    /**
     *
     * @param terapeutaDto
     * @return
     */
    @PostMapping("/terapeuta/register")
    public ResponseEntity<TerapeutaDTO> registerTerapeuta(
            @Valid @RequestBody final TerapeutaDTO terapeutaDto) {
        terapeutaService.registerTerapeuta(terapeutaDto);
        return ResponseEntity.ok(terapeutaDto);
    }
}
