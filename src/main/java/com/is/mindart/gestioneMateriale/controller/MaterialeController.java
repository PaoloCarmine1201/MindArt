package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneMateriale.service.InputMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller per la gestione del materiale.
 */
@RestController
@RequestMapping("/api/terapeuta/materiale")
public class MaterialeController {

    /**
     * Service relativa al materiale.
     */
    private final MaterialeService materialeServiceInjected;

    /**
     * Costruttore.
     *
     * @param materialeService {@link MaterialeService}
     */
    @Autowired
    public MaterialeController(final MaterialeService materialeService) {
        // Assegna il service al campo della classe
        this.materialeServiceInjected = materialeService;
    }

    /**
     * Rimuove un materiale.
     *
     * @param inputMaterialeDTO DTO contenente i dati del
     *                          materiale da rimuovere.
     * @return ResponseEntity con il messaggio di successo.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeMaterial(
            final @Valid InputMaterialeDTO inputMaterialeDTO) {
        // Rimuove il materiale specificato
        this.materialeServiceInjected.removeMaterial(inputMaterialeDTO);
        return ResponseEntity.ok("Materiale rimosso con successo.");
    }

    /**
     * Aggiorna un materiale.
     *
     * @param inputMaterialeDTO DTO contenente i dati del
     *                          materiale da aggiornare.
     * @return ResponseEntity con il messaggio di successo
     *         o di errore.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PutMapping(
            value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> updateMaterial(
            final @Valid InputMaterialeDTO inputMaterialeDTO) {
        // Controllo del tipo di file inviato
        String fileType = inputMaterialeDTO.getFile().getContentType();
        assert fileType != null;

        // Verifica se il tipo di file è tra quelli supportati
        if (!(fileType.startsWith("application/pdf")
                || fileType.startsWith("video/")
                || fileType.startsWith("image/"))) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il tipo di file non è supportato. "
                            + "Sono supportati solo file PDF, "
                            + "video e immagini.")
                    .body(null);
        }

        // Verifica se il file non è vuoto
        if (inputMaterialeDTO.getFile().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il file da salvare è nullo.")
                    .body(null);
        }

        // Aggiorna effettivamente il materiale
        this.materialeServiceInjected.updateMaterial(inputMaterialeDTO);
        return ResponseEntity.ok("Materiale aggiornato con successo.");
    }

    /**
     * Ottiene i materiali caricati da un terapeuta.
     *
     * @return ResponseEntity con la lista dei materiali
     *         o No Content se non ne esistono.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/")
    public ResponseEntity<List<OutputMaterialeDTO>> getMateriali() {
        // Estrae l'autenticazione dall'oggetto SecurityContextHolder
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Ottiene i dettagli del terapeuta loggato
        TerapeutaDetails principal = (TerapeutaDetails) authentication
                .getPrincipal();

        // Recupera i materiali caricati dal terapeuta
        List<OutputMaterialeDTO> materiali = this.materialeServiceInjected
                .getClientMateriali(principal.getTerapeuta().getId());

        // Se la lista è vuota, restituisce No Content
        if (materiali.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Altrimenti restituisce la lista dei materiali
        return ResponseEntity.ok(materiali);
    }

    /**
     * Aggiunge un nuovo materiale.
     *
     * @param terapeutaId   ID del terapeuta.
     * @param file          File da salvare.
     * @param nome          Nome del file da salvare (con estensione).
     * @param tipoMateriale Tipo del materiale (PDF, IMMAGINE, VIDEO).
     * @return ResponseEntity con il materiale appena salvato
     *         o un errore.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OutputMaterialeDTO> addMaterial(
            final @RequestParam("terapeutaId") Long terapeutaId,
            final @RequestParam("file") MultipartFile file,
            final @RequestParam("nome") String nome,
            final @RequestParam("tipoMateriale") TipoMateriale tipoMateriale) {

        // Controllo del tipo di file inviato
        String fileType = file.getContentType();
        assert fileType != null;

        // Verifica se il tipo di file è tra quelli supportati
        if (!(fileType.startsWith("application/pdf")
                || fileType.startsWith("video/")
                || fileType.startsWith("image/"))) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il tipo di file non è supportato. "
                            + "Sono supportati solo file PDF, "
                            + "video e immagini.")
                    .body(null);
        }

        // Verifica se il file non è vuoto
        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Il file da salvare è nullo.")
                    .body(null);
        }

        // Crea il DTO di input per aggiungere il nuovo materiale
        InputMaterialeDTO inputMaterialeDTO = new InputMaterialeDTO(
                nome, tipoMateriale, terapeutaId, file);

        // Controlla se esiste già un materiale con lo stesso nome
        if (this.materialeServiceInjected.existsMateriale(inputMaterialeDTO)) {
            return ResponseEntity
                    .badRequest()
                    .header("Error", "Esiste già un file chiamato \""
                            + nome + "\" per questo terapeuta.")
                    .body(null);
        }

        // Aggiunge il materiale e ottiene il risultato
        OutputMaterialeDTO outputMaterialeDTO = this.materialeServiceInjected
                .addMateriale(inputMaterialeDTO);

        // Restituisce l'oggetto creato
        return ResponseEntity.ok(outputMaterialeDTO);
    }
}
