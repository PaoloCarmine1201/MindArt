import React, { useEffect, useState, useRef } from 'react';
import { Stage, Layer, Line } from 'react-konva';
import axiosInstance from "../../config/axiosInstance";
import "../../style/Lavagna.css";
import "../../style/LavagnaVisualizzaDisegni.css";
import "../../style/Button.css";
import { Button } from "react-bootstrap";
import ValutazionePopup from "./ValutazionePopup";
import { toast } from "react-toastify";

const MostraDisegnoBambino = ({ disegnoId, tema }) => {
    const [actions, setActions] = useState([]);
    const stageRef = useRef(null);
    const [showValutazione, setShowValutazione] = useState(false);
    const [valutazione, setValutazione] = useState("");
    const [commento, setCommento] = useState("");
    const [dimensions, setDimensions] = useState({
        width: window.innerWidth * 0.8,
        height: window.innerHeight * 0.6,
    });
    const [drawingBounds, setDrawingBounds] = useState({
        minX: 0,
        minY: 0,
        maxX: window.innerWidth * 0.8,
        maxY: window.innerHeight * 0.6,
    });

    useEffect(() => {
        const loadActions = async () => {
            try {
                const disegnoResponse = await axiosInstance.get(`/api/terapeuta/disegno/${disegnoId}`);
                const disegno = disegnoResponse.data;

                const initialStrokes = disegno.strokes.map(stroke => ({
                    ...stroke,
                    type: stroke.type || "stroke",
                    points: stroke.points.flat(),
                }));

                const initialFilledAreas = (disegno.filledAreas || []).map(area => ({
                    ...area,
                    type: area.type || "lasso",
                    points: area.points.flat(),
                }));

                const initialActions = [...initialStrokes, ...initialFilledAreas];
                setActions(initialActions);

                // Calcola i limiti del disegno
                const allPoints = initialActions.flatMap(action => action.points);
                const minX = Math.min(...allPoints.filter((_, index) => index % 2 === 0));
                const minY = Math.min(...allPoints.filter((_, index) => index % 2 === 1));
                const maxX = Math.max(...allPoints.filter((_, index) => index % 2 === 0));
                const maxY = Math.max(...allPoints.filter((_, index) => index % 2 === 1));

                setDrawingBounds({
                    minX: Math.min(minX, 0),
                    minY: Math.min(minY, 0),
                    maxX: Math.max(maxX, dimensions.width),
                    maxY: Math.max(maxY, dimensions.height),
                });
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
            }
        };

        if (disegnoId) {
            loadActions();
        }
    }, [disegnoId, dimensions.width, dimensions.height]);

    useEffect(() => {
        const handleResize = () => {
            setDimensions({
                width: window.innerWidth * 0.8,
                height: window.innerHeight * 0.6,
            });
        };

        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    const handleSubmitValutazione = async () => {
        try {
            await axiosInstance.post(`/api/terapeuta/disegno/${disegnoId}/valutazione`, {
                valutazione,
            });

            toast.success("Valutazione inviata con successo!");
            setShowValutazione(false);
            window.location.reload(); // Ricarica la pagina per visualizzare la valutazione
        } catch (error) {
            console.error("Errore nell'invio della valutazione:", error);
            toast.error("Errore nell'invio della valutazione.");
        }
    };

    const drawingStyle = {
        width: Math.max(drawingBounds.maxX - drawingBounds.minX, dimensions.width),
        height: Math.max(drawingBounds.maxY - drawingBounds.minY, dimensions.height),
    };

    return (
        <div
            style={{
                marginTop: '20px',
                marginLeft: 'auto',
                marginRight: 'auto',
                border: '1px solid #ddd',
                borderRadius: '8px',
                width: dimensions.width, // Imposta la larghezza dinamicamente
            }}
        >
            {/* Riga superiore */}
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    padding: '10px',
                    backgroundColor: '#f1f1f1',
                    borderBottom: '1px solid #ccc',
                    borderRadius: '8px 8px 0 0',
                    width: '100%', // La larghezza è al 100% del contenitore padre
                }}
            >
                <h5 style={{ margin: 0 }}>{tema || "Nessun tema assegnato"}</h5>
                <Button
                    variant="primary"
                    className="btn-vota"
                    onClick={() => setShowValutazione(true)}
                    style={{
                        padding: '5px 10px',
                        fontSize: '14px',
                    }}
                >
                    Vota
                </Button>
            </div>

            {/* Board */}
            <div
                className="lavagna-container"
                style={{
                    overflow: 'auto',
                    width: dimensions.width,
                    height: dimensions.height,
                    position: 'relative',
                    margin: '0 auto',
                }}
            >
                <div style={{ ...drawingStyle, position: 'relative' }}>
                    <Stage
                        width={drawingStyle.width}
                        height={drawingStyle.height}
                        ref={stageRef}
                        style={{
                            backgroundColor: "transparent",
                            cursor: "default",
                        }}
                    >
                        <Layer name="drawing-layer">
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
                                    return null;
                                }
                            })}
                        </Layer>
                    </Stage>
                </div>

                <ValutazionePopup
                    show={showValutazione}
                    onClose={() => setShowValutazione(false)}
                    onSubmit={handleSubmitValutazione}
                    valutazione={valutazione}
                    setValutazione={setValutazione}
                    commento={commento}
                    setCommento={setCommento}
                />
            </div>
        </div>
    );
};

export default MostraDisegnoBambino;
