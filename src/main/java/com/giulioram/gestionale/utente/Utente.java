package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Utente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String userName;
    private String password;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Event> eventi = new ArrayList<>();

    public Utente() {}

    public Utente(Integer id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public void addEvent(Event evento) {
        evento.setOwner(this);
        this.eventi.add(evento);
    }

    public Event removeEvent(Event eventoToBeRemoved) {
        if(eventi.contains(eventoToBeRemoved)) {
            eventoToBeRemoved.setOwner(null);
            eventi.remove(eventoToBeRemoved);
        }
        return eventoToBeRemoved;
    }

    public void removeAllEvents() {
        eventi.stream().forEach(evento -> evento.setOwner(null));
        this.eventi.clear();
    }

    public void setEventi(List<Event> eventi) {
        this.eventi = eventi;
    }

    public List<Event> getEventi() {
        return eventi;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Integer getNumberOfEvents() {
        return this.eventi.size();
    }
}
