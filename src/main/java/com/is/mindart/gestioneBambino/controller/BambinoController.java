package com.is.mindart.gestioneBambino.controller;

import com.is.mindart.gestioneBambino.service.BambinoDTO;
import com.is.mindart.gestioneBambino.service.BambinoService;
import com.is.mindart.gestioneBambino.service.RegisterBambinoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bambino")
@CrossOrigin(origins = "http://localhost:3000")
public class BambinoController {

    /**
     * Servizio per la gestione dei bambini.
     */
    @Autowired
    private BambinoService bambinoService;

    /**
     * @autor gabrieleristallo
     * Restituisce tutti i bambini presenti nel database.
     *
     * @return lista di bambini presenti nel database
     */
    @GetMapping("/getall")
    public ResponseEntity<List<BambinoDTO>> getAllBambini() {
        List<BambinoDTO> bambini = bambinoService.getAllBambini();
        return ResponseEntity.ok(bambini);
    }

    @GetMapping("/getallbyterapeuta")
    public ResponseEntity<List<BambinoDTO>> getAllBambiniByTerapeuta(@RequestParam Long terapeuta) {
        List<BambinoDTO> bambini = bambinoService.getBambiniByT(terapeuta);
        return ResponseEntity.ok(bambini);
    }

    /**
     * @author gabrieleristallo
     * Restituisce il bambino con l'identificativo specificato.
     *
     * @param id identificativo del bambino
     * @return bambino con l'identificativo specificato
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<BambinoDTO> getBambino(@PathVariable final Long id) {
        BambinoDTO bambino = bambinoService.getBambino(id);
        return ResponseEntity.ok(bambino);
    }

    @PostMapping("/add")
    public ResponseEntity<RegisterBambinoDTO> addBambino(
            @Valid @RequestBody final RegisterBambinoDTO bambinoDto) {
        bambinoService.addBambino(bambinoDto);
        return ResponseEntity.ok(bambinoDto);
    }
}
