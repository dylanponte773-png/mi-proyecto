package modelo;

public class Pocion extends Item {
    private int curacion;

    public Pocion(String nombre, int valor, int curacion) {
        super(nombre, valor);
        this.curacion = curacion;
    }

    public int getCuracion() {
        return curacion;
    }

    // Método para consumir la poción y curar al héroe respetando su vida máxima
    public void usar(Heroe heroe) {
        Stats stats = heroe.getStats();
        int vidaNueva = stats.getVida() + curacion;
        
        if (vidaNueva > stats.getVidaMaxima()) {
            stats.setVida(stats.getVidaMaxima());
        } else {
            stats.setVida(vidaNueva);
        }
    }
}