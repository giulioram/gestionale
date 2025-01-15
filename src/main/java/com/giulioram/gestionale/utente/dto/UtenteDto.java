package com.giulioram.gestionale.utente.dto;

import jakarta.validation.constraints.NotEmpty;

public record UtenteDto(Integer id,
                        @NotEmpty(message = "userName is required")
                        String userName,
                        boolean enabled,
                        @NotEmpty(message = "roles are required")
                        String roles,
                        Integer numberOfEvents) {
}
