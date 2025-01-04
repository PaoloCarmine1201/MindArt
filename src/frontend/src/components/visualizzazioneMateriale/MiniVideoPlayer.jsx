import React, { useState, useRef } from 'react';
import './MiniVideoPlayer.css';

const MiniVideoPlayer = ({ videoUrl, onClose }) => {
    const videoRef = useRef(null);
    const [isPaused, setIsPaused] = useState(true);
    const [currentTime, setCurrentTime] = useState(0);
    const [duration, setDuration] = useState(0);

    // Update current time
    const handleTimeUpdate = () => {
        if (videoRef.current) {
            setCurrentTime(videoRef.current.currentTime);
        }
    };

    // Update total duration
    const handleLoadedMetadata = () => {
        if (videoRef.current) {
            setDuration(videoRef.current.duration);
        }
    };

    // Play/Pause toggle
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

    // Seek the video
    const handleSeek = (event) => {
        const time = parseFloat(event.target.value);
        if (videoRef.current) {
            videoRef.current.currentTime = time;
        }
        setCurrentTime(time);
    };

    // Format time
    const formatTime = (time) => {
        const minutes = Math.floor(time / 60);
        const seconds = Math.floor(time % 60).toString().padStart(2, '0');
        return `${minutes}:${seconds}`;
    };

    return (
        <div className="mini-video-player">
            <div className="player-header">
                <button className="close-btn" onClick={onClose}>
                    ×
                </button>
            </div>

            <video
                ref={videoRef}
                src={videoUrl}
                className="player-video"
                onTimeUpdate={handleTimeUpdate}
                onLoadedMetadata={handleLoadedMetadata}
                controls={false}
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
