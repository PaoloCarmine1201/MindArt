package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneDisegno.service.ValutazioneRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DisegnoControllerTest {

    @Mock
    private Authentication authentication;

    @Mock
    private DisegnoService disegnoService; // Mock the service dependency

    @InjectMocks
    private DisegnoController disegnoController;

    private AutoCloseable closeable;

    private final String terapeutaEmail = "terapeuta@example.com";

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(terapeutaEmail);
    }

    @AfterEach
    public void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        closeable.close();
    }

    /**
     * Test per verificare il comportamento quando la valutazione supera il limite massimo.
     * Deve lanciare un'eccezione IllegalArgumentException.
     */
    @Test
    @DisplayName("Assegnazione voto oltre limite -> BAD_REQUEST")
    public void testVota_InvalidValutazione_AboveMax() {
        Long disegnoId = 1L;
        ValutazioneRequest request = new ValutazioneRequest(12); // Assuming 12 is above the max limit

        // Configura il mock del servizio per lanciare un'eccezione
        doThrow(new IllegalArgumentException("Valutazione non valida"))
                .when(disegnoService).vota(disegnoId, request.getValutazione());

        // Invoca il metodo del controller e cattura l'eccezione
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            disegnoController.vota(disegnoId, request);
        });

        // Verifica il messaggio dell'eccezione
        assertEquals("Valutazione non valida", exception.getMessage());

        // Verifica che il servizio sia stato chiamato una volta
        verify(disegnoService, times(1)).vota(disegnoId, request.getValutazione());
    }

    /**
     * Test per verificare il comportamento quando il disegno non esiste.
     * Deve lanciare un'eccezione NoSuchElementException.
     */
    @Test
    @DisplayName("Disegno non trovato -> NOT_FOUND")
    public void testVota_DisegnoNotFound() {
        Long disegnoId = 999L;
        ValutazioneRequest request = new ValutazioneRequest(5);

        // Configura il mock del servizio per lanciare un'eccezione
        doThrow(new NoSuchElementException("Disegno non trovato con id " + disegnoId))
                .when(disegnoService).vota(disegnoId, request.getValutazione());

        // Invoca il metodo del controller e cattura l'eccezione
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            disegnoController.vota(disegnoId, request);
        });

        // Verifica il messaggio dell'eccezione
        assertEquals("Disegno non trovato con id " + disegnoId, exception.getMessage());

        // Verifica che il servizio sia stato chiamato una volta
        verify(disegnoService, times(1)).vota(disegnoId, request.getValutazione());
    }

    /**
     * Test per verificare il comportamento quando la valutazione è valida.
     * Deve ritornare 200 OK.
     */
    @Test
    @DisplayName("Assegnazione voto valida -> 200 OK")
    public void testVota_ValidValutazione() {
        Long disegnoId = 1L;
        ValutazioneRequest request = new ValutazioneRequest(8); // Assuming 8 is a valid vote

        Disegno disegno = new Disegno();
        disegno.setId(disegnoId);
        disegno.setVoto(request.getValutazione());

        // Configura il mock del servizio per ritornare il disegno votato
        when(disegnoService.vota(disegnoId, request.getValutazione())).thenReturn(disegno);

        // Invoca il metodo del controller
        ResponseEntity<Object> response = disegnoController.vota(disegnoId, request);

        // Asserisci che lo stato della risposta sia 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verifica che il servizio sia stato chiamato una volta
        verify(disegnoService, times(1)).vota(disegnoId, request.getValutazione());
    }
}
