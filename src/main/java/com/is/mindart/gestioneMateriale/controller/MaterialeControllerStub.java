package com.is.mindart.gestioneMateriale.controller;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import com.is.mindart.gestioneMateriale.service.GetMaterialeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@RestController
@RequestMapping("/api/terapeuta/materiale")
public class MaterialeControllerStub {
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<GetMaterialeDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            String fileName = file.getOriginalFilename();
            System.out.println("Caricamento file: " + fileName); // DEBUG

            GetMaterialeDTO dto = new GetMaterialeDTO(new Random().nextLong(), fileName, TipoMateriale.PDF);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
