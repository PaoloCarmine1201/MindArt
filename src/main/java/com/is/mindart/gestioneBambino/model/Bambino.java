package com.is.mindart.gestioneBambino.model;

import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;
import java.util.List;

/**
 * Rappresenta un bambino registrato nel sistema.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bambino {

    /**
     * Identificativo univoco del bambino.
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
     * Terapeuta associato al bambino.
     */
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    /**
     * Associazione molti a molti Disegno - Bambino.
     */
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "bambino_sessione",
            joinColumns = @JoinColumn(name = "bambino_id"),
            inverseJoinColumns = @JoinColumn(name = "sessione_id"))
    private List<Sessione> sessioni;

    /**
     * Visibilità del bambino.
     */
    @ToString.Exclude
    private Boolean visibile;
}
