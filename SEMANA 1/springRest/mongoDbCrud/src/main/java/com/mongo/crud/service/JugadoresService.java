package com.mongo.crud.service;

import java.util.List;
import java.util.Optional;

import com.mongo.crud.model.Jugador;

public interface JugadoresService {
	
    Jugador save(Jugador jugador);
    
    List<Jugador> findAll();
    
    Optional<Jugador> findById(String id);
           
    Jugador update(String id, Jugador jugador);
    
    Jugador deleteById(String id);

    boolean existsByName(String nombre);
}
