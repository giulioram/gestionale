package com.giulioram.gestionale.utente;

import com.giulioram.gestionale.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente findById(Integer utenteId) {
        return this.utenteRepository.findById(utenteId).orElseThrow(() -> new ObjectNotFoundException("Utente", utenteId));
    }

    public List<Utente> findAllUtenti() {
        return this.utenteRepository.findAll();
    }

    public Utente save(Utente utente) {
        return this.utenteRepository.save(utente);
    }

    public Utente update(Integer utenteId, Utente update) {
        return this.utenteRepository.findById(utenteId)
                .map(oldUtente -> {
                    oldUtente.setUserName(update.getUserName());
                    oldUtente.setPassword(update.getPassword());
                    return utenteRepository.save(oldUtente);
                }).orElseThrow(() -> new ObjectNotFoundException("Utente", utenteId));
    }

    public void delete(Integer utenteId) {
        Utente utenteDaRimuovere = this.utenteRepository.findById(utenteId)
                .orElseThrow(() -> new ObjectNotFoundException("Utente", utenteId));
        utenteDaRimuovere.removeAllEvents();
        this.utenteRepository.deleteById(utenteId);
    }
}
