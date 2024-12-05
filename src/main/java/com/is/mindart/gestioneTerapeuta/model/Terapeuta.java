package com.is.mindart.gestioneTerapeuta.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneDisegno.model.Disegno;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneSessione.model.Sessione;
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
public class Terapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;
    private String cognome;
    private String email;

    private Date dataDiNascita;

    private String password;

    @OneToMany(mappedBy = "terapeuta",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Evento> eventi;

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Materiale> materiali;

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sessione> sessioni;

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disegno> disegni;

    @OneToMany(mappedBy = "terapeuta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bambino> bambini;

}
