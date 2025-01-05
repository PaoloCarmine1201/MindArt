package com.is.mindart.gestioneDisegno;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneDisegno.service.ValutazioneRequest;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DisegnoControllerTest {
    private final String terapeutaEmail = "mariorossi@gmail.com";
    private final String terapeutaPassword = "password123";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
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

    private String obtainAccessToken() throws Exception {
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("email", terapeutaEmail);
        authRequest.put("password", terapeutaPassword);

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
        Disegno savedDisegno = disegnoRepository.save(d);

        // Ottieni il token JWT autenticandoti
        String token = obtainAccessToken();

        // Crea l'oggetto ValutazioneRequest
        ValutazioneRequest valutazioneRequest = new ValutazioneRequest();
        valutazioneRequest.setValutazione(8);

        String requestBody = objectMapper.writeValueAsString(valutazioneRequest);

        // Esecuzione della richiesta POST con il token JWT
        mockMvc.perform(post("/api/terapeuta/disegno/{disegnoId}/valutazione", savedDisegno.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // Verifica che la valutazione sia stata aggiornata correttamente
        Disegno updatedDisegno = disegnoRepository.findById(savedDisegno.getId()).orElse(null);
        assertNotNull(updatedDisegno);
        assertEquals(8, updatedDisegno.getVoto());
    }

    /**
     * Test per verificare il comportamento quando la valutazione non è valida.
     * Ad esempio, quando il campo "valutazione" è mancante o non è un numero.
     */
    @Test
    public void testVota_MissingOrInvalidValutazione() throws Exception {
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
        Disegno savedDisegno = disegnoRepository.save(d);

        // Ottieni il token JWT autenticandoti
        String token = obtainAccessToken();

        // Simulazione del corpo della richiesta senza il campo "valutazione"
        ValutazioneRequest valutazioneRequest = new ValutazioneRequest();
        valutazioneRequest.setValutazione(12); // Omesso per testare caso mancante

        String requestBody = objectMapper.writeValueAsString(valutazioneRequest);

        // Esecuzione della richiesta POST con il token JWT
        mockMvc.perform(post("/api/terapeuta/disegno/{disegnoId}/valutazione", savedDisegno.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verifica che non sia stato modificato il voto
        Disegno unchangedDisegno = disegnoRepository.findById(savedDisegno.getId()).orElse(null);
        assertNotNull(unchangedDisegno);
        assertEquals(7, unchangedDisegno.getVoto()); // Resta invariato
    }

}
