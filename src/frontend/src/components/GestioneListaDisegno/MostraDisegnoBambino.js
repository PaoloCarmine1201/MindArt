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

            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
            }
        };

        if (disegnoId) {
            loadActions();
        }
    }, [disegnoId]);

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


    return (
        <div className="lavagna-container">
            <Button
                variant="primary"
                className={"btn-vota"}
                style={{ alignContent: "right", marginLeft: "auto", marginRight: "10px", alignSelf: "flex-end" }}
                onClick={() => setShowValutazione(true)}
            >
                Vota
            </Button>

            <Stage
                width={dimensions.width}
                height={dimensions.height}
                ref={stageRef}
                style={{
                    backgroundColor: "transparent",
                    cursor: "default",
                }}
            >
                <Layer name="drawing-layer">
                    <Rect
                        x={DRAWING_AREA_OFFSET_X}
                        y={DRAWING_AREA_OFFSET_Y}
                        width={dimensions.width - DRAWING_AREA_OFFSET_X * 2}
                        height={dimensions.height - DRAWING_AREA_OFFSET_Y * 2}
                        fill="#ffffff"
                        stroke="black"
                        strokeWidth={2}
                        cornerRadius={15}
                        shadowColor="black"
                        shadowBlur={10}
                        shadowOffset={{ x: 5, y: 5 }}
                        shadowOpacity={0.2}
                    />

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

            {/* Usa il componente del modal */}
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
