package com.mongo.crud.repository;

import com.mongo.crud.model.Jugador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadoresRepository extends MongoRepository<Jugador, String> {

}
