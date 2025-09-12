package variable;

public class Mesero {

    private String nombre;
    private Orden orden;

    public Mesero(String nombre){
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
}
