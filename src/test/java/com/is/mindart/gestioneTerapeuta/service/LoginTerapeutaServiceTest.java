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


import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyString;



/**
 * @author mauriliolarocca
 * Classe di testing per la funzione di login
 * del Terapeuta nel service TerapeutaService.
 */
class LoginTerapeutaServiceTest {
    /**
     * Repository per l'entità Terapeuta.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;
    /**
     * Servizio per la criptazione della password.
     */
    @Mock
    private PasswordEncoder passwordEncoder;
    /**
     * Servizio per la generazione del token JWT.
     */
    @Mock
    private JwtUtil jwtUtil;
    /**
     * Servizio per la gestione del terapeuta.
     */
    @InjectMocks
    private TerapeutaService terapeutaService;
    /**
     * Metodo di setup dell'ambiente di testing.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test per il login del terapeuta con successo.
     */
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

        when(
                terapeutaRepository.findByEmail(email)
        )
                .thenReturn(Optional.of(terapeuta));
        when(
                passwordEncoder.matches(rawPassword, encodedPassword)
        )
                .thenReturn(true);
        when(
                jwtUtil.generateToken(email, "TERAPEUTA")
        )
                .thenReturn(expectedToken);

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNotNull(token);
        assertEquals(expectedToken, token);
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(email, "TERAPEUTA");
    }
    /**
     * Test per il login del terapeuta con password errata.
     */
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

        when(
                terapeutaRepository.findByEmail(email)
        )
                .thenReturn(Optional.of(terapeuta));
        when(
                passwordEncoder.matches(rawPassword, encodedPassword)
        )
                .thenReturn(false);

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNull(token);
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
    /**
     * Test per il login del terapeuta con email inesistente.
     */
    @Test
    @DisplayName("Login con email inesistente -> Token non generato")
    void shouldReturnNullWhenEmailIsNotFound() {
        // Arrange
        String email = "unknown@example.com";
        String rawPassword = "password123";

        when(
                terapeutaRepository.findByEmail(email)
        )
                .thenReturn(Optional.empty());

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNull(token);
        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}
