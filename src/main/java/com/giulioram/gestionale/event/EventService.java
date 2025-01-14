package com.giulioram.gestionale.event;

import com.giulioram.gestionale.event.utils.Snowflake;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final Snowflake snowflake;

    public EventService(EventRepository eventRepository, Snowflake snowflake) {
        this.eventRepository = eventRepository;
        this.snowflake = snowflake;
    }

    public Event findById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Event", eventId));
    }

    public List<Event> findAllEvents() {
        return this.eventRepository.findAll();
    }

    public Event save(Event event) {
        long idLong = snowflake.nextId();
        event.setId(idLong + "");
        return this.eventRepository.save(event);
    }

    public Event update(String eventId, Event update) {
        return this.eventRepository.findById(eventId)
                .map(oldEvent -> {
                    oldEvent.setName(update.getName());
                    oldEvent.setDataEvento(update.getDataEvento());
                    oldEvent.setCategory(update.getCategory());
                    oldEvent.setStatus(update.getStatus());
                    return eventRepository.save(oldEvent);
                }).orElseThrow(() -> new ObjectNotFoundException("Event", eventId));
    }

    public void delete(String eventId) {
        this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event", eventId));
        this.eventRepository.deleteById(eventId);
    }
}
