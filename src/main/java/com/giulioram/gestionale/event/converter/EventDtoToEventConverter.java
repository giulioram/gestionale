package com.giulioram.gestionale.event.converter;

import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.event.dto.EventDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventDtoToEventConverter implements Converter<EventDto, Event> {
    @Override
    public Event convert(EventDto source) {
        Event event = new Event();
        event.setId(source.id());
        event.setDataEvento(source.dataEvento());
        event.setName(source.name());
        event.setCategory(source.category());
        event.setStatus(source.status());
        return event;
    }
}
