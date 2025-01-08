package com.is.mindart.gestioneDisegno.model;


import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;
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
     * Versione del disegno.
     */
    @Version
    private Long version;

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
     * Immagine per valutazione emotiva
     */
    @Lob
    private byte[] immagine;
    /**
     * Campo che conterrà l'insieme dei tratti di disegno
     * in formato JSON (es. un array di stroke).
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String strokesJson;

    /**
     * Data in cui il disegno è stato creato.
     */
    @CreationTimestamp
    private LocalDateTime data;

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
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sessione_id", referencedColumnName = "id")
    private Sessione sessione;

    /**
     * Elenco dei bambini coinvolti nella creazione del disegno.
     */
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "bambino_disegno",
            joinColumns = @JoinColumn(name = "disegno_id"),
            inverseJoinColumns = @JoinColumn(name = "bambino_id"))
    private List<Bambino> bambini;




}
