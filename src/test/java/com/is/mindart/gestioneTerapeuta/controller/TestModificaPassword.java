package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaCambioPasswordDTO;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestModificaPassword {

    @Mock
    private TerapeutaRepository terapeutaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TerapeutaService terapeutaService;

    private Terapeuta terapeuta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        terapeuta = new Terapeuta();
        terapeuta.setEmail("test@terapeuta.com");
        terapeuta.setPassword("hashedOldPassword");
    }

    @Test
    void testChangePassword_Success() {
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

    @Test
    void testChangePassword_NoInput() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                terapeutaService.changePassword("test@terapeuta.com", null, null)
        );

        assertEquals("Terapeuta non trovato", exception.getMessage());
        verify(terapeutaRepository, never()).save(any());
    }

    @Test
    void testChangePassword_OldPasswordIncorrect() {
        when(terapeutaRepository.findByEmail("test@terapeuta.com"))
                .thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches("wrongOldPassword", "hashedOldPassword"))
                .thenReturn(false);

        boolean result = terapeutaService.changePassword(
                "test@terapeuta.com", "wrongOldPassword", "NewPassword1!");

        assertFalse(result);
        verify(terapeutaRepository, never()).save(any());
    }

    @Test
    void testChangePassword_NewPasswordNotCompliant() {
        // Inizializza un Validator
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Crea il DTO con una nuova password non conforme
        TerapeutaCambioPasswordDTO dto = new TerapeutaCambioPasswordDTO(
                1L, "oldPassword", "NewPassword123" // Password non conforme
        );

        // Valida il DTO
        Set<ConstraintViolation<TerapeutaCambioPasswordDTO>> violations = validator.validate(dto);

        // Assert che ci siano violazioni
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(
                "La password deve contenere almeno una lettera maiuscola, un numero e un carattere speciale."
        )));
    }
}
