package modelo;

public class Cazador extends Heroe {

    public Cazador(String nombre, Stats stats) {
        super(nombre, stats);
        // Inicialización de habilidades del cazador
        this.habilidades.add(new Habilidad("Tiro Certero", 35, 12, "Fisico"));
        this.habilidades.add(new Habilidad("Trampa Explosiva", 50, 22, "Magico"));
    }

    @Override
    public int calcularDano() {
        // El cazador aprovecha tanto el daño físico como el rastreo
        return getStats().getFuerza() + getStats().getInteligencia();
    }
}