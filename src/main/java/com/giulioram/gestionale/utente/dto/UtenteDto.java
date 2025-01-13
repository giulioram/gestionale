package com.giulioram.gestionale.utente.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UtenteDto(Integer id,
                        @NotEmpty
                        String userName,
                        @NotNull
                        String password,
                        Integer numberOfEvents) {
}
