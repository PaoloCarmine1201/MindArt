package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMaterialeDTO {
    /**
     * Id del materiale.
     * */
    @NotNull
    private Long id;

    /**
     * Nome del materiale.
     */
    @NotNull
    private String nome;

    /**
     * Tipo di file del materiale.
     */
    @NotNull
    private TipoMateriale tipoMateriale;
}
