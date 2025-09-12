package constructor;

public class Main {
    public static void main(String[] args) {

      Mesero mesero = Inyector.getMesero();

        mesero.entregarOrden();

    }
}