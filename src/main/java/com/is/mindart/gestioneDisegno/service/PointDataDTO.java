package com.is.mindart.gestioneDisegno.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDataDTO {
    private List<Integer> points; // Array di coordinate [x1, y1, x2, y2, ...]
    private String color;        // Colore in formato esadecimale, es. "#FF0000"
}