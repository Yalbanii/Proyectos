package com.mongo.crud.service;

import java.util.List;
import java.util.Optional;

import com.mongo.crud.model.Jugador;
import com.mongo.crud.repository.JugadoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JugadorServiceImpl implements JugadoresService {
	
    @Autowired
    private JugadoresRepository jugadoresRepository;

    @Override
    public Jugador save(Jugador jugador) {
        return jugadoresRepository.save(jugador);
    }

    @Override
    public List<Jugador> findAll() {
        return jugadoresRepository.findAll();
    }

    @Override
    public Optional<Jugador> findById(String id) {
        return Optional.ofNullable(jugadoresRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(("El jugador con el id " + id + " no existe."))
        ));
    }

    @Override
    public Jugador update(String id, Jugador jugador) {
        Optional<Jugador> existingJugador = jugadoresRepository.findById(id);
        if (existingJugador.isPresent()) {
            Jugador jugadorToUpdate = existingJugador.get();
            jugadorToUpdate.setNombre(jugador.getNombre());
            jugadorToUpdate.setEdad(jugador.getEdad());
            jugadorToUpdate.setNacionalidad(jugador.getNacionalidad());
            jugadorToUpdate.setClub(jugador.getClub());
            jugadorToUpdate.setGolAnio(jugador.getGolAnio());
            return jugadoresRepository.save(jugadorToUpdate);
        }
        throw new RuntimeException("Player not found with id: " + id);
    }

    @Override
    public Jugador deleteById(String id) {
        Optional<Jugador> optionalJugador = jugadoresRepository.findById(id);
        if(optionalJugador.isEmpty()) throw new IllegalArgumentException("El jugador con el id " + id + " no existe.");
        jugadoresRepository.deleteById(id);
        return optionalJugador.get();
    }

    @Override
    public boolean existsByName(String nombre) {
        return false;
    }
}
