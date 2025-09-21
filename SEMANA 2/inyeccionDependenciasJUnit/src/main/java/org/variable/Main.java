package org.variable;

public class Main {
    public static void main(String[] args) {

        Mesero mesero = new Mesero(1L, "Juan");

        Inyector.inyectarOrden(mesero);

        mesero.entregarOrden();

    }
}