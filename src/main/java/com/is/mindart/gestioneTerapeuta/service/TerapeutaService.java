package com.is.mindart.gestioneTerapeuta.service;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TerapeutaService {

    /**
     *  Provvede ad accedere al database per l'entità Terapeuta.
     */
    @Autowired
    private TerapeutaRepository repository;

    /**
     *  Provvede a mappare l'entità Terapeuta con TerapeutaDTO.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     *  Provvede a criptare la password.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Provvede alla registrazione del terapeuta.
     * @param terapeutaDto TerapeutaDto
     */
    public void registerTerapeuta(final TerapeutaDTO terapeutaDto) {

        // Hash the password
        String hashedPassword = passwordEncoder.encode(terapeutaDto
                .getPassword());
        terapeutaDto.setPassword(hashedPassword);

        Terapeuta terapeuta = modelMapper.map(terapeutaDto, Terapeuta.class);
        repository.save(terapeuta);
    }


}
