package constructor;

public class Mesero {

    private String nombre;
    private Orden orden;

    public Mesero(String nombre, Orden orden){
        this.nombre = nombre;
        this.orden = orden;
    }

    public void entregarOrden() {
        System.out.println(nombre);
        orden.entregar();
    }
}
