import React, { useState, useRef, useEffect } from 'react';
import './MiniVideoPlayer.css'; // Eventuale file CSS per lo stile

const MiniVideoPlayer = ({ videoUrl, onClose }) => {
    const videoRef = useRef(null);
    const [isPaused, setIsPaused] = useState(true);
    const [currentTime, setCurrentTime] = useState(0); // Tempo attuale del video
    const [duration, setDuration] = useState(0); // Durata totale del video

    // Aggiorna il tempo attuale del video
    const handleTimeUpdate = () => {
        if (videoRef.current) {
            setCurrentTime(videoRef.current.currentTime);
        }
    };

    // Aggiorna la durata totale del video
    const handleLoadedMetadata = () => {
        if (videoRef.current) {
            setDuration(videoRef.current.duration);
        }
    };

    // Gestisce il play/pausa
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

    // Gestisce la modifica della barra del tempo
    const handleSeek = (event) => {
        const time = parseFloat(event.target.value);
        if (videoRef.current) {
            videoRef.current.currentTime = time;
        }
        setCurrentTime(time);
    };

    // Formatta il tempo in mm:ss
    const formatTime = (time) => {
        const minutes = Math.floor(time / 60);
        const seconds = Math.floor(time % 60).toString().padStart(2, '0');
        return `${minutes}:${seconds}`;
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
                onTimeUpdate={handleTimeUpdate} // Aggiorna il tempo corrente
                onLoadedMetadata={handleLoadedMetadata} // Imposta la durata totale
                controls={false} // Rimuove i controlli nativi del browser
            />

            <div className="player-controls">
                <button className="play-pause-btn" onClick={handlePlayPause}>
                    {isPaused ? '▶️ Riprendi' : '⏸ Pausa'}
                </button>
                <input
                    type="range"
                    className="time-slider"
                    min="0"
                    max={duration}
                    step="0.1"
                    value={currentTime}
                    onChange={handleSeek}
                />
                <div className="time-display">
                    {formatTime(currentTime)} / {formatTime(duration)}
                </div>
            </div>
        </div>
    );
};

export default MiniVideoPlayer;
