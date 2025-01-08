package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import com.is.mindart.security.controller.AuthController;
import com.is.mindart.security.controller.TerapeutaLoginRequest;
import com.is.mindart.security.jwt.FileBasedTokenBlacklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Classe di testing per la funzione di login
 * del Terapeuta nel controller AuthController.
 */
public class LoginTerapeutaControllerTest {
    /**
     * Servizio per la gestione del terapeuta.
     */
    @Mock
    private TerapeutaService terapeutaService;
    /**
     * Servizio per la gestione del token blacklist.
     */
    @Mock
    private FileBasedTokenBlacklist tokenBlacklist;
    /**
     * Controller per la gestione dell'autenticazione.
     */
    @InjectMocks
    private AuthController authController;
    /**
     * Metodo di setup dell'ambiente di testing.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test per il login del terapeuta con successo.
     * @throws AuthenticationException
     */
    @Test
    public void testLoginTerapeutaSuccess() throws AuthenticationException {
        // Arrange
        final String email = "terapeuta@example.com";
        final String password = "password123";
        final String expectedToken = "mocked-jwt-token";

        TerapeutaLoginRequest request = new TerapeutaLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        when(terapeutaService.loginTerapeuta(email, password))
                .thenReturn(expectedToken);

        // Act
        ResponseEntity<String> response =
                authController.loginTerapeuta(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());

        verify(terapeutaService, times(1))
                .loginTerapeuta(email, password);
    }

    /**
     * Test per verificare il login del terapeuta con credenziali errate.
     * Deve ritornare 401 UNAUTHORIZED e il corpo della risposta nullo.
     *
     * @throws AuthenticationException
     * se si verifica un errore di autenticazione
     */
    @Test
    public void loginTerapeutaUnauthorizedTest()
            throws AuthenticationException {
        // Arrange
        final String email = "terapeuta@example.com";
        final String wrongPassword = "wrong-password";

        TerapeutaLoginRequest request = new TerapeutaLoginRequest();
        request.setEmail(email);
        request.setPassword(wrongPassword);

        when(terapeutaService.loginTerapeuta(email, wrongPassword))
                .thenReturn(null);

        // Act
        ResponseEntity<String> response =
                authController.loginTerapeuta(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(null, response.getBody());

        verify(terapeutaService, times(1))
                .loginTerapeuta(email, wrongPassword);
    }
}
