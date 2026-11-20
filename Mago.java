package modelo;

public class Mago extends Heroe {

    public Mago(String nombre, Stats stats) {
        super(nombre, stats);
        // Añadimos las habilidades iniciales con costo de maná
        this.habilidades.add(new Habilidad("Bola de Fuego", 40, 15, "Magico"));
        this.habilidades.add(new Habilidad("Rayo Arcano", 60, 25, "Magico"));
    }

    @Override
    public int calcularDano() {
        // Utiliza la inteligencia desde los getters protegidos de Personaje/Stats
        return getStats().getInteligencia() * 2+getStats().getFuerza();
    }
}