package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneSessione.model.TipoSessione;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class SessioneDTOValidatorTest {

    @InjectMocks
    private SessioneDTOValidator validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private MaterialeRepository materialeRepository;

    private final long IMMAGINE_MOCK_ID = 1L;
    private final long PDF_MOCK_ID = 2L;
    private final long VIDEO_MOCK_ID = 3L;

    private Materiale mockImmagine;
    private Materiale mockPdf;
    private Materiale mockVideo;

    @BeforeEach
    void setUp() {
        // Mocks dei materiali
        mockImmagine = new Materiale();
        mockImmagine.setId(IMMAGINE_MOCK_ID);
        mockImmagine.setTipo(TipoMateriale.IMMAGINE);

        mockPdf = new Materiale();
        mockPdf.setId(PDF_MOCK_ID);
        mockPdf.setTipo(TipoMateriale.PDF);

        mockVideo = new Materiale();
        mockVideo.setId(VIDEO_MOCK_ID);
        mockVideo.setTipo(TipoMateriale.VIDEO);

        MockitoAnnotations.openMocks(this);

        when(materialeRepository.findById(IMMAGINE_MOCK_ID)).thenReturn(java.util.Optional.of(mockImmagine));
        when(materialeRepository.findById(PDF_MOCK_ID)).thenReturn(java.util.Optional.of(mockPdf));
        when(materialeRepository.findById(VIDEO_MOCK_ID)).thenReturn(java.util.Optional.of(mockVideo));

        // validator = new SessioneDTOValidator();
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(violationBuilder);

        // Dato che il ConstraintValidatorContext normalmente viene iniettato dal container di Spring
        // bisogna configurarne il comportamento
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
    }

    /**
     * TC_4.1_1
     */
    @Test
    @DisplayName("Avvio sessione di tipo DISEGNO con un partecipante -> Valido")
    void testAvvioSessioneDisegnoConUnPartecipante() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setTemaAssegnato("Tema valido");

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_2
     */
    @Test
    @DisplayName("Avvio sessione di tipo DISEGNO con più di un partecipante -> Valido")
    void testAvvioSessioneDisegnoConPiuPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setTemaAssegnato("Tema valido");

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_3
     */
    @Test
    @DisplayName("Avvio sessione di tipo DISEGNO con un partecipante e senza tema assegnato -> Non valido")
    void testAvvioSessioneDisegnoSenzaTemaAssegnato() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setTemaAssegnato(null); // Nessun tema assegnato (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Disegno richiede un tema assegnato");
    }

    /**
     * TC_4.1_4
     */
    @Test
    @DisplayName("Avvio sessione di tipo DISEGNO con più di un partecipante e senza un tema assegnato -> Non valido")
    void testAvvioSessioneDisegnoConPiuPartecipantiETemaVuoto() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.DISEGNO);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setTemaAssegnato(null); // Tema assegnato vuoto (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Disegno richiede un tema assegnato");
    }

    /**
     * TC_4.1_5
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con un partecipante e con un materiale di tipo IMMAGINE -> non valido")
    void testAvvioSessioneApprendimentoConImmagine() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(IMMAGINE_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(IMMAGINE_MOCK_ID)).thenReturn(Optional.of(mockImmagine));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Apprendimento supporta solo materiali di tipo PDF o Video");
    }

    /**
     * TC_4.1_6
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con un partecipante e un materiale di tipo PDF -> Valido")
    void testAvvioSessioneApprendimentoConPdf() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(PDF_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(PDF_MOCK_ID)).thenReturn(Optional.of(mockPdf));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_7
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con un partecipante e un materiale di tipo VIDEO -> Valido")
    void testAvvioSessioneApprendimentoConVideo() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(VIDEO_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(VIDEO_MOCK_ID)).thenReturn(Optional.of(mockVideo));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_8
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con un partecipante e senza materiale -> Non valido")
    void testAvvioSessioneApprendimentoSenzaMateriale() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Apprendimento richiede un materiale allegato");
    }

    /**
     * TC_4.1_9
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con più di un partecipante e con un materiale di tipo IMMAGINE -> non valido")
    void testAvvioSessioneApprendimentoConImmaginePiuPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(IMMAGINE_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(IMMAGINE_MOCK_ID)).thenReturn(Optional.of(mockImmagine));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Apprendimento supporta solo materiali di tipo PDF o Video");
    }

    /**
     * TC_4.1_10
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con più di un partecipante e con un materiale di tipo PDF -> Valido")
    void testAvvioSessioneApprendimentoConPdfPiuPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(PDF_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(PDF_MOCK_ID)).thenReturn(Optional.of(mockPdf));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_11
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con più di un partecipante e con un materiale di tipo VIDEO -> Valido")
    void testAvvioSessioneApprendimentoConVideoPiuPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(VIDEO_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(VIDEO_MOCK_ID)).thenReturn(Optional.of(mockVideo));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_12
     */
    @Test
    @DisplayName("Avvio sessione di tipo APPRENDIMENTO con più di un partecipante e senza materiale -> Non valido")
    void testAvvioSessioneApprendimentoSenzaMaterialePiuPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.APPRENDIMENTO);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Apprendimento richiede un materiale allegato");
    }

    /**
     * TC_4.1_13
     */
    @Test
    @DisplayName("Avvio sessione di tipo COLORE con più di un partecipante -> Non valido")
    void testAvvioSessioneColoreConPiuPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L, 2L)); // Più di un bambino
        sessioneDTO.setMateriale(1L); // ID del materiale (valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Colore può avere un solo partecipante");
    }

    /**
     * TC_4.1_14
     */
    @Test
    @DisplayName("Avvio sessione di tipo COLORE con un partecipante e con un materiale di tipo IMMAGINE -> Valido")
    void testAvvioSessioneColoreConImmagine() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(IMMAGINE_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(IMMAGINE_MOCK_ID)).thenReturn(Optional.of(mockImmagine));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertTrue(isValid);
    }

    /**
     * TC_4.1_15
     */
    @Test
    @DisplayName("Avvio sessione di tipo COLORE con un partecipante e con un materiale di tipo PDF -> non Valido")
    void testAvvioSessioneColoreConPdf() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(PDF_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(PDF_MOCK_ID)).thenReturn(Optional.of(mockPdf));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Colore supporta solo materiali di tipo Immagine");
    }

    /**
     * TC_4.1_16
     */
    @Test
    @DisplayName("Avvio sessione di tipo COLORE con un partecipante e con un materiale di tipo VIDEO -> non Valido")
    void testAvvioSessioneColoreConVideo() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(VIDEO_MOCK_ID); // ID del materiale (valido)

        when(materialeRepository.findById(VIDEO_MOCK_ID)).thenReturn(Optional.of(mockVideo));

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Colore supporta solo materiali di tipo Immagine");
    }

    /**
     * TC_4.1_17
     */
    @Test
    @DisplayName("Avvio sessione di tipo COLORE con un partecipante e senza materiale -> Non valido")
    void testAvvioSessioneColoreSenzaMateriale() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(List.of(1L)); // Un bambino
        sessioneDTO.setMateriale(null); // Nessun materiale (non valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("La sessione di tipo Colore richiede un materiale allegato");
    }

    /**
     * TC_4.1_18
     */
    @Test
    @DisplayName("Avvio sessione senza partecipanti -> Non valido")
    void testAvvioSessioneSenzaPartecipanti() {
        // Inizializzazione dati di test
        SessioneDTO sessioneDTO = new SessioneDTO();
        sessioneDTO.setTipoSessione(TipoSessione.COLORE);
        sessioneDTO.setBambini(null); // Nessun bambino
        sessioneDTO.setMateriale(1L); // ID del materiale (valido)

        // Inizio test e assert
        boolean isValid = validator.isValid(sessioneDTO, constraintValidatorContext);
        assertFalse(isValid);
        verify(constraintValidatorContext, times(1))
                .buildConstraintViolationWithTemplate("Sessione deve avere almeno un partecipante");
    }



    /*---------------------------------------

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
    */
}
