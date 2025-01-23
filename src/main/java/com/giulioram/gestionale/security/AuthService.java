package com.giulioram.gestionale.security;

import com.giulioram.gestionale.utente.MyUserPrincipal;
import com.giulioram.gestionale.utente.Utente;
import com.giulioram.gestionale.utente.converter.UtenteToUtenteDtoConverter;
import com.giulioram.gestionale.utente.dto.UtenteDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UtenteToUtenteDtoConverter utenteToUtenteDtoConverter;

    public AuthService(JwtProvider jwtProvider, UtenteToUtenteDtoConverter utenteToUtenteDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.utenteToUtenteDtoConverter = utenteToUtenteDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();

        Utente utente = userPrincipal.getUtente();
        UtenteDto utenteDto = this.utenteToUtenteDtoConverter.convert(utente);
        Map<String, Object> loginResultMap = new HashMap<>();
        if(!userPrincipal.getUtente().isEnabled()) throw new AccessDeniedException("The user is disabled");
        String token = jwtProvider.createToken(authentication);

        loginResultMap.put("userInfo", utenteDto);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
