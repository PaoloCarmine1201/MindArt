package com.is.mindart.gestioneSessione.controller;

import com.is.mindart.gestioneSessione.model.Sessione;
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
import static org.mockito.Mockito.when;

public class TestAccessoBambino {
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
        when(authentication.getPrincipal()).thenReturn("1");
    }

    @Test
    @DisplayName("Accesso ad una sessione -> 200 OK")
    public void testAccessoSessione() {
        // Arrange
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setBambini(List.of(1L));
        sessioneService.creaSessione(sessioneDTO, "terapeuta@example.com");
        ResponseEntity<SessioneDTO> response = sessioneController.getSessioneBambino();
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}
