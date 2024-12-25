// src/components/MyCalendar/MyCalendar.jsx

import React, { useState, useEffect } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'moment/locale/it';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import EventoForm from "./EventoForm";
import axiosInstance from "../../config/axiosInstance"; // Ensure correct import path

// Initialize moment with Italian locale
moment.locale('it');
const localizer = momentLocalizer(moment);

function MyCalendar() {
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Fetch events when the component mounts
    useEffect(() => {
        const fetchEvents = async () => {
            setLoading(true);
            try {
                const response = await axiosInstance.get('/api/terapeuta/events');
                const convertedEvents = response.data.map(ev => ({
                    id: ev.id,
                    title: ev.nome,
                    start: new Date(ev.inizio),
                    end: new Date(ev.fine),
                    terapeuta: ev.terapeuta
                }));
                setEvents(convertedEvents);
                setError(null);
            } catch (err) {
                console.error('Error fetching events:', err);
                setError('Errore nel caricamento degli eventi.');
            } finally {
                setLoading(false);
            }
        };

        fetchEvents();
    }, []);

    // Handle slot selection for creating a new event
    const handleSelectSlot = (slotInfo) => {
        setSelectedEvent({
            start: slotInfo.start,
            end: slotInfo.end,
            title: '',
            terapeuta: parseInt(localStorage.getItem("idTerapeuta"), 10)
        });
        setShowModal(true);
    };

    // Handle event selection for viewing/editing
    const handleSelectEvent = (event) => {
        setSelectedEvent(event);
        setShowModal(true);
    };

    // Handle creating a new event
    const handleCreateEvent = async (eventData) => {
        const payload = {
            nome: eventData.title,
            inizio: eventData.start.toISOString(),
            fine: eventData.end.toISOString(),
            terapeuta: eventData.terapeuta
        };

        try {
            console.log('Dati inviati:', payload);
            const response = await axiosInstance.post('/api/terapeuta/event', payload);

            // Assuming the backend returns the created event
            const newEvent = {
                id: response.data.id,
                title: response.data.nome,
                start: new Date(response.data.inizio),
                end: new Date(response.data.fine),
                terapeuta: response.data.terapeuta
            };
            setEvents(prev => [...prev, newEvent]);
            setShowModal(false);
            setSelectedEvent(null);
        } catch (err) {
            console.error('Error creating event:', err);
            setError('Si è verificato un errore durante la creazione dell\'evento.');
        }
    };

    // Handle updating an existing event
    const handleUpdateEvent = async (eventData) => {
        const payload = {
            id: eventData.id,
            nome: eventData.title,
            inizio: eventData.start.toISOString(),
            fine: eventData.end.toISOString(),
            terapeuta: eventData.terapeuta || 1
        };

        try {
            console.log('Dati aggiornati inviati:', payload);
            const response = await axiosInstance.put('/api/terapeuta/event', payload);

            // Assuming the backend returns the updated event
            const updatedEvent = {
                id: response.data.id,
                title: response.data.nome,
                start: new Date(response.data.inizio),
                end: new Date(response.data.fine),
                terapeuta: response.data.terapeuta
            };
            setEvents(prev => prev.map(ev => ev.id === updatedEvent.id ? updatedEvent : ev));
            setShowModal(false);
            setSelectedEvent(null);
        } catch (err) {
            console.error('Error updating event:', err);
            setError('Si è verificato un errore durante l\'aggiornamento dell\'evento.');
        }
    };

    // Handle deleting an event
    const handleDeleteEvent = async (id) => {
        try {
            await axiosInstance.delete(`/api/terapeuta/event/${id}`);
            setEvents(prev => prev.filter(ev => ev.id !== id));
            setShowModal(false);
            setSelectedEvent(null);
        } catch (err) {
            console.error('Error deleting event:', err);
            setError('Si è verificato un errore durante la cancellazione dell\'evento.');
        }
    };

    // Decide whether to create or update based on eventData
    const handleSaveOrUpdate = (eventData) => {
        if (eventData.id) {
            handleUpdateEvent(eventData);
        } else {
            handleCreateEvent(eventData);
        }
    };

    return (
        <div className={`app-container ${showModal ? 'blur' : ''}`}>
            <div style={{ height: '60vh' }}>
                <Calendar
                    localizer={localizer}
                    culture='it'
                    views={['month', 'week', 'day']}
                    defaultView="week"
                    events={events}
                    startAccessor="start"
                    endAccessor="end"
                    messages={{
                        next: 'Successivo',
                        previous: 'Precedente',
                        today: 'Oggi',
                        month: 'Mese',
                        week: 'Settimana',
                        day: 'Giorno',
                        agenda: 'Agenda',
                        date: 'Data',
                        time: 'Orario',
                        event: 'Evento',
                        showMore: (total) => `+${total} di più`,
                    }}
                    selectable
                    onSelectSlot={handleSelectSlot}
                    onSelectEvent={handleSelectEvent}
                />
                {loading && <p>Caricamento eventi...</p>}
                {error && <p className="error-message">{error}</p>}
            </div>
            {showModal && (
                <EventoForm
                    event={selectedEvent}
                    onSave={handleSaveOrUpdate}
                    onDelete={handleDeleteEvent}
                    onClose={() => {
                        setShowModal(false);
                        setSelectedEvent(null);
                        setError(null);
                    }}
                />
            )}
        </div>
    );
}

export default MyCalendar;
