package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.GetMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/terapeuta/materiale")
public class MaterialeController {

    /**
     * Service relativa al materiale.
     */
    private final MaterialeService materialeService;

    /**
     * Costruttore.
     * @param materialeService {@link MaterialeService}
     */
    @Autowired
    public MaterialeController(final MaterialeService materialeService) {
        this.materialeService = materialeService;
    }

    /**
     * Endpoint per l'ottenimento dei materiali caricati da un terapeuta.
     * @return 200 e json per successo e 204 se No Content
     */
    @GetMapping("getallbyterapeuta")
    public ResponseEntity<List<GetMaterialeDTO>> getMateriale() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        List<GetMaterialeDTO> materiali = materialeService.getClientMateriale(principal);

        if (materiali.isEmpty()) {
            return ResponseEntity.noContent().build(); // Restituisce 204 No Content
        }

        return ResponseEntity.ok(materiali);
    }
}
