
package com.is.mindart.gestioneDisegno.service;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisegnoMessage {
    @NotNull(message = "TerapeutaId cannot be null")
    private Long bambinoId;
    @NotNull(message = "SessionId cannot be null")
    private Long sessionId;

    @NotNull(message = "Points cannot be null")
    @NotEmpty(message = "Points cannot be empty")
    private List<Integer> points; // Array di coordinate [x1, y1, x2, y2, ...]

    @NotNull(message = "Color cannot be null")
    private String color;
}
