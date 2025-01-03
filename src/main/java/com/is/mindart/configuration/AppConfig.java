package com.is.mindart.configuration;

import org.modelmapper.ModelMapper;
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

        return new ModelMapper();
    }
}
