package com.giulioram.gestionale.event;

import com.giulioram.gestionale.event.converter.EventDtoToEventConverter;
import com.giulioram.gestionale.event.converter.EventToEventDtoConverter;
import com.giulioram.gestionale.event.dto.EventDto;
import com.giulioram.gestionale.system.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    private final EventToEventDtoConverter eventToEventDtoConverter;

    private final EventDtoToEventConverter eventDtoToEventConverter;

    public EventController(EventService eventService, EventToEventDtoConverter eventToEventDtoConverter, EventDtoToEventConverter eventDtoToEventConverter) {
        this.eventService = eventService;
        this.eventToEventDtoConverter = eventToEventDtoConverter;
        this.eventDtoToEventConverter = eventDtoToEventConverter;
    }

    @GetMapping("/{eventId}")
    public Result findEventById(@PathVariable String eventId) {
        Event evento = this.eventService.findById(eventId);
        EventDto eventDto = this.eventToEventDtoConverter.convert(evento);
        return new Result(true, HttpStatus.OK.value(), "Find One Success", eventDto);
    }

    @GetMapping
    public Result findAllEvents() {
        List<Event> events = eventService.findAllEvents();
        List<EventDto> eventsDto = events
                .stream()
                .map(eventToEventDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, HttpStatus.OK.value(), "Find All Success", eventsDto);
    }

    @PostMapping
    public Result addEvent(@Valid @RequestBody EventDto eventDto) {
        Event event = this.eventDtoToEventConverter.convert(eventDto);
        Event savedEvent = this.eventService.save(event);
        EventDto savedEventDto = this.eventToEventDtoConverter.convert(savedEvent);
        return new Result(true, HttpStatus.OK.value(), "Add Success", savedEventDto);
    }

    @PutMapping("/{eventId}")
    public Result updateEvent(@PathVariable String eventId, @Valid @RequestBody EventDto eventDto) {
        Event update = this.eventDtoToEventConverter.convert(eventDto);
        Event updatedEvent = this.eventService.update(eventId, update);
        EventDto updatedEventDto = this.eventToEventDtoConverter.convert(updatedEvent);
        return new Result(true, HttpStatus.OK.value(), "Update Success", updatedEventDto);
    }

    @DeleteMapping("/{eventId}")
    public Result deleteEvent(@PathVariable String eventId) {
        this.eventService.delete(eventId);
        return new Result(true, HttpStatus.OK.value(), "Delete Success");
    }
}
