package modelo;

public class Stats {
    private int vida;
    private int vidaMaxima;
    private int mana;         // NUEVO: Maná actual
    private int manaMaximo;   // NUEVO: Maná máximo
    private int fuerza;
    private int defensa;
    private int inteligencia;

    public Stats(int vidaMaxima, int fuerza, int defensa, int inteligencia) {
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima; 
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.inteligencia = inteligencia;
        
        // El maná máximo escala con la inteligencia (Mínimo 50)
        this.manaMaximo = Math.max(50, inteligencia * 3);
        this.mana = this.manaMaximo; // Al iniciar, el maná está lleno
    }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public int getVidaMaxima() { return vidaMaxima; }
    public void setVidaMaxima(int vidaMaxima) { this.vidaMaxima = vidaMaxima; }

    // NUEVOS GETTERS Y SETTERS PARA EL MANÁ
    public int getMana() { return mana; }
    public void setMana(int mana) { 
        if (mana > manaMaximo) this.mana = manaMaximo;
        else if (mana < 0) this.mana = 0;
        else this.mana = mana;
    }

    public int getManaMaximo() { return manaMaximo; }
    public void setManaMaximo(int manaMaximo) { this.manaMaximo = manaMaximo; }

    public int getFuerza() { return fuerza; }
    public void setFuerza(int fuerza) { this.fuerza = fuerza; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }
}