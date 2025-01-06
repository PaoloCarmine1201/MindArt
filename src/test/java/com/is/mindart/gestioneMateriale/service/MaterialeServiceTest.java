package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.configuration.MaterialeMapper;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

/**
 * Classe di testing per la funzione di login
 * del Terapeuta nel service TerapeutaService.
 */
public final class MaterialeServiceTest {

    /**
     * Mock del repository per la gestione del materiale.
     */
    @Mock
    private MaterialeRepository materialeRepositoryInjected;

    /**
     * Mock del repository per la gestione dei terapeuti.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;

    /**
     * Mock del mapper per la conversione tra DTO e entità.
     */
    @Mock
    private MaterialeMapper materialeMapperInjected;

    /**
     * Servizio per la gestione del materiale, iniettato con i mock.
     */
    @InjectMocks
    private MaterialeService materialeService;

    /**
     * Inizializza i mock prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per aggiungere un materiale con successo.
     * Deve ritornare un OutputMaterialeDTO non nullo con i dati attesi.
     */
    @Test
    @DisplayName("Aggiunta di un materiale con successo -> OutputMaterialeDTO")
    void addMaterialeSuccessTest() {
        // Arrange
        final String email = "terapeuta@example.com";
        final long terapeutaId = 1L;
        final String filename = "test.pdf";
        final TipoMateriale tipoMateriale = TipoMateriale.PDF;
        final String expectedFilename = "test.pdf";
        final String fileContent = "Test content";
        final byte[] fileBytes = fileContent.getBytes();

        final MockMultipartFile file = new MockMultipartFile(
                "file",
                filename,
                "application/pdf",
                fileBytes
        );

        final Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(terapeutaId);

        final InputMaterialeDTO inputMaterialeDTO = new InputMaterialeDTO(
                filename,
                tipoMateriale,
                terapeutaId,
                file
        );

        final Materiale materiale = new Materiale();
        materiale.setId(1L);
        materiale.setNome(filename);
        materiale.setTipo(tipoMateriale);
        materiale.setPath("path/to/test.pdf");

        final OutputMaterialeDTO outputMaterialeDTO = new OutputMaterialeDTO(
                materiale.getId(),
                materiale.getNome(),
                materiale.getTipo()
        );

        final Path mockDirectoryPath =
                Paths.get("base_directory", String.valueOf(terapeutaId));
        final Path mockFilePath = mockDirectoryPath.resolve(filename);

        // Configurazione dei mock
        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.of(terapeuta));
        when(materialeMapperInjected
                .toEntity(any(InputMaterialeDTO.class), any(String.class)))
                .thenReturn(materiale);
        when(materialeRepositoryInjected.save(any(Materiale.class)))
                .thenReturn(materiale);
        when(materialeMapperInjected.toDTO(any(Materiale.class)))
                .thenReturn(outputMaterialeDTO);

        // Act
        OutputMaterialeDTO result =
                materialeService.addMateriale(inputMaterialeDTO);

        // Assert
        assertNotNull(result, "Il risultato non dovrebbe essere null");
        assertEquals(outputMaterialeDTO, result, "Il risultato dovrebbe "
                + "corrispondere all'OutputMaterialeDTO atteso");

        // Verifica delle interazioni con i mock
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(materialeMapperInjected, times(1))
                .toEntity(any(InputMaterialeDTO.class), any(String.class));
        verify(materialeRepositoryInjected, times(1))
                .save(any(Materiale.class));
        verify(materialeMapperInjected, times(1)).toDTO(any(Materiale.class));
    }
}
