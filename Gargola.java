package modelo;

public class Gargola extends Monstruo {

    public Gargola(String nombre, Stats stats, int xpQueDa) {
        super(nombre, stats, xpQueDa);
    }

    @Override
    public int calcularDano() {
        return this.stats.getFuerza() + 6;
    }

    // Probabilidad de que los ataques cuerpo a cuerpo fallen debido a que vuela
    public boolean evitarAtaqueFisico() {
        return Math.random() < 0.25;
    }
}