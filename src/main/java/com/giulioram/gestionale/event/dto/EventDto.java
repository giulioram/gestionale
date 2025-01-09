package com.giulioram.gestionale.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.giulioram.gestionale.enums.CategoryEnum;
import com.giulioram.gestionale.enums.StatusEnum;
import com.giulioram.gestionale.utente.dto.UtenteDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(String id,
                       @NotNull(message = "dataEvento is required")
                       @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                       LocalDateTime dataEvento,
                       @NotEmpty(message = "name is required")
                       String name,
                       @NotNull(message = "category is required")
                       CategoryEnum category,
                       StatusEnum status,
                       UtenteDto owner) {
}
