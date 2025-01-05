package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.service.InputMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

public class MaterialeControllerTest {

    @Mock
    private MaterialeService materialeService;

    @InjectMocks
    private MaterialeController materialeController;

    @Mock
    private Authentication authentication;

    @Mock
    private TerapeutaRepository terapeutaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    void testAddMaterial_Success() {
        // Mock del file
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "Test content".getBytes());

        // Mock del terapeuta
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1L);

        // Mock di Output DTO
        OutputMaterialeDTO outputMaterialeDTO = new OutputMaterialeDTO(1L, "test.pdf", TipoMateriale.PDF);

        // Configura i mock
        when(terapeutaRepository.findByEmail("terapeuta@example.com")).thenReturn(Optional.of(terapeuta));
        when(materialeService.addMateriale(any(InputMaterialeDTO.class))).thenReturn(outputMaterialeDTO);

        // Chiamata al controller
        ResponseEntity<OutputMaterialeDTO> response = materialeController.addMaterial(file);

        // Verifica la risposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputMaterialeDTO, response.getBody());

        // Verifica le interazioni
        verify(materialeService, times(1)).addMateriale(any(InputMaterialeDTO.class));
        verify(terapeutaRepository, times(1)).findByEmail("terapeuta@example.com");
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    void testAddMaterial_EmptyFile() {
        // Mock del file vuoto
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", "application/pdf", new byte[0]);

        // Configura il mock per il terapeuta
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1L);
        when(terapeutaRepository.findByEmail("terapeuta@example.com")).thenReturn(Optional.of(terapeuta));

        // Chiamata al controller e verifica che venga lanciata l'eccezione
        EmptyFileException exception = assertThrows(EmptyFileException.class, () -> {
            materialeController.addMaterial(emptyFile);
        });

        // Verifica che il messaggio dell'eccezione sia quello atteso
        assertEquals("File nullo o vuoto", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    void testAddMaterial_DuplicatedFile() {
        // Mock del file duplicato
        MockMultipartFile file = new MockMultipartFile(
                "file", "duplicate.pdf", "application/pdf", "Test content".getBytes());

        // Configura il mock per il terapeuta
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1L);
        when(terapeutaRepository.findByEmail("terapeuta@example.com")).thenReturn(Optional.of(terapeuta));
        when(materialeService.existsMateriale(any(InputMaterialeDTO.class))).thenReturn(true);

        // Chiamata al controller e verifica che venga lanciata l'eccezione
        DuplicatedFileException exception = assertThrows(DuplicatedFileException.class, () -> {
            materialeController.addMaterial(file);
        });

        // Verifica che il messaggio dell'eccezione sia quello atteso
        assertEquals("Esiste già un file chiamato duplicate.pdf", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    void testAddMaterial_UnsupportedFileType() {
        // Mock del file con tipo non supportato
        MockMultipartFile unsupportedFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Test content".getBytes());

        // Configura il mock per il terapeuta
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setId(1L);
        when(terapeutaRepository.findByEmail("terapeuta@example.com")).thenReturn(Optional.of(terapeuta));

        // Chiamata al controller e verifica che venga lanciata l'eccezione
        UnsupportedFileException exception = assertThrows(UnsupportedFileException.class, () -> {
            materialeController.addMaterial(unsupportedFile);
        });

        // Verifica che il messaggio dell'eccezione sia quello atteso
        assertEquals("Il tipo di file non è supportato", exception.getMessage());
    }
}
