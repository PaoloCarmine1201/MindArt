import React, { useEffect, useState, useRef } from 'react';
import { Stage, Layer, Line, Rect } from 'react-konva';
import axiosInstance from "../../config/axiosInstance";
import "../../style/Lavagna.css";

const MostraDisegnoBambino = ({ disegnoId }) => {
    const [actions, setActions] = useState([]);
    const stageRef = useRef(null);

    const [dimensions, setDimensions] = useState({
        width: window.innerWidth,
        height: window.innerHeight,
    });

    const DRAWING_AREA_OFFSET_X = 120;
    const DRAWING_AREA_OFFSET_Y = 25;
    const DRAWING_AREA_WIDTH = dimensions.width - 150;
    const DRAWING_AREA_HEIGHT = dimensions.height - DRAWING_AREA_OFFSET_Y - 50;

    const formatPoints = (points) => {
        if (Array.isArray(points) && points.length > 0) {
            if (Array.isArray(points[0])) {
                return points.flat();
            }
            return points;
        }
        return [];
    };

    useEffect(() => {
        const loadActions = async () => {
            try {
                const disegnoResponse = await axiosInstance.get(`/api/terapeuta/disegno/${disegnoId}`);
                const disegno = disegnoResponse.data;

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
            }
        };

        if (disegnoId) {
            loadActions();
        }
    }, [disegnoId]);

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

    useEffect(() => {
        console.log('Current actions:', actions);
    }, [actions]);

    return (
        <div>
            <Stage
                width={dimensions.width}
                height={dimensions.height}
                ref={stageRef}
                style={{
                    backgroundColor: "#f0f0f0",
                    cursor: "default",
                }}
            >
                <Layer name="drawing-layer">
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
    );
};

export default MostraDisegnoBambino;