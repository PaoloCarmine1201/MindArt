package com.is.mindart.gestioneCalendario.service;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import jakarta.validation.constraints.AssertTrue;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    /**
     * Identificativo univoco dell'evento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome dell'evento.
     */
    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    /**
     * Data e ora di inizio dell'evento.
     * La data di inizio non può essere nel passato.
     */
    @NotNull(message = "La data di inizio non può essere nulla")
    private Date inizio;

    /**
     * Data e ora di fine dell'evento.
     */
    @NotNull(message = "La data di fine non può essere nulla")
    private Date fine;


    /**
     * Validazione per assicurarsi che
     * la data di inizio sia antecedente alla data di fine.
     * @return true se la data di inizio è antecedente alla data di fine
     */
    @AssertTrue(message = "La data di inizio deve"
            + "essere antecedente alla data di fine")
    public boolean isDateRangeValid() {
        if (inizio != null && fine != null) {
            return inizio.before(fine);
        }
        return false;
    }
}
