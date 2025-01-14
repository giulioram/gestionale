package com.giulioram.gestionale.event;

import com.giulioram.gestionale.enums.CategoryEnum;
import com.giulioram.gestionale.enums.StatusEnum;
import com.giulioram.gestionale.event.utils.Snowflake;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventRepository eventRepository;

    @Mock
    Snowflake snowflake;

    @InjectMocks
    EventService eventService;

    List<Event> events;

    @BeforeEach
    void setUp() {
        Event event1 = new Event("1", LocalDateTime.now(), "uno", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Event event2 = new Event("2", LocalDateTime.now(), "due", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //given
        Event e = new Event();
        e.setId("1");
        e.setCategory(CategoryEnum.EVENTO);
        e.setName("descrizione evento");
        e.setStatus(StatusEnum.NEXT);
        e.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));
        given(eventRepository.findById("1")).willReturn(Optional.of(e));
        Event returnedEvent = eventService.findById("1");
        assertThat(returnedEvent.getId()).isEqualTo(e.getId());
        assertThat(returnedEvent.getName()).isEqualTo(e.getName());
        assertThat(returnedEvent.getCategory()).isEqualTo(e.getCategory());
        assertThat(returnedEvent.getStatus()).isEqualTo(e.getStatus());
        verify(eventRepository, times(1)).findById("1");
    }

    @Test
    void testFindByIdNotFound() {
        given(eventRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() ->{
            Event returnedEvent = eventService.findById("1");
        });
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Event with Id 1 :(");
    }

    @Test
    void testFindAllEventsSuccess() {
        given(eventRepository.findAll()).willReturn(this.events);
        List<Event> actualEvents = eventService.findAllEvents();
        assertThat(actualEvents.size()).isEqualTo(this.events.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        Event e = new Event();
        e.setCategory(CategoryEnum.EVENTO);
        e.setName("descrizione evento x");
        e.setStatus(StatusEnum.NEXT);
        e.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));
        given(snowflake.nextId()).willReturn(123456L);
        given(eventRepository.save(e)).willReturn(e);

        Event savedEvent = eventService.save(e);

        assertThat(savedEvent.getId()).isEqualTo("123456");
        assertThat(savedEvent.getName()).isEqualTo("descrizione evento x");

        verify(eventRepository, times(1)).save(e);
    }

    @Test
    void testUpdateSuccess() {
        Event oldEvent = new Event();
        oldEvent.setId("1");
        oldEvent.setCategory(CategoryEnum.EVENTO);
        oldEvent.setName("descrizione evento x");
        oldEvent.setStatus(StatusEnum.NEXT);
        oldEvent.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));

        Event updateEvent = new Event();
        updateEvent.setId("1");
        updateEvent.setCategory(CategoryEnum.EVENTO);
        updateEvent.setName("Nuova descrizione");
        updateEvent.setStatus(StatusEnum.NEXT);
        updateEvent.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));

        given(eventRepository.findById("1")).willReturn(Optional.of(oldEvent));
        given(eventRepository.save(oldEvent)).willReturn(oldEvent);

        Event updatedEvent = eventService.update("1", updateEvent);

        assertThat(updatedEvent.getId()).isEqualTo(updateEvent.getId());
        assertThat(updatedEvent.getName()).isEqualTo(updateEvent.getName());

        verify(eventRepository, times(1)).findById("1");
        verify(eventRepository, times(1)).save(oldEvent);
    }

    @Test
    void testUpdateNotFound() {
        Event updateEvent = new Event();
        updateEvent.setCategory(CategoryEnum.EVENTO);
        updateEvent.setName("Nuova descrizione");
        updateEvent.setStatus(StatusEnum.NEXT);
        updateEvent.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));

        given(eventRepository.findById("1")).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.update("1", updateEvent);
        });

        verify(eventRepository, times(1)).findById("1");
    }

    @Test
    void testDeleteSuccess() {
        Event event = new Event();
        event.setId("1");
        event.setCategory(CategoryEnum.EVENTO);
        event.setName("Una descrizione");
        event.setStatus(StatusEnum.NEXT);
        event.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));

        given(eventRepository.findById("1")).willReturn(Optional.of(event));
        doNothing().when(eventRepository).deleteById("1");

        eventService.delete("1");

        verify(eventRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteNotFound() {
        given(eventRepository.findById("1")).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            eventService.delete("1");
        });

        verify(eventRepository, times(1)).findById("1");
    }
}