package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneSessione.model.TipoSessione;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SessioneDTOValidatorTest {

    private SessioneDTOValidator validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new SessioneDTOValidator();
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(violationBuilder);

        // Dato che il ConstraintValidatorContext normalmente viene iniettato dal container di Spring
        // bisogna configurarne il comportamento
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
    }


    @Test
    @DisplayName("Validazione di Tipo DISEGNO con materiale non nullo -> Non valido")
    void testTipoDisegnoConMateriale() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setMateriale(1L); // ID del materiale (non valido per DISEGNO)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo DISEGNO non può avere del materiale");
    }

    @Test
    @DisplayName("Validazione di Tipo DISEGNO senza tema assegnato -> Non valido")
    void testTipoDisegnoSenzaTemaAssegnato() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setMateriale(null); // Nessun materiale
        sessioneDTO.setTemaAssegnato(null); // Nessun tema assegnato (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo DISEGNO deve avere un tema assegnato");
    }

    @Test
    @DisplayName("Validazione di Tipo DISEGNO corretta -> Valido")
    void testTipoDisegnoValido() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setMateriale(null); // Nessun materiale
        sessioneDTO.setTemaAssegnato("Tema valido");

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Validazione di Tipo COLORE con più di un bambino -> Non valido")
    void testTipoColoreConPiuBambini() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(1L); // ID del materiale (valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo COLORE deve avere esattamente un bambino");
    }

    @Test
    @DisplayName("Validazione di Tipo COLORE senza materiale -> Non valido")
    void testTipoColoreSenzaMateriale() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo COLORE deve avere un materiale");
    }

    @Test
    @DisplayName("Validazione di Tipo COLORE con tema assegnato -> Non valido")
    void testTipoColoreConTemaAssegnato() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(1L); // ID del materiale
        sessioneDTO.setTemaAssegnato("Tema non consentito"); // Tema assegnato (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo DISEGNO non può avere un tema assegnato");
    }

    @Test
    @DisplayName("Validazione di Tipo APPRENDIMENTO senza materiale -> Non valido")
    void testTipoApprendimentoSenzaMateriale() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo APPRENDIMENTO deve avere un materiale");
    }

    @Test
    @DisplayName("Validazione di Tipo APPRENDIMENTO con tema assegnato -> Non valido")
    void testTipoApprendimentoConTemaAssegnato() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setMateriale(1L); // ID del materiale
        sessioneDTO.setTemaAssegnato("Tema non consentito"); // Tema assegnato (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione di Tipo DISEGNO non può avere un tema assegnato");
    }

    @Test
    @DisplayName("Validazione di Tipo APPRENDIMENTO corretta -> Valido")
    void testTipoApprendimentoValido() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setMateriale(1L); // ID del materiale
        sessioneDTO.setTemaAssegnato(null); // Nessun tema assegnato

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }
}
