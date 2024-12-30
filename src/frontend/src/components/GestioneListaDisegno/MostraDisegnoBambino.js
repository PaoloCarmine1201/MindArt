import React, { useEffect, useState, useRef } from 'react';
import { Stage, Layer, Line, Rect } from 'react-konva';
import axiosInstance from "../../config/axiosInstance";
import "../../style/Lavagna.css";
import "../../style/LavagnaVisualizzaDisegni.css"
import "../../style/Button.css";
import { Button } from "react-bootstrap";
import ValutazionePopup from "./ValutazionePopup"; // Importa il modal

const MostraDisegnoBambino = ({ disegnoId }) => {
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

    const DRAWING_AREA_OFFSET_X = 20;
    const DRAWING_AREA_OFFSET_Y = 20;

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
            await Promise.all([
                axiosInstance.post(`/api/terapeuta/disegno/${disegnoId}/valutazione`, {
                    valutazione,
                }),
            ]);

            alert("Valutazione inviata con successo!");
            setShowValutazione(false);
        } catch (error) {
            console.error("Errore nell'invio della valutazione:", error);
            alert("Errore nell'invio della valutazione.");
        }
    };

    const drawingStyle = {
        width: Math.max(drawingBounds.maxX - drawingBounds.minX, dimensions.width),
        height: Math.max(drawingBounds.maxY - drawingBounds.minY, dimensions.height),
    };

    return (
        <div
            className="lavagna-container"
            style={{
                overflow: 'auto',
                width: dimensions.width,
                height: dimensions.height,
                border: '1px solid black',
                position: 'relative', // Permette il posizionamento del bottone in posizione assoluta
                margin: '0 auto', // Centra orizzontalmente l'intera lavagna
            }}
        >
            {/* Bottone centrato */}
            <Button
                variant="primary"
                className={"btn-vota"}
                style={{
                    position: 'absolute',
                    left: '50%',
                    zIndex: 10, // Assicura che il bottone sia sopra tutti gli altri elementi
                }}
                onClick={() => setShowValutazione(true)}
            >
                Vota
            </Button>

            <div style={{...drawingStyle, position: 'relative'}}>
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

    );
};

export default MostraDisegnoBambino;
