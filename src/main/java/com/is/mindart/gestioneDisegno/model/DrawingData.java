package com.is.mindart.gestioneDisegno.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DrawingData {
    @ElementCollection
    private List<PointData> strokes;
}
