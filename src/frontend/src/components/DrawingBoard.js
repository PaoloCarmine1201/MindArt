// src/components/DrawingBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line } from 'react-konva';
import {connectWebSocket, subscribeToDraw} from "../utils/websocket";
import axiosInstance from "../config/axiosInstance";


const DrawingBoard = ({ disegnoId = 1 }) => {
    const [strokes, setStrokes] = useState([]);
    const [color, setColor] = useState('#000000'); // Colore di default
    const stageRef = useRef(null);
    const [currentStroke, setCurrentStroke] = useState(null);
    const stompClientRef = useRef(null);

    useEffect(() => {
        // 1. Carica i dati iniziali via REST
        const loadStrokes = async () => {
            try {
                const disegno =  await axiosInstance.get(`/api/bambino/sessione/${disegnoId}`);
                setStrokes(disegno.data.strokes);
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
                // Puoi mostrare un messaggio all'utente o gestire l'errore come preferisci
            }
        };

        loadStrokes();

        // 2. Connessione WebSocket
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

    // Gestione degli eventi di disegno
    const handleMouseDown = (e) => {
        const pos = e.target.getStage().getPointerPosition();
        const newStroke = {
            color: color,
            points: [pos.x, pos.y],
        };
        setCurrentStroke(newStroke);
        setStrokes((prevStrokes) => [...prevStrokes, newStroke]);
    };

    const handleMouseMove = (e) => {
        if (!currentStroke) return;

        const stage = e.target.getStage();
        const point = stage.getPointerPosition();
        const updatedStroke = {
            ...currentStroke,
            points: [...currentStroke.points, point.x, point.y],
        };

        // Aggiorna l'ultimo stroke
        setStrokes((prevStrokes) => {
            const newStrokes = [...prevStrokes];
            newStrokes[newStrokes.length - 1] = updatedStroke;
            return newStrokes;
        });

        setCurrentStroke(updatedStroke);
    };

    const handleMouseUp = () => {
        if (currentStroke) {
            // Invio dello stroke via WebSocket
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.publish({
                    destination: `/app/draw/${disegnoId}`,
                    body: JSON.stringify({
                        color: currentStroke.color,
                        points: [
                            // Trasforma i punti in array di coppie [x, y]
                            ...currentStroke.points.reduce((acc, value, index) => {
                                if (index % 2 === 0) acc.push([value, currentStroke.points[index + 1]]);
                                return acc;
                            }, []),
                        ],
                    }),
                });
            }

            setCurrentStroke(null);
        }
    };

    // Gestione del cambio colore
    const handleColorChange = (e) => {
        setColor(e.target.value);
    };

    // Gestione della dimensione del canvas
    const [dimensions, setDimensions] = useState({
        width: window.innerWidth,
        height: window.innerHeight - 50,
    });

    useEffect(() => {
        const handleResize = () => {
            setDimensions({
                width: window.innerWidth,
                height: window.innerHeight - 50,
            });
        };

        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    return (
        <div>
            <div style={{ marginBottom: '10px' }}>
                <label>Seleziona Colore: </label>
                <input type="color" value={color} onChange={handleColorChange} />
            </div>
            <Stage
                width={dimensions.width}
                height={dimensions.height}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onTouchStart={handleMouseDown}
                onTouchMove={handleMouseMove}
                onTouchEnd={handleMouseUp}
                ref={stageRef}
                style={{ border: '1px solid grey' }}
            >
                <Layer>
                    {strokes.map((stroke, index) => (
                        <Line
                            key={index}
                            points={Array.isArray(stroke.points[0]) ? stroke.points.flat() : stroke.points} // Appiattimento
                            stroke={stroke.color}
                            strokeWidth={3} // Spessore costante
                            lineCap="round"
                            lineJoin="round"
                            tension={0.5}
                            globalCompositeOperation={'source-over'} /* per gestione eventuale della gomma */
                        />
                    ))}
                </Layer>
            </Stage>
        </div>
    );
};

export default DrawingBoard;
