package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.MaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/terapeuta/materiale")
public class MaterialeController {

    private static final String MATERIALI_PATH = "../src/materiali/";

    /**
     * Service relativa al materiale.
     */
    private final MaterialeService materialeService;

    /**
     * Costruttore.
     * @param materialeService {@link MaterialeService}
     */
    @Autowired
    public MaterialeController(final MaterialeService materialeService, ServletContext context) {
        this.materialeService = materialeService;
    }

    /**
     * Endpoint per l'ottenimento dei materiali caricati da un terapeuta.
     * @return 200 e json per successo e 204 se No Content
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/")
    public ResponseEntity<List<MaterialeDTO>> getMateriale() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        List<MaterialeDTO> materiali = materialeService.getClientMateriale(principal.getTerapeuta().getId());

        if (materiali.isEmpty()) {
            return ResponseEntity.noContent().build(); // Restituisce 204 No Content
        }

        return ResponseEntity.ok(materiali);
    }

    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/save")
    public ResponseEntity<String> addMaterial(@Valid @RequestBody MaterialeDTO materialeDTO) {


        return ResponseEntity.ok("Material added");
    }

}
