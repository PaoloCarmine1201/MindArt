package com.is.mindart.gestioneDisegno.service;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

/**
 * Test unitari per la classe DisegnoService.
 */
public class DisegnoServiceTest {

    /**
     * Mock del repository per l'entità Disegno.
     */
    @Mock
    private DisegnoRepository disegnoRepository;

    /**
     * Servizio per la gestione dei Disegni,
     * iniettato con il mock del repository.
     */
    @InjectMocks
    private DisegnoService disegnoService;

    /**
     * Risorsa per chiudere automaticamente i mock.
     */
    private AutoCloseable closeable;

    /**
     * Inizializza i mock e il servizio prima di ogni test.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Chiude la risorsa per i mock dopo ogni test.
     */
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Test del metodo vota con un ID Disegno e voto validi.
     * Aspettativa: Il voto del Disegno viene aggiornato
     * e il Disegno aggiornato viene restituito.
     */
    @Test
    @DisplayName("vota - ID Disegno e voto validi")
    public void testVotaValidInput() {
        final Long disegnoId = 1L;
        final Integer voto = 8;
        final int votoIniziale = 5;

        // Crea un oggetto Disegno da restituire dal repository
        Disegno existingDisegno = new Disegno();
        existingDisegno.setId(disegnoId);
        existingDisegno.setVoto(votoIniziale); // Voto iniziale

        // Configura il comportamento del repository mock
        when(disegnoRepository
                .findById(disegnoId)).thenReturn(Optional.of(existingDisegno));
        when(disegnoRepository
                .save(any(Disegno.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Invoca il metodo del servizio
        Disegno updatedDisegno = disegnoService.vota(disegnoId, voto);

        // Asserzioni
        assertNotNull(updatedDisegno,
                "Il Disegno aggiornato non dovrebbe essere nullo");
        assertEquals(voto, updatedDisegno.getVoto(),
                "Il voto dovrebbe essere aggiornato al nuovo valore");

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
    public void testVotaDisegnoNonTrovato() {
        final Long disegnoId = 999L;
        final Integer voto = 5;

        // Configura il comportamento del repository
        // mock per restituire Optional.empty()
        when(disegnoRepository
                .findById(disegnoId)).thenReturn(Optional.empty());

        // Invoca il metodo del servizio e
        // verifica che venga lanciata l'eccezione
        NoSuchElementException exception =
                assertThrows(NoSuchElementException.class, () -> {
            disegnoService.vota(disegnoId, voto);
        }, "Ci si aspettava che vota() "
                        + "lanciasse un'eccezione, ma non lo ha fatto");

        // Verifica il messaggio dell'eccezione
        assertEquals("Disegno non trovato con id "
                        + disegnoId, exception.getMessage(),
                "Il messaggio dell'eccezione dovrebbe corrispondere");

        // Verifica le interazioni con il repository
        verify(disegnoRepository, times(1)).findById(disegnoId);
        verify(disegnoRepository, never()).save(any(Disegno.class));
    }
}
