package com.is.mindart.gestioneSessione.service;

import com.is.mindart.configuration.SessioneMapper;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;


class SessioneServiceTest {

    /**
     * Mock del repository delle sessioni.
     */
    @Mock
    private SessioneRepository sessioneRepository;

    /**
     * Mock del mapper delle sessioni.
     */
    @Mock
    private SessioneMapper sessioneMapper;

    /**
     * Mock del repository dei terapeuti.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;

    /**
     * Mock del repository dei disegni.
     */
    @Mock
    private DisegnoRepository disegnoRepository;

    /**
     * Service da testare.
     */
    @InjectMocks
    private SessioneService sessioneService;

    /**
     * Inizializza i mock.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per il metodo.
     * {@link SessioneService#creaSessione(SessioneDTO, String)}.
     */
    @Test
    @DisplayName("Creazione di una sessione valida -> Successo")
    void testCreaSessioneSuccess() {
        // Inizializzazione dati di test
        String terapeutaEmail = "terapeuta@example.com";
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setEmail(terapeutaEmail);

        Sessione sessione = new Sessione();

        // Definizione comportamento dei mock
        when(sessioneRepository
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail))
                .thenReturn(Collections.emptyList()); // No sessioni attive
        when(terapeutaRepository.findByEmail(terapeutaEmail))
                .thenReturn(Optional.of(terapeuta));
        when(sessioneMapper.toEntity(sessioneDTO)).thenReturn(sessione);

        // Inizio test e verifica
        sessioneService.creaSessione(sessioneDTO, terapeutaEmail);

        verify(sessioneRepository, times(1))
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail);
        verify(terapeutaRepository, times(1)).findByEmail(terapeutaEmail);
        verify(sessioneMapper, times(1)).toEntity(sessioneDTO);
        verify(disegnoRepository, times(1)).save(any(Disegno.class));
        verify(sessioneRepository, times(1)).save(sessione);
    }

    @Test
    @DisplayName("Creazione di una sessione con"
            + "sessione attiva esistente -> Errore")
    void testCreaSessioneSessioneAttivaEsistente() {
        // Inizializzazione dati di test
        String terapeutaEmail = "terapeuta@example.com";
        SessioneDTO sessioneDTO = new SessioneDTO();

        // Definizione comportamento dei mock
        when(sessioneRepository
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail))
                .thenReturn(Collections.singletonList(new Sessione()));

        // Inizio test e verifica
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                sessioneService.creaSessione(sessioneDTO, terapeutaEmail));
        assertEquals("Terapeuta ha già una sessione attiva",
                exception.getMessage());

        verify(sessioneRepository, times(1))
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail);
        verify(terapeutaRepository, never()).findByEmail(terapeutaEmail);
        verify(sessioneMapper, never()).toEntity(any(SessioneDTO.class));
        verify(disegnoRepository, never()).save(any(Disegno.class));
        verify(sessioneRepository, never()).save(any(Sessione.class));
    }

    @Test
    @DisplayName("Creazione di una sessione con"
            + "terapeuta non trovato -> Errore")
    void testCreaSessioneTerapeutaNonTrovato() {
        // Inizializzazione dati di test
        String terapeutaEmail = "terapeuta@example.com";
        SessioneDTO sessioneDTO = new SessioneDTO();

        // Definizione comportamento dei mock
        when(sessioneRepository
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail))
                .thenReturn(Collections.emptyList());
        when(terapeutaRepository
                .findByEmail(terapeutaEmail)).thenReturn(Optional.empty());

        // Inizio test e verifica
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                sessioneService.creaSessione(sessioneDTO, terapeutaEmail));
        assertEquals("Terapeuta not found", exception.getMessage());

        verify(sessioneRepository, times(1))
                .findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(terapeutaEmail);
        verify(terapeutaRepository, times(1)).findByEmail(terapeutaEmail);
        verify(sessioneMapper, never()).toEntity(any(SessioneDTO.class));
        verify(disegnoRepository, never()).save(any(Disegno.class));
        verify(sessioneRepository, never()).save(any(Sessione.class));
    }
}
