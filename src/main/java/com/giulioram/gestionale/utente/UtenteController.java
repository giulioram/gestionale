package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.system.Result;
import com.giulioram.gestionale.utente.converter.UtenteDtoToUtenteConverter;
import com.giulioram.gestionale.utente.converter.UtenteToUtenteDtoConverter;
import com.giulioram.gestionale.utente.dto.UtenteDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/utenti")
public class UtenteController {
    private final UtenteService utenteService;

    private final UtenteToUtenteDtoConverter utenteToUtenteDtoConverter;

    private final UtenteDtoToUtenteConverter utenteDtoToUtenteConverter;

    public UtenteController(UtenteService utenteService, UtenteToUtenteDtoConverter utenteToUtenteDtoConverter, UtenteDtoToUtenteConverter utenteDtoToUtenteConverter) {
        this.utenteService = utenteService;
        this.utenteToUtenteDtoConverter = utenteToUtenteDtoConverter;
        this.utenteDtoToUtenteConverter = utenteDtoToUtenteConverter;
    }

    @GetMapping("/{utenteId}")
    public Result findUtenteById(@PathVariable Integer utenteId) {
        Utente utente = this.utenteService.findById(utenteId);
        UtenteDto utenteDto = this.utenteToUtenteDtoConverter.convert(utente);
        return new Result(true, HttpStatus.OK.value(), "Find One Success", utenteDto);
    }

    @GetMapping
    public Result findAllUtenti() {
        List<Utente> utenti = utenteService.findAllUtenti();
        List<UtenteDto> utentiDto = utenti
                .stream()
                .map(utenteToUtenteDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, HttpStatus.OK.value(), "Find All Success", utentiDto);
    }

    @PostMapping
    public Result addUtente(@Valid @RequestBody Utente utente) {
        Utente savedUtente = this.utenteService.save(utente);
        UtenteDto savedUtenteDto = this.utenteToUtenteDtoConverter.convert(savedUtente);
        return new Result(true, HttpStatus.OK.value(), "Add Success", savedUtenteDto);
    }

    @PutMapping("/{utenteId}")
    public Result updateUtente(@PathVariable Integer utenteId, @Valid @RequestBody UtenteDto utenteDto) {
        Utente update = this.utenteDtoToUtenteConverter.convert(utenteDto);
        Utente updatedUtente = this.utenteService.update(utenteId, update);
        UtenteDto updatedUtenteDto = this.utenteToUtenteDtoConverter.convert(updatedUtente);
        return new Result(true, HttpStatus.OK.value(), "Update Success", updatedUtenteDto);
    }

    @DeleteMapping("/{utenteId}")
    public Result deleteUtente(@PathVariable Integer utenteId) {
        this.utenteService.delete(utenteId);
        return new Result(true, HttpStatus.OK.value(), "Delete Success");
    }

    @PutMapping("/{utenteId}/events/{eventId}")
    public Result assignEvent(@PathVariable Integer utenteId, @PathVariable String eventId) {
        this.utenteService.assignEvent(utenteId, eventId);
        return new Result(true, HttpStatus.OK.value(), "Event Assignment Success");
    }
}
