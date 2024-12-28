import React, { useState, useRef } from "react";
import { Stage, Layer, Line, Circle, Rect } from "react-konva";
import { FaEraser } from "react-icons/fa"; // Import dell'icona della gomma
import "../style/Lavagna.css"; // Import del file CSS

const DrawingBoard = () => {
    const [lines, setLines] = useState([]); // Array di linee disegnate
    const isDrawing = useRef(false); // Per tracciare se l'utente sta disegnando
    const [selectedColor, setSelectedColor] = useState("black"); // Colore selezionato
    const [isEraserActive, setIsEraserActive] = useState(false); // Modalità gomma attiva o meno
    const [cursorPosition, setCursorPosition] = useState({ x: -100, y: -100 }); // Posizione del mouse

    const LINE_STROKE = 2;
    const ERASER_STROKE = 20;

    const DRAWING_AREA_WIDTH = window.innerWidth - 150; // Larghezza dell'area di disegno
    const DRAWING_AREA_HEIGHT = window.innerHeight - 50; // Altezza dell'area di disegno
    const DRAWING_AREA_OFFSET_X = 120; // Offset orizzontale (largo quanto il selettore)
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale (per lasciare un margine in alto)

    // Quando l'utente preme il mouse
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

        isDrawing.current = true; // TODO: vedere se serve
        const pos = pointerPosition;
        setLines([
            ...lines,
            {
                points: [pos.x, pos.y],
                color: isEraserActive ? "white" : selectedColor, // Usa il bianco come colore se la gomma è attiva
                strokeWidth: isEraserActive ? ERASER_STROKE : LINE_STROKE, // Spessore maggiore per la gomma
            },
        ]);
    };

    // Quando l'utente muove il mouse
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
    };

    // Quando l'utente rilascia il mouse
    const handleMouseUp = () => {
        isDrawing.current = false;//TODO: vedere se serve
    };

    // Colori disponibili
    const colors = ["red", "green", "yellow", "black", "blue"];



    return (
        <div style={{ position: "relative", width: "100%", height: "100vh"}}>
            {/* Canvas di disegno */}
            <Stage
                width={window.innerWidth}
                height={window.innerHeight}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                style={{
                    backgroundColor: "#f0f0f0",
                    cursor: isEraserActive ? "none" : "default", // Nascondi il cursore solo in modalità gomma
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
                        shadowOffset={{ x: 5, y: 5 }} // Spostamento dell'ombra
                        shadowOpacity={0.2} // Opacità dell'ombra
                    />
                </Layer>

                {/* Layer per i tratti */}
                <Layer>
                    {lines.map((line, index) => (
                        <Line
                            key={index}
                            points={line.points}
                            stroke={line.color} // Usa il colore definito nella linea
                            strokeWidth={line.strokeWidth} // Usa lo spessore definito nella linea
                            tension={0.5}
                            lineCap="round"
                            lineJoin="round"
                        />
                    ))}
                </Layer>

                {/* Layer per il cerchio che segue il mouse */}
                {isEraserActive && (
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
                {colors.map((color, index) => (
                    <div
                        key={index}
                        className={`color-circle ${
                            selectedColor === color && !isEraserActive ? "selected" : ""
                        }`}
                        style={{ backgroundColor: color }}
                        onClick={() => {
                            setSelectedColor(color);
                            setIsEraserActive(false); // Disattiva la gomma quando selezioni un colore
                        }}
                    />
                ))}
            </div>

            {/* Pulsante gomma */}
            <button
                onClick={() => setIsEraserActive(true)}
                className={`eraser-button ${isEraserActive ? "active" : ""}`}
            >
                <FaEraser size={20} /> {/* Icona della gomma */}
            </button>
        </div>
    );
};

export default DrawingBoard;