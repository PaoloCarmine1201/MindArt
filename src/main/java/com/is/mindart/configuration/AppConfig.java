package com.is.mindart.configuration;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.service.RegisterBambinoDTO;
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
                map().setTerapeuta(source.getTerapeuta().getId());
                // La lista dei partecipanti verrà mappata automaticamente
            }
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

//        mapper.addMappings(new PropertyMap<RegisterBambinoDTO, Bambino>() {
//            @Override
//            protected void configure() {
//                map().setNome(source.getNome());
//                map().setCognome(source.getCognome());
//                map().setSesso(source.getSesso());
//                map().setDataDiNascita(source.getDataDiNascita());
//                map().setCodiceFiscale(source.getCodiceFiscale());
//                map().setEmailGenitore(source.getEmailGenitore());
//                map().setTelefonoGenitore(source.getTelefonoGenitore());
//            }
//        });

        return mapper;
    }
}
