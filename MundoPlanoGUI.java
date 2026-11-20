package view;

import modelo.*;
import data.GestorArchivo;
import excepcion.DatoInvalidoException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MundoPlanoGUI extends JFrame {
    private Heroe heroe;
    private String dificultad;
    private JLabel lblInfoHeroe;
    private JProgressBar barraExp;
    private JButton btnInventario, btnHabilidades, btnBuscarCombate, btnGuardar, btnSalir;

    private int nivel = 1;
    private int expActual = 0;
    private int expRequerida = 100;
    private int xpUltimoEnemigo = 25;

    public MundoPlanoGUI(Heroe heroe, String dificultad, int nivelCargado, int expCargada) {
        this.heroe = heroe;
        this.dificultad = dificultad;
        this.nivel = nivelCargado;
        this.expActual = expCargada;
        this.expRequerida = 100 + ((nivel - 1) * 50);

        setTitle("Mundo Principal - " + heroe.getNombre());
        setSize(460, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        lblInfoHeroe = new JLabel();
        actualizarInfoLabel();
        lblInfoHeroe.setBounds(30, 15, 380, 110);
        add(lblInfoHeroe);

        JLabel lblExpText = new JLabel("Progreso de Nivel (XP):");
        lblExpText.setBounds(30, 135, 200, 20);
        add(lblExpText);

        barraExp = new JProgressBar(0, expRequerida);
        barraExp.setValue(expActual);
        barraExp.setStringPainted(true);
        barraExp.setBounds(30, 160, 390, 25);
        add(barraExp);

        btnInventario = new JButton("Ver Inventario");
        btnInventario.setBounds(30, 205, 190, 35);
        add(btnInventario);

        btnHabilidades = new JButton("Ver Habilidades");
        btnHabilidades.setBounds(230, 205, 190, 35);
        add(btnHabilidades);

        btnBuscarCombate = new JButton("¡Buscar Combate!");
        btnBuscarCombate.setBounds(30, 255, 390, 35);
        add(btnBuscarCombate);

        btnGuardar = new JButton("Guardar Partida");
        btnGuardar.setBounds(30, 305, 390, 35);
        add(btnGuardar);

        btnSalir = new JButton("Salir al Menú Principal");
        btnSalir.setBounds(30, 355, 390, 35);
        add(btnSalir);

        btnInventario.addActionListener(e -> new InventarioGUI(heroe, this).setVisible(true));
        btnHabilidades.addActionListener(e -> new HabilidadesGUI(heroe).setVisible(true));

        // --- ACCIÓN DE GUARDAR PARTIDA ---
        btnGuardar.addActionListener(e -> {
            try {
                List<String> datos = new ArrayList<>();
                datos.add(heroe.getNombre());                                // Índice 0
                datos.add(heroe.getClass().getSimpleName());                 // Índice 1
                datos.add(String.valueOf(heroe.getStats().getVida()));       // Índice 2: Vida actual
                datos.add(String.valueOf(heroe.getStats().getVidaMaxima())); // Índice 3: Vida máxima
                datos.add(String.valueOf(heroe.getStats().getMana()));       // Índice 4: Maná actual
                
                // Cálculo de la fuerza y defensa base (restando el bono temporal del equipo actual)
                int fuerzaBase = heroe.getStats().getFuerza();
                if (heroe.getArmaEquipada() != null) {
                    fuerzaBase -= heroe.getArmaEquipada().getBonusFuerza();
                }
                int defensaBase = heroe.getStats().getDefensa();
                if (heroe.getArmaduraEquipada() != null) {
                    defensaBase -= heroe.getArmaduraEquipada().getBonusDefensa();
                }

                datos.add(String.valueOf(fuerzaBase));                       // Índice 5: Fuerza Base
                datos.add(String.valueOf(defensaBase));                      // Índice 6: Defensa Base
                datos.add(String.valueOf(heroe.getStats().getInteligencia()));// Índice 7: Inteligencia
                datos.add(dificultad);                                       // Índice 8
                datos.add(String.valueOf(nivel));                            // Índice 9
                datos.add(String.valueOf(expActual));                        // Índice 10

                // Guardar ítems del inventario con su estado de equipado y su bono exacto
                StringBuilder itemsStr = new StringBuilder();
                if (heroe.getInventario() != null && heroe.getInventario().getItems() != null) {
                    for (Item it : heroe.getInventario().getItems()) {
                        String tipo = (it instanceof Arma) ? "Arma" : (it instanceof Armadura) ? "Armadura" : "Pocion";
                        boolean equipado = (it.equals(heroe.getArmaEquipada()) || it.equals(heroe.getArmaduraEquipada()));
                        
                        int bonoExtra = 0;
                        if (it instanceof Arma) {
                            bonoExtra = ((Arma) it).getBonusFuerza();
                        } else if (it instanceof Armadura) {
                            bonoExtra = ((Armadura) it).getBonusDefensa();
                        }

                        // Formato guardado: Tipo:Nombre:Equipado:Bono
                        itemsStr.append(tipo).append(":")
                                .append(it.getNombre()).append(":")
                                .append(equipado).append(":")
                                .append(bonoExtra).append(";");
                    }
                }
                datos.add(itemsStr.toString());                              // Índice 11

                String nombreArchivo = "partida_" + heroe.getNombre().toLowerCase().replaceAll("\\s+", "_") + ".txt";
                GestorArchivo.guardarDatos(nombreArchivo, datos);
                JOptionPane.showMessageDialog(this, "¡Partida guardada con éxito!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBuscarCombate.addActionListener(e -> {
            try {
                Monstruo enemigo = generarMonstruoAleatorioPorDificultad(this.dificultad);
                CombateGUI combate = new CombateGUI(heroe, enemigo, this, xpUltimoEnemigo);
                combate.setVisible(true);
                this.setVisible(false);
            } catch (DatoInvalidoException ex) {
                JOptionPane.showMessageDialog(this, "Error al generar enemigo: " + ex.getMessage());
            }
        });

        btnSalir.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });
    }

    public MundoPlanoGUI(Heroe heroe, String dificultad) {
        this(heroe, dificultad, 1, 0);
    }

    public void ganarExperiencia(int cantidad) {
        if (nivel >= 20) {
            JOptionPane.showMessageDialog(this, "¡Has alcanzado el nivel máximo (20)!");
            return;
        }

        expActual += cantidad;
        if (expActual >= expRequerida) {
            expActual -= expRequerida;
            nivel++;
            expRequerida += 50; 
            subirEstadisticasPorClase();
            JOptionPane.showMessageDialog(this, "¡FELICIDADES! Has subido al Nivel " + nivel);
        }
        barraExp.setMaximum(expRequerida);
        barraExp.setValue(expActual);
        actualizarInfoLabel();
    }

    private void subirEstadisticasPorClase() {
        Stats s = heroe.getStats();
        String clase = heroe.getClass().getSimpleName();

        s.setVidaMaxima(s.getVidaMaxima() + 15);
        s.setVida(s.getVidaMaxima()); 
        s.setDefensa(s.getDefensa() + 5);
        s.setInteligencia(s.getInteligencia() + 5);
        s.setManaMaximo(s.getInteligencia() * 3);
        s.setMana(s.getManaMaximo());

        if (clase.equals("Gladiador") || clase.equals("Golem") || clase.equals("Cazador")) {
            s.setFuerza(s.getFuerza() + 10);
        } else if (clase.equals("Mago") || clase.equals("Arquero")) {
            s.setInteligencia(s.getInteligencia() + 10);
        }
    }

    public void actualizarInfoLabel() {
        Stats s = heroe.getStats();
        lblInfoHeroe.setText("<html><b>Héroe:</b> " + heroe.getNombre() + " | <b>Nivel:</b> " + nivel + 
                              "<br><b>Clase:</b> " + heroe.getClass().getSimpleName() +
                              "<br><b>Vida:</b> " + s.getVida() + "/" + s.getVidaMaxima() +
                              " | <b>Maná:</b> " + s.getMana() + "/" + s.getManaMaximo() +
                              "<br><b>Fuerza:</b> " + s.getFuerza() + 
                              " | <b>Defensa:</b> " + s.getDefensa() +
                              " | <b>Intel:</b> " + s.getInteligencia() +
                              "<br><b>Dificultad:</b> " + dificultad + "</html>");
    }
    
       public int getNivel() {
        return nivel;
    }

    public String getDificultad() {
        return dificultad;
    }

    private Monstruo reforzar(Monstruo m) {
        int n = nivel - 1;

        int vidaPorNivel = 40;
        int fuerzaPorNivel = 6;
        if ("Media".equals(dificultad)) {
            vidaPorNivel = 60;
            fuerzaPorNivel = 9;
        }
        if ("Difícil".equals(dificultad)) {
            vidaPorNivel = 80;
            fuerzaPorNivel = 11;
        }

        Stats s = m.getStats();
        s.setVidaMaxima(s.getVidaMaxima() + n * vidaPorNivel);
        s.setVida(s.getVidaMaxima());
        s.setFuerza(s.getFuerza() + n * fuerzaPorNivel);
        s.setDefensa(s.getDefensa() + n * 2);
        xpUltimoEnemigo += n * 5;
        return m;
    }

    private Monstruo generarMonstruoAleatorioPorDificultad(String dif) throws DatoInvalidoException {
        if (dif == null) dif = "Fácil";
        Random rand = new Random();

        switch (dif.trim()) {
            case "Fácil":
                if (rand.nextBoolean()) {
                    xpUltimoEnemigo = 25;
                    return reforzar(new Goblin("Goblin Salvaje", new Stats(50, 10, 5, 10), 20));
                } else {
                    xpUltimoEnemigo = 30;
                    return reforzar(new Esqueleto("Esqueleto Errante", new Stats(60, 12, 6, 10), 25));
                }
            case "Media":
                if (rand.nextBoolean()) {
                    xpUltimoEnemigo = 50;
                    return reforzar(new Orco("Orco Guerrero", new Stats(80, 18, 10, 12), 40));
                } else {
                    xpUltimoEnemigo = 60;
                    return reforzar(new Gargola("Gárgola de Piedra", new Stats(90, 20, 14, 15), 45));
                }
            case "Difícil":
                if (rand.nextBoolean()) {
                    xpUltimoEnemigo = 80;
                    return reforzar(new Gargola("Gárgola Alfa", new Stats(120, 24, 18, 20), 70));
                } else {
                    xpUltimoEnemigo = 120;
                    return reforzar(new Dragon("Dragón Oscuro", new Stats(150, 28, 20, 25), 100));
                }
            default:
                xpUltimoEnemigo = 20;
                return reforzar(new Goblin("Goblin Menor", new Stats(40, 8, 4, 10), 15));
        }
    }
}