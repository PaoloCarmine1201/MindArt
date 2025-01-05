package com.is.mindart.gestioneMateriale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneMateriale.service.MaterialeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddMaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialeService materialeService;

    @Autowired
    private ObjectMapper objectMapper;

    private String obtainAccessToken() throws Exception {
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("email", "mariorossi@gmail.com");
        authRequest.put("password", "password123");

        String requestBody = objectMapper.writeValueAsString(authRequest);

        // Simula l'endpoint di login e ottieni un token JWT
        return mockMvc.perform(post("/auth/terapeuta/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testAddMaterial_Success() throws Exception {
        // Ottieni un token JWT simulando il login
        String token = obtainAccessToken();

        // Crea un file valido
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "Test content".getBytes());

        // Simula il servizio
        Mockito.when(materialeService.existsMateriale(Mockito.any()))
                .thenReturn(false); // Nessun duplicato

        Mockito.when(materialeService.addMateriale(Mockito.any()))
                .thenReturn(new OutputMaterialeDTO(1L, "test.pdf", TipoMateriale.PDF));

        // Esegui la richiesta con l'endpoint corretto
        mockMvc.perform(multipart("/api/terapeuta/materiale/")
                        .file(file)
                        .header("Authorization", "Bearer " + token) // Token JWT
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("test.pdf"))
                .andExpect(jsonPath("$.tipoMateriale").value("PDF"));

        // Verifica che il metodo existsMateriale sia stato chiamato una volta
        Mockito.verify(materialeService, Mockito.times(1))
                .existsMateriale(Mockito.any());
        // Verifica che il metodo addMateriale sia stato chiamato una volta
        Mockito.verify(materialeService, Mockito.times(1))
                .addMateriale(Mockito.any());
    }

    @Test
    void testAddMaterial_EmptyFile() throws Exception {
        // Ottieni un token JWT simulando il login
        String token = obtainAccessToken();

        // Simula un file vuoto
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", "application/pdf", new byte[0]);

        // Esegui la richiesta e cattura il risultato
        MvcResult result = mockMvc.perform(multipart("/api/terapeuta/materiale/")
                        .file(emptyFile)
                        .header("Authorization", "Bearer " + token) // Token JWT
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Verifica che l'eccezione risolta sia del tipo EmptyFileException
        assertInstanceOf(EmptyFileException.class, result.getResolvedException());


        // Verifica che il servizio non venga chiamato
        Mockito.verify(materialeService, Mockito.never())
                .existsMateriale(Mockito.any());
        Mockito.verify(materialeService, Mockito.never())
                .addMateriale(Mockito.any());
    }

    @Test
    void testAddMaterial_UnsupportedFileType() throws Exception {
        // Ottieni un token JWT simulando il login
        String token = obtainAccessToken();

        // Simula un file con tipo non supportato
        MockMultipartFile unsupportedFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Test content".getBytes());

        // Esegui la richiesta e cattura il risultato
        MvcResult result = mockMvc.perform(multipart("/api/terapeuta/materiale/")
                        .file(unsupportedFile)
                        .header("Authorization", "Bearer " + token) // Token JWT
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Verifica che l'eccezione risolta sia del tipo UnsupportedFileException
        assertInstanceOf(UnsupportedFileException.class, result.getResolvedException());

        // Verifica che il servizio non venga chiamato
        Mockito.verify(materialeService, Mockito.never())
                .existsMateriale(Mockito.any());
        Mockito.verify(materialeService, Mockito.never())
                .addMateriale(Mockito.any());
    }

    @Test
    void testAddMaterial_DuplicatedFile() throws Exception {
        // Ottieni un token JWT simulando il login
        String token = obtainAccessToken();

        // Simula un file valido
        MockMultipartFile file = new MockMultipartFile(
                "file", "duplicate.pdf", "application/pdf", "Test content".getBytes());

        // Simula il servizio che rileva un duplicato
        Mockito.when(materialeService.existsMateriale(Mockito.any()))
                .thenReturn(true);

        // Esegui la richiesta e cattura il risultato
        MvcResult result = mockMvc.perform(multipart("/api/terapeuta/materiale/")
                        .file(file)
                        .header("Authorization", "Bearer " + token) // Token JWT
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isConflict())
                .andReturn();

        // Verifica che l'eccezione risolta sia del tipo DuplicatedFileException
        assertInstanceOf(DuplicatedFileException.class, result.getResolvedException());

        // Verifica che il metodo existsMateriale sia stato chiamato una volta
        Mockito.verify(materialeService, Mockito.times(1))
                .existsMateriale(Mockito.any());

        // Verifica che il metodo addMateriale non venga mai chiamato
        Mockito.verify(materialeService, Mockito.never())
                .addMateriale(Mockito.any());
    }
}
