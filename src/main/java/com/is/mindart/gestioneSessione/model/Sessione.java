package com.is.mindart.gestioneSessione.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import lombok.ToString;

import java.time.LocalDateTime;
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

    public Sessione(Long id,
                    String temaAssegnato,
                    LocalDateTime data,
                    String nota,
                    TipoSessione tipo,
                    Materiale materiale,
                    Terapeuta terapeuta,
                    List<Bambino> bambini) {
        this.id = id;
        this.temaAssegnato = temaAssegnato;
        this.data = data;
        this.nota = nota;
        this.tipo = tipo;
        this.materiale = materiale;
        this.terapeuta = terapeuta;
        this.bambini = bambini;
        this.terminata = false;
    }

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
    private LocalDateTime data;

    /**
     * Indica se la sessione è terminata
     */
    @ColumnDefault("false")
    private Boolean terminata;

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
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "materiale_id")
    private Materiale materiale;

    /**
     * Terapeuta associato alla sessione.
     */
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

    /**
     * Bambini associati alla sessione
     */
    @ToString.Exclude
    @ManyToMany(mappedBy = "sessioni")
    private List<Bambino> bambini;
}
