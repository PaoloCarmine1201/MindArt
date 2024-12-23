import React, { useEffect, useState } from "react";
import '../../style/VisualizzaEventiStyle.css';

function VisualizzaEventiComponent() {
    const [idTerapeuta, setIdTerapeuta] = useState(1);
    const [events, setEvents] = useState({});

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

                const sortedEvents = converted.sort((a, b) => a.start - b.start);

                const groupedEvents = sortedEvents.reduce((acc, event) => {
                    const eventDate = event.start.toLocaleDateString();
                    if (!acc[eventDate]) {
                        acc[eventDate] = [];
                    }
                    acc[eventDate].push(event);
                    return acc;
                }, {});

                setEvents(groupedEvents);
            })
            .catch(err => console.error(err));
    }, [idTerapeuta]);

    function getDayOfWeek(dateString) {
        const [day, month, year] = dateString.split('/');
        const date = new Date(`${year}-${month}-${day}`);
        if (isNaN(date)) {
            return 'Invalid Date';
        }
        const options = { weekday: 'long' };
        const dayName = date.toLocaleDateString('it-IT', options);
        return dayName.charAt(0).toUpperCase() + dayName.slice(1);
    }

    const today = new Date();
    const todayString = today.toLocaleDateString();
    const currentTime = today.getTime();

    // Filtrare eventi precedenti a oggi
    const futureEvents = Object.keys(events).reduce((acc, date) => {
        const eventDate = new Date(date.split('/').reverse().join('-')).getTime();
        if (eventDate >= new Date(todayString.split('/').reverse().join('-')).getTime()) {
            acc[date] = events[date];
        }
        return acc;
    }, {});

    const todayEvents = futureEvents[todayString] || [];
    const remainingEvents = todayEvents.filter(ev => ev.end.getTime() > currentTime);

    return (
        <div className="schedule">
            <div className="day">
                <div className="day-header">
                    <div className="day-number">{today.getDate()}</div>
                    <div className="day-name">Oggi</div>
                </div>
                {remainingEvents.length === 0 ? (
                    <div className="no-events">Per oggi non hai altri impegni</div>
                ) : (
                    remainingEvents.map(ev => (
                        <div key={ev.id} className={`event ${ev.title.replace(/\s+/g, '-').toLowerCase()}`}>
                            <div className="event-title">{ev.title}</div>
                            <div className="event-time">
                                {ev.start.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - {ev.end.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                            </div>
                        </div>
                    ))
                )}
            </div>
            {Object.keys(futureEvents).map(date => {
                const dayName = getDayOfWeek(date);
                const dayNumber = date.split('/')[0];

                if (date !== todayString && Array.isArray(futureEvents[date])) {
                    return (
                        <div key={date} className="day">
                            <div className="day-header">
                                <div className="day-number">{dayNumber}</div>
                                <div className="day-name">{dayName}</div>
                            </div>
                            {futureEvents[date].map(ev => (
                                <div key={ev.id} className={`event ${ev.title.replace(/\s+/g, '-').toLowerCase()}`}>
                                    <div className="event-title">{ev.title}</div>
                                    <div className="event-time">
                                        {ev.start.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - {ev.end.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                    </div>
                                </div>
                            ))}
                        </div>
                    );
                }
                return null;
            })}
        </div>
    );
}

export default VisualizzaEventiComponent;
