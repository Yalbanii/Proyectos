package org.variable;

public class Inyector {
    public static void inyectarOrden(Mesero mes) {
        mes.setOrden(new Bebida("Grande"));

    }
}
