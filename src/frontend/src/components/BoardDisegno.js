// src/components/DrawingBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line, Rect, Circle } from 'react-konva';
import { connectWebSocket, subscribeToDraw } from "../utils/websocket";
import axiosInstance from "../config/axiosInstance";
import { FaEraser } from "react-icons/fa"; // Import dell'icona della gomma
import "../style/Lavagna.css"; // Import del file CSS

const DrawingBoard = ({ disegnoId = 1 }) => {
    const [strokes, setStrokes] = useState([]);
    const [color, setColor] = useState('#000000'); // Colore di default
    const stageRef = useRef(null);
    const [currentStroke, setCurrentStroke] = useState(null);
    const stompClientRef = useRef(null);
    const [isInsideDrawingArea, setIsInsideDrawingArea] = useState(false); // Se il cursore è dentro l'area di disegno

    // State for Stage dimensions
    const [dimensions, setDimensions] = useState({
        width: window.innerWidth,
        height: window.innerHeight,
    });

    const [selectedColor, setSelectedColor] = useState("black"); // Colore selezionato
    const [isEraserActive, setIsEraserActive] = useState(false); // Modalità gomma attiva o meno
    const [cursorPosition, setCursorPosition] = useState({ x: -100, y: -100 }); // Posizione del mouse

    const LINE_STROKE = 2;
    const ERASER_STROKE = 20;

    // Drawing area offsets and dimensions based on state
    const DRAWING_AREA_OFFSET_X = 120; // Offset orizzontale (largo quanto il selettore)
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale (per lasciare un margine in alto)
    const DRAWING_AREA_WIDTH = dimensions.width - 150; // Larghezza dell'area di disegno
    const DRAWING_AREA_HEIGHT = dimensions.height - DRAWING_AREA_OFFSET_Y - 50; // Altezza dell'area di disegno

    useEffect(() => {
        // 1. Carica i dati iniziali via REST
        const loadStrokes = async () => {
            try {
                const disegno = await axiosInstance.get(`/api/bambino/sessione/${disegnoId}`);
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

    // Gestione degli eventi di disegno
    const handleMouseDown = (e) => {
        const pos = e.target.getStage().getPointerPosition();

        // Controlla se il clic è dentro l'area di disegno
        if (
            pos.x < DRAWING_AREA_OFFSET_X ||
            pos.x > DRAWING_AREA_OFFSET_X + DRAWING_AREA_WIDTH ||
            pos.y < DRAWING_AREA_OFFSET_Y ||
            pos.y > DRAWING_AREA_OFFSET_Y + DRAWING_AREA_HEIGHT
        ) {
            return;
        }

        const newStroke = {
            color: isEraserActive ? "white" : selectedColor,
            points: [pos.x, pos.y],
            strokeWidth: isEraserActive ? ERASER_STROKE : LINE_STROKE,
        };
        setCurrentStroke(newStroke);
        setStrokes((prevStrokes) => [...prevStrokes, newStroke]);
    };

    const handleMouseMove = (e) => {
        const stage = e.target.getStage();
        const point = stage.getPointerPosition();

        if (!point) return;

        // Aggiorna la posizione del puntatore
        setCursorPosition(point);

        // Determina se il puntatore è dentro l'area di disegno
        const inside =
            point.x >= DRAWING_AREA_OFFSET_X &&
            point.x <= DRAWING_AREA_OFFSET_X + DRAWING_AREA_WIDTH &&
            point.y >= DRAWING_AREA_OFFSET_Y &&
            point.y <= DRAWING_AREA_OFFSET_Y + DRAWING_AREA_HEIGHT;

        setIsInsideDrawingArea(inside);

        // Solo se un stroke è attivo e il cursore è dentro, disegna
        if (!currentStroke || !inside) return;

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
                        strokeWidth: currentStroke.strokeWidth,
                    }),
                });
            }

            setCurrentStroke(null);
        }
    };

    // Gestione del cambio colore
    const handleColorChange = (e) => {
        setColor(e.target.value);
        setIsEraserActive(false);
    };


    const colors = ["red", "green", "yellow", "black", "blue"];

    return (
        <div>
            <div style={{marginBottom: '10px'}}>
                <label>Seleziona Colore: </label>
                <input type="color" value={color} onChange={handleColorChange}/>
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
                style={{
                    backgroundColor: "#f0f0f0",
                    cursor: isEraserActive && isInsideDrawingArea ? "none" : "default",
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
                {/* Layer per il cerchio che segue il mouse */}
                {isEraserActive && isInsideDrawingArea && cursorPosition && (
                    <Layer>
                        <Circle
                            x={cursorPosition.x}
                            y={cursorPosition.y}
                            radius={ERASER_STROKE / 2} // La dimensione del cerchio è uguale alla dimensione della gomma
                            fill="rgba(0, 0, 0, 0.2)" // Cerchio semitrasparente
                        />
                    </Layer>
                )}
            </Stage>
            {/* Selettore colori */}
            <div className="color-selector">
                {colors.map((colorOption, index) => (
                    <div
                        key={index}
                        className={`color-circle ${
                            selectedColor === colorOption && !isEraserActive ? "selected" : ""
                        }`}
                        style={{ backgroundColor: colorOption }}
                        onClick={() => {
                            setSelectedColor(colorOption);
                            setIsEraserActive(false); // Disattiva la gomma quando selezioni un colore
                        }}
                    />
                ))}
            </div>
            {/* Pulsante gomma */}
            <button
                onClick={() => setIsEraserActive(!isEraserActive)}
                className={`eraser-button ${isEraserActive ? "active" : ""}`}
            >
                <FaEraser size={20}/> {/* Eraser icon */}
            </button>
        </div>
    );

};

export default DrawingBoard;
