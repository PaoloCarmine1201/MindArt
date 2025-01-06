package com.is.mindart.gestioneTerapeuta.service;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author mauriliolarocca
 * Classe di testing per la funzione di login
 * del Terapeuta nel service TerapeutaService.
 */
class LoginTerapeutaServiceTest {

    @Mock
    private TerapeutaRepository terapeutaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TerapeutaService terapeutaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Login con credenziali valide -> Token JWT generato")
    void shouldReturnTokenWhenCredentialsAreValid() {
        // Arrange
        String email = "terapeuta@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";
        String expectedToken = "mocked-jwt-token";

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setEmail(email);
        terapeuta.setPassword(encodedPassword);

        when(terapeutaRepository.findByEmail(email)).thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(email, "TERAPEUTA")).thenReturn(expectedToken);

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNotNull(token);
        assertEquals(expectedToken, token);
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(email, "TERAPEUTA");
    }

    @Test
    @DisplayName("Login con password errata -> Token non generato")
    void shouldReturnNullWhenPasswordIsInvalid() {
        // Arrange
        String email = "terapeuta@example.com";
        String rawPassword = "wrongPassword";
        String encodedPassword = "encodedPassword123";

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setEmail(email);
        terapeuta.setPassword(encodedPassword);

        when(terapeutaRepository.findByEmail(email)).thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNull(token);
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Login con email inesistente -> Token non generato")
    void shouldReturnNullWhenEmailIsNotFound() {
        // Arrange
        String email = "unknown@example.com";
        String rawPassword = "password123";

        when(terapeutaRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNull(token);
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}
