package controller;

import modelo.Heroe;
import modelo.Habilidad;
import modelo.Monstruo;

public class CombateController {
    private Heroe heroe;
    private Monstruo monstruo;
    private boolean turnoHeroe;

    public CombateController(Heroe heroe, Monstruo monstruo) {
        this.heroe = heroe;
        this.monstruo = monstruo;
        this.turnoHeroe = true; // Empieza el héroe
    }

    public boolean isTurnoHeroe() {
        return turnoHeroe;
    }

    // Ejecuta la habilidad del héroe y cambia el turno al monstruo
    public String ejecutarHabilidadHeroe(Habilidad habilidad) {
        if (!turnoHeroe) return "No es tu turno.";

        int dañoExtra = habilidad.getTipo().equals("Magico") ? (heroe.getStats().getInteligencia() / 2) : (heroe.getStats().getFuerza() / 2);
        int dañoTotal = habilidad.getDañoBase() + dañoExtra;

        int vidaActualMonstruo = Math.max(0, monstruo.getStats().getVida() - dañoTotal);
        monstruo.getStats().setVida(vidaActualMonstruo);

        // Cambiamos el turno al monstruo
        turnoHeroe = false; 

        return heroe.getNombre() + " usó [ " + habilidad.getNombre() + " ] e infligió " + dañoTotal + " de daño a " + monstruo.getNombre() + ".";
    }

    // Ejecuta el contraataque del monstruo y regresa el turno al héroe
    public String ejecutarAtaqueMonstruo() {
        if (turnoHeroe) return "No es el turno del monstruo.";

        int dañoBruto = monstruo.getStats().getFuerza();
        int defensa = heroe.getStats().getDefensa();
        int dañoFinal = Math.max(5, dañoBruto - (defensa / 2));

        int nuevaVidaHeroe = Math.max(0, heroe.getStats().getVida() - dañoFinal);
        heroe.getStats().setVida(nuevaVidaHeroe);

        // Regresamos el turno al héroe
        turnoHeroe = true; 

        return monstruo.getNombre() + " contraataca e inflige " + dañoFinal + " de daño a " + heroe.getNombre() + ".";
    }

    public boolean verificarFinCombate() {
        return heroe.getStats().getVida() <= 0 || monstruo.getStats().getVida() <= 0;
    }

    public String obtenerResultado() {
        if (heroe.getStats().getVida() <= 0) {
            return "¡Has sido derrotado por " + monstruo.getNombre() + "...!";
        } else if (monstruo.getStats().getVida() <= 0) {
            return "¡Has derrotado a " + monstruo.getNombre() + " con éxito!";
        }
        return "El combate continúa.";
    }
}