package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe per validare i campi di {@link SessioneDTO}.
 */
public class SessioneDTOValidator
        implements ConstraintValidator<ValidSessioneDTO, SessioneDTO> {

    @Autowired
    private MaterialeRepository materialeRepository;

    @Override
    public boolean isValid(
            final SessioneDTO sessioneDTO,
            final ConstraintValidatorContext constraintValidatorContext) {
        if (sessioneDTO == null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Sessione non può essere nulla")
                    .addConstraintViolation();
            return false;
        }


        Materiale materiale = sessioneDTO.getMateriale() != null ?
                materialeRepository.findById(sessioneDTO.getMateriale()).orElse(null) : null;

        // Controllo generale: verifica che ci sia almeno un partecipante
        // Test Case: TC_4.1_1, TC_4.1_2, TC_4.1_18
        if (sessioneDTO.getBambini() == null || sessioneDTO.getBambini().isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Sessione deve avere almeno un partecipante")
                    .addPropertyNode("bambini")
                    .addConstraintViolation();
            return false;
        }

        // Validazione per tipi di sessione specifici
        switch (sessioneDTO.getTipoSessione()) {
            case DISEGNO:
                // Controllo che il tema sia obbligatorio
                // Test Case: TC_4.1_3, TC_4.1_4
                if (sessioneDTO.getTemaAssegnato() == null || sessioneDTO.getTemaAssegnato().isEmpty()) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Disegno richiede un tema assegnato")
                            .addPropertyNode("temaAssegnato")
                            .addConstraintViolation();
                    return false;
                }
                break;

            case APPRENDIMENTO:
                // Controllo che il materiale sia obbligatorio
                // Test Case: TC_4.1_8, TC_4.1_12
                if (sessioneDTO.getMateriale() == null) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Apprendimento richiede un materiale allegato")
                            .addPropertyNode("materiale")
                            .addConstraintViolation();
                    return false;
                }

                if (materiale == null) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Apprendimento richiede un materiale allegato")
                            .addPropertyNode("materiale")
                            .addConstraintViolation();
                    return false;
                }

                // Controllo che il materiale sia supportato
                // Test Case: TC_4.1_5, TC_4.1_9
                if (materiale.getTipo().equals(TipoMateriale.IMMAGINE)) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Apprendimento supporta solo materiali di tipo PDF o Video")
                            .addPropertyNode("materiale")
                            .addConstraintViolation();
                    return false;
                }
                break;

            case COLORE:
                // Controllo che ci sia un solo partecipante
                // Test Case: TC_4.1_13
                if (sessioneDTO.getBambini().size() > 1) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Colore può avere un solo partecipante")
                            .addPropertyNode("bambini")
                            .addConstraintViolation();
                    return false;
                }
                // Controllo che il materiale sia obbligatorio
                // Test Case: TC_4.1_17
                if (sessioneDTO.getMateriale() == null) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Colore richiede un materiale allegato")
                            .addPropertyNode("materiale")
                            .addConstraintViolation();
                    return false;
                }

                if (materiale == null) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Apprendimento richiede un materiale allegato")
                            .addPropertyNode("materiale")
                            .addConstraintViolation();
                    return false;
                }
                // Controllo che il materiale sia di tipo Immagine
                // Test Case: TC_4.1_14, TC_4.1_16
                if (!materiale.getTipo().equals(TipoMateriale.IMMAGINE)) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "La sessione di tipo Colore supporta solo materiali di tipo Immagine")
                            .addPropertyNode("materiale")
                            .addConstraintViolation();
                    return false;
                }
                break;

            default:
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                                "Tipo di sessione non riconosciuto")
                        .addPropertyNode("tipoSessione")
                        .addConstraintViolation();
                return false;
        }

        return true;
    }
}
