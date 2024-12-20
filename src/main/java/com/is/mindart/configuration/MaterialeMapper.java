package com.is.mindart.configuration;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.service.GetMaterialeDTO;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaterialeMapper {
    /**
     * Istanza iniettata del modelMapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Costruttore.
     * @param modelMapper -
     */
    @Autowired
    public MaterialeMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Post construct initialization.
     */
    @PostConstruct
    public void init() {
        configureMappings();
    }

    /**
     * Imposta i mapping custom.
     * Mapping presenti:
     * <ul>
     *     <li>{@link Materiale} -> {@link GetMaterialeDTO}</li>
     * </ul>
     */
    public void configureMappings() {
        modelMapper.createTypeMap(Materiale.class, GetMaterialeDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Materiale::getId, GetMaterialeDTO::setId);
                    mapper.map(Materiale::getNome, GetMaterialeDTO::setNome);
                    mapper.map(Materiale::getTipo, GetMaterialeDTO::setTipoMateriale);
                });
    }

    /**
     * Richiama il mapper {@link Materiale} -> {@link GetMaterialeDTO}.
     * @param materiale {@link Materiale}
     * @return {@link GetMaterialeDTO}
     */
    public GetMaterialeDTO toDTO(final Materiale materiale) {
        return modelMapper.map(materiale, GetMaterialeDTO.class);
    }
}
