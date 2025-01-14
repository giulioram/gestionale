package com.giulioram.gestionale.utente;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import com.giulioram.gestionale.utente.dto.UtenteDto;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(utenteController, "utenteService", utenteService);
        Utente utente1 = new Utente(1, "uno", "");
        Utente utente2 = new Utente(2, "due", "");
        Utente utente3 = new Utente(3, "tre", "");
        utenti = new ArrayList<>();
        utenti.add(utente1);
        utenti.add(utente2);
        utenti.add(utente3);
    }

    @Test
    void testFindUtenteByIdSuccess() throws Exception {
        Utente utente = new Utente();
        utente.setId(1);
        utente.setUserName("franco");
        utente.setPassword("");
        when(utenteService.findById(1)).thenReturn(utente);

        this.mockMvc.perform(get(this.baseUrl + "/utenti/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testFindUtenteByIdNotFound() throws Exception {
        given(utenteService.findById(1)).willThrow(new ObjectNotFoundException("Utente", 1));

        this.mockMvc.perform(get("/api/v1/utenti/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Utente with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllUtentiSuccess() throws Exception {
        given(this.utenteService.findAllUtenti()).willReturn(this.utenti);

        this.mockMvc.perform(get("/api/v1/utenti").accept(MediaType.APPLICATION_JSON))
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
        UtenteDto utenteDto = new UtenteDto(null,
                "username",
                "", null);
        String jsonObj = objectMapper.writeValueAsString(utenteDto);


        Utente e = new Utente();
        e.setId(123456);
        e.setUserName("test name");
        e.setPassword("");

        given(this.utenteService.save(Mockito.any(Utente.class))).willReturn(e);

        this.mockMvc.perform(post("/api/v1/utenti").contentType(MediaType.APPLICATION_JSON).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.userName").value(e.getUserName()));
    }

    @Test
    void testUpdateUtenteSuccess() throws Exception {
        UtenteDto utenteDto = new UtenteDto(123456,
                                    "username",
                                    "", null);
        String jsonObj = objectMapper.writeValueAsString(utenteDto);

        Utente updatedUtente = new Utente();
        updatedUtente.setId(123456);
        updatedUtente.setUserName("username aggiornata");
        updatedUtente.setPassword("");

        given(this.utenteService.update(eq(123456), Mockito.any(Utente.class))).willReturn(updatedUtente);

        this.mockMvc.perform(put("/api/v1/utenti/123456").contentType(MediaType.APPLICATION_JSON).content(jsonObj).accept(MediaType.APPLICATION_JSON))
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
                "", null);
        String jsonObj = objectMapper.writeValueAsString(utenteDto);

        given(this.utenteService.update(eq(123456), Mockito.any(Utente.class))).willThrow(new ObjectNotFoundException("Utente", 123456));

        this.mockMvc.perform(put("/api/v1/utenti/123456").contentType(MediaType.APPLICATION_JSON).content(jsonObj).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Utente with Id 123456 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUtenteSuccess() throws Exception {
        doNothing().when(utenteService).delete(1);

        this.mockMvc.perform(delete("/api/v1/utenti/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUtenteErrorWithNonExistentId() throws Exception {
        doThrow(new ObjectNotFoundException("Utente", 1)).when(this.utenteService).delete(1);

        this.mockMvc.perform(delete("/api/v1/utenti/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Could not find Utente with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
