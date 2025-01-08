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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

/**
 * Classe di testing per la funzione di
 * login del Terapeuta nel service TerapeutaService.
 */
public final class LoginTerapeutaServiceTest {

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
     * Mock dell'utilità per la gestione dei JWT.
     */
    @Mock
    private JwtUtil jwtUtil;

    /**
     * Servizio per la gestione dei terapeuti, iniettato con i mock.
     */
    @InjectMocks
    private TerapeutaService terapeutaService;

    /**
     * Inizializza i mock prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test per verificare il login del terapeuta con credenziali valide.
     * Deve ritornare un token JWT generato.
     */
    @Test
    @DisplayName("Login con credenziali valide -> Token JWT generato")
    public void loginTerapeutaSuccessTest() {
        // Arrange
        final String email = "terapeuta@example.com";
        final String rawPassword = "password123";
        final String encodedPassword = "encodedPassword123";
        final String expectedToken = "mocked-jwt-token";

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setEmail(email);
        terapeuta.setPassword(encodedPassword);

        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);
        when(jwtUtil.generateToken(email, "TERAPEUTA"))
                .thenReturn(expectedToken);

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNotNull(token);
        assertEquals(expectedToken, token);
        verify(terapeutaRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(1))
                .generateToken(email, "TERAPEUTA");
    }

    /**
     * Test per verificare il login del terapeuta con una password errata.
     * Deve ritornare null e non generare un token.
     */
    @Test
    @DisplayName("Login con password errata -> Token non generato")
    public void loginTerapeutaUnauthorizedTest() {
        // Arrange
        final String email = "terapeuta@example.com";
        final String wrongPassword = "wrong-password";
        final String encodedPassword = "encodedPassword123";

        Terapeuta terapeuta = new Terapeuta();
        terapeuta.setEmail(email);
        terapeuta.setPassword(encodedPassword);

        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.of(terapeuta));
        when(passwordEncoder.matches(wrongPassword, encodedPassword))
                .thenReturn(false);

        // Act
        String token = terapeutaService.loginTerapeuta(email, wrongPassword);

        // Assert
        assertNull(token);
        verify(terapeutaRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(wrongPassword, encodedPassword);
        verify(jwtUtil, never())
                .generateToken(anyString(), anyString());
    }

    /**
     * Test per verificare il login del terapeuta con un'email inesistente.
     * Deve ritornare null e non generare un token.
     */
    @Test
    @DisplayName("Login con email inesistente -> Token non generato")
    public void loginTerapeutaEmailNotFoundTest() {
        // Arrange
        final String email = "unknown@example.com";
        final String rawPassword = "password123";

        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act
        String token = terapeutaService.loginTerapeuta(email, rawPassword);

        // Assert
        assertNull(token);
        verify(terapeutaRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, never())
                .matches(anyString(), anyString());
        verify(jwtUtil, never())
                .generateToken(anyString(), anyString());
    }
}
