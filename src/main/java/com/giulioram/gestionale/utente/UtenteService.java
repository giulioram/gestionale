package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.event.EventRepository;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class UtenteService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utente findById(Integer utenteId) {
        return this.utenteRepository.findById(utenteId).orElseThrow(() -> new ObjectNotFoundException("Utente", utenteId));
    }

    public List<Utente> findAllUtenti() {
        return this.utenteRepository.findAll();
    }

    public Utente save(Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        return this.utenteRepository.save(utente);
    }

    public Utente update(Integer utenteId, Utente update) {
        return this.utenteRepository.findById(utenteId)
                .map(oldUtente -> {
                    oldUtente.setUserName(update.getUserName());
                    oldUtente.setPassword(update.getPassword());
                    return utenteRepository.save(oldUtente);
                }).orElseThrow(() -> new ObjectNotFoundException("Utente", utenteId));
    }

    public void delete(Integer utenteId) {
        Utente utenteDaRimuovere = this.utenteRepository.findById(utenteId)
                .orElseThrow(() -> new ObjectNotFoundException("Utente", utenteId));
        utenteDaRimuovere.removeAllEvents();
        this.utenteRepository.deleteById(utenteId);
    }

    public void assignEvent(Integer utenteId, String eventId) {
        //Find this Event by Id from DB
        Event eventoToBeAssigned = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("event", eventId));
        //Find this Utente by Id from DB
        Utente utente = this.utenteRepository.findById(utenteId)
                .orElseThrow(() -> new ObjectNotFoundException("utente", utenteId));
        //Event assignment
        if(eventoToBeAssigned.getOwner() != null) {
            eventoToBeAssigned.getOwner().removeEvent(eventoToBeAssigned);
        }
        eventoToBeAssigned.setOwner(utente);
        utente.addEvent(eventoToBeAssigned);
        utenteRepository.save(utente);
        eventRepository.save(eventoToBeAssigned);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utenteRepository.findByUserName(username)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " is not found."));
    }
}
