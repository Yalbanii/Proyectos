package constructor;

public class Inyector {
    static Mesero getMesero() {
       Orden bebida = new Bebida("Grande");
       Orden platoFuerte = new PlatoFuerte("Familiar");
       Orden postre = new Postre("Para Compartir");
       return new Mesero("Juan", bebida);

    }
}
