package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerapeutaDTO {
    /**
     * Lunghezza minima della password.
     */
    private static final int MINPSWD = 8;

    /**
     * Nome del terapeuta.
     */
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    /**
     * Cognome del terapeuta.
     */
    @NotBlank(message = "Il cognome non può essere vuoto")
    private String cognome;

    /**
     * Email del terapeuta.
     */
    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Devi inserire un'email valida")
    private String email;

    /**
     * Data di nascita del terapeuta.
     */
    @NotNull(message = "La data di nascita è obbligatoria")
    @Past(message = "La data di nascita deve essere nel passato")
    private Date dataDiNascita;

    /**
     * Password del terapeuta. Segue i vincoli del requisito RF_T23.
     */
    @NotBlank(message = "La password non può essere vuota")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!?_.,:;@#$%^&*])[A-Za-z0-9!?_.,:;@#$%^&*]{8,}$",
            message = "La password deve contenere almeno una "
                    + "lettera maiuscola, un numero e un carattere speciale."
    )
    @Size(min = MINPSWD, message = "La password deve avere almeno 8 caratteri.")
    private String password;
}
