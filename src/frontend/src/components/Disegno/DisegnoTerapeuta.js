// src/components/DrawingBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line, Rect } from 'react-konva';

import "../../style/Lavagna.css";
import axiosInstance from "../../config/axiosInstance";
import {connectWebSocket, subscribeToDraw} from "../../utils/websocket"; // Import del file CSS

const DrawingBoard = () => {
    const [strokes, setStrokes] = useState([]);
    const [disegnoId, setDisegnoId] = useState();
    const stompClientRef = useRef(null);

    // State for Stage dimensions
    const [dimensions, setDimensions] = useState({
        width: window.innerWidth,
        height: window.innerHeight,
    });

    // Drawing area offsets and dimensions based on state
    const DRAWING_AREA_OFFSET_X = 120; // Offset orizzontale (largo quanto il selettore)
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale (per lasciare un margine in alto)
    const DRAWING_AREA_WIDTH = dimensions.width - 150; // Larghezza dell'area di disegno
    const DRAWING_AREA_HEIGHT = dimensions.height - DRAWING_AREA_OFFSET_Y - 50; // Altezza dell'area di disegno

    useEffect(() => {
        // 1. Carica i dati iniziali via REST
        const loadStrokes = async () => {
            try {
                const disegno = await axiosInstance.get(`/api/terapeuta/sessione/disegno/`);
                setStrokes(disegno.data.strokes);
                setDisegnoId(disegno.data.id);
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
                // Puoi mostrare un messaggio all'utente o gestire l'errore come preferisci
            }
        };

        loadStrokes();
        console.log(disegnoId);
        // 2. Connessione WebSocket per aggiornamenti in tempo reale
        const client = connectWebSocket();

        client.onConnect = () => {
            console.log('Connesso al WebSocket');
            subscribeToDraw(client, disegnoId, (newStroke) => {
                setStrokes((prevStrokes) => [...prevStrokes, newStroke]);
            });
        };

        client.onStompError = (frame) => {
            console.error('Errore STOMP:', frame.headers['message']);
            // Puoi gestire errori STOMP qui, ad esempio mostrando un messaggio all'utente
        };

        stompClientRef.current = client;

        // Cleanup alla disconnessione
        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [disegnoId]);

    // Handle window resize to update Stage dimensions
    useEffect(() => {
        const handleResize = () => {
            setDimensions({
                width: window.innerWidth,
                height: window.innerHeight,
            });
        };

        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    return (
        <Stage
            width={dimensions.width}
            height={dimensions.height}
            ref={null} // Non serve più il riferimento
            style={{
                backgroundColor: "#f0f0f0",
                cursor: "default", // Imposta il cursore di default
            }}
        >
            {/* Layer per l'area di disegno */}
            <Layer>
                {/* Rettangolo visibile per l'area di disegno */}
                <Rect
                    x={DRAWING_AREA_OFFSET_X}
                    y={DRAWING_AREA_OFFSET_Y}
                    width={DRAWING_AREA_WIDTH}
                    height={DRAWING_AREA_HEIGHT}
                    fill="#ffffff" // Colore di sfondo dell'area di disegno
                    stroke="black" // Colore del bordo
                    strokeWidth={2} // Spessore del bordo
                    cornerRadius={15} // Arrotonda i bordi del rettangolo
                    shadowColor="black" // Colore dell'ombra
                    shadowBlur={10} // Intensità della sfumatura dell'ombra
                    shadowOffset={{x: 5, y: 5}} // Spostamento dell'ombra
                    shadowOpacity={0.2} // Opacità dell'ombra
                />
            </Layer>

            {/* Layer per i tratti */}
            <Layer>
                {strokes.map((stroke, index) => (
                    <Line
                        key={index}
                        points={stroke.points.flat()} // Appiattimento
                        stroke={stroke.color}
                        strokeWidth={stroke.strokeWidth}
                        lineCap="round"
                        lineJoin="round"
                        tension={0.5}
                        globalCompositeOperation={'source-over'} /* per gestione eventuale della gomma */
                    />
                ))}
            </Layer>
        </Stage>
    );
};

export default DrawingBoard;
