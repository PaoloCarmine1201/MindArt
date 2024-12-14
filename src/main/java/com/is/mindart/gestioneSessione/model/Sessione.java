package com.is.mindart.gestioneSessione.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Rappresenta una sessione terapeutica.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sessione {

    /**
     * Identificativo univoco della sessione.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tema assegnato alla sessione terapeutica.
     */
    private String temaAssegnato;

    /**
     * Data in cui si svolge la sessione.
     */
    private Date data;

    /**
     * Nota aggiuntiva relativa alla sessione.
     */
    private String nota;

    /**
     * Tipo della sessione (ad esempio: Disegno, Colore, Apprendimento).
     */
    private TipoSessione tipo;

    /**
     * Materiale utilizzato durante la sessione.
     */
    @ManyToOne
    @JoinColumn(name = "materiale_id")
    private Materiale materiale;

    /**
     * Terapeuta associato alla sessione.
     */
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    /**
     * Bambini associati alla sessione
     */
    @ManyToMany(mappedBy = "sessioni")
    private List<Bambino> bambini;
}
