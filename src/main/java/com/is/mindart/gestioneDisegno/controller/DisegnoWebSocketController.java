package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class DisegnoWebSocketController {

    private final DisegnoService disegnoService;



    // Riceviamo i messaggi inviati a /app/draw/{disegnoId}
    @MessageMapping("/draw/{disegnoId}")
    @SendTo("/topic/draw/{disegnoId}")
    public StrokeDTO handleNewStroke(@DestinationVariable("disegnoId") Long disegnoId,
                                     @Payload StrokeDTO strokeDto) {
        System.out.println("Ricevuto nuovo stroke: " + strokeDto);

        // Aggiorno il Disegno sul DB
        disegnoService.addStrokeToDisegno(disegnoId, strokeDto);

        // Ritorno lo stroke in broadcast a tutti i client connessi a /topic/draw/{disegnoId}
        return strokeDto;
    }
}

