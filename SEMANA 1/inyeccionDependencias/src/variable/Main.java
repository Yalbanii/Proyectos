package variable;

public class Main {
    public static void main(String[] args) {

        Mesero mesero = new Mesero("Juan");

        Inyector.inyectarOrden(mesero);

        mesero.entregarOrden();

    }
}