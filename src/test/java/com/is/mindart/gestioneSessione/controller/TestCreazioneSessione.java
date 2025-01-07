package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestCreazioneSessione {

    @Mock
    private SessioneService sessioneService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SessioneController sessioneController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");
    }

    @Test
    @DisplayName("Creazione di una sessione -> 200 OK")
    public void testCreateSessione() {
        // Arrange
        SessioneDTO sessioneDTO = new SessioneDTO();

        // Act
        ResponseEntity<Void> response = sessioneController.create(sessioneDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessioneService, times(1))
                .creaSessione(sessioneDTO, "terapeuta@example.com");
    }


}
