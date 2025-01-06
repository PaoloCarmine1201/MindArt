package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneMateriale.service.MaterialeDTOResponse;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.gestioneMateriale.service.InputMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller per la gestione del materiale.
 */
@RestController
@RequestMapping("/api")
public class MaterialeController {

    /**
     * Service relativa al materiale.
     */
    private final MaterialeService materialeServiceInjected;
    /**
     * Repository del Terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     * Costruttore.
     *
     * @param materialeService {@link MaterialeService}
     * @param terapeutaRepositoryParam {@link TerapeutaRepository}
     */
    @Autowired
    public MaterialeController(
            final MaterialeService materialeService,
            final TerapeutaRepository terapeutaRepositoryParam) {
        // Assegna il service al campo della classe
        this.materialeServiceInjected = materialeService;
        this.terapeutaRepository = terapeutaRepositoryParam;
    }

    /**
     * Rimuove un materiale.
     * @param id id del materiale da rimuovere.
     * @return ResponseEntity con il messaggio di successo.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @DeleteMapping("/terapeuta/materiale/{id}")
    public ResponseEntity<String> removeMaterial(
            final @PathVariable long id) {
        // Rimuove il materiale specificato
        this.materialeServiceInjected.removeMaterial(id);
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
            value = "/terapeuta/materiale/update",
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
    @GetMapping("/terapeuta/materiale/")
    public ResponseEntity<List<OutputMaterialeDTO>> getMateriali() {
        // Estrae l'autenticazione dall'oggetto SecurityContextHolder
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String principal = (String) authentication.getPrincipal();

        long terapeutaId = terapeutaRepository
                .findByEmail(principal)
                .orElseThrow()
                .getId();

        // Recupera i materiali caricati dal terapeuta
        List<OutputMaterialeDTO> materiali = this.materialeServiceInjected
                .getClientMateriali(terapeutaId);

        // Se la lista è vuota, restituisce No Content
        if (materiali.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Altrimenti restituisce la lista dei materiali
        return ResponseEntity.ok(materiali);
    }

    /**
     * Aggiunge un nuovo materiale.
     * @param file File da salvare.
     * @return ResponseEntity con il materiale appena salvato
     *         o un errore.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping(value = "/terapeuta/materiale/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OutputMaterialeDTO> addMaterial(
            final @RequestParam("file") MultipartFile file) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String principal = (String) authentication.getPrincipal();

        long terapeutaId = terapeutaRepository
                .findByEmail(principal)
                .orElseThrow()
                .getId();

        // Verifica se il file non è vuoto
        if (file == null || file.isEmpty()) {
            throw new EmptyFileException("File nullo o vuoto");
        }

        // Controllo del tipo di file inviato
        String fileType = file.getContentType();
        if (fileType == null || fileType.isEmpty()) {
            throw new FileTypeNullException("FileType nullo o vuoto");
        }

        TipoMateriale tipoMateriale;

        // Verifica se il tipo di file è tra quelli supportati
        if (fileType.startsWith("application/pdf")) {
            tipoMateriale = TipoMateriale.PDF;
        } else if (fileType.startsWith("video/")) {
            tipoMateriale = TipoMateriale.VIDEO;
        } else if (fileType.startsWith("image/")) {
            tipoMateriale = TipoMateriale.IMMAGINE;
        } else {
            throw new UnsupportedFileException(
                    "Il tipo di file non è supportato");

        }

        //Estra nome dal file
        String nome = file.getOriginalFilename();
        // Crea il DTO di input per aggiungere il nuovo materiale
        InputMaterialeDTO inputMaterialeDTO = new InputMaterialeDTO(
                nome, tipoMateriale, terapeutaId, file);

        // Controlla se esiste già un materiale con lo stesso nome
        if (this.materialeServiceInjected.existsMateriale(inputMaterialeDTO)) {
            throw new DuplicatedFileException(
                    "Esiste già un file chiamato " + nome);
        }

        // Aggiunge il materiale e ottiene il risultato
        OutputMaterialeDTO outputMaterialeDTO = this.materialeServiceInjected
                .addMateriale(inputMaterialeDTO);

        // Restituisce l'oggetto creato
        return ResponseEntity.ok(outputMaterialeDTO);
    }

    /**
     * Ottiene il materiale associato a una specifica sessione.
     *
     * @return ResponseEntity con il MaterialeDTO o un errore.
     */
    @PreAuthorize("hasRole('BAMBINO')")
    @GetMapping("/bambino/materiale/sessione/")
    public ResponseEntity<MaterialeDTOResponse>
                        getMaterialeBySessioneBambino() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String principal = (String) authentication.getPrincipal();
        try {
            MaterialeDTOResponse materialeDTO = materialeServiceInjected
                    .getMaterialeByCodice(principal);
            return ResponseEntity.ok(materialeDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Ottiene il materiale associato a una specifica sessione.
     *
     * @return ResponseEntity con il MaterialeDTO o un errore.
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/terapeuta/materiale/sessione/")
    public ResponseEntity<MaterialeDTOResponse>
                        getMaterialeBySessioneTerapeuta() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String principal = (String) authentication.getPrincipal();
        try {
            MaterialeDTOResponse materialeDTO = materialeServiceInjected
                    .getMaterialeByEmail(principal);
            return ResponseEntity.ok(materialeDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
