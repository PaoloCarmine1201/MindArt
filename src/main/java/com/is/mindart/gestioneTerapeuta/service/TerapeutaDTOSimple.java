package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerapeutaDTOSimple {
    /**
     * Nome del terapeuta.
     */
    @NotNull(message = "Il nome non può essere nullo.")
    @NotBlank(message = "Il nome non può essere vuoto.")
    private String nome;

    /**
     * Cognome del terapeuta.
     */
    @NotNull(message = "Il cognome non può essere nullo.")
    @NotBlank(message = "Il cognome non può essere vuoto.")
    private String cognome;

    /**
     * Email del terapeuta.
     */
    @NotNull(message = "L'email non può essere nulla.")
    @NotBlank(message = "L'email non può essere vuota.")
    @Email(message = "L'email deve essere valida.")
    private String email;
}
