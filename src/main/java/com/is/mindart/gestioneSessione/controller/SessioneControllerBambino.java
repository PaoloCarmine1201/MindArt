package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.service.MaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * Servizio per la gestione delle materiale.
     */
    private final MaterialeService materialeService;


    /**
     *
     * @param id della sessione
     * @return Materiale far visualizzare il video
     */
    @GetMapping("/getMateriale/{id}")
    public ResponseEntity<MaterialeDTO> getMateriale(
            @PathVariable final long id) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String idBambino = (String) authentication.getPrincipal();

        Materiale materiale = sessioneService.getMaterialeFromSessione(id);

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

        }catch (IOException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
