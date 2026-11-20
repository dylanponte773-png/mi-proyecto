package modelo;

public abstract class Personaje {
    protected String nombre;
    protected Stats stats;

    public Personaje(String nombre, Stats stats) {
        this.nombre = nombre;
        this.stats = stats;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }

    public abstract int calcularDano();
}