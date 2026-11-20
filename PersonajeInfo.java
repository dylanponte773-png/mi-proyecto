package view;

import modelo.*;

/**
 * Concentra en un solo lugar los datos iniciales de cada clase jugable
 * (stats, equipo y pociones), para que la pantalla de detalle muestre
 * exactamente lo mismo que recibirá el héroe al comenzar la partida.
 */
public class PersonajeInfo {

    public static final String[] CLASES = {"Gladiador", "Mago", "Golem", "Arquero", "Cazador"};

    public static String descripcion(String clase) {
        switch (clase) {
            case "Gladiador": return "Guerrero equilibrado que domina el combate cuerpo a cuerpo.";
            case "Mago":      return "Frágil pero devastador: su poder viene de la inteligencia.";
            case "Golem":     return "Coloso de piedra con muchísima vida y defensa.";
            case "Arquero":   return "Atacante a distancia que combina fuerza y puntería.";
            case "Cazador":   return "Rastreador astuto con trampas y ataques certeros.";
            default:          return "";
        }
    }

    private static Stats statsDe(String clase) {
        switch (clase) {
            case "Mago":  return new Stats(80, 10, 10, 30);
            case "Golem": return new Stats(150, 15, 30, 5);
            default:      return new Stats(100, 20, 15, 10); // Gladiador, Arquero, Cazador
        }
    }

    private static Item equipoInicial(String clase) {
        switch (clase) {
            case "Gladiador": return new Arma("Espada de Acero", 50, 10);
            case "Mago":      return new Armadura("Túnica Mágica", 40, 5);
            case "Golem":     return new Armadura("Placas de Piedra", 60, 20);
            case "Arquero":   return new Arma("Arco Corto", 45, 8);
            case "Cazador":   return new Arma("Dagas Dobles", 50, 9);
            default:          return null;
        }
    }

    /** Crea el héroe tal y como empieza la aventura (con equipo y pociones). */
    public static Heroe crearHeroe(String clase, String nombre) {
        Stats stats = statsDe(clase);
        Heroe h;
        switch (clase) {
            case "Mago":    h = new Mago(nombre, stats); break;
            case "Golem":   h = new Golem(nombre, stats); break;
            case "Arquero": h = new Arquero(nombre, stats); break;
            case "Cazador": h = new Cazador(nombre, stats); break;
            default:        h = new Gladiador(nombre, stats); break;
        }
        h.getInventario().agregarItem(equipoInicial(clase));
        h.getInventario().agregarItem(new Pocion("Poción de Salud Menor", 15, 30));
        h.getInventario().agregarItem(new Pocion("Poción de Salud Menor", 15, 30));
        h.getInventario().agregarItem(new Pocion("Poción de Maná", 15, 30));
        h.getInventario().agregarItem(new Pocion("Poción de Maná", 15, 30));
        return h;
    }

    /** Texto descriptivo del equipo inicial (para mostrarlo antes de elegir). */
    public static String[] resumenEquipo(String clase) {
        Item eq = equipoInicial(clase);
        String principal = "";
        if (eq instanceof Arma) {
            principal = eq.getNombre() + " (+" + ((Arma) eq).getBonusFuerza() + " Fuerza)";
        } else if (eq instanceof Armadura) {
            principal = eq.getNombre() + " (+" + ((Armadura) eq).getBonusDefensa() + " Defensa)";
        }
        return new String[]{principal, "2x Poción de Salud Menor", "2x Poción de Maná"};
    }
}
