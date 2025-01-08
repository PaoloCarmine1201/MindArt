package com.is.mindart.gestioneBambino.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneBambino.model.Sesso;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddBambinoServiceTest {

    /**
     * Repository per la gestione dei bambini.
     */
    @Mock
    private BambinoRepository bambinoRepository;

    /**
     * Repository per la gestione dei terapeuti.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;

    /**
     * Mapper per la conversione degli oggetti.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * Service per la gestione dei bambini.
     */
    @InjectMocks
    private BambinoService bambinoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test addBambino con successo")
    void testAddBambinoSuccess() {
        // Arrange
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        String terapeutaEmail = "terapeuta@example.com";

        Terapeuta terapeuta = new Terapeuta();
        Bambino mappedBambino = new Bambino();
        mappedBambino.setNome(bambinoDto.getNome());
        mappedBambino.setCognome(bambinoDto.getCognome());

        when(terapeutaRepository.findByEmail(terapeutaEmail))
                .thenReturn(Optional.of(terapeuta));
        when(modelMapper.map(bambinoDto, Bambino.class))
                .thenReturn(mappedBambino);
        when(bambinoRepository.findByCodice(anyString()))
                .thenReturn(Optional.empty());

        // Act
        bambinoService.addBambino(bambinoDto, terapeutaEmail);

        // Assert
        verify(terapeutaRepository, times(1)).findByEmail(terapeutaEmail);
        verify(modelMapper, times(1)).map(bambinoDto, Bambino.class);
        verify(bambinoRepository, times(1)).save(mappedBambino);

        assertNotNull(mappedBambino.getCodice());
        assertTrue(mappedBambino.getVisibile());
        assertEquals(terapeuta, mappedBambino.getTerapeuta());
    }

    @Test
    @DisplayName("Test addBambino con terapeuta non trovato -> Errore")
    void testAddBambinoTerapeutaNonTrovato() {
        // Arrange
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        String terapeutaEmail = "terapeuta@example.com";

        when(terapeutaRepository.findByEmail(terapeutaEmail))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                bambinoService.addBambino(bambinoDto, terapeutaEmail));
        assertEquals("Terapeuta non trovato", exception.getMessage());

        verify(terapeutaRepository, times(1)).findByEmail(terapeutaEmail);
        verify(modelMapper, never())
                .map(any(RegisterBambinoDTO.class), any(Bambino.class));
        verify(bambinoRepository, never()).save(any(Bambino.class));
    }

    @Test
    @DisplayName("Test addBambino con codice "
            + "bambino duplicato -> Rigenera codice")
    void testAddBambinoCodiceDuplicato() {
        // Arrange
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        String terapeutaEmail = "terapeuta@example.com";

        Terapeuta terapeuta = new Terapeuta();
        Bambino mappedBambino = new Bambino();

        when(terapeutaRepository.findByEmail(terapeutaEmail))
                .thenReturn(Optional.of(terapeuta));
        when(modelMapper.map(bambinoDto, Bambino.class))
                .thenReturn(mappedBambino);
        when(bambinoRepository.findByCodice(anyString()))
                // Primo codice duplicato
                .thenReturn(Optional.of(new Bambino()))
                // Secondo codice valido
                .thenReturn(Optional.empty());

        // Act
        bambinoService.addBambino(bambinoDto, terapeutaEmail);

        // Assert
        verify(bambinoRepository, times(2)).findByCodice(anyString());
        verify(bambinoRepository, times(1)).save(mappedBambino);
    }

    private RegisterBambinoDTO createValidBambinoDTO() {
        final int time = 100000;
        return new RegisterBambinoDTO(
                null,
                "CODICE123",
                "Mario",
                "Rossi",
                Sesso.MASCHIO,
                // Data di nascita passata
                new Date(System.currentTimeMillis() - time),
                "RSSMRA85M01H501Z",
                "genitore@example.com",
                "+39 333 1234567",
                1L
        );
    }
}
