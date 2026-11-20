package modelo;

public class Arquero extends Heroe {

    public Arquero(String nombre, Stats stats) {
        super(nombre, stats);
        // Inicialización de habilidades del arquero
        this.habilidades.add(new Habilidad("Disparo Preciso", 25, 10, "Fisico"));
        this.habilidades.add(new Habilidad("Lluvia de Flechas", 40, 20, "Fisico"));
    }

    @Override
    public int calcularDano() {
        // El arquero combina fuerza e inteligencia para la precisión de sus disparos
        return getStats().getFuerza() + getStats().getInteligencia();
    }
}