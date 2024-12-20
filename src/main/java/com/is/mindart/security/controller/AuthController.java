
package com.is.mindart.security.controller;

import com.is.mindart.security.jwt.JwtUtil;
import com.is.mindart.security.model.BambinoDetails;
import com.is.mindart.security.service.BambinoUserDetailsService;
import com.is.mindart.security.service.TerapeutaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    private JwtUtil jwtUtil;

    // DTO per login terapeuta
    static class TerapeutaLoginRequest {
        public String email;
        public String password;
    }

    // DTO per login bambino
    static class BambinoLoginRequest {
        public String codiceFiscale;
        public String codice;
    }

    @PostMapping("/terapeuta/login")
    public String loginTerapeuta(@RequestBody TerapeutaLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email, request.password)
        );
        UserDetails userDetails = terapeutaUserDetailsService.loadUserByUsername(request.email);
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/bambino/login")
    public String loginBambino(@RequestBody BambinoLoginRequest request) {
        // Non usiamo l'AuthenticationManager classico perché qui non abbiamo password encoder classico.
        // Possiamo considerare che il bambinoUserDetailsService ha già validato il codice.
        BambinoDetails bambinoDetails = bambinoUserDetailsService.loadBambinoByCodiceFiscaleECodice(
                request.codiceFiscale, request.codice
        );
        // Generiamo un token basato sul codiceFiscale
        return jwtUtil.generateToken(bambinoDetails.getUsername());
    }
}
