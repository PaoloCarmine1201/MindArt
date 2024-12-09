import React, { useState, useEffect } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'moment/locale/it';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import EventoForm from "./EventoForm";

moment.locale('it');
const localizer = momentLocalizer(moment);

function MyCalendar() {
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        fetch('http://localhost:8080/api/terapeuta/1/events')
            .then(res => res.json())
            .then(data => {
                const converted = data.map(ev => ({
                    id: ev.id,
                    title: ev.nome,
                    start: new Date(ev.inizio),
                    end: new Date(ev.fine),
                    terapeuta: ev.terapeuta
                }));
                setEvents(converted);
            })
            .catch(err => console.error(err));
    }, []);

    const handleSelectSlot = (slotInfo) => {
        setSelectedEvent({
            start: slotInfo.start,
            end: slotInfo.end,
            title: '',
            terapeuta: 1
        });
        setShowModal(true);
    };

    const handleSelectEvent = (event) => {
        setSelectedEvent(event);
        setShowModal(true);
    };

    const handleCreateEvent = (eventData) => {
        const payload = {
            nome: eventData.title,
            inizio: eventData.start.toISOString(),
            fine: eventData.end.toISOString(),
            terapeuta: eventData.terapeuta || 1
        };

        fetch('http://localhost:8080/api/terapeuta/event', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(res => res.json())
            .then(newEvent => {
                const mappedEvent = {
                    id: newEvent.id,
                    title: newEvent.nome,
                    start: new Date(newEvent.inizio),
                    end: new Date(newEvent.fine),
                    terapeuta: newEvent.terapeuta
                };
                setEvents(prev => [...prev, mappedEvent]);
                setShowModal(false);
            })
            .catch(err => console.error(err));
    };

    const handleUpdateEvent = (eventData) => {
        const payload = {
            id: eventData.id,
            nome: eventData.title,
            inizio: eventData.start.toISOString(),
            fine: eventData.end.toISOString(),
            terapeuta: eventData.terapeuta || 1
        };

        fetch('http://localhost:8080/api/terapeuta/event', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(res => res.json())
            .then(updatedEvent => {
                const mappedEvent = {
                    id: updatedEvent.id,
                    title: updatedEvent.nome,
                    start: new Date(updatedEvent.inizio),
                    end: new Date(updatedEvent.fine),
                    terapeuta: updatedEvent.terapeuta
                };

                setEvents(prev => prev.map(ev => ev.id === mappedEvent.id ? mappedEvent : ev));
                setShowModal(false);
            })
            .catch(err => console.error(err));
    };

    const handleDeleteEvent = (id) => {
        fetch(`http://localhost:8080/api/terapeuta/event/${id}`, { method: 'DELETE' })
            .then(() => {
                setEvents(prev => prev.filter(ev => ev.id !== id));
                setShowModal(false);
            })
            .catch(err => console.error(err));
    };

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
                    }}
                    selectable
                    onSelectSlot={handleSelectSlot}
                    onSelectEvent={handleSelectEvent}
                />
            </div>
            {showModal && (
                <EventoForm
                    event={selectedEvent}
                    onSave={handleSaveOrUpdate}
                    onDelete={handleDeleteEvent}
                    onClose={() => setShowModal(false)}
                    existingEvents={events}
                />
            )}
        </div>
    );
}

export default MyCalendar;
