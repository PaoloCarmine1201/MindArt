// src/components/DrawingBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line, Rect, Circle } from 'react-konva';
import { connectWebSocket, subscribeToDraw} from "../../utils/websocket";
import axiosInstance from "../../config/axiosInstance";
import { FaEraser, FaSlash } from "react-icons/fa"; // Import delle icone
import "../../style/Lavagna.css"; // Import del file CSS

const DrawingBoard = () => {
    const [strokes, setStrokes] = useState([]);
    const [filledAreas, setFilledAreas] = useState([]); // Array per le aree riempite con il lasso
    const [disegnoId, setDisegnoId] = useState(); // ID del disegno
    const stageRef = useRef(null);
    const [currentStroke, setCurrentStroke] = useState(null);
    const [currentLasso, setCurrentLasso] = useState(null); // Stato per il lasso corrente
    const stompClientRef = useRef(null);
    const [isInsideDrawingArea, setIsInsideDrawingArea] = useState(false); // Se il cursore è dentro l'area di disegno

    // State for Stage dimensions
    const [dimensions, setDimensions] = useState({
        width: window.innerWidth,
        height: window.innerHeight,
    });

    const [selectedColor, setSelectedColor] = useState("black"); // Colore selezionato
    const [selectedTool, setSelectedTool] = useState("draw"); // Strumento selezionato: 'draw', 'eraser', 'lasso'
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
                const disegno = await axiosInstance.get(`/api/bambino/sessione/disegno/`);
                setDisegnoId(disegno.data.id);
                setStrokes(disegno.data.strokes);
                setFilledAreas(disegno.data.filledAreas || []); // Assicurati che l'API ritorni anche filledAreas
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
                window.location.href = "/childLogin";
                localStorage.removeItem("jwtToken");
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

    // Funzione per ottenere il contesto del canvas
    const getCanvasContext = () => {
        const stage = stageRef.current.getStage();
        const layer = stage.findOne('.drawing-layer'); // Usa il nome corretto
        if (!layer) {
            console.error('Layer "drawing-layer" non trovato');
            return null;
        }
        const canvas = layer.getCanvas();
        const context = canvas.getContext();
        return context;
    };

    // Funzione per convertire hex in RGBA
    const hexToRGBA = (hex) => {
        // Rimuove il simbolo #
        hex = hex.replace('#', '');
        const bigint = parseInt(hex, 16);
        const r = (bigint >> 16) & 255;
        const g = (bigint >> 8) & 255;
        const b = bigint & 255;
        return [r, g, b, 255];
    };

    // Funzione per confrontare i colori
    const colorsMatch = (data, index, targetColor) => {
        return (
            data[index] === targetColor[0] &&
            data[index + 1] === targetColor[1] &&
            data[index + 2] === targetColor[2] &&
            data[index + 3] === targetColor[3]
        );
    };

    // Implementazione dell'algoritmo Flood Fill
    const floodFill = (startX, startY, fillColor) => {
        const context = getCanvasContext();
        if (!context) return; // Esci se il contesto non è disponibile

        const imageData = context.getImageData(
            DRAWING_AREA_OFFSET_X,
            DRAWING_AREA_OFFSET_Y,
            DRAWING_AREA_WIDTH,
            DRAWING_AREA_HEIGHT
        );
        const data = imageData.data;
        const width = imageData.width;
        const height = imageData.height;

        const x = Math.floor(startX - DRAWING_AREA_OFFSET_X);
        const y = Math.floor(startY - DRAWING_AREA_OFFSET_Y);

        if (x < 0 || x >= width || y < 0 || y >= height) {
            console.error('Clic fuori dall\'area di disegno');
            return;
        }

        const startPos = (y * width + x) * 4;
        const targetColor = [
            data[startPos],
            data[startPos + 1],
            data[startPos + 2],
            data[startPos + 3],
        ];

        const fillRGBA = hexToRGBA(fillColor);
        if (
            fillRGBA[0] === targetColor[0] &&
            fillRGBA[1] === targetColor[1] &&
            fillRGBA[2] === targetColor[2] &&
            fillRGBA[3] === targetColor[3]
        ) {
            return;
        }

        const stack = [[x, y]];

        while (stack.length > 0) {
            const [currentX, currentY] = stack.pop();
            const currentPos = (currentY * width + currentX) * 4;

            if (colorsMatch(data, currentPos, targetColor)) {
                // Imposta il colore di riempimento
                data[currentPos] = fillRGBA[0];
                data[currentPos + 1] = fillRGBA[1];
                data[currentPos + 2] = fillRGBA[2];
                data[currentPos + 3] = fillRGBA[3];

                // Aggiungi pixel adiacenti
                if (currentX > 0) stack.push([currentX - 1, currentY]);
                if (currentX < width - 1) stack.push([currentX + 1, currentY]);
                if (currentY > 0) stack.push([currentX, currentY - 1]);
                if (currentY < height - 1) stack.push([currentX, currentY + 1]);
            }
        }

        // Aggiorna l'immagine sul canvas
        context.putImageData(imageData, DRAWING_AREA_OFFSET_X, DRAWING_AREA_OFFSET_Y);
        // Aggiorna il layer di Konva
        const stage = stageRef.current.getStage();
        const layer = stage.findOne('.drawing-layer'); // Assicurati di usare il nome corretto
        layer.batchDraw();
    };

    // Implementazione dello strumento Lasso
    const handleLassoComplete = (forceClosure = false) => {
        if (!currentLasso || currentLasso.points.length < 6) {
            alert('Per favore, disegna un contorno chiuso.');
            setCurrentLasso(null);
            return;
        }

        const points = currentLasso.points;
        let isClosed = false;
        if (!forceClosure) {
            const distance = Math.sqrt(
                Math.pow(points[0] - points[points.length - 2], 2) +
                Math.pow(points[1] - points[points.length - 1], 2)
            );
            isClosed = distance <= 20; // Aumenta la soglia a 20 pixel
        } else {
            isClosed = true;
        }

        if (!isClosed) {
            alert('Il contorno non è chiuso. Continua a disegnare o fai doppio clic per chiudere.');
            return;
        }

        // Crea una nuova area riempita
        const filledArea = {
            points: currentLasso.points,
            color: selectedColor,
        };

        setFilledAreas((prevFilledAreas) => [...prevFilledAreas, filledArea]);

        // Invia l'azione via WebSocket
        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.publish({
                destination: `/app/fill/${disegnoId}`,
                body: JSON.stringify(filledArea),
            });
        }

        setCurrentLasso(null);
    };

    // Handler per il doppio clic
    const handleDoubleClick = () => {
        if (selectedTool === "lasso" && currentLasso) {
            handleLassoComplete(true); // Forza la chiusura del lasso
        }
    };

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

        if (selectedTool === "draw" || selectedTool === "eraser") {
            const newStroke = {
                color: selectedTool === "eraser" ? "white" : selectedColor,
                points: [pos.x, pos.y],
                strokeWidth: selectedTool === "eraser" ? ERASER_STROKE : LINE_STROKE,
            };
            setCurrentStroke(newStroke);
            setStrokes((prevStrokes) => [...prevStrokes, newStroke]);
        }

        if (selectedTool === "lasso") {
            if (!currentLasso) {
                // Inizia un nuovo lasso
                setCurrentLasso({
                    points: [pos.x, pos.y],
                });
            } else {
                // Aggiungi punti al lasso
                setCurrentLasso((prevLasso) => ({
                    ...prevLasso,
                    points: [...prevLasso.points, pos.x, pos.y],
                }));
            }
        }
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
        if ((selectedTool === "draw" || selectedTool === "eraser") && currentStroke && inside) {
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
        }

        // Se lo strumento è lasso e il lasso è attivo, aggiungi punti
        if (selectedTool === "lasso" && currentLasso && inside) {
            setCurrentLasso((prevLasso) => ({
                ...prevLasso,
                points: [...prevLasso.points, point.x, point.y],
            }));
        }
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

        if (selectedTool === "lasso" && currentLasso) {
            // Completa il lasso se il contorno è chiuso
            handleLassoComplete();
        }
    };

    // Gestione del cambio colore
    const handleColorChange = (e) => {
        setSelectedColor(e.target.value);
        setSelectedTool("draw"); // Torna allo strumento disegno quando si cambia colore
    };

    // Gestione del cambio strumento
    const handleToolChange = (tool) => {
        setSelectedTool(tool);
    };

    const colors = ["red", "green", "yellow", "black", "blue", "#FF5733", "#33FF57", "#3357FF"]; // Aggiungi altri colori se necessario

    return (
        <div>
            <Stage
                width={dimensions.width}
                height={dimensions.height}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onTouchStart={handleMouseDown}
                onTouchMove={handleMouseMove}
                onTouchEnd={handleMouseUp}
                onDblClick={handleDoubleClick} // Aggiungi l'handler per il doppio clic
                ref={stageRef}
                style={{
                    backgroundColor: "#f0f0f0",
                    cursor:
                        selectedTool === "eraser" && isInsideDrawingArea
                            ? "none"
                            : selectedTool === "lasso" && isInsideDrawingArea
                                ? "crosshair"
                                : "default",
                }}
            >

                {/* Layer unificato per l'area di disegno, tratti e lasso */}
                <Layer name="drawing-layer">
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

                    {/* Tratti disegnati */}
                    {strokes.map((stroke, index) => (
                        <Line
                            key={index}
                            points={stroke.points.flat()} // Appiattimento
                            stroke={stroke.color}
                            strokeWidth={stroke.strokeWidth}
                            lineCap="round"
                            lineJoin="round"
                            tension={0.5}
                            globalCompositeOperation={stroke.color === "white" ? 'destination-out' : 'source-over'} /* per gestione eventuale della gomma */
                        />
                    ))}

                    {/* Aree riempite con il lasso */}
                    {filledAreas.map((area, index) => (
                        <Line
                            key={`filled-${index}`}
                            points={area.points}
                            closed={true}
                            fill={area.color}
                            stroke={area.color}
                            strokeWidth={1}
                            opacity={0.5}
                        />
                    ))}

                    {/* Tratto temporaneo per il lasso in fase di disegno */}
                    {selectedTool === "lasso" && currentLasso && (
                        <Line
                            points={currentLasso.points}
                            stroke={selectedColor}
                            strokeWidth={2}
                            lineCap="round"
                            lineJoin="round"
                            tension={0.5}
                        />
                    )}

                    {/* Cerchio che segue il mouse (Gomma) */}
                    {(selectedTool === "eraser") && isInsideDrawingArea && cursorPosition && (
                        <Circle
                            x={cursorPosition.x}
                            y={cursorPosition.y}
                            radius={ERASER_STROKE / 2} // La dimensione del cerchio è uguale alla dimensione della gomma
                            fill="rgba(0, 0, 0, 0.2)" // Cerchio semitrasparente
                        />
                    )}
                </Layer>
            </Stage>

            {/* Selettore colori */}
            <div className="color-selector">
                {colors.map((colorOption, index) => (
                    <div
                        key={index}
                        className={`color-circle ${
                            selectedColor === colorOption && selectedTool === "draw" ? "selected" : ""
                        }`}
                        style={{ backgroundColor: colorOption }}
                        onClick={() => {
                            setSelectedColor(colorOption);
                            setSelectedTool("draw"); // Seleziona lo strumento disegno quando si sceglie un colore
                        }}
                    />
                ))}
            </div>

            {/* Selettore strumenti */}
            <div className="tool-selector">
                {/* Pulsante gomma */}
                <button
                    onClick={() => handleToolChange(selectedTool === "eraser" ? "draw" : "eraser")}
                    className={`eraser-button ${selectedTool === "eraser" ? "active" : ""}`}
                    title="Gomma"
                >
                    <FaEraser size={20}/> {/* Icona della gomma */}
                </button>

                {/* Pulsante lasso */}
                <button
                    onClick={() => handleToolChange(selectedTool === "lasso" ? "draw" : "lasso")}
                    className={`lasso-button ${selectedTool === "lasso" ? "active" : ""}`}
                    title="Lasso"
                >
                    <FaSlash size={20}/> {/* Icona del lasso */}
                </button>
            </div>
        </div>
    );

};

export default DrawingBoard;
