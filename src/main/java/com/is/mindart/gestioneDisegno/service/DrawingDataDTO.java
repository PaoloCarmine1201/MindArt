package com.is.mindart.gestioneDisegno.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingDataDTO {
    private List<PointDataDTO> strokes = new ArrayList<>();
}