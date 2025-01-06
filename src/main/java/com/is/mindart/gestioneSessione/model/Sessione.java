package com.is.mindart.gestioneSessione.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rappresenta una sessione terapeutica.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sessione {

    /**
     * Costruttore della classe Sessione.
     *
     * @param idParam            Identificativo univoco della sessione.
     * @param temaAssegnatoParam Tema assegnato alla sessione terapeutica.
     * @param dataParam          Data in cui si svolge la sessione.
     * @param notaParam          Nota aggiuntiva relativa alla sessione.
     * @param tipoParam          Tipo della sessione.
     * @param materialeParam     Materiale utilizzato durante la sessione.
     * @param terapeutaParam     Terapeuta associato alla sessione.
     * @param bambiniParam       Bambini associati alla sessione.
     */
    public Sessione(final Long idParam,
                    final String temaAssegnatoParam,
                    final LocalDateTime dataParam,
                    final String notaParam,
                    final TipoSessione tipoParam,
                    final Materiale materialeParam,
                    final Terapeuta terapeutaParam,
                    final List<Bambino> bambiniParam) {
        this.id = idParam;
        this.temaAssegnato = temaAssegnatoParam;
        this.data = dataParam;
        this.nota = notaParam;
        this.tipo = tipoParam;
        this.materiale = materialeParam;
        this.terapeuta = terapeutaParam;
        this.bambini = bambiniParam;
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
     * Indica se la sessione è terminata.
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
     * Bambini associati alla sessione.
     */
    @ToString.Exclude
    @ManyToMany(mappedBy = "sessioni")
    private List<Bambino> bambini;
}
