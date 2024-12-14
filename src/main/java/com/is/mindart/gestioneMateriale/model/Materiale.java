package com.is.mindart.gestioneMateriale.model;

import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Rappresenta un materiale utilizzato durante le sessioni terapeutiche.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Materiale {

    /**
     * Identificativo univoco del materiale.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome del materiale.
     */
    private String nome;

    /**
     * Tipo del materiale (PDF, immagine, video).
     */
    private TipoMateriale tipo;

    /**
     * Percorso del file associato al materiale.
     */
    private String path;

    /**
     * Terapeuta associato al materiale.
     */
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    /**
     * Elenco delle sessioni terapeutiche
     * che utilizzano questo materiale.
     */
    @OneToMany(mappedBy = "materiale",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Sessione> sessioni;
}
