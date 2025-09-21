package org.variable;

import java.util.HashMap;

public class MeseroServicio {
    private final HashMap<Long, Mesero> meseros = new HashMap<>();

    public Mesero crearMesero(Long id, String nombre) {
        meseros.put(id, new Mesero(id, nombre));
        return  meseros.get(id);
    }

    public Mesero obtenerMesero(Long id){
        return meseros.get(id);
    }

    public Mesero actualizarMesero(Long id, String nombre){
        return meseros.put(id, new Mesero(id, nombre));
    }

}
