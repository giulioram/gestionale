package com.giulioram.gestionale.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giulioram.gestionale.enums.CategoryEnum;
import com.giulioram.gestionale.enums.StatusEnum;
import com.giulioram.gestionale.event.dto.EventDto;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    List<Event> events;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventController eventController;

    @Mock
    EventService eventService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(eventController, "eventService", eventService);
        Event event1 = new Event("1", LocalDateTime.now(), "uno", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Event event2 = new Event("2", LocalDateTime.now(), "due", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Event event3 = new Event("3", LocalDateTime.now(), "tre", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
    }

    @Test
    void testFindEventByIdSuccess() throws Exception {
        Event event = new Event("1", LocalDateTime.now(), "uno", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        when(eventService.findById("1")).thenReturn(event);

        this.mockMvc.perform(get(this.baseUrl + "/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1"));
    }

    @Test
    void testFindEventByIdNotFound() throws Exception {
        given(eventService.findById("1")).willThrow(new ObjectNotFoundException("Event", "1"));

        this.mockMvc.perform(get(this.baseUrl + "/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Event with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllEventsSuccess() throws Exception {
        given(this.eventService.findAllEvents()).willReturn(this.events);

        this.mockMvc.perform(get(this.baseUrl + "/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.events.size())))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("uno"))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("due"));
    }

    @Test
    void testAddEventSuccess() throws Exception {
        LocalDateTime ldt = LocalDateTime.of(2024, 12, 18, 18, 0);
        EventDto eventDto = new EventDto(null,
                                        ldt,
                                        "test name",
                                        CategoryEnum.EVENTO,
                                        StatusEnum.NEXT,
                                        null);
        String jsonObj = objectMapper.writeValueAsString(eventDto);

        List<String> tags = new ArrayList<>();
        Event e = new Event();
        e.setId("123456");
        e.setCategory(CategoryEnum.EVENTO);
        e.setName("test name");
        e.setStatus(StatusEnum.NEXT);
        e.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));

        given(this.eventService.save(Mockito.any(Event.class))).willReturn(e);

        this.mockMvc.perform(post(this.baseUrl + "/events").contentType(MediaType.APPLICATION_JSON).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(e.getName()));
    }

    @Test
    void testUpdateEventSuccess() throws Exception {
        LocalDateTime ldt = LocalDateTime.of(2024, 12, 18, 18, 0);
        EventDto eventDto = new EventDto("123456",
                ldt,
                "test name",
                CategoryEnum.EVENTO,
                StatusEnum.NEXT,
                null);
        String jsonObj = objectMapper.writeValueAsString(eventDto);

        Event updatedEvent = new Event();
        updatedEvent.setId("123456");
        updatedEvent.setCategory(CategoryEnum.EVENTO);
        updatedEvent.setName("Descrizione aggiornata.");
        updatedEvent.setStatus(StatusEnum.NEXT);
        updatedEvent.setDataEvento(LocalDateTime.of(2024, 12, 18, 18, 0));

        given(this.eventService.update(eq("123456"), Mockito.any(Event.class))).willReturn(updatedEvent);

        this.mockMvc.perform(put(this.baseUrl + "/events/123456").contentType(MediaType.APPLICATION_JSON).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("123456"))
                .andExpect(jsonPath("$.data.name").value(updatedEvent.getName()));
    }

    @Test
    void testUpdateEventErrorWithNonExistentId() throws Exception {
        LocalDateTime ldt = LocalDateTime.of(2024, 12, 18, 18, 0);
        EventDto eventDto = new EventDto("123456",
                ldt,
                "test name",
                CategoryEnum.EVENTO,
                StatusEnum.NEXT,
                null);
        String jsonObj = objectMapper.writeValueAsString(eventDto);

        given(this.eventService.update(eq("123456"), Mockito.any(Event.class))).willThrow(new ObjectNotFoundException("Event", "123456"));

        this.mockMvc.perform(put(this.baseUrl + "/events/123456").contentType(MediaType.APPLICATION_JSON).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Event with Id 123456 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteEventSuccess() throws Exception {
        doNothing().when(eventService).delete("1");

        this.mockMvc.perform(delete(this.baseUrl + "/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteEventErrorWithNonExistentId() throws Exception {
        doThrow(new ObjectNotFoundException("Event", "1")).when(this.eventService).delete("1");

        this.mockMvc.perform(delete(this.baseUrl + "/events/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Event with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

