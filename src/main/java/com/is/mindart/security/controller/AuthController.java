
package com.is.mindart.security.controller;

import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import com.is.mindart.security.jwt.JwtUtil;
import com.is.mindart.security.service.BambinoUserDetailsService;
import com.is.mindart.security.service.TerapeutaUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
;

@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * L'oggetto AuthenticationManager è fornito da Spring Security e viene utilizzato
     * per autenticare un utente.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * L'oggetto TerapeutaUserDetailsService è fornito da Spring Security e viene utilizzato
     * per gestire i dettagli dell'utente terapeuta.
     */
    @Autowired
    private TerapeutaUserDetailsService terapeutaUserDetailsService;

    /**
     * L'oggetto BambinoUserDetailsService è fornito da Spring Security e viene utilizzato
     * per gestire i dettagli dell'utente bambino.
     */
    @Autowired
    private BambinoUserDetailsService bambinoUserDetailsService;


    /**
     * Il repository delle sessioni viene utilizzato per verificare se esiste almeno una sessione
     */
    @Autowired
    private SessioneRepository sessioneRepository;

    /**
     * L'oggetto JwtUtil è fornito da Spring Security e viene utilizzato
     * per generare il token JWT.
     */
    @Autowired
    private TerapeutaService terapeutaService;

    @Autowired
    private JwtUtil jwtUtil;

    // DTO per login terapeuta
    static class TerapeutaLoginRequest {
        public String email;
        public String password;
    }

    static class BambinoLoginRequest {
        public String codice;
    }

    /**
     * Questo metodo gestisce la richiesta di login per un terapeuta.
     * @param request La richiesta di login
     * @return Il token JWT
     */
    @PostMapping("/terapeuta/login")
    public ResponseEntity<String> loginTerapeuta(@RequestBody final TerapeutaLoginRequest request) {


        UserDetails userDetails = terapeutaUserDetailsService.loadUserByUsername(request.email);
        return ResponseEntity.ok(jwtUtil.generateToken(userDetails.getUsername(), "ROLE_TERAPEUTA"));
    }

    /**
     * Questo metodo gestisce la richiesta di login per un bambino.
     * @param request La richiesta di login
     * @return Il token JWT
     */
    @PostMapping("/bambino/login")
    public ResponseEntity<String> loginBambino(@RequestBody final BambinoLoginRequest request) {

        UserDetails userDetails = bambinoUserDetailsService.loadUserByUsername(request.codice);


        // Verifica se esiste almeno una sessione programmata per oggi
        Sessione session = sessioneRepository.findByTerminataFalseAndBambini_CodiceOrderByDataAsc(request.codice).stream()
                .findFirst()
                .orElse(null);

        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No session scheduled for today.");
        }

        // Verifica se la sessione è già iniziata
        if (session.getData().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Session not started yet.");
        }

        return ResponseEntity.ok(jwtUtil.generateToken(userDetails.getUsername(), "ROLE_BAMBINO"));
    }

    @PostMapping("/terapeuta/register")
    public ResponseEntity<TerapeutaDTO> registerTerapeuta(
            @Valid @RequestBody final TerapeutaDTO terapeutaDto) {
        terapeutaService.registerTerapeuta(terapeutaDto);
        return ResponseEntity.ok(terapeutaDto);
    }
}
