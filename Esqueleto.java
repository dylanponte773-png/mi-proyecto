package modelo;

public class Esqueleto extends Monstruo {
    private boolean yaRevivio = false;

    public Esqueleto(String nombre, Stats stats, int xpQueDa) {
        super(nombre, stats, xpQueDa);
    }

    @Override
    public int calcularDano() {
        return this.stats.getFuerza();
    }

    // Si es derrotado por primera vez, recupera el 20% de su vida
    public boolean intentarRevivir() {
        if (!yaRevivio) {
            yaRevivio = true;
            this.stats.setVida((int)(this.stats.getVidaMaxima() * 0.20));
            return true;
        }
        return false;
    }
}