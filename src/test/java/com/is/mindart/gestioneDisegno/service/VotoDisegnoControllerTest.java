
package com.is.mindart.gestioneDisegno;

import com.is.mindart.gestioneDisegno.controller.DisegnoController;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneDisegno.service.ValutazioneRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test di unità per DisegnoController.
 */
public class VotoDisegnoControllerTest {

    @Mock
    private DisegnoService disegnoService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DisegnoController disegnoController;

    private AutoCloseable closeable;

    private final String terapeutaEmail = "terapeuta@example.com";

    @BeforeEach
    public void setUp() {
        // Inizializza i mock
        closeable = MockitoAnnotations.openMocks(this);
        // Imposta l'autenticazione nel SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(terapeutaEmail);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Chiude i mock e pulisce il SecurityContext
        closeable.close();
        SecurityContextHolder.clearContext();
    }

    /**
     * SERVICE TEST
     * Test per verificare che il voto venga assegnato correttamente
     * e che il metodo restituisca il Disegno aggiornato.
     */
    @Test
    @DisplayName("Assegnazione voto valida -> Disegno")
    public void testVota_Success_Service() {
        Long disegnoId = 1L;
        Integer valutazione = 8;

        // Crea un Disegno simulato
        Disegno updatedDisegno = new Disegno();
        updatedDisegno.setId(disegnoId);
        updatedDisegno.setVoto(valutazione);
        updatedDisegno.setValutazioneEmotiva(ValutazioneEmotiva.FELICE);

        // Configura il mock del servizio
        when(disegnoService.vota(disegnoId, valutazione)).thenReturn(updatedDisegno);

        Disegno result = disegnoService.vota(disegnoId, valutazione);
        assertEquals(updatedDisegno, result);
    }

    /**
     * CONTROLLER TEST
     * Test per verificare che il voto venga assegnato correttamente
     * e che il metodo restituisca 200 OK.
     */
    @Test
    @DisplayName("Assegnazione voto valida -> 200 OK")
    public void testVota_Success() {
        Long disegnoId = 1L;
        Integer valutazione = 8;

        // Crea un Disegno simulato
        Disegno updatedDisegno = new Disegno();
        updatedDisegno.setId(disegnoId);
        updatedDisegno.setVoto(valutazione);
        updatedDisegno.setValutazioneEmotiva(ValutazioneEmotiva.FELICE);

        // Configura il mock del servizio
        when(disegnoService.vota(disegnoId, valutazione)).thenReturn(updatedDisegno);

        // Crea l'oggetto ValutazioneRequest
        ValutazioneRequest request = new ValutazioneRequest();
        request.setValutazione(valutazione);

        // Invoca il metodo del controller
        ResponseEntity<Object> response = disegnoController.vota(disegnoId, request);

        // Verifica la risposta
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(disegnoService, times(1)).vota(disegnoId, valutazione);
    }

    /**
     * Test per verificare il comportamento quando la valutazione supera il limite massimo.
     * Deve lanciare un'eccezione IllegalArgumentException.
     */
    @Test
    @DisplayName("Assegnazione voto oltre limite -> IllegalArgumentException")
    public void testVota_InvalidValutazione_AboveMax() {
        Long disegnoId = 1L;
        Integer valutazione = 12; // Valore oltre il limite massimo

        // Configura il mock del servizio per lanciare un'eccezione
        doThrow(new IllegalArgumentException("Valutazione non valida")).when(disegnoService).vota(disegnoId, valutazione);

        // Crea l'oggetto ValutazioneRequest
        ValutazioneRequest request = new ValutazioneRequest();
        request.setValutazione(valutazione);

        // Invoca il metodo del controller e cattura l'eccezione
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            disegnoController.vota(disegnoId, request);
        });

        // Verifica il messaggio dell'eccezione
        assertEquals("Valutazione non valida", exception.getMessage());
        verify(disegnoService, times(1)).vota(disegnoId, valutazione);
    }

    /**
     * Test per verificare il comportamento quando la valutazione è mancante.
     * Deve lanciare un'eccezione IllegalArgumentException.
     */
    @Test
    @DisplayName("Assegnazione voto mancante -> IllegalArgumentException")
    public void testVota_MissingValutazione() {
        Long disegnoId = 1L;
        Integer valutazione = null; // Valutazione mancante

        // Configura il mock del servizio per lanciare un'eccezione
        doThrow(new IllegalArgumentException("Valutazione mancante")).when(disegnoService).vota(disegnoId, valutazione);

        // Crea l'oggetto ValutazioneRequest senza impostare il valore di "valutazione"
        ValutazioneRequest request = new ValutazioneRequest();
        request.setValutazione(valutazione);

        // Invoca il metodo del controller e cattura l'eccezione
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            disegnoController.vota(disegnoId, request);
        });

        // Verifica il messaggio dell'eccezione
        assertEquals("Valutazione mancante", exception.getMessage());
        verify(disegnoService, times(1)).vota(disegnoId, valutazione);
    }

    /**
     * Test per verificare il comportamento quando il Disegno non esiste.
     * Deve lanciare un'eccezione DisegnoNotFoundException.
     */
    @Test
    @DisplayName("Assegnazione voto a Disegno inesistente -> NoSuchElementException")
    public void testVota_DisegnoInesistente() {
        Long disegnoId = 999L;
        Integer valutazione = 5;

        // Configura il mock del servizio per lanciare un'eccezione
        doThrow(new NoSuchElementException("Disegno non trovato")).when(disegnoService).vota(disegnoId, valutazione);

        // Crea l'oggetto ValutazioneRequest
        ValutazioneRequest request = new ValutazioneRequest();
        request.setValutazione(valutazione);

        // Invoca il metodo del controller e cattura l'eccezione
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            disegnoController.vota(disegnoId, request);
        });

        // Verifica il messaggio dell'eccezione
        assertEquals("Disegno non trovato", exception.getMessage());
        verify(disegnoService, times(1)).vota(disegnoId, valutazione);
    }
}