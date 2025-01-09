package com.giulioram.gestionale.utente.dto;

public record UtenteDto(Integer id,
                        String userName,
                        String password,
                        Integer numberOfEvents) {
}
