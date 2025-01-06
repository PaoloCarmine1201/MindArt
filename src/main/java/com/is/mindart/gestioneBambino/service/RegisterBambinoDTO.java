package com.is.mindart.gestioneBambino.service;
import com.is.mindart.gestioneBambino.model.Sesso;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBambinoDTO {

    /**
     * Id del bambino.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Codice univoco del bambino.
     */
    private String codice;

    /**
     * Nome del bambino.
     */
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$",
            message = "Il nome deve contenere solo lettere e spazi e "
                    + "deve essere lungo tra i 2 e i 50 caratteri.")
    private String nome;

    /**
     * Cognome del bambino.
     */
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$",
            message = "Il cognome deve contenere solo lettere e spazi e "
                    + "deve essere lungo tra i 2 e i 50 caratteri.")
    private String cognome;

    /**
     * Sesso del bambino.
     */
    private Sesso sesso;

    /**
     * Data di nascita del bambino.
     */
    @Past(message = "La data di nascita deve essere nel passato.")
    private Date dataDiNascita;

    /**
     * Codice fiscale del bambino.
     */
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
            message = "Il codice fiscale deve essere composto da 16 caratteri.")
    private String codiceFiscale;

    /**
     * Email del genitore.
     */
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "L'email deve essere valida.")
    private String emailGenitore;

    /**
     * Telefono del genitore.
     */
    @Pattern(regexp =
            "^(\\+\\d{1,2}\\s?)?1?-?\\.?\\s?\\("
                    + "?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$",
            message = "Il numero di telefono deve essere valido.")
    private String telefonoGenitore;


    /**
     * Id del Terapeuta associato.
     */
    @NotNull(message = "Il terapeuta non può essere vuoto")
    private Long terapeutaId;
}
