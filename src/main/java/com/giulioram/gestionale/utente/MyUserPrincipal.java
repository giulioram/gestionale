package com.giulioram.gestionale.utente;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {

    private final Utente utente;

    public MyUserPrincipal(Utente utente) {
        this.utente = utente;
    }

    public Utente getUtente() {
        return utente;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(this.utente.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
    }

    @Override
    public String getPassword() {
        return this.utente.getPassword();
    }

    @Override
    public String getUsername() {
        return this.utente.getUserName();
    }
}
