package com.spring.batch.repository;

import com.spring.batch.entity.PokemonMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PokemonMongoRepository extends MongoRepository<PokemonMongo, String> {
}