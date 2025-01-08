package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialeDTOResponse {
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
     * File in Base64.
     */
    @NotNull
    private String file;


    /**
     * Costruttore personalizzato per evitare campi nascosti
     * e rispettare le regole di CheckStyle.
     *
     * @param paramId             Identificatore del materiale
     * @param paramNome           Nome del materiale
     * @param paramTipoMateriale  Tipo del materiale
     * @param paramFile           File in ByteArrayResource
     */
    public MaterialeDTOResponse(
            final Long paramId,
            final String paramNome,
            final TipoMateriale paramTipoMateriale,
            final String paramFile
    ) {
        this.id = paramId;
        this.nome = paramNome;
        this.tipoMateriale = paramTipoMateriale;
        this.file = paramFile;

    }
}
