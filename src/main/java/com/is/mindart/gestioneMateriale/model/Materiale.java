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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Rappresenta un materiale utilizzato durante le
 * sessioni terapeutiche.
 */
@Entity
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
     * Tipo del materiale (es. PDF, immagine, video).
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
     * Elenco delle sessioni terapeutiche che utilizzano
     * questo materiale.
     */
    @OneToMany(
            mappedBy = "materiale",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Sessione> sessioni;

    /**
     * Costruttore personalizzato per inizializzare i campi
     * principali evitando l'errore di HiddenField.
     *
     * @param paramId        ID del materiale
     * @param paramNome      Nome del materiale
     * @param paramTipo      Tipo del materiale (PDF, immagine, video)
     * @param paramPath      Percorso del file associato
     * @param paramTerapeuta Terapeuta associato
     * @param paramSessioni  Lista delle sessioni correlate
     */
    public Materiale(
            final Long paramId,
            final String paramNome,
            final TipoMateriale paramTipo,
            final String paramPath,
            final Terapeuta paramTerapeuta,
            final List<Sessione> paramSessioni
    ) {
        this.id = paramId;
        this.nome = paramNome;
        this.tipo = paramTipo;
        this.path = paramPath;
        this.terapeuta = paramTerapeuta;
        this.sessioni = paramSessioni;
    }
}
