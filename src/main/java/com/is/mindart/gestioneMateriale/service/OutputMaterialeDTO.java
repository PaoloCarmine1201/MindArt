package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per l'output del materiale. Contiene le informazioni
 * di un materiale da restituire come risultato di un'operazione.
 */
@Data
@NoArgsConstructor
public class OutputMaterialeDTO {

    /**
     * Identificatore del materiale.
     */
    private Long id;

    /**
     * Nome del materiale.
     */
    @NotNull
    private String nome;

    /**
     * Tipo del materiale (es. PDF, IMMAGINE, VIDEO).
     */
    @NotNull
    private TipoMateriale tipoMateriale;

    /**
     * Costruttore personalizzato per evitare campi nascosti
     * e rispettare le regole di CheckStyle.
     *
     * @param paramId             Identificatore del materiale
     * @param paramNome           Nome del materiale
     * @param paramTipoMateriale  Tipo del materiale
     */
    public OutputMaterialeDTO(
            final Long paramId,
            final String paramNome,
            final TipoMateriale paramTipoMateriale
    ) {
        this.id = paramId;
        this.nome = paramNome;
        this.tipoMateriale = paramTipoMateriale;
    }
}
