import React, { useState, useRef } from 'react';
import './MiniVideoPlayer.css'; // eventuale file CSS per lo stile

const MiniVideoPlayer = ({ videoUrl, onClose }) => {
    const videoRef = useRef(null);
    const [isPaused, setIsPaused] = useState(true);

    const handlePlayPause = () => {
        if (!videoRef.current) return;

        if (isPaused) {
            videoRef.current.play();
            setIsPaused(false);
        } else {
            videoRef.current.pause();
            setIsPaused(true);
        }
    };

    return (
        <div className="mini-video-player">
            <div className="player-header">
                <button className="close-btn" onClick={onClose}>
                    × {/* Icona di chiusura (X) */}
                </button>
            </div>

            <video
                ref={videoRef}
                src={videoUrl}
                className="player-video"
                // Rimuove i controlli nativi del browser
                controls={false}
            />

            <div className="player-controls">
                <button className="play-pause-btn" onClick={handlePlayPause}>
                    {isPaused ? '▶️ Riprendi' : '⏸ Pausa'}
                </button>
            </div>
        </div>
    );
};

export default MiniVideoPlayer;
