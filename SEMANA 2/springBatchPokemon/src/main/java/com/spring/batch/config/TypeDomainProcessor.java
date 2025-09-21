package com.spring.batch.config;

import com.spring.batch.entity.Pokemon;
import com.spring.batch.entity.PokemonMongo;
import org.springframework.batch.item.ItemProcessor;

public class TypeDomainProcessor implements ItemProcessor<Pokemon, PokemonMongo> {

    @Override
    public PokemonMongo process(Pokemon pokemon) throws Exception {
        PokemonMongo pokemonMongo = new PokemonMongo();

        pokemonMongo.setPokeId(pokemon.getId());
        pokemonMongo.setName(pokemon.getName());
        pokemonMongo.setType1(pokemon.getType1());
        pokemonMongo.setType2(pokemon.getType2());
        pokemonMongo.setTotal(pokemon.getTotal());
        pokemonMongo.setHp(pokemon.getHp());
        pokemonMongo.setAttack(pokemon.getAttack());
        pokemonMongo.setDefense(pokemon.getDefense());
        pokemonMongo.setSpAtk(pokemon.getSpAtk());
        pokemonMongo.setSpDef(pokemon.getSpDef());
        pokemonMongo.setSpeed(pokemon.getSpeed());
        pokemonMongo.setGeneration(pokemon.getGeneration());

        if (pokemon.getType1() != null && pokemon.getType2() != null) {
            String pokeGrassPys = pokemon.getName().toUpperCase();
            String newName = pokeGrassPys;
            pokemonMongo.setName(newName);
        } else {
            pokemonMongo.setName(pokemon.getName());
        }

        return pokemonMongo;
    }
}
