package com.giulioram.gestionale.event.converter;

import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.event.dto.EventDto;
import com.giulioram.gestionale.utente.converter.UtenteToUtenteDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventToEventDtoConverter implements Converter<Event, EventDto> {

    private final UtenteToUtenteDtoConverter utenteToUtenteDtoConverter;

    public EventToEventDtoConverter(UtenteToUtenteDtoConverter utenteToUtenteDtoConverter) {
        this.utenteToUtenteDtoConverter = utenteToUtenteDtoConverter;
    }

    @Override
    public EventDto convert(Event source) {
        EventDto eventDto = new EventDto(source.getId(),
                source.getDataEvento(),
                source.getName(),
                source.getCategory(),
                source.getStatus(),
                source.getOwner() != null
                        ? this.utenteToUtenteDtoConverter.convert(source.getOwner())
                        : null);
        return eventDto;
    }
}
