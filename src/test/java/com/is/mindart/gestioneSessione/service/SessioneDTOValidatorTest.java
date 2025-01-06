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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SessioneDTOValidator}.
 */
class SessioneDTOValidatorTest {

    /**
     * Validator da testare.
     */
    private SessioneDTOValidator validator;

    /**
     * Mock del contesto di validazione.
     */
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    /**
     * Mock del builder per le violazioni di constraint.
     */
    @Mock
    private ConstraintValidatorContext
            .ConstraintViolationBuilder violationBuilder;

    /**
     * Inizializza i mock e configura il comportamento
     * del {@link ConstraintValidatorContext}.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new SessioneDTOValidator();

        when(constraintValidatorContext
                .buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(violationBuilder);

        ConstraintValidatorContext.ConstraintViolationBuilder
                .NodeBuilderCustomizableContext nodeBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder
                        .NodeBuilderCustomizableContext.class);
        when(violationBuilder.addPropertyNode(anyString()))
                .thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation())
                .thenReturn(constraintValidatorContext);
    }

    /**
     * Test per la validazione di una sessione di
     * tipo DISEGNO con materiale non nullo.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo DISEGNO con materiale "
            + "non nullo -> Non valido")
    void testTipoDisegnoConMateriale() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        // ID del materiale (non valido per DISEGNO)
        sessioneDTO.setMateriale(1L);

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo DISEGNO non può avere del materiale");
    }

    /**
     * Test per la validazione di una sessione di tipo
     * DISEGNO senza tema assegnato.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo DISEGNO senza tema "
            + "assegnato -> Non valido")
    void testTipoDisegnoSenzaTemaAssegnato() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        // Nessun materiale
        sessioneDTO.setMateriale(null);
        // Nessun tema assegnato (non valido)
        sessioneDTO.setTemaAssegnato(null);

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo DISEGNO"
                                + "deve avere un tema assegnato");
    }

    /**
     * Test per la validazione corretta di una sessione di tipo DISEGNO.
     * Deve risultare valida.
     */
    @Test
    @DisplayName("Validazione di Tipo DISEGNO corretta -> Valido")
    void testTipoDisegnoValido() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setMateriale(null); // Nessun materiale
        sessioneDTO.setTemaAssegnato("Tema valido");

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * Test per la validazione di una sessione di
     * tipo COLORE con più di un bambino.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo COLORE "
            + "con più di un bambino -> Non valido")
    void testTipoColoreConPiuBambini() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(1L); // ID del materiale (valido)

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo COLORE"
                                + " deve avere esattamente un bambino");
    }

    /**
     * Test per la validazione di una sessione di tipo COLORE senza materiale.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo COLORE senza materiale -> Non valido")
    void testTipoColoreSenzaMateriale() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo COLORE deve avere un materiale");
    }

    /**
     * Test per la validazione di una sessione di
     * tipo COLORE con tema assegnato.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo COLORE con tema assegnato -> Non valido")
    void testTipoColoreConTemaAssegnato() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        // Un bambino
        sessioneDTO.setBambini(List.of(1L));
        // ID del materiale
        sessioneDTO.setMateriale(1L);
        // Tema assegnato (non valido)
        sessioneDTO.setTemaAssegnato("Tema non consentito");

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo COLORE non può "
                                + "avere un tema assegnato");
    }

    /**
     * Test per la validazione di una sessione di
     * tipo APPRENDIMENTO senza materiale.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo APPRENDIMENTO "
            + "senza materiale -> Non valido")
    void testTipoApprendimentoSenzaMateriale() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo APPRENDIMENTO "
                                + "deve avere un materiale");
    }

    /**
     * Test per la validazione di una sessione di
     * tipo APPRENDIMENTO con tema assegnato.
     * Deve risultare non valida.
     */
    @Test
    @DisplayName("Validazione di Tipo APPRENDIMENTO "
            + "con tema assegnato -> Non valido")
    void testTipoApprendimentoConTemaAssegnato() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        // ID del materiale
        sessioneDTO.setMateriale(1L);
        // Tema assegnato (non valido)
        sessioneDTO.setTemaAssegnato("Tema non consentito");

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate(
                        "Sessione di Tipo APPRENDIMENTO "
                                + "non può avere un tema assegnato");
    }

    /**
     * Test per la validazione corretta di una sessione di tipo APPRENDIMENTO.
     * Deve risultare valida.
     */
    @Test
    @DisplayName("Validazione di Tipo APPRENDIMENTO corretta -> Valido")
    void testTipoApprendimentoValido() {
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setMateriale(1L); // ID del materiale
        sessioneDTO.setTemaAssegnato(null); // Nessun tema assegnato

        boolean isValid =
                validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }
}
