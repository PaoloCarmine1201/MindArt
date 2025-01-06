// src/components/ColoreBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line, Rect, Circle } from 'react-konva';
import { connectWebSocket, subscribeToDraw } from "../../utils/websocket";
import axiosInstance from "../../config/axiosInstance";
import { FaEraser, FaSlash } from "react-icons/fa";
import "../../style/Lavagna.css";
import ConfermaDisegno from "../ConfermaDisegno/ConfermaDisegno";
import { useNavigate } from 'react-router-dom'; // Correzione dell'importazione

// Helper function to map file extensions to MIME types
/*const getMimeType = (filename) => {
    const extension = filename.split('.').pop().toLowerCase();
    const mimeTypes = {
        'png': 'image/png',
        'jpg': 'image/jpeg',
        'jpeg': 'image/jpeg',
        'gif': 'image/gif',
        'svg': 'image/svg+xml',
        'bmp': 'image/bmp',
        'webp': 'image/webp',
        // Aggiungi altre estensioni se necessario
    };
    return mimeTypes[extension] || 'image/png'; // Default MIME type
};*/

const ColoreBoard = () => {
    const [actions, setActions] = useState([]); // Unico array per tutte le azioni
    const [disegnoId, setDisegnoId] = useState(null); // ID del disegno
    const stageRef = useRef(null);
    const [currentStroke, setCurrentStroke] = useState(null);
    const [currentLasso, setCurrentLasso] = useState(null); // Stato per il lasso corrente
    const stompClientRef = useRef(null);
    const [isInsideDrawingArea, setIsInsideDrawingArea] = useState(false); // Se il cursore è dentro l'area di disegno

    const navigate = useNavigate(); // Hook per la navigazione

    // Dimensioni fisse per il Stage e l'area di disegno
    const STAGE_WIDTH = 1920;
    const STAGE_HEIGHT = 1080;
    const DRAWING_AREA_OFFSET_X = 25; // Offset orizzontale (largo quanto il selettore)
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale (per lasciare un margine in alto)
    const DRAWING_AREA_WIDTH = 1870; // Larghezza fissa dell'area di disegno (1920 - 2*25)
    const DRAWING_AREA_HEIGHT = 1030; // Altezza fissa dell'area di disegno (1080 - 2*25)

    const [selectedColor, setSelectedColor] = useState("black"); // Colore selezionato
    const [selectedTool, setSelectedTool] = useState("draw"); // Strumento selezionato: 'draw', 'eraser', 'lasso'
    const [cursorPosition, setCursorPosition] = useState({x: -100, y: -100}); // Posizione del mouse

    const ERASER_STROKE = 20;

    // Funzione per formattare i punti
    const formatPoints = (points) => {
        if (Array.isArray(points) && points.length > 0) {
            // Verifica se i punti sono annidati, ad esempio [[x1, y1], [x2, y2], ...]
            if (Array.isArray(points[0])) {
                return points.flat();
            }
            // Se i punti sono già in formato piatto [x1, y1, x2, y2, ...]
            return points;
        }
        return [];
    };



    // Effetto per caricare il disegno e l'immagine di sfondo
    useEffect(() => {
        const loadActions = async () => {
            try {
                const disegnoResponse = await axiosInstance.get(`/api/bambino/sessione/disegno/`);
                const disegno = disegnoResponse.data;
                setDisegnoId(disegno.id);

                console.log('Disegno caricato:', disegno);


                // Gestisci strokes e filledAreas
                const initialStrokes = disegno.strokes.map(stroke => ({
                    ...stroke,
                    type: stroke.type || "stroke",
                    points: formatPoints(stroke.points),
                }));

                const initialFilledAreas = (disegno.filledAreas || []).map(area => ({
                    ...area,
                    type: area.type || "lasso",
                    points: formatPoints(area.points),
                }));

                const initialActions = [...initialStrokes, ...initialFilledAreas];
                setActions(initialActions);

                console.log('Actions loaded:', initialActions);
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
                localStorage.removeItem('jwtToken');
                navigate('/childlogin')
            }
        };

        loadActions();
    }, []);

    // Effetto per gestire la connessione WebSocket
    useEffect(() => {
        if (!disegnoId) return;

        const client = connectWebSocket();

        client.onConnect = () => {
            console.log('Connesso al WebSocket');
            subscribeToDraw(client, disegnoId, (data) => {
                const formattedAction = {
                    ...data,
                    points: formatPoints(data.points),
                };
                setActions((prevActions) => [...prevActions, formattedAction]);
                console.log('Received action via WebSocket:', formattedAction);
            });
        };

        client.onStompError = (frame) => {
            console.error('Errore STOMP:', frame.headers['message']);
            // Puoi gestire errori STOMP qui, ad esempio mostrando un messaggio all'utente
        };

        client.activate();
        stompClientRef.current = client;

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [disegnoId]);


    // Funzione per completare il lasso
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
            isClosed = distance <= 20;
        } else {
            isClosed = true;
        }

        if (!isClosed) {
            alert('Il contorno non è chiuso. Continua a disegnare o fai doppio clic per chiudere.');
            return;
        }

        const formattedPoints = [];
        for (let i = 0; i < currentLasso.points.length; i += 2) {
            const x = Math.round(currentLasso.points[i]);
            const y = Math.round(currentLasso.points[i + 1]);
            formattedPoints.push([x, y]);
        }

        const filledArea = {
            points: formattedPoints,
            color: selectedColor,
            type: "lasso",
        };

        setActions((prevActions) => [...prevActions, filledArea]);

        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.publish({
                destination: `/app/draw/${disegnoId}`,
                body: JSON.stringify(filledArea),
            });
        }

        setCurrentLasso(null);
    };

    // Handler per il doppio clic
    const handleDoubleClick = () => {
        if (selectedTool === "lasso" && currentLasso) {
            handleLassoComplete(true);
        }
    };

    // Gestione degli eventi di disegno
    const handleMouseDown = (e) => {
        const pos = e.target.getStage().getPointerPosition();

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
                points: [Math.round(pos.x), Math.round(pos.y)], // Arrotonda le coordinate
                strokeWidth: selectedTool === "eraser" ? ERASER_STROKE : 2,
                type: "stroke", // Aggiungi il tipo "stroke"
            };
            setCurrentStroke(newStroke);
            setActions((prevActions) => [...prevActions, newStroke]);
        }


        if (selectedTool === "lasso") {
            if (!currentLasso) {
                setCurrentLasso({
                    points: [Math.round(pos.x), Math.round(pos.y)],
                });
            } else {
                setCurrentLasso((prevLasso) => ({
                    ...prevLasso,
                    points: [...prevLasso.points, Math.round(pos.x), Math.round(pos.y)],
                }));
            }
        }
    };

    const handleMouseMove = (e) => {
        const stage = e.target.getStage();
        const point = stage.getPointerPosition();

        if (!point) return;

        setCursorPosition(point);

        const inside =
            point.x >= DRAWING_AREA_OFFSET_X &&
            point.x <= DRAWING_AREA_OFFSET_X + DRAWING_AREA_WIDTH &&
            point.y >= DRAWING_AREA_OFFSET_Y &&
            point.y <= DRAWING_AREA_OFFSET_Y + DRAWING_AREA_HEIGHT;

        setIsInsideDrawingArea(inside);

        if ((selectedTool === "draw" || selectedTool === "eraser") && currentStroke && inside) {
            const updatedStroke = {
                ...currentStroke,
                points: [
                    ...currentStroke.points,
                    Math.round(point.x),
                    Math.round(point.y),
                ],
            };

            setActions((prevActions) => {
                const newActions = [...prevActions];
                newActions[newActions.length - 1] = updatedStroke;
                return newActions;
            });

            setCurrentStroke(updatedStroke);
        }

        if (selectedTool === "lasso" && currentLasso && inside) {
            setCurrentLasso((prevLasso) => ({
                ...prevLasso,
                points: [
                    ...prevLasso.points,
                    Math.round(point.x),
                    Math.round(point.y),
                ],
            }));
        }
    };

    const handleMouseUp = () => {
        if (currentStroke) {
            if (stompClientRef.current && stompClientRef.current.connected) {
                const formattedPoints = [];
                for (let i = 0; i < currentStroke.points.length; i += 2) {
                    const x = currentStroke.points[i];
                    const y = currentStroke.points[i + 1];
                    formattedPoints.push([x, y]);
                }

                const message = {
                    color: currentStroke.color,
                    points: formattedPoints,
                    strokeWidth: currentStroke.strokeWidth,
                    type: currentStroke.type,
                };

                console.log('Sending message:', message);

                stompClientRef.current.publish({
                    destination: `/app/draw/${disegnoId}`,
                    body: JSON.stringify(message),
                });
            }

            setCurrentStroke(null);
        }

        if (selectedTool === "lasso" && currentLasso) {
            handleLassoComplete();
        }
    };

    // Gestione del cambio strumento
    const handleToolChange = (tool) => {
        setSelectedTool(tool);
    };

    const colors = ["red", "green", "yellow", "black", "blue"];

    return (
        <>


            <div className="drawing-container">
                <ConfermaDisegno/>
                <Stage
                    width={STAGE_WIDTH}
                    height={STAGE_HEIGHT}
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
                    {/* Layer 1: Rettangolo dell'area di disegno */}
                    <Layer name="rect-layer">
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



                    {/* Layer 3: Tratti e Lasso */}
                    <Layer name="drawing-layer">
                        {/* Itera su tutte le azioni */}
                        {actions.map((action, index) => {
                            if (action.type === "lasso") {
                                return (
                                    <Line
                                        key={`action-${index}`}
                                        points={action.points}
                                        closed={true}
                                        fill={action.color}
                                        stroke={action.color}
                                        strokeWidth={1}
                                        opacity={0.5}
                                    />
                                );
                            } else if (action.type === "stroke") {
                                return (
                                    <Line
                                        key={`action-${index}`}
                                        points={action.points}
                                        stroke={action.color}
                                        strokeWidth={action.strokeWidth}
                                        lineCap="round"
                                        lineJoin="round"
                                        tension={0.5}
                                        globalCompositeOperation={action.color === "white" ? 'destination-out' : 'source-over'}
                                    />
                                );
                            } else {
                                return null; // In caso di tipi sconosciuti
                            }
                        })}

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
                                radius={ERASER_STROKE / 2}
                                fill="rgba(0, 0, 0, 0.2)"
                            />
                        )}
                    </Layer>
                </Stage>
                <div
                    style={{
                        position: "fixed",
                        bottom: 0,
                        left: "50%",
                        transform: "translateX(-50%)",
                        display: "flex",
                        alignItems: "center",
                        padding: "10px",
                        backgroundColor: "#ffffffcc",
                        borderTop: "1px solid #ccc"
                    }}>
                    {/* Selettore colori */}
                    <div className="color-selector" style={{display: "flex", marginRight: "20px"}}>
                        {colors.map((colorOption, index) => (
                            <div
                                key={index}
                                className={`color-circle ${
                                    selectedColor === colorOption && selectedTool === "draw" ? "selected" : ""
                                }`}
                                style={{
                                    backgroundColor: colorOption,
                                    width: "24px",
                                    height: "24px",
                                    borderRadius: "50%",
                                    marginRight: "8px",
                                    border: selectedColor === colorOption && selectedTool === "draw" ? "2px solid #000" : "none",
                                    cursor: "pointer"
                                }}
                                onClick={() => {
                                    setSelectedColor(colorOption);
                                    setSelectedTool("draw"); // Seleziona lo strumento disegno quando si sceglie un colore
                                }}
                            />
                        ))}
                    </div>

                    {/* Selettore strumenti */}
                    <div className="tool-selector" style={{display: "flex", alignItems: "center"}}>
                        {/* Pulsante Gomma */}
                        <button
                            onClick={() => handleToolChange(selectedTool === "eraser" ? "draw" : "eraser")}
                            className={`eraser-button ${selectedTool === "eraser" ? "active" : ""}`}
                            title="Gomma"
                            style={{
                                backgroundColor: selectedTool === "eraser" ? "#ddd" : "#fff",
                                border: "1px solid #ccc",
                                borderRadius: "4px",
                                padding: "6px",
                                marginRight: "8px",
                                cursor: "pointer"
                            }}
                        >
                            <FaEraser size={20}/>
                        </button>

                        {/* Pulsante Lasso */}
                        <button
                            onClick={() => handleToolChange(selectedTool === "lasso" ? "draw" : "lasso")}
                            className={`lasso-button ${selectedTool === "lasso" ? "active" : ""}`}
                            title="Lasso"
                            style={{
                                backgroundColor: selectedTool === "lasso" ? "#ddd" : "#fff",
                                border: "1px solid #ccc",
                                borderRadius: "4px",
                                padding: "6px",
                                cursor: "pointer"
                            }}
                        >
                            <FaSlash size={20}/>
                        </button>
                    </div>
                </div>
            </div>
        </>
    );
};
export default ColoreBoard;
