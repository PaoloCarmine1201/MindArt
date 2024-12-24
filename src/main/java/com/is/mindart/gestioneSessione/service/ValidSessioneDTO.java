package com.is.mindart.gestioneSessione.service;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SessioneDTOValidator.class)
public @interface ValidSessioneDTO {
    /**
     * Ritorna la motivazione del fallimento della validazione.
     * @return message
     */
    String message() default "SessioneDTO non valida";
    /**
     * Gruppi di validazione.
     * @return groups
     */
    Class<?>[] groups() default {};
    /**
     * Payload per eventuale logging di informazioni aggiuntive.
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};
}
