package modelo;

public abstract class Monstruo extends Personaje {
    protected int xpQueDa;

    public Monstruo(String nombre, Stats stats, int xpQueDa) {
        super(nombre, stats);
        this.xpQueDa = xpQueDa;
    }

    public int getXpQueDa() {
        return xpQueDa;
    }

    public void setXpQueDa(int xpQueDa) {
        this.xpQueDa = xpQueDa;
    }
}