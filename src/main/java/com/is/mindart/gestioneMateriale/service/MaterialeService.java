package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.configuration.MaterialeMapper;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class MaterialeService {

    /**
     * Repository relativa al materiale.
     */
    private final MaterialeRepository materialeRepository;
    /**
     * Repository relativa al terapeuta
     */
    private final TerapeutaRepository terapeutaRepository;
    /**
     * Custom mapper per il materiale.
     */
    private final MaterialeMapper materialeMapper;

    private static final String BASE_DIRECTORY = "src/materiali/";

    private final Logger logger = Logger.getLogger(MaterialeService.class.getName());

    /**
     * Costruttore.
     * @param materialeRepository {@link MaterialeRepository}
     * @param materialeMapper {@link MaterialeMapper}
     */
    @Autowired
    public MaterialeService(final MaterialeRepository materialeRepository,
                            final MaterialeMapper materialeMapper, TerapeutaRepository terapeutaRepository) {
        this.materialeRepository = materialeRepository;
        this.materialeMapper = materialeMapper;
        this.terapeutaRepository = terapeutaRepository;
    }

    /**
     * Trova tutti i materiali associati al terapeutae li converte in
     * {@link InputMaterialeDTO} per la visualizzazione sul client.
     * @param terapeutaId - id del terapeuta loggato
     * @return listaDTO
     */
    public List<OutputMaterialeDTO> getClientMateriali(final long terapeutaId) {
        return materialeRepository.findByTerapeutaId(terapeutaId)
                .stream()
                .map(materialeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean existsMateriale(InputMaterialeDTO inputMaterialeDTO) {
        return materialeRepository.existsByNomeAndTipoAndTerapeuta(
                inputMaterialeDTO.getNome(),
                inputMaterialeDTO.getTipoMateriale(),
                terapeutaRepository.findById(inputMaterialeDTO.getTerapeutaId()).orElseThrow(() ->
                        new EntityNotFoundException("Terapeuta non trovato"))
        );
    }

    public OutputMaterialeDTO addMateriale(InputMaterialeDTO materialeDTO) {
        Path directoryPath = Paths.get(BASE_DIRECTORY, String.valueOf(materialeDTO.getTerapeutaId()));

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Errore nella creazione della directory: {0}", e.getMessage());
            }
        }

        String fileName = materialeDTO.getNome();
        Path filePath = directoryPath.resolve(fileName);
        try {
            Files.write(filePath, materialeDTO.getFile().getBytes());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante la scrittura del file : {0}", e.getMessage());
        }

        Materiale materiale = materialeRepository.save(materialeMapper.toEntity(materialeDTO, filePath.toString()));

        return materialeMapper.toDTO(materiale);
    }

    public void removeMaterial(InputMaterialeDTO materialeDTO) {
        Path directoryPath = Paths.get(BASE_DIRECTORY, String.valueOf(materialeDTO.getTerapeutaId()));
        try {
            String fileName = materialeDTO.getNome();
            logger.log(Level.INFO, "Cancellazione del file {0}", directoryPath + fileName);
            Files.delete(directoryPath.resolve(fileName));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore durante la cancellazione del file : {0}", e.getMessage());
        }

        materialeRepository.deleteById(materialeDTO.getId());

    }

    public void updateMaterial(InputMaterialeDTO materialeDTO) {
        Path directoryPath = Paths.get(BASE_DIRECTORY, String.valueOf(materialeDTO.getTerapeutaId()));
        MultipartFile file = materialeDTO.getFile();
        materialeRepository.findById(materialeDTO.getId()).ifPresent(materiale -> {
            try {
                Path existingFilePath = Paths.get(materiale.getPath());
                if (Files.exists(existingFilePath)) {
                    Files.delete(existingFilePath);
                }

                Path newFilePath = directoryPath.resolve(materialeDTO.getNome());
                Files.write(newFilePath, file.getBytes());

                materiale.setNome(materialeDTO.getNome());
                materiale.setTipo(materialeDTO.getTipoMateriale());
                materiale.setPath(newFilePath.toString());
                materialeRepository.save(materiale);

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Errore durante l'aggiornamento del file: {0}", e.getMessage());
            }
        });
    }
}
