package com.is.mindart.configuration;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessioneMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private MaterialeRepository materialeRepository;

    @Autowired
    private BambinoRepository bambinoRepository;

    private void configureMappings() {
        modelMapper.addMappings(new PropertyMap<SessioneDTO, Sessione>() {
            public void configure(){
                /*
                     Da mappare:
                     - TipoSessione
                     - TemaAssegnato
                     - Lista di bambini (id -> ref)
                     - materiale (id -> ref)
                     - data (da generare)
                     - id terapeuta (id -> ref)
                 */
                map().setTipo(source.getTipoSessione());
                map().setTemaAssegnato(source.getTemaAssegnato());
                map().setMateriale(materialeRepository.getReferenceById(source.getMateriale()));
                map().setData(new Date());
                map().setTerapeuta(terapeutaRepository.getReferenceById(source.getIdTerapeuta()));
            }
        });

        Converter<List<Long>, List<Bambino>> bambiniConverter = ctx ->
                ctx.getSource() == null ? null :
                ctx.getSource().stream()
                    .map(id -> bambinoRepository.getReferenceById(id)) // Supponendo un costruttore Bambino(Long id)
                    .collect(Collectors.toList());

        modelMapper.createTypeMap(SessioneDTO.class, Sessione.class)
                .addMappings(mapper -> mapper.using(bambiniConverter).map(SessioneDTO::getBambini, Sessione::setBambini));
    }

    public Sessione toEntity(SessioneDTO sessioneDTO) {
        return modelMapper.map(sessioneDTO, Sessione.class);
    }
}
