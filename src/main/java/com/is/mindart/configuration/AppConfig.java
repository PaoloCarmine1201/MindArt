package com.is.mindart.configuration;

import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneCalendario.service.EventDto;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaEventoDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
    /**
     *      Ritorna un model mapper di default.
     *      @return ModelMapper
     * */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Mappatura Evento -> EventDto
        mapper.addMappings(new PropertyMap<Evento, EventDto>() {
            @Override
            protected void configure() {
                map().setTerapeuta(source.getTerapeuta().getId());}
        });

        mapper.addMappings(new PropertyMap<Terapeuta, TerapeutaEventoDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setNome(source.getNome());
                map().setCognome(source.getCognome());
                map().setEmail(source.getEmail());
                map().setDataDiNascita(source.getDataDiNascita());
            }
        });

        return mapper;
    }
}
