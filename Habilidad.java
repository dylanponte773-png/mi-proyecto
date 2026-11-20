package modelo;

public class Habilidad {
    private String nombre;
    private int dañoBase;
    private int costoMana; // Reemplaza los usos por el costo de maná
    private String tipo;   // "Fisico", "Magico", "Estado"

    public Habilidad(String nombre, int dañoBase, int costoMana, String tipo) {
        this.nombre = nombre;
        this.dañoBase = dañoBase;
        this.costoMana = costoMana;
        this.tipo = tipo;
    }

    // Intenta consumir el maná necesario de los stats del ejecutor
    public boolean usar(Stats stats) {
        if (stats.getMana() >= costoMana) {
            stats.setMana(stats.getMana() - costoMana);
            return true; // Éxito: tenía suficiente maná y se descuenta
        }
        return false; // Falla: maná insuficiente
    }

    // Getters y Setters completos
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDañoBase() {
        return dañoBase;
    }

    public void setDañoBase(int dañoBase) {
        this.dañoBase = dañoBase;
    }

    public int getCostoMana() {
        return costoMana;
    }

    public void setCostoMana(int costoMana) {
        this.costoMana = costoMana;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}