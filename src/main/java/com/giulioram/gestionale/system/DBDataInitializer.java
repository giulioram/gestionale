package com.giulioram.gestionale.system;

import com.giulioram.gestionale.enums.CategoryEnum;
import com.giulioram.gestionale.enums.StatusEnum;
import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.event.EventRepository;
import com.giulioram.gestionale.utente.Utente;
import com.giulioram.gestionale.utente.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DBDataInitializer implements CommandLineRunner {


    private final EventRepository eventRepository;

    private final UtenteRepository utenteRepository;

    public DBDataInitializer(EventRepository eventRepository, UtenteRepository utenteRepository) {
        this.eventRepository = eventRepository;
        this.utenteRepository = utenteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Event event1 = new Event("1", LocalDateTime.now(), "uno", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Event event2 = new Event("2", LocalDateTime.now(), "due", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Event event3 = new Event("3", LocalDateTime.now(), "tre", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Event event4 = new Event("4", LocalDateTime.now(), "quattro", CategoryEnum.EVENTO, StatusEnum.NEXT, null);
        Utente u1 = new Utente();
        //u1.setId(1);
        u1.setUserName("user1");
        u1.setPassword("test");
        u1.addEvent(event1);
        Utente u2 = new Utente();
        //u2.setId(2);
        u2.setUserName("user2");
        u2.setPassword("test");
        u2.addEvent(event2);
        Utente u3 = new Utente();
        //u3.setId(3);
        u3.setUserName("user3");
        u3.setPassword("test");
        u3.addEvent(event3);

        utenteRepository.save(u1);
        utenteRepository.save(u2);
        utenteRepository.save(u3);

        eventRepository.save(event4);
    }
}
