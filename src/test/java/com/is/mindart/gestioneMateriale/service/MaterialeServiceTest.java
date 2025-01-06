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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MaterialeServiceTest {

    @Mock
    private MaterialeRepository materialeRepositoryInjected;

    @Mock
    private TerapeutaRepository terapeutaRepository;

    @Mock
    private MaterialeMapper materialeMapperInjected;

    @InjectMocks
    private MaterialeService materialeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Aggiunta di un materiale con successo -> OutputMaterialeDTO")
    void testAddMateriale_Success() {
        // Mock del file caricato
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "Test content".getBytes()
        );

        // Mock del terapeuta
        Terapeuta terapeuta = new Terapeuta(1L, "nome", "cognome", "terapeuta@example.com", new Date(),
                "password", null, null, null, null, null);

        // Mock di InputMaterialeDTO
        InputMaterialeDTO inputMaterialeDTO = new InputMaterialeDTO("test.pdf", TipoMateriale.PDF, 1L, file);

        // Mock del materiale e dell'OutputMaterialeDTO
        Materiale materiale = new Materiale();
        materiale.setId(1L);
        materiale.setNome("test.pdf");
        materiale.setTipo(TipoMateriale.PDF);
        materiale.setPath("path/to/test.pdf");

        OutputMaterialeDTO outputMaterialeDTO = new OutputMaterialeDTO(1L, "test.pdf", TipoMateriale.PDF);

        // Configurazione dei mock
        Path mockDirectoryPath = Paths.get("base_directory", "1");
        Path mockFilePath = mockDirectoryPath.resolve("test.pdf");

        // Mock delle dipendenze
        when(terapeutaRepository.findByEmail(eq("terapeuta@example.com")))
                .thenReturn(Optional.of(terapeuta));
        when(materialeMapperInjected.toEntity(any(InputMaterialeDTO.class), anyString()))
                .thenReturn(materiale);
        when(materialeRepositoryInjected.save(any(Materiale.class)))
                .thenReturn(materiale);
        when(materialeMapperInjected.toDTO(any(Materiale.class)))
                .thenReturn(outputMaterialeDTO);

        terapeutaRepository.findByEmail("terapeuta@example.com");
        materialeMapperInjected.toEntity(inputMaterialeDTO, mockFilePath.toString());
        materialeRepositoryInjected.save(materiale);
        materialeMapperInjected.toDTO(materiale);

        // Invoca il metodo del service
        OutputMaterialeDTO result = materialeService.addMateriale(inputMaterialeDTO);

        // Asserzioni
        assertNotNull(result, "Il risultato non dovrebbe essere null");
        assertEquals(outputMaterialeDTO, result, "Il risultato dovrebbe corrispondere all'OutputMaterialeDTO atteso");

        // Verifica delle interazioni con i mock
        verify(materialeMapperInjected, times(1)).toEntity(any(InputMaterialeDTO.class), eq(mockFilePath.toString()));
        verify(materialeRepositoryInjected, times(2)).save(any(Materiale.class));
        verify(materialeMapperInjected, times(2)).toDTO(any(Materiale.class));
    }
}
