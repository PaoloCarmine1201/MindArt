package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.MaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PreAuthorize("hasRole('TERAPEUTA')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeMaterial(
            @Valid InputMaterialeDTO inputMaterialeDTO
    ) {
        materialeService.removeMaterial(inputMaterialeDTO);
        return ResponseEntity.ok("Materiale rimosso con successo.");
    }

    @PreAuthorize("hasRole('TERAPEUTA')")
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMaterial(@Valid InputMaterialeDTO inputMaterialeDTO) {
        String fileType = inputMaterialeDTO.getFile().getContentType();
        if (!(("application/pdf".equals(fileType) || "video/mp4".equals(fileType)))) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il tipo di file non è supportato. Sono supportati solo file PDF e video MP4.")
                    .body(null);
        }

        if(inputMaterialeDTO.getFile().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il file da salvare è nullo.")
                    .body(null);
        }

        materialeService.updateMaterial(inputMaterialeDTO);

        return ResponseEntity.ok("Materiale aggiornato con successo.");
    }

    /**
     * Endpoint per l'ottenimento dei materiali caricati da un terapeuta.
     * @return 200 e json per successo e 204 se No Content
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/")
    public ResponseEntity<List<OutputMaterialeDTO>> getMateriali() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        List<OutputMaterialeDTO> materiali = materialeService.getClientMateriali(principal.getTerapeuta().getId());

        if (materiali.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(materiali);
    }

    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OutputMaterialeDTO> addMaterial(
            @RequestParam("terapeutaId") Long terapeutaId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("nome") String nome,
            @RequestParam("tipoMateriale") TipoMateriale tipoMateriale) {

        String fileType = file.getContentType();
        if (!(("application/pdf".equals(fileType) || "video/mp4".equals(fileType)))) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il tipo di file non è supportato. Sono supportati solo file PDF e video MP4.")
                    .body(null);
        }

        if(file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il file da salvare è nullo.")
                    .body(null);
        }

        InputMaterialeDTO inputMaterialeDTO = new InputMaterialeDTO(nome, tipoMateriale, terapeutaId, file);
        if(materialeService.existsMateriale(inputMaterialeDTO)) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Esiste già un file chiamato \"" + nome + "\" per questo terapeuta.")
                    .body(null);
        }

        OutputMaterialeDTO outputMaterialeDTO = materialeService.addMateriale(inputMaterialeDTO);

        return ResponseEntity.ok(outputMaterialeDTO);
    }
}
