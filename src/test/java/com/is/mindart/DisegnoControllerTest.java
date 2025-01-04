package com.is.mindart;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DisegnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DisegnoService disegnoService;

    @Autowired
    private DisegnoRepository disegnoRepository; // Mock del repository se necessario

    @Autowired
    private SessioneRepository sessioneRepository; // Mock del repository se necessario

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private String obtainAccessToken(final String email,final String password) throws Exception {
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("email", email);
        authRequest.put("password", password);

        String requestBody = objectMapper.writeValueAsString(authRequest);

        // Supponendo che l'endpoint di login sia /api/auth/login
        return mockMvc.perform(post("/auth/terapeuta/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    /**
     * Test per verificare che il voto venga assegnato correttamente
     * e che l'endpoint restituisca 200 OK.
     */
    @Test
    public void testVota_Success() throws Exception {

        // Crea e salva una Sessione
        Sessione s = new Sessione(null, "Tema di disegno",
                LocalDateTime.now(), true,
                "Nota aggiuntiva sulla sessione",
                TipoSessione.DISEGNO, null, null,
                List.of());
        Sessione savedSessione = sessioneRepository.save(s);

        // Crea e salva un Disegno associato alla Sessione
        Disegno d = new Disegno(null, null, 7, null, null,
                ValutazioneEmotiva.FELICE,
                null, savedSessione,
                null);
        Disegno savedDisegno = disegnoRepository.save(d); // Assicurati di avere un metodo createDisegno nel servizio

        // Ottieni il token JWT autenticandoti
        String token = obtainAccessToken("mariorossi@gmail.com", "password123");

        // Simulazione del corpo della richiesta
        Map<String, String> valutazioneMap = new HashMap<>();
        valutazioneMap.put("valutazione", String.valueOf(8));

        String requestBody = objectMapper.writeValueAsString(valutazioneMap);

        // Esecuzione della richiesta POST con il token JWT
        mockMvc.perform(post("/api/terapeuta/disegno/{disegnoId}/valutazione", savedDisegno.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // Verifica che il metodo disegnoService.vota sia stato chiamato correttamente
        verify(disegnoService, times(1)). vota(eq(savedDisegno.getId()), eq(8));
    }
    /**
     * Test per verificare il comportamento quando la valutazione non è valida.
     * Ad esempio, quando il campo "valutazione" è mancante o non è un numero.
     */
    @Test
    public void testVota_MissingOrInvalidValutazione() throws Exception {
        long disegnoId = 1L;

        // Ottieni il token JWT autenticandoti
        String token = obtainAccessToken("mariorossi@gmail.com", "password123");

        // Simulazione del corpo della richiesta senza "valutazione"
        Map<String, String> valutazioneMap = new HashMap<>();
        // valutazioneMap.put("valutazione", String.valueOf(valutazione)); // Campo mancante

        String requestBody = objectMapper.writeValueAsString(valutazioneMap);

        // Esecuzione della richiesta POST con il token JWT
        mockMvc.perform(post("/api/terapeuta/disegno/{disegnoId}/valutazione", disegnoId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verifica che il metodo disegnoService.vota NON sia stato chiamato
        verify(disegnoService, times(0)).vota(anyLong(), anyInt());
    }

}
