package com.is.mindart.gestioneDisegno.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PointData {
    private List<Integer> points;
    private String color;        // Colore in formato esadecimale, es. "#FF0000"
}
