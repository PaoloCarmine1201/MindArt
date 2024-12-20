
package com.is.mindart.security.controller;

import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.security.jwt.JwtUtil;
import com.is.mindart.security.service.BambinoUserDetailsService;
import com.is.mindart.security.service.TerapeutaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TerapeutaUserDetailsService terapeutaUserDetailsService;

    @Autowired
    private BambinoUserDetailsService bambinoUserDetailsService;

    @Autowired
    private BambinoRepository bambinoRepository;

    @Autowired
    private SessioneRepository sessioneRepository;

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

    @PostMapping("/terapeuta/login")
    public ResponseEntity<String> loginTerapeuta(@RequestBody TerapeutaLoginRequest request) {


        UserDetails userDetails = terapeutaUserDetailsService.loadUserByUsername(request.email);
        return ResponseEntity.ok(jwtUtil.generateToken(userDetails.getUsername()));
    }

    @PostMapping("/bambino/login")
    public ResponseEntity<String> loginBambino(@RequestBody BambinoLoginRequest request) {

        UserDetails userDetails = bambinoUserDetailsService.loadUserByUsername(request.codice);


        // Verifica se esiste almeno una sessione programmata per oggi
        Sessione session = sessioneRepository.findByTerminataFalseAndBambini_CodiceOrderByDataAsc(request.codice).stream()
                .findFirst()
                .orElse( null);

        if( session == null ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No session scheduled for today.");
        }

        // Verifica se la sessione è già iniziata
        if (session.getData().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Session not started yet.");
        }


        return ResponseEntity.ok(jwtUtil.generateToken(userDetails.getUsername()));
    }
}
