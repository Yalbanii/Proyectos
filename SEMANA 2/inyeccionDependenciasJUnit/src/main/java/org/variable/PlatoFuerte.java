package org.variable;

public class PlatoFuerte implements Orden{

    private String tamanio;

    public PlatoFuerte(String tamanio) {
        this.tamanio = tamanio;
    }

    @Override
    public void entregar() {
        System.out.println("Entrega Plato Fuerte :" + tamanio);

    }
}
