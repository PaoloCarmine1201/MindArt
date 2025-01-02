package com.is.mindart.gestioneTerapeuta.controller;

import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOSimple;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaDTOStat;
import com.is.mindart.gestioneTerapeuta.service.TerapeutaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/terapeuta")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class TerapeutaController {

    private final TerapeutaService terapeutaService;

    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/get")
    public ResponseEntity<TerapeutaDTOStat> getTerapeuta() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getPrincipal().toString();
        TerapeutaDTOStat terapeuta = terapeutaService.getTerapeuta(email);
        return ResponseEntity.ok(terapeuta);
    }

    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/update")
    public ResponseEntity<TerapeutaDTOSimple> updateTerapeuta(@RequestBody TerapeutaDTOSimple terapeutaDTO) {
        TerapeutaDTOSimple terapeuta = terapeutaService.updateTerapeuta(terapeutaDTO);
        return ResponseEntity.ok(terapeuta);
    }
}
