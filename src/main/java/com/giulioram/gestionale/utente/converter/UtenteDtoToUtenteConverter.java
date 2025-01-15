package com.giulioram.gestionale.utente.converter;

import com.giulioram.gestionale.utente.Utente;
import com.giulioram.gestionale.utente.dto.UtenteDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UtenteDtoToUtenteConverter implements Converter<UtenteDto, Utente> {
    @Override
    public Utente convert(UtenteDto source) {
        Utente utente = new Utente();
        utente.setId(source.id());
        utente.setUserName(source.userName());
        utente.setEnabled(source.enabled());
        utente.setRoles(source.roles());
        return utente;
    }
}
