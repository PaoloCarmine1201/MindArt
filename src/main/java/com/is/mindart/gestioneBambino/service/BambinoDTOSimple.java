package com.is.mindart.gestioneBambino.service;

import com.is.mindart.gestioneBambino.model.Sesso;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BambinoDTOSimple {

    /**
     * Identificativo univoco del bambino.
     */
    private Long id;

    /**
     * Codice univoco del bambino.
     */
    @NotBlank(message = "Il codice non può essere vuoto")
    private String codice;

    /**
     * Nome del bambino.
     */
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    /**
     * Cognome del bambino.
     */
    @NotBlank(message = "Il cognome non può essere vuoto")
    private String cognome;

    /**
     * Data di nascita del bambino.
     */
    @NotNull(message = "La data di nascita non può essere vuota")
    @Past(message = "La data di nascita deve essere nel passato")
    private Date dataDiNascita;

    /**
     * Sesso del bambino.
     */
    @NotNull(message = "Il sesso non può essere vuoto")
    private Sesso sesso;

    /**
     * Codice fiscale del bambino.
     */
    @NotBlank(message = "Il codice fiscale non può essere vuoto")
    private String codiceFiscale;

    /**
     * Numero del telefono del genitore.
     */
    @NotBlank(message = "Il telefono del genitore non può essere vuoto")
    private String telefonoGenitore;

    /**
     * Email del genitore.
     */
    @NotBlank(message = "L'email del genitore non può essere vuoto")
    @Email(message = "Devi inserire un'email valida")
    private String emailGenitore;
}
