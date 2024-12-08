package com.is.mindart.configuration;

import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneCalendario.service.EventDto;
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
                map().setTerapeuta(source.getTerapeuta().getId());
            }
        });


        return mapper;
    }
}
