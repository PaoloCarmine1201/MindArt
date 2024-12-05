package com.is.mindart.gestioneDisegno.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Disegno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int voto;
    private Date data;
    private ValutazioneEmotiva valutazioneEmotiva;

    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sessione_id", referencedColumnName = "id")
    private Sessione profilo;

    @ManyToMany
    @JoinTable(
            name = "disegno_bambino",
            joinColumns = @JoinColumn(name = "disegno_id"),
            inverseJoinColumns = @JoinColumn(name = "bambino_id"))
    private List<Bambino> bambini;



}
