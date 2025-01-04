// src/components/MyCalendar/MyCalendar.jsx

import React, { useState, useEffect } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'moment/locale/it';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import EventoForm from "./EventoForm";
import axiosInstance from "../../config/axiosInstance";
import {toast} from "react-toastify"; // Ensure correct import path

// Initialize moment with Italian locale
moment.locale('it');
const localizer = momentLocalizer(moment);

function MyCalendar() {
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(false);

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
                }));
                setEvents(convertedEvents);
            } catch (err) {
                console.error('Error fetching events:', err);
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
            };
            if(response && (response.status === 200 || response.status === 201)){
                toast.success("Evento creato con successo!");
            }

            setEvents(prev => [...prev, newEvent]);
            setShowModal(false);
            setSelectedEvent(null);
        } catch (err) {
            console.error('Error creating event:', err);
            toast.error("Errore nella creazione di un evento.");
        }
    };

    // Handle updating an existing event
    const handleUpdateEvent = async (eventData) => {
        const payload = {
            id: eventData.id,
            nome: eventData.title,
            inizio: eventData.start.toISOString(),
            fine: eventData.end.toISOString(),
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
            };

            if(response && (response.status === 200 || response.status === 201)){
                toast.success("Evento aggiornato con successo!");
            }

            setEvents(prev => prev.map(ev => ev.id === updatedEvent.id ? updatedEvent : ev));
            setShowModal(false);
            setSelectedEvent(null);
        } catch (err) {
            console.error('Error updating event:', err);
            toast.error("Errore nell'aggiornamento dell'evento.");
        }
    };

    // Handle deleting an event
    const handleDeleteEvent = async (id) => {
        try {
            await axiosInstance.delete(`/api/terapeuta/event/${id}`);

            toast.success("Evento eliminato con successo!");
            setEvents(prev => prev.filter(ev => ev.id !== id));
            setShowModal(false);
            setSelectedEvent(null);
        } catch (err) {
            console.error('Error deleting event:', err);
            toast.error("Errore nell'eliminazione dell'evento.");
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
        <div className={` ${showModal ? 'blur' : ''}`}>
            <div style={{ height: '80vh' }}>
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
            </div>
            {showModal && (
                <EventoForm
                    event={selectedEvent}
                    onSave={handleSaveOrUpdate}
                    onDelete={handleDeleteEvent}
                    onClose={() => {
                        setShowModal(false);
                        setSelectedEvent(null);
                    }}
                />
            )}
        </div>
    );
}

export default MyCalendar;
