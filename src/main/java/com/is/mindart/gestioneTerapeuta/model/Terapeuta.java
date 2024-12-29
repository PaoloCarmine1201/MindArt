package com.is.mindart.gestioneTerapeuta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOStat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Rappresenta un terapeuta registrato nel sistema.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Terapeuta{

    /**
     * Identificativo univoco del terapeuta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome del terapeuta.
     */
    private String nome;

    /**
     * Cognome del terapeuta.
     */
    private String cognome;

    /**
     * Email del terapeuta.
     */
    private String email;

    /**
     * Data di nascita del terapeuta.
     */
    private Date dataDiNascita;

    /**
     * Password del terapeuta per l'accesso al sistema.
     */
    private String password;

    /**
     * Elenco degli eventi associati al terapeuta.
     */
    @OneToMany(mappedBy = "terapeuta",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private List<Evento> eventi;

    /**
     * Elenco dei materiali creati o gestiti dal terapeuta.
     */
    @OneToMany(mappedBy = "terapeuta",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private List<Materiale> materiali;

    /**
     * Elenco delle sessioni condotte dal terapeuta.
     */
    @OneToMany(mappedBy = "terapeuta",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private List<Sessione> sessioni;

    /**
     * Elenco dei disegni creati sotto la supervisione del terapeuta.
     */
    @OneToMany(mappedBy = "terapeuta",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private List<Disegno> disegni;

    /**
     * Elenco dei bambini seguiti dal terapeuta.
     */
    @OneToMany(mappedBy = "terapeuta",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private List<Bambino> bambini;

    public int getNumeroSessioni() {
        return sessioni.stream()
                .map(Sessione::getId)
                .distinct()
                .toList()
                .size();
    }

    public int getNumeroBambini() {
        return bambini.stream()
                .map(Bambino::getId)
                .distinct()
                .toList()
                .size();
    }
}
