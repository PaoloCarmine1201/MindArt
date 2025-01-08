package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.InputMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

/**
 * Classe di testing per le funzioni di gestione
 * del materiale nel controller MaterialeController.
 */
public final class MaterialeControllerTest {

    /**
     * Mock del servizio per la gestione del materiale.
     */
    @Mock
    private MaterialeService materialeService;

    /**
     * Controller per la gestione del materiale, iniettato con i mock.
     */
    @InjectMocks
    private MaterialeController materialeController;

    /**
     * Mock dell'oggetto Authentication.
     */
    @Mock
    private Authentication authentication;

    /**
     * Mock del repository per la gestione dei terapeuti.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;

    /**
     * Inizializza i mock prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");
    }

    /**
     * Test per aggiungere un materiale con successo.
     * Deve ritornare 200 OK e l'OutputMaterialeDTO atteso.
     */
    @Test
    @DisplayName("Aggiunta materiale con successo -> 200 OK")
    @WithMockUser(roles = "TERAPEUTA")
    void addMaterialSuccessTest() {
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

        final OutputMaterialeDTO outputMaterialeDTO =
                new OutputMaterialeDTO(1L, expectedFilename, tipoMateriale);

        when(terapeutaRepository
                .findByEmail(email)).thenReturn(Optional.of(terapeuta));
        when(materialeService
                .addMateriale(any(InputMaterialeDTO.class)))
                .thenReturn(outputMaterialeDTO);

        // Act
        final ResponseEntity<OutputMaterialeDTO> response =
                materialeController.addMaterial(file);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputMaterialeDTO, response.getBody());

        verify(materialeService, times(1))
                .addMateriale(any(InputMaterialeDTO.class));
        verify(terapeutaRepository, times(1)).findByEmail(email);
    }

    /**
     * Test per aggiungere un materiale con un file vuoto.
     * Deve lanciare un'eccezione EmptyFileException.
     */
    @Test
    @DisplayName("Aggiunta materiale con file vuoto -> EmptyFileException")
    @WithMockUser(roles = "TERAPEUTA")
    void addMaterialEmptyFileTest() {
        // Arrange
        final String email = "terapeuta@example.com";
        final long terapeutaId = 1L;
        final String filename = "empty.pdf";

        final MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                filename,
                "application/pdf",
                new byte[0]
        );

        final Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(terapeutaId);

        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.of(terapeuta));

        // Act & Assert
        final EmptyFileException exception =
                assertThrows(EmptyFileException.class, () -> {
            materialeController.addMaterial(emptyFile);
        });

        assertEquals("File nullo o vuoto", exception.getMessage());

        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(materialeService, never())
                .addMateriale(any(InputMaterialeDTO.class));
    }

    /**
     * Test per aggiungere un materiale con un file duplicato.
     * Deve lanciare un'eccezione DuplicatedFileException.
     */
    @Test
    @DisplayName("Aggiunta materiale con file duplicato"
            + "-> DuplicatedFileException")
    @WithMockUser(roles = "TERAPEUTA")
    void addMaterialDuplicatedFileTest() {
        // Arrange
        final String email = "terapeuta@example.com";
        final long terapeutaId = 1L;
        final String filename = "duplicate.pdf";

        final MockMultipartFile file = new MockMultipartFile(
                "file",
                filename,
                "application/pdf",
                "Test content".getBytes()
        );

        final Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(terapeutaId);

        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.of(terapeuta));
        when(materialeService
                .existsMateriale(any(InputMaterialeDTO.class)))
                .thenReturn(true);

        // Act & Assert
        final DuplicatedFileException exception =
                assertThrows(DuplicatedFileException.class, () -> {
            materialeController.addMaterial(file);
        });

        assertEquals("Esiste già un file chiamato duplicate.pdf",
                exception.getMessage());

        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(materialeService, times(1))
                .existsMateriale(any(InputMaterialeDTO.class));
        verify(materialeService, never())
                .addMateriale(any(InputMaterialeDTO.class));
    }

    /**
     * Test per aggiungere un materiale con un tipo di file non supportato.
     * Deve lanciare un'eccezione UnsupportedFileException.
     */
    @Test
    @DisplayName("Aggiunta materiale con tipo file non supportato"
            + " -> UnsupportedFileException")
    @WithMockUser(roles = "TERAPEUTA")
    void addMaterialUnsupportedFileTypeTest() {
        // Arrange
        final String email = "terapeuta@example.com";
        final long terapeutaId = 1L;
        final String filename = "test.txt";

        final MockMultipartFile unsupportedFile = new MockMultipartFile(
                "file",
                filename,
                "text/plain",
                "Test content".getBytes()
        );

        final Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(terapeutaId);

        when(terapeutaRepository
                .findByEmail(email)).thenReturn(Optional.of(terapeuta));

        // Act & Assert
        final UnsupportedFileException exception =
                assertThrows(UnsupportedFileException.class, () -> {
            materialeController.addMaterial(unsupportedFile);
        });

        assertEquals("Il tipo di file non è supportato",
                exception.getMessage());

        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(materialeService, never())
                .addMateriale(any(InputMaterialeDTO.class));
    }
}
