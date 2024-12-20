package com.is.mindart.gestioneSessione.model;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

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
     * Data in cui si svolge la sessione.
     */
    private Date fine;

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
}
