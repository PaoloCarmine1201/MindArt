package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/terapeuta")
@CrossOrigin(origins = "http://localhost:3000")
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
