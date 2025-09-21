package com.mongo.crud.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Objects;
// Este es un ranking de los máximos goleadores en un año natural en las ligas top en 2024
@Getter
@Setter
@Document(collection = "jugadores")
public class Jugador {

    @Id
    private String  id;

    private String nombre;

    private int edad;

    @Indexed(unique = true)
    private String nacionalidad;

    private String club;

    private int golAnio;


    public Jugador(String id, String nombre, int edad, String nacionalidad, String club, int golAnio) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.club = club;
        this.golAnio = golAnio;
    }

    public Jugador() {

    }

    public String getId() {
        return id;
    }


    public Object checkNull(Object obj) {
        if (obj != null) {
            return obj;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return edad == jugador.edad && golAnio == jugador.golAnio && Objects.equals(id, jugador.id) && Objects.equals(nombre, jugador.nombre) && Objects.equals(nacionalidad, jugador.nacionalidad) && Objects.equals(club, jugador.club);
    }

}