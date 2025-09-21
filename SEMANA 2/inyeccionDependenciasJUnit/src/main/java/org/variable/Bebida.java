package org.variable;

public class Bebida implements Orden {

    private String tamanio;

    public Bebida(String tamanio) {
        this.tamanio = tamanio;
    }

    @Override
    public void entregar() {
        System.out.println("Entrega Bebida:" + tamanio);

    }
}
