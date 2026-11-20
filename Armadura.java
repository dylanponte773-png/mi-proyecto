package modelo;

public class Armadura extends Item implements Equipable {
    private int bonusDefensa;

    public Armadura(String nombre, int valor, int bonusDefensa) {
        super(nombre, valor);
        this.bonusDefensa = bonusDefensa;
    }

    public int getBonusDefensa() {
        return bonusDefensa;
    }

    @Override
    public void equipar(Heroe heroe) {
        // Incrementa la defensa del héroe
        heroe.getStats().setDefensa(heroe.getStats().getDefensa() + bonusDefensa);
    }

    @Override
    public void desequipar(Heroe heroe) {
        // Resta la defensa al desequipar
        heroe.getStats().setDefensa(heroe.getStats().getDefensa() - bonusDefensa);
    }
}