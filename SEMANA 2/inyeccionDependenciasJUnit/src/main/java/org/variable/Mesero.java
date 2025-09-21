package org.variable;

import java.util.Objects;

public class Mesero {

    private String nombre;
    private Orden orden;

    public Mesero(Long id, String nombre){
        this.nombre = nombre;
    }

    public void entregarOrden() {
        System.out.println(nombre);
        orden.entregar();
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
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
        Mesero mesero = (Mesero) o;
        return Objects.equals(nombre, mesero.nombre) && Objects.equals(orden, mesero.orden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, orden);
    }
}
