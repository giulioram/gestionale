package com.giulioram.gestionale.event;

import com.giulioram.gestionale.enums.CategoryEnum;
import com.giulioram.gestionale.enums.StatusEnum;
import com.giulioram.gestionale.utente.Utente;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Event implements Serializable {

    @Id
    private String id;
    private LocalDateTime dataEvento;
    private String name;
    private CategoryEnum category;
    private StatusEnum status;
    @ManyToOne
    private Utente owner;

    public Event() {}

    public Event(String id, LocalDateTime dataEvento, String name, CategoryEnum category, StatusEnum status, Utente owner) {
        this.id = id;
        this.dataEvento = dataEvento;
        this.name = name;
        this.category = category;
        this.status = status;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", dataEvento=" + dataEvento +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", owner=" + owner +
                '}';
    }

    public Utente getOwner() {
        return owner;
    }

    public void setOwner(Utente owner) {
        this.owner = owner;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDataEvento(LocalDateTime dataEvento) {
        this.dataEvento = dataEvento;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDataEvento() {
        return dataEvento;
    }

    public String getName() {
        return name;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public StatusEnum getStatus() {
        return status;
    }
}
