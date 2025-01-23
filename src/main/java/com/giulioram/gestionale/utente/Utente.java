package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.event.Event;
import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Utente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty(message = "userName is required")
    private String userName;
    @NotEmpty(message = "password is required")
    private String password;
    private boolean enabled;
    @NotEmpty(message = "roles are required")
    private String roles;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "owner",
            fetch = FetchType.EAGER)
    private List<Event> eventi = new ArrayList<>();

    public Utente() {}

    public Utente(Integer id, String userName, String password, boolean enabled, String roles) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
