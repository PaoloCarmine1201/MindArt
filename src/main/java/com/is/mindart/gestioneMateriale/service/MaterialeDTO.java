package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class MaterialeDTO {
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

    @NotNull
    private byte[] file;


    /**
     * Costruttore personalizzato per evitare campi nascosti
     * e rispettare le regole di CheckStyle.
     *
     * @param paramId             Identificatore del materiale
     * @param paramNome           Nome del materiale
     * @param paramTipoMateriale  Tipo del materiale
     * @param paramFile           File in ByteArrayResource
     */
    public MaterialeDTO(
            final Long paramId,
            final String paramNome,
            final TipoMateriale paramTipoMateriale,
            final byte[] paramFile
    ) {
        this.id = paramId;
        this.nome = paramNome;
        this.tipoMateriale = paramTipoMateriale;
        this.file = paramFile;

    }
}
