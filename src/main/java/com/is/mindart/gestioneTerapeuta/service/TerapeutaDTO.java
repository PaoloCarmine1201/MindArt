package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerapeutaDTO {

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

    //todo controllare se la password deve avere dei vincoli
    /**
     * Password del terapeuta. Segue i vincoli del requisito RF_T23
     */
    @NotBlank(message = "La password non può essere vuota")
    private String password;
}