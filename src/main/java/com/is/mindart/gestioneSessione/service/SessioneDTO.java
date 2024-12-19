package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneSessione.model.TipoSessione;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidSessioneDTO
public class SessioneDTO {

    /**
     * Id, da non inserire, verrà generato da spring.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * {@link TipoSessione} - Tipo di sessione scelta.
     */
    @NotNull(message = "Il tipo di sessione non può essere vuoto")
    private TipoSessione tipoSessione;

    /**
     * Id del terapeuta loggato quando viene creata la sessione.
     */
    @NotNull(message = "Campo id terapeuta vuoto")
    private Long idTerapeuta;

    /**
     * Tema assegnato se {@link TipoSessione} è di disegno.
     */
    private String temaAssegnato;

    /**
     * Id del materiale scelto se {@link TipoSessione} è di apprendimento.
     */
    private Long materiale;

    /**
     * Lista dei bambini partecipanti alla sessione.
     * Dovrebbe contenere un solo bambino se {@link TipoSessione} è di disegno.
     */
    @NotNull(message = "La lista di bambini non può essere nulla")
    private List<Long> bambini;


}
