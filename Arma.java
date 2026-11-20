package modelo;

public class Arma extends Item implements Equipable {
    private int bonusFuerza;

    public Arma(String nombre, int valor, int bonusFuerza) {
        super(nombre, valor);
        this.bonusFuerza = bonusFuerza;
    }

    public int getBonusFuerza() {
        return bonusFuerza;
    }

    @Override
    public void equipar(Heroe heroe) {
        // Incrementa la fuerza del héroe sumando el bonus del arma
        heroe.getStats().setFuerza(heroe.getStats().getFuerza() + bonusFuerza);
    }

    @Override
    public void desequipar(Heroe heroe) {
        // Resta el bonus al desequipar el arma
        heroe.getStats().setFuerza(heroe.getStats().getFuerza() - bonusFuerza);
    }
}