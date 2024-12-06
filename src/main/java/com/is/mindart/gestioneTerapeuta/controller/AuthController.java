package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/terapeuta")
public class AuthController {

    /**
     * Prende il service del terapeuta.
     */
    @Autowired
    private TerapeutaService terapeutaService;

    /**
     * @param terapeutaDto - DTO del terapeuta
     * @return ResponseEntity
     */
    @PostMapping("/register")
    public ResponseEntity<TerapeutaDTO> registerTerapeuta(
            @Valid @RequestBody final TerapeutaDTO terapeutaDto) {
        terapeutaService.registerTerapeuta(terapeutaDto);
        return ResponseEntity.ok(terapeutaDto);
    }
}
