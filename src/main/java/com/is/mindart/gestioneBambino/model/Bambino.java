package com.is.mindart.gestioneBambino.model;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String nome;

    /**
     * Cognome del bambino.
     */
    private String cognome;

    /**
     * Sesso del bambino.
     */
    private Sesso sesso;

    /**
     * Data di nascita del bambino.
     */
    private Date dataDiNascita;

    /**
     * Codice fiscale del bambino.
     */
    private String codiceFiscale;
    private String emailGenitore;
    private String telefonoGenitore;

    /**
     * Terapeuta associato al bambino.
     */
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    /**
     * Associazione molti a molti Disegno - Bambino
     */
    @ManyToMany
    @JoinTable(
            name = "bambino_disegno",
            joinColumns = @JoinColumn(name = "bambino_id"),
            inverseJoinColumns = @JoinColumn(name = "disegno_id"))
    private List<Disegno> disegni;

}
