package com.is.mindart.gestioneSessione.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneSessione.service.SessioneService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestCreazioneSessione {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessioneService sessioneService;

    @Autowired
    private ObjectMapper objectMapper;

    private String obtainAccessToken(String email, String password) throws Exception {
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("email", email);
        authRequest.put("password", password);

        String requestBody = objectMapper.writeValueAsString(authRequest);

        // Supponendo che l'endpoint di login sia /api/auth/login
        String token = mockMvc.perform(post("/auth/terapeuta/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return token;
    }


    @Test
    void testCreateSessione_Success() throws Exception {
        // Simula l'autenticazione
        String token = obtainAccessToken("mariorossi@gmail.com", "password123");

        // Crea un DTO valido per la richiesta
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setTemaAssegnato("Tema valido");
        sessioneDTO.setMateriale(null);
        sessioneDTO.setBambini(Collections.emptyList());

        // Serializza il DTO in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(sessioneDTO);

        // Simula il comportamento del servizio
        Mockito.doNothing().when(sessioneService).creaSessione(Mockito.any(), Mockito.any());

        // Esegue la richiesta POST con il token JWT nell'intestazione Authorization
        mockMvc.perform(post("/api/terapeuta/sessione/create")
                        .header("Authorization", "Bearer " + token) // Aggiungi il token JWT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()); // 200 OK


        // Verifica che il servizio sia stato chiamato una sola volta
        Mockito.verify(sessioneService, Mockito.times(1))
                .creaSessione(Mockito.any(SessioneDTO.class), Mockito.eq("mariorossi@gmail.com"));
    }

    @Test
    void testCreateSessione_InvalidDTO() throws Exception {
        // Simula l'autenticazione
        String token = obtainAccessToken("mariorossi@gmail.com", "password123");

        // Crea un DTO non valido (violazione del validator)
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setTemaAssegnato(null); // Tema assegnato mancante
        sessioneDTO.setMateriale(1L); // Materiale non permesso per DISEGNO

        // Serializza il DTO in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(sessioneDTO);

        // Esegue la richiesta POST con il token JWT nell'intestazione Authorization
        mockMvc.perform(post("/api/terapeuta/sessione/create")
                        .header("Authorization", "Bearer " + token) // Aggiungi il token JWT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()); // 400 BAD REQUEST

        // Verifica che il servizio non venga chiamato
        Mockito.verify(sessioneService, Mockito.never())
                .creaSessione(Mockito.any(), Mockito.any());
    }
}
