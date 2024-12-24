package com.is.mindart.configuration;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.service.MaterialeDTO;
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
     *     <li>{@link Materiale} -> {@link MaterialeDTO}</li>
     * </ul>
     */
    public void configureMappings() {
        modelMapper.createTypeMap(Materiale.class, MaterialeDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Materiale::getId, MaterialeDTO::setId);
                    mapper.map(Materiale::getNome, MaterialeDTO::setNome);
                    mapper.map(Materiale::getTipo, MaterialeDTO::setTipoMateriale);
                });
    }

    /**
     * Richiama il mapper {@link Materiale} -> {@link MaterialeDTO}.
     * @param materiale {@link Materiale}
     * @return {@link MaterialeDTO}
     */
    public MaterialeDTO toDTO(final Materiale materiale) {
        return modelMapper.map(materiale, MaterialeDTO.class);
    }
}
