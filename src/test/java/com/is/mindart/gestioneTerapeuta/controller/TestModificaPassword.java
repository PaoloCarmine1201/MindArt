package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaCambioPasswordDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestModificaPassword {

    @InjectMocks
    private TerapeutaController terapeutaController;

    @Mock
    private TerapeutaRepository terapeutaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    public TestModificaPassword() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void cambiaPassword_CorrectPassword_Success() {
        // Arrange
        String oldPassword = "oldPassword";//cambio password corretto
        String newPassword = "NewPassword123!";
        String email = "user@example.com";
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setPassword("$2a$10$hashedOldPassword"); // Simulated hashed password

        TerapeutaCambioPasswordDTO dto = new TerapeutaCambioPasswordDTO();
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(newPassword);

        when(terapeutaRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(terapeuta));
        when(passwordEncoder.matches(oldPassword, terapeuta.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$hashedNewPassword");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
        // Act
        ResponseEntity<String> response = terapeutaController.cambiaPassword(dto);

        // Assert
        assertEquals(ResponseEntity.ok("SUCCESS"), response);
        verify(terapeutaRepository, times(1)).save(any(Terapeuta.class));

        SecurityContextHolder.clearContext();
    }

    @Test
    void cambiaPassword_WrongOldPassword_Failure() {
        // Arrange
        String oldPassword = "wrongPassword";//Password vecchia errata
        String newPassword = "NewPassword123!";
        String email = "user@example.com";
        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setPassword("$2a$10$hashedOldPassword"); // Simulated hashed password

        TerapeutaCambioPasswordDTO dto = new TerapeutaCambioPasswordDTO();
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(newPassword);

        when(terapeutaRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(terapeuta));
        when(passwordEncoder.matches(oldPassword, terapeuta.getPassword())).thenReturn(false);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<String> response = terapeutaController.cambiaPassword(dto);

        // Assert
        assertEquals(ResponseEntity.status(400).build(), response);
        verify(terapeutaRepository, never()).save(any(Terapeuta.class));

        SecurityContextHolder.clearContext();
    }


    @Test
    void cambiaPassword_NewPasswordNotCompliant_Failure() {
        // Arrange
        String oldPassword = "oldPassword";
        String newPassword = "short"; // Password non conforme
        String email = "user@example.com";

        TerapeutaCambioPasswordDTO dto = new TerapeutaCambioPasswordDTO();
        dto.setOldPassword(oldPassword);
        dto.setNewPassword(newPassword);

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setPassword("$2a$10$hashedOldPassword"); // Password attuale mockata

        // Configurazione del contesto di sicurezza
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);

        // Configurazione del repository e del password encoder
        when(terapeutaRepository.findByEmail(email)).thenReturn(java.util.Optional.of(terapeuta));
        when(passwordEncoder.matches(oldPassword, terapeuta.getPassword())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            terapeutaController.cambiaPassword(dto);
        });

        assertEquals("Password non conforme", exception.getMessage()); // Verifica del messaggio di errore

        // Cleanup
        SecurityContextHolder.clearContext();
    }
}

