package com.is.mindart.gestioneDisegno.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrokeDTO {
    /**
     * Colore dello stroke.
     */
    private String color;
    /**
     * Lista di punti che compongono lo stroke.
     */
    private List<List<Integer>> points;
    /**
     * Larghezza dello stroke.
     */
    private Integer strokeWidth;
    /**
     * Tipo di stroke.
     */
    private String type;
}

