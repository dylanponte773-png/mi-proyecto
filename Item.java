package modelo;

public abstract class Item {
    protected String nombre;
    protected int valor;

    public Item(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return nombre; // Muestra el nombre limpio en el JList del inventario
    }
}