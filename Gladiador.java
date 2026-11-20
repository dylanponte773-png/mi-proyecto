package modelo;

public class Gladiador extends Heroe {

    public Gladiador(String nombre, Stats stats) {
        super(nombre, stats);
        // Inicialización de habilidades con costo de maná
        this.habilidades.add(new Habilidad("Golpe Feroz", 30, 8, "Fisico"));
        this.habilidades.add(new Habilidad("Embestida", 45, 15, "Fisico"));
    }

    @Override
    public int calcularDano() {
        // El gladiador basa su daño principal en la fuerza
        return getStats().getFuerza() * 2;
    }
}