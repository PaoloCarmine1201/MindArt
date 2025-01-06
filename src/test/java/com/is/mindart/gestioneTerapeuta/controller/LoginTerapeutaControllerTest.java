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
import static org.mockito.Mockito.*;

/**
 * @author mauriliolarocca
 * Classe di testing per la funzione di login
 * del Terapeuta nel controller AuthController.
 */
public class LoginTerapeutaControllerTest {

    @Mock
    private TerapeutaService terapeutaService;

    @Mock
    private FileBasedTokenBlacklist tokenBlacklist;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginTerapeuta_Success() throws AuthenticationException {
        // Arrange
        TerapeutaLoginRequest request = new TerapeutaLoginRequest();
        request.setEmail("terapeuta@example.com");
        request.setPassword("password123");
        String expectedToken = "mocked-jwt-token";

        when(terapeutaService.loginTerapeuta("terapeuta@example.com", "password123"))
                .thenReturn(expectedToken);

        // Act
        ResponseEntity<String> response = authController.loginTerapeuta(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());
        verify(terapeutaService, times(1))
                .loginTerapeuta("terapeuta@example.com", "password123");
    }

    @Test
    public void testLoginTerapeuta_Unauthorized() throws AuthenticationException {
        // Arrange
        TerapeutaLoginRequest request = new TerapeutaLoginRequest();
        request.setEmail("terapeuta@example.com");
        request.setPassword("wrong-password");

        when(terapeutaService.loginTerapeuta("terapeuta@example.com", "wrong-password"))
                .thenReturn(null);

        // Act
        ResponseEntity<String> response = authController.loginTerapeuta(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(terapeutaService, times(1))
                .loginTerapeuta("terapeuta@example.com", "wrong-password");
    }
}

