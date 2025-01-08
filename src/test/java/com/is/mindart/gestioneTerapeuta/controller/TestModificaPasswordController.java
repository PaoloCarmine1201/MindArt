package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaCambioPasswordDTO;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


public class TestModificaPasswordController {
    /**
     * Servizio per la gestione dei terapeuti mock.
     */
    @Mock
    private TerapeutaService terapeutaService;
    /**
     * Autenticazione mock.
     */
    @Mock
    private Authentication authentication;
    /**
     * Controller per la gestione dei terapeuti da iniettare.
     */
    @InjectMocks
    private TerapeutaController terapeutaController;
    /**
     * DTO per la modifica della password.
     */
    private TerapeutaCambioPasswordDTO dto;

    /**
     * Metodo di setup dell'ambiente di testing.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");
    }

    /**
     * Test per il cambio della password con successo.
     */
    @Test
    void testChangePasswordControllerSuccess() {
        dto = new TerapeutaCambioPasswordDTO("oldPassword", "newPassword123");
        when(terapeutaService.changePassword(
                anyString(), anyString(), anyString())).thenReturn(true);

        ResponseEntity<String> response =
                terapeutaController.cambiaPassword(dto);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(terapeutaService, times(1)).changePassword(
                (String) authentication.getPrincipal(),
                dto.getOldPassword(),
                dto.getNewPassword()
        );
    }
    /**
     * Test per il cambio della password con password vecchia errata.
     */
    @Test
    void testChangePasswordControllerOldPasswordIncorrect() {
        dto = new TerapeutaCambioPasswordDTO("oldPassword", "newPassword123");
        when(terapeutaService.changePassword(
                anyString(), anyString(), anyString())).thenReturn(false);

        ResponseEntity<String> response =
                terapeutaController.cambiaPassword(dto);

        assertFalse(response.getStatusCode().is2xxSuccessful());
        verify(terapeutaService, times(1)).changePassword(
                (String) authentication.getPrincipal(),
                dto.getOldPassword(),
                dto.getNewPassword()
        );
    }
    /**
     * Test per il cambio della password con password attuale mancante.
     */
    @Test
    void testChangePasswordControllerNoInput() {
        Validator validator =
                Validation.buildDefaultValidatorFactory().getValidator();

        // Crea il DTO con una nuova password non conforme
        dto = new TerapeutaCambioPasswordDTO(
                null, null // Password non conforme
        );

        // Valida il DTO
        Set<ConstraintViolation<TerapeutaCambioPasswordDTO>> violations =
                validator.validate(dto);

        // Assert che ci siano violazioni
        assertFalse(violations.isEmpty());
    }

    /**
     * Test per il cambio della password con password attuale errata.
     */
    @Test
    void testChangePasswordControllerNewPasswordNotCompliant() {
        // Inizializza un Validator
        Validator validator =
                Validation.buildDefaultValidatorFactory().getValidator();

        // Crea il DTO con una nuova password non conforme
        dto = new TerapeutaCambioPasswordDTO(
                "oldPassword", "NewPassword123" // Password non conforme
        );

        // Valida il DTO
        Set<ConstraintViolation<TerapeutaCambioPasswordDTO>> violations =
                validator.validate(dto);

        // Assert che ci siano violazioni
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(
                "La password deve contenere almeno una lettera maiuscola,"
                        + " un numero e un carattere speciale."
        )));
    }
}
