package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.service.MaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.service.SessioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/api/bambino/sessione")
@RequiredArgsConstructor
public class SessioneControllerBambino {

    /**
     * Servizio per la gestione delle sessioni.
     */
    private final SessioneService sessioneService;


    /**
     *
     * @return Materiale far visualizzare il video
     */
    @GetMapping("/getMateriale/")
    public ResponseEntity<MaterialeDTO> getMateriale() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String codiceBambino = (String) authentication.getPrincipal();
        Sessione sessione = sessioneService
                .trovaSessioneDaCodiceBambino(codiceBambino);
        Materiale materiale = sessione.getMateriale();

        String path = materiale.getPath();
        try {
            byte[] file = Files.readAllBytes(Path.of(path));
            MaterialeDTO materialeDTO = new MaterialeDTO(
                    materiale.getId(),
                    materiale.getNome(),
                    materiale.getTipo(),
                    file
            );

            return ResponseEntity
                    .ok()
                    .body(materialeDTO);

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
