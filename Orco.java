package modelo;

public class Orco extends Monstruo {

    public Orco(String nombre, Stats stats, int xpQueDa) {
        super(nombre, stats, xpQueDa);
    }

    @Override
    public int calcularDano() {
        return this.stats.getFuerza() + 4;
    }

    // Probabilidad de aplicar un mareo que hace perder el turno al héroe (15%)
    public boolean intentarMarear() {
        return Math.random() < 0.15;
    }
}