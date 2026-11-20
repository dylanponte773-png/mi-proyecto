package modelo;

public class Goblin extends Monstruo {

    public Goblin(String nombre, Stats stats, int xpQueDa) {
        super(nombre, stats, xpQueDa);
    }

    @Override
    public int calcularDano() {
        return this.stats.getFuerza();
    }

    // Ataque rápido que golpea dos veces en un solo turno
    public int realizarAtaqueMultiple() {
        return this.stats.getFuerza() * 2;
    }
}