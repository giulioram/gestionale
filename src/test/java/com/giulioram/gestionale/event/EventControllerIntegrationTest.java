package com.giulioram.gestionale.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giulioram.gestionale.enums.CategoryEnum;
import com.giulioram.gestionale.enums.StatusEnum;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Event API endpoints")
@Tag("integration")
@ActiveProfiles(value = "dev")
public class EventControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseUrl + "/utenti/login")
                        .with(httpBasic("user1", "test")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testFindAllEventsSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }

    @Test
    @DisplayName("Check addEvent with valid input (POST)")
    void testAddEventSuccess() throws Exception {
        Event e = new Event();
        e.setName("Rame");
        e.setStatus(StatusEnum.NEXT);
        e.setCategory(CategoryEnum.EVENTO);
        e.setDataEvento(LocalDateTime.of(2025, 12, 25, 0, 0));
        String jsonObj = this.objectMapper.writeValueAsString(e);
        this.mockMvc.perform(post(this.baseUrl + "/events").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Rame"))
                .andExpect(jsonPath("$.data.status").value("NEXT"))
                .andExpect(jsonPath("$.data.category").value("EVENTO"));
        this.mockMvc.perform(get(this.baseUrl + "/events").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(5)));
    }

    @Test
    @DisplayName("Check updateEvent with valid input (PUT)")
    void testUpdateEventSuccess() throws Exception {

    }
}
