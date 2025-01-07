package com.is.mindart.gestioneBambino.controller;

import com.is.mindart.gestioneBambino.model.Sesso;
import com.is.mindart.gestioneBambino.service.BambinoService;
import com.is.mindart.gestioneBambino.service.RegisterBambinoDTO;
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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AddBambinoControllerTest {
    /**
     * Service per la gestione dei bambini.
     */
    @Mock
    private BambinoService bambinoService;
    /**
     * Oggetto per la gestione dell'autenticazione.
     */
    @Mock
    private Authentication authentication;
    /**
     * Controller per la gestione dei bambini.
     */
    @InjectMocks
    private BambinoController bambinoController;
    /**
     * Validatore per i parametri del bambino.
     */
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Test addBambino con tutti i parametri corretti -> Successo")
    void testAddBambinoSuccess() {
        // Arrange
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();

        // Act
        ResponseEntity<RegisterBambinoDTO> response =
                bambinoController.addBambino(bambinoDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bambinoDto, response.getBody());
        verify(bambinoService, times(1))
                .addBambino(bambinoDto, "terapeuta@example.com");
    }

    @Test
    @DisplayName("Test addBambino con nome non valido -> Errore")
    void testAddBambinoNomeNonValido() {
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto.setNome("M1"); // Nome non valido

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(1, violations.size());
        assertEquals("Il nome deve contenere solo lettere e spazi e "
                        + "deve essere lungo tra i 2 e i 50 caratteri.",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Test addBambino con cognome non valido -> Errore")
    void testAddBambinoCognomeNonValido() {
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto.setCognome("R1"); // Cognome non valido

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(1, violations.size());
        assertEquals("Il cognome deve contenere solo lettere e spazi e "
                        + "deve essere lungo tra i 2 e i 50 caratteri.",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Test addBambino con sesso nullo -> Errore")
    void testAddBambinoSessoNullo() {
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto.setSesso(null); // Sesso non valido

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("Test addBambino con data di nascita futura -> Errore")
    void testAddBambinoDataDiNascitaFutura() {
        final int time = 100000;
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto
                // Data di nascita futura
                .setDataDiNascita(new Date(System.currentTimeMillis() + time));

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(1, violations.size());
        assertEquals("La data di nascita deve essere nel passato.",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Test addBambino con codice fiscale non valido -> Errore")
    void testAddBambinoCodiceFiscaleNonValido() {
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto.setCodiceFiscale("12345"); // Codice fiscale non valido

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(1, violations.size());
        assertEquals("Il codice fiscale deve essere composto da 16 caratteri.",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Test addBambino con email genitore non valida -> Errore")
    void testAddBambinoEmailGenitoreNonValida() {
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto.setEmailGenitore("email-invalid"); // Email non valida

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(1, violations.size());
        assertEquals("Devi inserire un'email valida",
                violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Test addBambino con telefono genitore non valido -> Errore")
    void testAddBambinoTelefonoGenitoreNonValido() {
        RegisterBambinoDTO bambinoDto = createValidBambinoDTO();
        bambinoDto.setTelefonoGenitore("123"); // Telefono non valido

        Set<ConstraintViolation<RegisterBambinoDTO>> violations =
                validator.validate(bambinoDto);

        assertEquals(1, violations.size());
        assertEquals("Il numero di telefono deve essere valido.",
                violations.iterator().next().getMessage());
    }

    private RegisterBambinoDTO createValidBambinoDTO() {
        final int time = 10000000;
        return new RegisterBambinoDTO(
                null,
                "CODICE123",
                "Mario",
                "Rossi",
                Sesso.MASCHIO,
                // Data di nascita passata
                new Date(System.currentTimeMillis() - time),
                "RSSMRA85M01H501Z",
                "genitore@example.com",
                "+39 333 1234567",
                null
        );
    }
}
