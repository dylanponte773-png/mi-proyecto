package modelo;

public class Golem extends Heroe {

    public Golem(String nombre, Stats stats) {
        super(nombre, stats);
        // Inicialización de habilidades del golem
        this.habilidades.add(new Habilidad("Puño de Roca", 35, 10, "Fisico"));
        this.habilidades.add(new Habilidad("Terremoto", 55, 20, "Magico"));
    }

    @Override
    public int calcularDano() {
        // El golem hace daño masivo basado en su fuerza bruta
        return getStats().getFuerza() * 2;
    }
}