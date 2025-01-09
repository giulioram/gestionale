package com.giulioram.gestionale.event;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String id) {
        super("Could not find Event with id " + id + " :(");
    }
}
