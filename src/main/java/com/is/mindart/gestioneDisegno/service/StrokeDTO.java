package com.is.mindart.gestioneDisegno.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrokeDTO {
    private String color;
    private List<List<Integer>> points;
    private int strokeWidth;
    private String type;
}

