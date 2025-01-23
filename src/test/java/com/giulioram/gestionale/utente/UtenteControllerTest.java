package com.giulioram.gestionale.utente;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import com.giulioram.gestionale.utente.dto.UtenteDto;
import org.hamcrest.Matchers;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UtenteControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UtenteController utenteController;

    @Mock
    UtenteService utenteService;

    List<Utente> utenti;

    String token;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(utenteController, "utenteService", utenteService);
        Utente utente1 = new Utente(1, "uno", "", true, "admin");
        Utente utente2 = new Utente(2, "due", "", true, "admin");
        Utente utente3 = new Utente(3, "tre", "", true, "admin");
        utenti = new ArrayList<>();
        utenti.add(utente1);
        utenti.add(utente2);
        utenti.add(utente3);
        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseUrl + "/utenti/login")
                        .with(httpBasic("user1", "test")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    void testFindUtenteByIdSuccess() throws Exception {
        Utente utente1 = new Utente(1, "uno", "", true, "admin");
        when(utenteService.findById(1)).thenReturn(utente1);

        this.mockMvc.perform(get(this.baseUrl + "/utenti/1").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testFindUtenteByIdNotFound() throws Exception {
        given(utenteService.findById(1)).willThrow(new ObjectNotFoundException("Utente", 1));

        this.mockMvc.perform(get(this.baseUrl + "/utenti/1").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Utente with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllUtentiSuccess() throws Exception {
        given(this.utenteService.findAllUtenti()).willReturn(this.utenti);

        this.mockMvc.perform(get(this.baseUrl + "/utenti").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.utenti.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("uno"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].userName").value("due"));
    }

    @Test
    void testAddUtenteSuccess() throws Exception {
        Utente utente = new Utente(123456,
                "username", "ccc",
                true, "admin");

        String jsonObj = objectMapper.writeValueAsString(utente);

        given(this.utenteService.save(Mockito.any(Utente.class))).willReturn(utente);

        this.mockMvc.perform(post(this.baseUrl + "/utenti").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.userName").value(utente.getUserName()));
    }

    @Test
    void testUpdateUtenteSuccess() throws Exception {
        UtenteDto utenteDto = new UtenteDto(123456,
                                    "username",
                                    true,
                "admin", 0);
        String jsonObj = objectMapper.writeValueAsString(utenteDto);

        Utente updatedUtente = new Utente();
        updatedUtente.setId(123456);
        updatedUtente.setUserName("username aggiornata");
        updatedUtente.setPassword("");

        given(this.utenteService.update(eq(123456), Mockito.any(Utente.class))).willReturn(updatedUtente);

        this.mockMvc.perform(put(this.baseUrl + "/utenti/123456").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(123456))
                .andExpect(jsonPath("$.data.userName").value(updatedUtente.getUserName()));
    }

    @Test
    void testUpdateUtenteErrorWithNonExistentId() throws Exception {
        UtenteDto utenteDto = new UtenteDto(123456,
                "username",
                true, "admin", 0);
        String jsonObj = objectMapper.writeValueAsString(utenteDto);

        given(this.utenteService.update(eq(123456), Mockito.any(Utente.class))).willThrow(new ObjectNotFoundException("Utente", 123456));

        this.mockMvc.perform(put(this.baseUrl + "/utenti/123456").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Utente with Id 123456 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUtenteSuccess() throws Exception {
        doNothing().when(utenteService).delete(1);

        this.mockMvc.perform(delete(this.baseUrl + "/utenti/1").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUtenteErrorWithNonExistentId() throws Exception {
        doThrow(new ObjectNotFoundException("Utente", 1)).when(this.utenteService).delete(1);

        this.mockMvc.perform(delete(this.baseUrl + "/utenti/1").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Utente with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignEventSuccess() throws Exception {
        //Given
        doNothing().when(this.utenteService).assignEvent(2, "123456");
        //When & Then
        this.mockMvc.perform(put(this.baseUrl + "/utenti/2/events/123456").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Event Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignEventErrorWithNonExistentUtenteId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("utente", 5)).when(this.utenteService).assignEvent(5, "123456");
        //When & Then
        this.mockMvc.perform(put(this.baseUrl + "/utenti/5/events/123456").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find utente with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignEventErrorWithNonExistentEventId() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("event", "123457")).when(this.utenteService).assignEvent(9, "123457");
        //When & Then
        this.mockMvc.perform(put(this.baseUrl + "/utenti/9/events/123457").accept(MediaType.APPLICATION_JSON).header("Authorization", this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find event with Id 123457 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
