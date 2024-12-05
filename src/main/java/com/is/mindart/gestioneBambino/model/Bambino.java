package com.is.mindart.gestioneBambino.model;

import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bambino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codice;
    private String nome;
    private String cognome;
    private Sesso sesso;
    private Date dataDiNascita;
    private String codiceFiscale;
    private String emailGenitore;
    private String telefonoGenitore;

    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    @ManyToMany
    @JoinTable(
            name = "bambino_disegno",
            joinColumns = @JoinColumn(name = "bambino_id"),
            inverseJoinColumns = @JoinColumn(name = "disegno_id"))
    private List<Disegno> disegni;




}
