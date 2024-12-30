package com.is.mindart.gestioneDisegno.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessioneMessage {
    private Long sessionId;
    private Long bambinoId;
}