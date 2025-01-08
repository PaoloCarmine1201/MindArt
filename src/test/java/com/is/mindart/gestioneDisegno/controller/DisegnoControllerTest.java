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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe di test per DisegnoController.
 */
public final class DisegnoControllerTest {

    /**
     * Mock dell'Authentication di Spring Security.
     */
    @Mock
    private Authentication authentication;

    /**
     * Mock del servizio per la gestione dei Disegni.
     */
    @Mock
    private DisegnoService disegnoService;

    /**
     * Controller dei Disegni, iniettato con i mock.
     */
    @InjectMocks
    private DisegnoController disegnoController;

    /**
     * Risorsa per chiudere automaticamente i mock.
     */
    private AutoCloseable closeable;

    /**
     * Email del terapeuta mockato.
     */
    private final String terapeutaEmail = "terapeuta@example.com";

    /**
     * Valutazione non valida.
     */
    private static final int INVALID_VALUTAZIONE = 12;

    /**
     * Identificativo di un disegno non esistente.
     */
    private static final long DISEGNO_NON_ESISTENTE = 999L;

    /**
     * Valutazioni valide per i test.
     */
    private static final int VALUTAZIONE_CINQUE = 5;

    /**
     * Valutazioni valide per i test.
     */
    private static final int VALUTAZIONE_OTTO = 8;

    /**
     * Inizializza i mock e configura l'autenticazione prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(terapeutaEmail);
    }

    /**
     * Ripulisce il contesto di sicurezza e chiude i mock dopo ogni test.
     *
     * @throws Exception se si verifica un problema con la chiusura dei mock
     */
    @AfterEach
    public void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        closeable.close();
    }

    /**
     * Test per verificare il comportamento quando
     * la valutazione supera il limite massimo.
     */
    @Test
    @DisplayName("Assegnazione voto oltre limite -> BAD_REQUEST")
    public void votaInvalidValutazioneAboveMaxTest() {
        Long disegnoId = 1L;
        ValutazioneRequest request =
                new ValutazioneRequest(INVALID_VALUTAZIONE);

        doThrow(new IllegalArgumentException("Valutazione non valida"))
                .when(disegnoService).vota(disegnoId, request.getValutazione());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> disegnoController.vota(disegnoId, request)
        );

        assertEquals("Valutazione non valida", exception.getMessage());
        verify(disegnoService, times(1))
                .vota(disegnoId, request.getValutazione());
    }

    /**
     * Test per verificare il comportamento quando il disegno non esiste.
     */
    @Test
    @DisplayName("Disegno non trovato -> NOT_FOUND")
    public void votaDisegnoNotFoundTest() {
        Long disegnoId = DISEGNO_NON_ESISTENTE;
        ValutazioneRequest request = new ValutazioneRequest(VALUTAZIONE_CINQUE);

        doThrow(new NoSuchElementException(
                "Disegno non trovato con id " + disegnoId
        )).when(disegnoService).vota(disegnoId, request.getValutazione());

        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> disegnoController.vota(disegnoId, request)
        );

        assertEquals("Disegno non trovato con id " + disegnoId,
                exception.getMessage());
        verify(disegnoService, times(1))
                .vota(disegnoId, request.getValutazione());
    }

    /**
     * Test per verificare il comportamento quando la valutazione è valida.
     * Deve ritornare 200 OK.
     */
    @Test
    @DisplayName("Assegnazione voto valida -> 200 OK")
    public void votaValidValutazioneTest() {
        Long disegnoId = 1L;
        ValutazioneRequest request = new ValutazioneRequest(VALUTAZIONE_OTTO);

        Disegno disegno = new Disegno();
        disegno.setId(disegnoId);
        disegno.setVoto(request.getValutazione());

        when(disegnoService.vota(disegnoId, request.getValutazione()))
                .thenReturn(disegno);

        ResponseEntity<Object> response = disegnoController.vota(
                disegnoId,
                request
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(disegnoService, times(1))
                .vota(disegnoId, request.getValutazione());
    }
}
