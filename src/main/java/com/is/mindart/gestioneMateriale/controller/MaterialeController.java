package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.MaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.servlet.ServletContext;
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

    private static final String BASE_DIRECTORY = "src/materiali/";

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
    @PostMapping("/remove")
    public ResponseEntity<String> removeMaterial(MaterialeDTO materialeDTO) {
        try {
            materialeService.removeMaterial(materialeDTO);
            return ResponseEntity.ok("Material removed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error removing material: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/update")
    public ResponseEntity<String> updateMaterial(MaterialeDTO materialeDTO) {
        return null;
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
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(materiali);
    }

    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addMaterial(
            @RequestParam("terapeutaId") Long terapeutaId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("nome") String nome) {

        try {
            String fileType = file.getContentType();
            if (!(fileType != null && ("application/pdf".equals(fileType) || "video/mp4".equals(fileType)))) {
                return ResponseEntity.badRequest().body("File type not supported. Only PDF and MP4 allowed.");
            }

            Path directoryPath = Paths.get(BASE_DIRECTORY, String.valueOf(terapeutaId));
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            String fileName = nome + getFileExtension(file.getOriginalFilename());
            Path filePath = directoryPath.resolve(fileName);

            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("Material added to: " + filePath.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving material: " + e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

}
