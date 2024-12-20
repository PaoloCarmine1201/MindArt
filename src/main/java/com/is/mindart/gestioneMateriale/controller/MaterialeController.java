package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.GetMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/materiale")
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
    @GetMapping()
    public ResponseEntity<List<GetMaterialeDTO>> getMateriale() {
        long idTerapeuta = 5; // TODO: cambia quando viene implementato il meccanismo di sessioni
        List<GetMaterialeDTO> materiali = materialeService.getClientMateriale(idTerapeuta);

        if (materiali.isEmpty()) {
            return ResponseEntity.noContent().build(); // Restituisce 204 No Content
        }

        return ResponseEntity.ok(materiali);
    }
}
