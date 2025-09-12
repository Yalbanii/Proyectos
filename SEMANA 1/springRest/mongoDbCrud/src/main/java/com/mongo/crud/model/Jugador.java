package com.mongo.crud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public int getGolAnio() {
        return golAnio;
    }

    public void setGolAnio(int golAnio) {
        this.golAnio = golAnio;
    }
}