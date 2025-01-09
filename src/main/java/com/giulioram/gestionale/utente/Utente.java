package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.event.EventNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Utente implements Serializable {
    @Id
    private Integer id;
    private String userName;
    private String password;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Event> eventi = new ArrayList<>();

    public Utente() {}

    public void addEvent(Event evento) {
        evento.setOwner(this);
        this.eventi.add(evento);
    }

    public Event removeEvent(String id) {
        Event eventToRemove = eventi.stream()
                .filter(event -> id.equals(event.getId()))
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException(id));
        eventi.remove(eventToRemove);
        return eventToRemove;
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

    public Integer getNumberOfEvets() {
        return this.eventi.size();
    }
}
