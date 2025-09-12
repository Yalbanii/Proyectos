package constructor;

public class Postre implements Orden {

    private String tamanio;

    public Postre(String tamanio) {
        this.tamanio = tamanio;
    }

    @Override
    public void entregar() {
        System.out.println("Postre:" + tamanio);

    }
}
