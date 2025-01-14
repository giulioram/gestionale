package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtenteServiceTest {

    @Mock
    UtenteRepository utenteRepository;

    @InjectMocks
    UtenteService utenteService;

    List<Utente> utenti;

    @BeforeEach
    void setUp() {
        Utente utente1 = new Utente(1, "franco", "");
        Utente utente2 = new Utente(2, "gigi", "");
        utenti = List.of(utente1, utente2);
    }

    @Test
    void testFindByIdSuccess() {
//given
        Utente u = new Utente();
        u.setId(1);
        u.setUserName("franco");
        u.setPassword("");
        given(utenteRepository.findById(1)).willReturn(Optional.of(u));
        Utente returnedUtente = utenteService.findById(1);
        assertThat(returnedUtente.getId()).isEqualTo(u.getId());
        assertThat(returnedUtente.getUserName()).isEqualTo(u.getUserName());
        verify(utenteRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(utenteRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() ->{
            Utente returnedUtente = utenteService.findById(1);
        });
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Utente with Id 1 :(");
    }

    @Test
    void testFindAllUtentiSuccess() {
        given(utenteRepository.findAll()).willReturn(this.utenti);
        List<Utente> actualUtenti = utenteService.findAllUtenti();
        assertThat(actualUtenti.size()).isEqualTo(utenti.size());
        verify(utenteRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        Utente e = new Utente();
        e.setUserName("franco");
        e.setPassword("");
        given(utenteRepository.save(e)).willReturn(e);

        Utente savedUtente = utenteService.save(e);

        //assertThat(savedUtente.getId()).isEqualTo(123456);
        assertThat(savedUtente.getUserName()).isEqualTo("franco");

        verify(utenteRepository, times(1)).save(e);
    }

    @Test
    void testUpdateSuccess() {
        Utente oldUtente = new Utente();
        oldUtente.setId(1);
        oldUtente.setUserName("franco");
        oldUtente.setPassword("");

        Utente updateUtente = new Utente();
        updateUtente.setId(1);
        updateUtente.setUserName("ciaofranco");
        updateUtente.setPassword("");

        given(utenteRepository.findById(1)).willReturn(Optional.of(oldUtente));
        given(utenteRepository.save(oldUtente)).willReturn(oldUtente);

        Utente updatedUtente = utenteService.update(1, updateUtente);

        assertThat(updatedUtente.getId()).isEqualTo(updateUtente.getId());
        assertThat(updatedUtente.getUserName()).isEqualTo(updateUtente.getUserName());

        verify(utenteRepository, times(1)).findById(1);
        verify(utenteRepository, times(1)).save(oldUtente);
    }

    @Test
    void testUpdateNotFound() {
        Utente updateUtente = new Utente();
        updateUtente.setUserName("unUsername");
        updateUtente.setPassword("");
        given(utenteRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            utenteService.update(1, updateUtente);
        });

        verify(utenteRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        Utente utente = new Utente();
        utente.setId(1);
        utente.setUserName("unUsername");
        utente.setPassword("");

        given(utenteRepository.findById(1)).willReturn(Optional.of(utente));
        doNothing().when(utenteRepository).deleteById(1);

        utenteService.delete(1);

        verify(utenteRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        given(utenteRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            utenteService.delete(1);
        });

        verify(utenteRepository, times(1)).findById(1);
    }
}
