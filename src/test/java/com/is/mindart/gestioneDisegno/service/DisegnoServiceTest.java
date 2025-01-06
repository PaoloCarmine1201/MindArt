package com.is.mindart.gestioneDisegno.service;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Test unitari per la classe DisegnoService.
 */
public class DisegnoServiceTest {

    @Mock
    private DisegnoRepository disegnoRepository;

    @InjectMocks
    private DisegnoService disegnoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Test del metodo vota con un ID Disegno e voto validi.
     * Aspettativa: Il voto del Disegno viene aggiornato e il Disegno aggiornato viene restituito.
     */
    @Test
    @DisplayName("vota - ID Disegno e voto validi")
    public void testVota_ValidInput() {
        Long disegnoId = 1L;
        Integer voto = 8;

        // Crea un oggetto Disegno da restituire dal repository
        Disegno existingDisegno = new Disegno();
        existingDisegno.setId(disegnoId);
        existingDisegno.setVoto(5); // Voto iniziale

        // Configura il comportamento del repository mock
        when(disegnoRepository.findById(disegnoId)).thenReturn(Optional.of(existingDisegno));
        when(disegnoRepository.save(any(Disegno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoca il metodo del servizio
        Disegno updatedDisegno = disegnoService.vota(disegnoId, voto);

        // Asserzioni
        assertNotNull(updatedDisegno, "Il Disegno aggiornato non dovrebbe essere nullo");
        assertEquals(voto, updatedDisegno.getVoto(), "Il voto dovrebbe essere aggiornato al nuovo valore");

        // Verifica le interazioni con il repository
        verify(disegnoRepository, times(1)).findById(disegnoId);
        verify(disegnoRepository, times(1)).save(existingDisegno);
    }

    /**
     * Test del metodo vota con un ID Disegno non esistente.
     * Aspettativa: Viene lanciata un'eccezione NoSuchElementException.
     */
    @Test
    @DisplayName("vota - ID Disegno non esistente")
    public void testVota_DisegnoNotFound() {
        Long disegnoId = 999L;
        Integer voto = 5;

        // Configura il comportamento del repository mock per restituire Optional.empty()
        when(disegnoRepository.findById(disegnoId)).thenReturn(Optional.empty());

        // Invoca il metodo del servizio e verifica che venga lanciata l'eccezione
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            disegnoService.vota(disegnoId, voto);
        }, "Ci si aspettava che vota() lanciasse un'eccezione, ma non lo ha fatto");

        // Verifica il messaggio dell'eccezione
        assertEquals("Disegno non trovato con id " + disegnoId, exception.getMessage(), "Il messaggio dell'eccezione dovrebbe corrispondere");

        // Verifica le interazioni con il repository
        verify(disegnoRepository, times(1)).findById(disegnoId);
        verify(disegnoRepository, never()).save(any(Disegno.class));
    }
}
