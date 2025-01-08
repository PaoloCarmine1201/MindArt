package com.is.mindart.gestioneTerapeuta.service;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

class TestModificaPasswordService {

    /**
     * Mock del repository per la gestione dei terapeuti.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;
    /**
     * Mock del PasswordEncoder di Spring Security.
     */
    @Mock
    private PasswordEncoder passwordEncoder;
    /**
     * Servizio per la gestione dei terapeuti, iniettato con i mock.
     */
    @InjectMocks
    private TerapeutaService terapeutaService;

    /**
     * Terapeuta di test.
     */
    private Terapeuta terapeuta;

    /**
     * Inizializza i mock prima di ogni test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        terapeuta = new Terapeuta();
        terapeuta.setEmail("test@terapeuta.com");
        terapeuta.setPassword("hashedOldPassword");
    }

    /**
     * Test per il cambio della password con successo.
     */
    @Test
    void testChangePasswordServiceSuccess() {
        when(terapeutaRepository.findByEmail("test@terapeuta.com"))
                .thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches("oldPassword", "hashedOldPassword"))
                .thenReturn(true);
        when(passwordEncoder.encode("NewPassword1!"))
                .thenReturn("hashedNewPassword");

        boolean result = terapeutaService.changePassword(
                "test@terapeuta.com", "oldPassword", "NewPassword1!");

        assertTrue(result);
        verify(terapeutaRepository).save(terapeuta);
        assertEquals("hashedNewPassword", terapeuta.getPassword());
    }

    /**
     * Test per il cambio della password senza input.
     */
    @Test
    void testChangePasswordServiceNoInput() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () ->
                    terapeutaService.changePassword(
                            "test@terapeuta.com", null, null
                    )
                );

        assertEquals("Terapeuta non trovato", exception.getMessage());
        verify(terapeutaRepository, never()).save(any());
    }

    /**
     * Test per il cambio della password con vecchia password errata.
     */
    @Test
    void testChangePasswordServiceOldPasswordIncorrect() {
        when(terapeutaRepository.findByEmail("test@terapeuta.com"))
                .thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches("wrongOldPassword", "hashedOldPassword"))
                .thenReturn(false);

        boolean result = terapeutaService.changePassword(
                "test@terapeuta.com", "wrongOldPassword", "NewPassword1!");

        assertFalse(result);
        verify(terapeutaRepository, never()).save(any());
    }
}
