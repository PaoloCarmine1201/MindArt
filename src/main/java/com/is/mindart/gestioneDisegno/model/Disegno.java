package com.is.mindart.gestioneDisegno.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.List;

/**
 * Rappresenta un disegno creato in una sessione terapeutica.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Disegno {

    /**
     * Identificativo univoco del disegno.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Voto assegnato al disegno.
     */
    private int voto;

    /**
     * Data in cui il disegno è stato creato.
     */
    private Date data;

    /**
     * Valutazione emotiva associata al disegno.
     */
    private ValutazioneEmotiva valutazioneEmotiva;

    /**
     * Terapeuta che supervisiona la creazione del disegno.
     */
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    /**
     * Profilo della sessione associata al disegno.
     * Relazione obbligatoria.
     */
    @NonNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sessione_id", referencedColumnName = "id")
    private Sessione profilo;

    /**
     * Elenco dei bambini coinvolti nella creazione del disegno.
     */
    @ManyToMany
    @JoinTable(
            name = "bambino_disegno",
            joinColumns = @JoinColumn(name = "disegno_id"),
            inverseJoinColumns = @JoinColumn(name = "bambino_id"))
    private List<Bambino> bambini;

}
