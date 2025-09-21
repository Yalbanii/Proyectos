package com.spring.batch.config;

import com.spring.batch.entity.Pokemon;
import org.springframework.batch.item.ItemProcessor;

public class PokemonProcessor implements ItemProcessor<Pokemon, Pokemon> {

    @Override
    public Pokemon process(Pokemon pokemon) throws Exception {
        if(pokemon.getType1().equals("Grass") &&
           pokemon.getType2().equals("Poison")) {
            return pokemon;
        }else{
            return null;
        }
    }
}
