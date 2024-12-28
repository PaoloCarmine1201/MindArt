// src/components/DrawingBoard.jsx

import React, { useState, useRef, useEffect } from "react";
import { Stage, Layer, Line, Circle, Rect } from "react-konva";
import { FaEraser } from "react-icons/fa"; // Import dell'icona della gomma
import "../style/Lavagna.css"; // Import del file CSS
import { connectWebSocket, subscribeToDraw } from "../utils/websocket";
import axiosInstance from "../config/axiosInstance";

const DrawingBoard1 = ({ disegnoId = 1 }) => {
    // Local drawing state
    const [lines, setLines] = useState([]); // Linee disegnate localmente
    const isDrawing = useRef(false); // Traccia se l'utente sta disegnando
    const [selectedColor, setSelectedColor] = useState("black"); // Colore selezionato
    const [isEraserActive, setIsEraserActive] = useState(false); // Modalità gomma attiva o meno
    const [cursorPosition, setCursorPosition] = useState({ x: -100, y: -100 }); // Posizione del mouse

    // Collaborative drawing state
    const [strokes, setStrokes] = useState([]); // Stroke ricevuti via WebSocket
    const [color, setColor] = useState('#000000'); // Colore di default (può sincronizzarsi con selectedColor)
    const stompClientRef = useRef(null);

    const LINE_STROKE = 2;
    const ERASER_STROKE = 20;

    const DRAWING_AREA_WIDTH = window.innerWidth - 150; // Larghezza dell'area di disegno
    const DRAWING_AREA_HEIGHT = window.innerHeight - 50; // Altezza dell'area di disegno
    const DRAWING_AREA_OFFSET_X = 120; // Offset orizzontale (largo quanto il selettore)
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale (per lasciare un margine in alto)

    // Colori disponibili
    const colors = ["red", "green", "yellow", "black", "blue"];

    // Carica i dati iniziali via REST e configura WebSocket
    useEffect(() => {
        // 1. Carica i dati iniziali via REST
        const loadStrokes = async () => {
            try {
                const disegno = await axiosInstance.get(`/api/bambino/sessione/${disegnoId}`);
                setStrokes(disegno.data.strokes || []);
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
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
        };

        stompClientRef.current = client;

        // Cleanup alla disconnessione
        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [disegnoId]);

    // Gestione degli eventi di disegno locali
    const handleMouseDown = (e) => {
        const stage = e.target.getStage();
        const pointerPosition = stage.getPointerPosition();

        // Controlla se il clic è dentro l'area di disegno
        if (
            pointerPosition.x < DRAWING_AREA_OFFSET_X ||
            pointerPosition.x > DRAWING_AREA_OFFSET_X + DRAWING_AREA_WIDTH ||
            pointerPosition.y < DRAWING_AREA_OFFSET_Y ||
            pointerPosition.y > DRAWING_AREA_OFFSET_Y + DRAWING_AREA_HEIGHT
        ) {
            return;
        }

        isDrawing.current = true;
        const pos = pointerPosition;
        const newLine = {
            points: [pos.x, pos.y],
            color: isEraserActive ? "white" : selectedColor,
            strokeWidth: isEraserActive ? ERASER_STROKE : LINE_STROKE,
        };
        setLines([...lines, newLine]);
    };

    const handleMouseMove = (e) => {
        const stage = e.target.getStage();
        const pointerPosition = stage.getPointerPosition();

        // Aggiorna la posizione del puntatore
        setCursorPosition(pointerPosition);

        if (!isDrawing.current) return;

        // Controlla se il movimento è dentro l'area di disegno
        if (
            pointerPosition.x < DRAWING_AREA_OFFSET_X ||
            pointerPosition.x > DRAWING_AREA_OFFSET_X + DRAWING_AREA_WIDTH ||
            pointerPosition.y < DRAWING_AREA_OFFSET_Y ||
            pointerPosition.y > DRAWING_AREA_OFFSET_Y + DRAWING_AREA_HEIGHT
        ) {
            return;
        }

        // Aggiungi nuovi punti all'ultima linea
        const lastLine = lines[lines.length - 1];
        const newLines = lines.slice(0, lines.length - 1);
        newLines.push({
            ...lastLine,
            points: [...lastLine.points, pointerPosition.x, pointerPosition.y],
        });

        setLines(newLines);

        // Invia l'ultimo punto via WebSocket
        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.publish({
                destination: `/app/draw/${disegnoId}`,
                body: JSON.stringify({
                    color: lastLine.color,
                    points: [pointerPosition.x, pointerPosition.y],
                }),
            });
        }
    };

    const handleMouseUp = () => {
        isDrawing.current = false;
    };

    // Gestione degli eventi di disegno collaborativo
    useEffect(() => {
        if (strokes.length === 0) return;

        // Potresti voler gestire la visualizzazione degli strokes collaborativi qui
        // Ad esempio, combinare `lines` e `strokes` per renderizzare tutte le linee
    }, [strokes]);

    // Gestione del cambio colore
    const handleColorChange = (color) => {
        setSelectedColor(color);
        setColor(color); // Sincronizza il colore collaborativo
        setIsEraserActive(false); // Disattiva la gomma quando selezioni un colore
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
        <div style={{ position: "relative", width: "100%", height: "100vh" }}>
            {/* Canvas di disegno */}
            <Stage
                width={dimensions.width}
                height={dimensions.height}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onTouchStart={handleMouseDown}
                onTouchMove={handleMouseMove}
                onTouchEnd={handleMouseUp}
                style={{
                    backgroundColor: "#f0f0f0",
                    cursor: isEraserActive ? "none" : "default",
                }}
            >
                {/* Layer per l'area di disegno */}
                <Layer>
                    <Rect
                        x={DRAWING_AREA_OFFSET_X}
                        y={DRAWING_AREA_OFFSET_Y}
                        width={DRAWING_AREA_WIDTH}
                        height={DRAWING_AREA_HEIGHT}
                        fill="#ffffff"
                        stroke="black"
                        strokeWidth={2}
                        cornerRadius={15}
                        shadowColor="black"
                        shadowBlur={10}
                        shadowOffset={{ x: 5, y: 5 }}
                        shadowOpacity={0.2}
                    />
                </Layer>

                {/* Layer per i tratti locali */}
                <Layer>
                    {lines.map((line, index) => (
                        <Line
                            key={index}
                            points={line.points}
                            stroke={line.color}
                            strokeWidth={line.strokeWidth}
                            tension={0.5}
                            lineCap="round"
                            lineJoin="round"
                        />
                    ))}
                </Layer>

                {/* Layer per i tratti collaborativi */}
                <Layer>
                    {strokes.map((stroke, index) => (
                        <Line
                            key={`stroke-${index}`}
                            points={stroke.points}
                            stroke={stroke.color}
                            strokeWidth={stroke.strokeWidth || LINE_STROKE}
                            tension={0.5}
                            lineCap="round"
                            lineJoin="round"
                        />
                    ))}
                </Layer>

                {/* Layer per il cerchio che segue il mouse in modalità gomma */}
                {isEraserActive && (
                    <Layer>
                        <Circle
                            x={cursorPosition.x}
                            y={cursorPosition.y}
                            radius={ERASER_STROKE / 2}
                            fill="rgba(0, 0, 0, 0.2)"
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
                        onClick={() => handleColorChange(colorOption)}
                    />
                ))}
            </div>

            {/* Pulsante gomma */}
            <button
                onClick={() => setIsEraserActive(!isEraserActive)}
                className={`eraser-button ${isEraserActive ? "active" : ""}`}
            >
                <FaEraser size={20} /> {/* Icona della gomma */}
            </button>
        </div>
    );
};

export default DrawingBoard1;
