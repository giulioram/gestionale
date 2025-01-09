package com.giulioram.gestionale.utente.converter;

import com.giulioram.gestionale.utente.Utente;
import com.giulioram.gestionale.utente.dto.UtenteDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UtenteToUtenteDtoConverter implements Converter<Utente, UtenteDto> {
    @Override
    public UtenteDto convert(Utente source) {
        UtenteDto utenteDto = new UtenteDto(source.getId(),
                source.getUserName(),
                source.getPassword(),
                source.getNumberOfEvets());
        return utenteDto;
    }
}
