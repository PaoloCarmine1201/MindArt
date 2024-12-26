package com.is.mindart.gestioneDisegno.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneDisegno.service.DisegnoMessage;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
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
     * Dati vettoriali del disegno in formato JSON.
     */
    @Column(name = "disegno", columnDefinition = "JSON", nullable = false)
    @Convert(converter = DisegnoMessageConverter.class)
    private DrawingData disegno;

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


    /**
     * Convertitore per serializzare/deserializzare il campo disegno.
     */
    @Converter(autoApply = true)
    public static class DisegnoMessageConverter implements AttributeConverter<DisegnoMessage, String> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(final DisegnoMessage drawingData) {
            try {
                return objectMapper.writeValueAsString(drawingData);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Errore nella serializzazione del disegno", e);
            }
        }

        @Override
        public DisegnoMessage convertToEntityAttribute(final String json) {
            try {
                return objectMapper.readValue(json, DisegnoMessage.class);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Errore nella deserializzazione del disegno", e);
            }
        }
    }


}
