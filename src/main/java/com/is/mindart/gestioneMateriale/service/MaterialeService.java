package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.configuration.MaterialeMapper;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service per la gestione delle operazioni relative ai materiali.
 * Offre metodi per aggiungere, rimuovere, aggiornare o recuperare
 * i materiali associati ai terapeuti.
 */
@Service
public class MaterialeService {

    /**
     * Repository relativa al materiale.
     */
    private final MaterialeRepository materialeRepositoryInjected;

    /**
     * Repository relativa al terapeuta.
     */
    private final TerapeutaRepository terapeutaRepositoryInjected;

    /**
     * Mapper personalizzato per convertire tra entità e DTO.
     */
    private final MaterialeMapper materialeMapperInjected;

    /**
     * Directory di base dove salvare i file.
     */
    private static final String BASE_DIRECTORY = "src/materiali/";

    /**
     * Logger per tracciare eventi e possibili errori.
     */
    private final Logger logger = Logger.getLogger(
            MaterialeService.class.getName()
    );

    /**
     * Costruttore principale per l'iniezione delle dipendenze.
     *
     * @param paramMaterialeRepository  Repository per gestire
     *                                  l'entità Materiale
     * @param paramMaterialeMapper      Mapper per convertire tra
     *                                  entità e DTO
     * @param paramTerapeutaRepository  Repository per gestire
     *                                  l'entità Terapeuta
     */
    @Autowired
    public MaterialeService(
            final MaterialeRepository paramMaterialeRepository,
            final MaterialeMapper paramMaterialeMapper,
            final TerapeutaRepository paramTerapeutaRepository
    ) {
        // Assegniamo alle variabili di istanza per evitare campi nascosti
        this.materialeRepositoryInjected = paramMaterialeRepository;
        this.materialeMapperInjected = paramMaterialeMapper;
        this.terapeutaRepositoryInjected = paramTerapeutaRepository;
    }

    /**
     * Trova tutti i materiali associati al terapeuta e li converte in
     * {@link OutputMaterialeDTO} per la visualizzazione sul client.
     *
     * @param terapeutaId ID del terapeuta loggato
     * @return Lista di {@link OutputMaterialeDTO}
     */
    public List<OutputMaterialeDTO> getClientMateriali(
            final long terapeutaId
    ) {
        return this.materialeRepositoryInjected
                .findByTerapeutaId(terapeutaId)
                .stream()
                .map(this.materialeMapperInjected::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifica se esiste già un materiale con lo stesso nome e tipo
     * per uno specifico terapeuta.
     *
     * @param inputMaterialeDTO DTO contenente i dati del materiale
     * @return true se il materiale esiste già, false altrimenti
     */
    public boolean existsMateriale(
            final InputMaterialeDTO inputMaterialeDTO
    ) {
        return this.materialeRepositoryInjected.existsByNomeAndTipoAndTerapeuta(
                inputMaterialeDTO.getNome(),
                inputMaterialeDTO.getTipoMateriale(),
                this.terapeutaRepositoryInjected.findById(
                        inputMaterialeDTO.getTerapeutaId()
                ).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Terapeuta non trovato"
                        )
                )
        );
    }

    /**
     * Aggiunge un nuovo materiale salvando il file nella directory
     * dedicata e persistendo i dati nel database.
     *
     * @param materialeDTO DTO contenente i dati del materiale
     * @return {@link OutputMaterialeDTO} del materiale salvato
     */
    public OutputMaterialeDTO addMateriale(
            final InputMaterialeDTO materialeDTO
    ) {
        // Percorso della cartella basato sul terapeutaId
        Path directoryPath = Paths.get(
                BASE_DIRECTORY,
                String.valueOf(materialeDTO.getTerapeutaId())
        );

        // Crea la directory se non esiste
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                this.logger.log(
                        Level.SEVERE,
                        "Errore nella creazione della directory: {0}",
                        e.getMessage()
                );
            }
        }

        // Risolve il percorso completo del file
        String fileName = materialeDTO.getNome();
        Path filePath = directoryPath.resolve(fileName);
        try {
            // Scrive i byte del file sul filesystem
            Files.write(filePath, materialeDTO.getFile().getBytes());
        } catch (IOException e) {
            this.logger.log(
                    Level.SEVERE,
                    "Errore durante la scrittura del file : {0}",
                    e.getMessage()
            );
        }

        // Converte il DTO in entità e salva nel database
        Materiale materiale = this.materialeRepositoryInjected.save(
                this.materialeMapperInjected.toEntity(
                        materialeDTO,
                        filePath.toString()
                )
        );

        // Ritorna il DTO del materiale appena creato
        return this.materialeMapperInjected.toDTO(materiale);
    }

    public ByteArrayResource getByteArray(final String path) throws IOException {
        // Creazione del file dal percorso
        File file = new File(path);
        // Lettura del contenuto del file
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = fis.readAllBytes();
        fis.close();
        // Converte il file in una risorsa
        return new ByteArrayResource(fileBytes);
    }


    /**
     * Rimuove un materiale cancellando il file dal filesystem
     * ed eliminando i dati nel database.
     *
     * @param id id del materiale da rimuovere
     */
    public void removeMaterial(final long id) {
        // Percorso della cartella basato sul terapeutaId

        Materiale materiale = materialeRepositoryInjected.findById(id)
                .orElseThrow();

        Path filePath = Path.of(materiale.getPath());
        try {
            // Recupera il nome del file da cancellare
            String fileName = materiale.getNome();
            this.logger.log(
                    Level.INFO,
                    "Cancellazione del file {0}",
                    filePath
            );
            Files.delete(filePath);
        } catch (IOException e) {
            this.logger.log(
                    Level.SEVERE,
                    "Errore durante la cancellazione del file : {0}",
                    e.getMessage()
            );
        }

        // Elimina il record dal database
        this.materialeRepositoryInjected.deleteById(materiale.getId());
    }

    /**
     * Aggiorna un materiale rimuovendo il vecchio file
     * e sostituendolo con il nuovo, poi salva le modifiche
     * nel database.
     *
     * @param materialeDTO DTO contenente i dati del materiale
     *                     aggiornato
     */
    public void updateMaterial(final InputMaterialeDTO materialeDTO) {
        // Percorso della cartella basato sul terapeutaId
        Path directoryPath = Paths.get(
                BASE_DIRECTORY,
                String.valueOf(materialeDTO.getTerapeutaId())
        );
        MultipartFile file = materialeDTO.getFile();

        // Recupera il materiale dal database e, se presente, aggiorna il file
        this.materialeRepositoryInjected.findById(materialeDTO.getId())
                .ifPresent(materiale -> {
                    try {
                        // Cancella il vecchio file se esiste
                        Path existingFilePath = Paths.get(materiale.getPath());
                        if (Files.exists(existingFilePath)) {
                            Files.delete(existingFilePath);
                        }

                        // Scrive il nuovo file
                        Path newFilePath = directoryPath.resolve(
                                materialeDTO.getNome()
                        );
                        Files.write(newFilePath, file.getBytes());

                        // Aggiorna i campi dell'entità
                        materiale.setNome(materialeDTO.getNome());
                        materiale.setTipo(materialeDTO.getTipoMateriale());
                        materiale.setPath(newFilePath.toString());

                        // Salva le modifiche nel database
                        this.materialeRepositoryInjected.save(materiale);

                    } catch (IOException e) {
                        this.logger.log(
                                Level.SEVERE,
                                "Errore durante l'aggiornamento del file: {0}",
                                e.getMessage()
                        );
                    }
                });
    }
}
