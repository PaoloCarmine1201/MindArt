package com.is.mindart.configuration;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOStat;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerapeutaMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public TerapeutaMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        configureMappings();
    }

    private void configureMappings() {
        // Mapping da Terapeuta -> TerapeutaDTOStat
        modelMapper.createTypeMap(Terapeuta.class, TerapeutaDTOStat.class)
                .addMappings(mapper -> {
                    mapper.map(Terapeuta::getId, TerapeutaDTOStat::setId);
                    mapper.map(Terapeuta::getNome, TerapeutaDTOStat::setNome);
                    mapper.map(Terapeuta::getCognome, TerapeutaDTOStat::setCognome);
                    mapper.map(Terapeuta::getEmail, TerapeutaDTOStat::setEmail);
                    mapper.map(Terapeuta::getDataDiNascita, TerapeutaDTOStat::setDataDiNascita);
                });
    }
}
