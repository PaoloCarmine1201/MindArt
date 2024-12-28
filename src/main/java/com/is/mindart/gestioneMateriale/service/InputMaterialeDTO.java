package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO per l'input del materiale. Fornisce le informazioni
 * necessarie per creare o aggiornare un materiale.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputMaterialeDTO {

    /**
     * Identificatore del materiale.
     */
    private Long id;

    /**
     * Nome del materiale (obbligatorio).
     */
    @NotNull
    private String nome;

    /**
     * Tipo del materiale (es. PDF, IMMAGINE, VIDEO).
     */
    @NotNull
    private TipoMateriale tipoMateriale;

    /**
     * Identificatore del terapeuta associato al materiale.
     */
    @NotNull
    private Long terapeutaId;

    /**
     * File del materiale.
     */
    private MultipartFile file;

    /**
     * Costruttore che permette di specificare i campi
     * principali del materiale (senza il file).
     *
     * @param paramId           Identificatore del materiale
     * @param paramNome         Nome del materiale
     * @param paramTipoMateriale Tipo del materiale (PDF, IMMAGINE, VIDEO)
     * @param paramTerapeutaId  ID del terapeuta associato
     */
    public InputMaterialeDTO(
            final Long paramId,
            final String paramNome,
            final TipoMateriale paramTipoMateriale,
            final Long paramTerapeutaId
    ) {
        // Assegnazioni dei campi
        this.id = paramId;
        this.nome = paramNome;
        this.tipoMateriale = paramTipoMateriale;
        this.terapeutaId = paramTerapeutaId;
    }

    /**
     * Costruttore che permette di specificare i campi
     * principali del materiale, incluso il file.
     * @param paramNome          Nome del materiale
     * @param paramTipoMateriale Tipo del materiale (PDF, IMMAGINE, VIDEO)
     * @param paramTerapeutaId   ID del terapeuta associato
     * @param paramFile          File del materiale
     */
    public InputMaterialeDTO(
            final String paramNome,
            final TipoMateriale paramTipoMateriale,
            final Long paramTerapeutaId,
            final MultipartFile paramFile
    ) {
        // Assegnazioni dei campi
        this.nome = paramNome;
        this.tipoMateriale = paramTipoMateriale;
        this.terapeutaId = paramTerapeutaId;
        this.file = paramFile;
    }
}
