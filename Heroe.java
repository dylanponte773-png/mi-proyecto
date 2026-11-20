package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Heroe extends Personaje {
    private Inventario inventario;
    protected List<Habilidad> habilidades;
    private Arma armaEquipada;
    private Armadura armaduraEquipada;

    public Heroe(String nombre, Stats stats) {
        super(nombre, stats);
        this.inventario = new Inventario();
        this.habilidades = new ArrayList<>();
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public List<Habilidad> getHabilidades() {
        if (habilidades == null) {
            habilidades = new ArrayList<>();
        }
        return habilidades;
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Armadura getArmaduraEquipada() {
        return armaduraEquipada;
    }

// --- LÓGICA DE EQUIPAR ARMA (Sin borrarla del inventario) ---
    public void equiparArma(Arma nuevaArma) {
        if (this.armaEquipada != null) {
            // Si ya tenía otra, restamos su bono anterior
            this.stats.setFuerza(this.stats.getFuerza() - this.armaEquipada.getBonusFuerza());
        }
        this.armaEquipada = nuevaArma;
        // Sumar el bono de la nueva arma a la fuerza
        this.stats.setFuerza(this.stats.getFuerza() + nuevaArma.getBonusFuerza());
    }

    // --- LÓGICA DE EQUIPAR ARMADURA (Sin borrarla del inventario) ---
    public void equiparArmadura(Armadura nuevaArmadura) {
        if (this.armaduraEquipada != null) {
            // Si ya tenía otra, restamos su bono anterior
            this.stats.setDefensa(this.stats.getDefensa() - this.armaduraEquipada.getBonusDefensa());
        }
        this.armaduraEquipada = nuevaArmadura;
        // Sumar el bono de la nueva armadura a la defensa
        this.stats.setDefensa(this.stats.getDefensa() + nuevaArmadura.getBonusDefensa());
    }

    // --- DESEQUIPAR ARMA ---
    public void desequiparArma() {
        if (this.armaEquipada != null) {
            this.stats.setFuerza(this.stats.getFuerza() - this.armaEquipada.getBonusFuerza());
            this.armaEquipada = null; // Solo se desmarca, el ítem sigue en la mochila
        }
    }

    // --- DESEQUIPAR ARMADURA ---
    public void desequiparArmadura() {
        if (this.armaduraEquipada != null) {
            this.stats.setDefensa(this.stats.getDefensa() - this.armaduraEquipada.getBonusDefensa());
            this.armaduraEquipada = null; // Solo se desmarca, el ítem sigue en la mochila
        }
    }
}