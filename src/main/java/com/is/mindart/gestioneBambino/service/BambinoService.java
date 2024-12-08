package com.is.mindart.gestioneBambino.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BambinoService {

    /**
     *  Provvede ad accedere al database per l'entità Bambino.
     */
    @Autowired
    private BambinoRepository repository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @autor gabrieleristallo
     * Restituisce tutti i bambini presenti nel database.
     *
     * @return lista di bambini presenti nel database
     */
    public List<BambinoDTO> getAllBambini() {
        List<Bambino> bambini = repository.findAll();
        return bambini.stream()
                .map(bambino -> modelMapper.map(bambino, BambinoDTO.class))
                .collect(Collectors.toList());
    }

//    public BambinoDTO addBambino(BambinoDTO bambino) {
//        Bambino bambinoEntity = modelMapper.map(bambino, Bambino.class);
//        Bambino savedBambino = repository.save(bambinoEntity);
//        return modelMapper.map(savedBambino, BambinoDTO.class);
//    }

    /**
     * @author gabrieleristallo
     * Restituisce il bambino con l'identificativo specificato.
     *
     * @param id identificativo del bambino
     * @return bambino con l'identificativo specificato
     */
    public BambinoDTO getBambino(Long id) {
        Bambino bambino = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bambino non trovato"));
        return modelMapper.map(bambino, BambinoDTO.class);
    }

    public List<BambinoDTO> getBambiniByT(Long terapeuta) {
        List<Bambino> bambini = repository.findAll();

        return bambini.stream()
                .filter(bambino -> bambino.getTerapeuta().getId().equals(terapeuta))
                .map(bambino -> modelMapper.map(bambino, BambinoDTO.class))
                .collect(Collectors.toList());
    }

    public void addBambino(final RegisterBambinoDTO bambinoDto) {
//        Bambino bambino = new Bambino();
//        bambino.setCodiceFiscale(bambinoDto.getCodiceFiscale());
//        bambino.setCodice(bambinoDto.getCodice());
//        bambino.setCognome(bambinoDto.getCognome());
//        bambino.setDataDiNascita(bambinoDto.getDataDiNascita());
//        bambino.setEmailGenitore(bambinoDto.getEmailGenitore());
//        bambino.setNome(bambinoDto.getNome());
//        bambino.setSesso(bambinoDto.getSesso());
//        bambino.setTelefonoGenitore(bambinoDto.getTelefonoGenitore());

        Bambino bambino = modelMapper.map(bambinoDto, Bambino.class);
        bambino.setTerapeuta(terapeutaRepository.findById(bambinoDto.getTerapeutaId()).orElseThrow(() -> new IllegalArgumentException("Terapeuta non trovato")));
        repository.save(bambino);
    }
}
