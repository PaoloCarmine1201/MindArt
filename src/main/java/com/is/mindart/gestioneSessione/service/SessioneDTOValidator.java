package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneSessione.model.TipoSessione;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Classe per validare i campi di {@link SessioneDTO}.
 */
public class SessioneDTOValidator
        implements ConstraintValidator<ValidSessioneDTO, SessioneDTO> {

    /**
     * Metodo per validare i campi di {@link SessioneDTO}.
     *
     * @param sessioneDTO {@link SessioneDTO} da validare
     * @param constraintValidatorContext contesto del validatore
     * @return true se i campi sono validi, false altrimenti
     */
    @Override
    public boolean isValid(
            final SessioneDTO sessioneDTO,
            final ConstraintValidatorContext constraintValidatorContext) {
        if (sessioneDTO.getTipoSessione().equals(TipoSessione.DISEGNO)) {
            if (sessioneDTO.getMateriale() != null) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo DISEGNO non può avere del materiale")
                        .addPropertyNode("materiale")
                        .addConstraintViolation();
                return false;
            }
            if (sessioneDTO.getTemaAssegnato() == null
                    || sessioneDTO.getTemaAssegnato().isEmpty()) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo DISEGNO deve avere un tema assegnato")
                        .addPropertyNode("temaAssegnato")
                        .addConstraintViolation();
                return false;
            }
        } else if (sessioneDTO.getTipoSessione().equals(TipoSessione.COLORE)) {
            if (sessioneDTO.getBambini().size() != 1) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo COLORE deve avere "
                                + "esattamente un bambino")
                        .addPropertyNode("bambini")
                        .addConstraintViolation();
                return false;
            }
            if (sessioneDTO.getMateriale() == null) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo COLORE deve avere un materiale")
                        .addPropertyNode("materiale")
                        .addConstraintViolation();
                return false;
            }
            if (sessioneDTO.getTemaAssegnato() != null
                    && !sessioneDTO.getTemaAssegnato().isEmpty()) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo DISEGNO non "
                                + "può avere un tema assegnato")
                        .addPropertyNode("temaAssegnato")
                        .addConstraintViolation();
                return false;
            }
        } else { // TipoSessione.APPRENDIMENTO
            if (sessioneDTO.getMateriale() == null) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo APPRENDIMENTO "
                                + "deve avere un materiale")
                        .addPropertyNode("materiale")
                        .addConstraintViolation();
                return false;
            }
            if (sessioneDTO.getTemaAssegnato() != null
                    && !sessioneDTO.getTemaAssegnato().isEmpty()) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Sessione di Tipo DISEGNO "
                                + "non può avere un tema assegnato")
                        .addPropertyNode("temaAssegnato")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
