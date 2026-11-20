package modelo;

public class Dragon extends Monstruo {

    public Dragon(String nombre, Stats stats, int xpQueDa) {
        super(nombre, stats, xpQueDa);
    }

    @Override
    public int calcularDano() {
        return this.stats.getFuerza() + 15;
    }

    // Aplica efecto de quemado que quita daño progresivo
    public int aplicarFuegoDragon() {
        return 8; // Daño por quemadura al inicio del turno del héroe
    }
}