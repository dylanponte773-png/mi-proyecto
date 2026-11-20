package view;

import modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CombateGUI extends JFrame {
    private Heroe heroe;
    private Monstruo enemigo;
    private JFrame ventanaPadre;
    private int xpRecompensa;

    // Componentes visuales de barras y etiquetas
    private JLabel lblNombreHeroe, lblNombreEnemigo;
    private JProgressBar barraVidaHeroe, barraManaHeroe;
    private JProgressBar barraVidaEnemigo;
    private JTextArea txtLogCombate;
    private JButton btnAtacar, btnHabilidad, btnInventario, btnHuir;

    public CombateGUI(Heroe heroe, Monstruo enemigo, JFrame ventanaPadre, int xpRecompensa) {
        this.heroe = heroe;
        this.enemigo = enemigo;
        this.ventanaPadre = ventanaPadre;
        this.xpRecompensa = xpRecompensa;

        setTitle("Combate: " + heroe.getNombre() + " vs " + enemigo.getNombre());
        setSize(540, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);

        // --- PANEL / ETIQUETAS DE NOMBRES ---
        lblNombreHeroe = new JLabel("[ Héroe: " + heroe.getNombre().toUpperCase() + " ]", SwingConstants.CENTER);
        lblNombreHeroe.setBounds(20, 15, 230, 25);
        lblNombreHeroe.setOpaque(true);
        lblNombreHeroe.setBackground(new Color(210, 210, 210));
        lblNombreHeroe.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblNombreHeroe);

        lblNombreEnemigo = new JLabel("[ " + enemigo.getNombre() + " ]", SwingConstants.CENTER);
        lblNombreEnemigo.setBounds(270, 15, 230, 25);
        lblNombreEnemigo.setOpaque(true);
        lblNombreEnemigo.setBackground(new Color(255, 180, 180));
        lblNombreEnemigo.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblNombreEnemigo);

        // --- BARRAS DE PROGRESO DEL HÉROE (VIDA Y MANÁ) ---
        JLabel lblVHp = new JLabel("HP Héroe:");
        lblVHp.setBounds(20, 48, 70, 20);
        add(lblVHp);

        barraVidaHeroe = new JProgressBar();
        barraVidaHeroe.setBounds(95, 48, 155, 20);
        barraVidaHeroe.setStringPainted(true);
        add(barraVidaHeroe);

        JLabel lblVMana = new JLabel("Maná:");
        lblVMana.setBounds(20, 75, 70, 20);
        add(lblVMana);

        barraManaHeroe = new JProgressBar();
        barraManaHeroe.setBounds(95, 75, 155, 20);
        barraManaHeroe.setStringPainted(true);
        add(barraManaHeroe);

        // --- BARRAS DE PROGRESO DEL ENEMIGO ---
        JLabel lblEHp = new JLabel("HP Enemigo:");
        lblEHp.setBounds(270, 48, 80, 20);
        add(lblEHp);

        barraVidaEnemigo = new JProgressBar();
        barraVidaEnemigo.setBounds(355, 48, 145, 20);
        barraVidaEnemigo.setStringPainted(true);
        add(barraVidaEnemigo);

        // Inicializar valores de las barras
        actualizarBarrasEstiloVisual();

        // --- LOG DE COMBATE ---
        txtLogCombate = new JTextArea();
        txtLogCombate.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtLogCombate);
        scrollPane.setBounds(20, 110, 480, 200);
        add(scrollPane);

        // --- LAS 4 OPCIONES DE COMBATE ---
        btnAtacar = new JButton("Atacar Físico");
        btnAtacar.setBounds(20, 330, 115, 35);
        add(btnAtacar);

        btnHabilidad = new JButton("Habilidad");
        btnHabilidad.setBounds(145, 330, 115, 35);
        add(btnHabilidad);

        btnInventario = new JButton("Inventario");
        btnInventario.setBounds(270, 330, 115, 35);
        add(btnInventario);

        btnHuir = new JButton("Huir");
        btnHuir.setBounds(395, 330, 105, 35);
        add(btnHuir);

        // --- LÓGICA: ATAQUE BÁSICO ---
        btnAtacar.addActionListener(e -> {
            int dañoHeroe = heroe.calcularDano();
            int defensaEnemigo = enemigo.getStats().getDefensa();
            int dañoReal = Math.max(5, dañoHeroe - (defensaEnemigo / 2));
            
            enemigo.getStats().setVida(enemigo.getStats().getVida() - dañoReal);
            txtLogCombate.append(heroe.getNombre() + " atacó a " + enemigo.getNombre() + " e infligió " + dañoReal + " de daño.\n");

            verificarEstadoCombate();
        });

        // --- LÓGICA: HABILIDAD (MANÁ) ---
        btnHabilidad.addActionListener(e -> {
            if (heroe.getHabilidades() == null || heroe.getHabilidades().isEmpty()) {
                JOptionPane.showMessageDialog(this, "¡No tienes habilidades disponibles!");
                return;
            }

            String[] nombresHabs = new String[heroe.getHabilidades().size()];
            for (int i = 0; i < heroe.getHabilidades().size(); i++) {
                Habilidad h = heroe.getHabilidades().get(i);
                nombresHabs[i] = h.getNombre() + " (Costo: " + h.getCostoMana() + " Maná | Daño: " + h.getDañoBase() + ")";
            }

            String eleccion = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona una habilidad para usar:",
                    "Usar Habilidad",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    nombresHabs,
                    nombresHabs[0]
            );

            if (eleccion != null) {
                int index = -1;
                for (int i = 0; i < nombresHabs.length; i++) {
                    if (nombresHabs[i].equals(eleccion)) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    Habilidad habSeleccionada = heroe.getHabilidades().get(index);
                    
                    if (habSeleccionada.usar(heroe.getStats())) {
                        int dañoTotal = heroe.calcularDano() + habSeleccionada.getDañoBase();
                        enemigo.getStats().setVida(enemigo.getStats().getVida() - dañoTotal);
                        
                        txtLogCombate.append("¡" + heroe.getNombre() + " usó " + habSeleccionada.getNombre() + 
                                            " e infligió " + dañoTotal + " de daño!\n");
                        
                        verificarEstadoCombate();
                    } else {
                        JOptionPane.showMessageDialog(this, "¡No tienes suficiente Maná para usar " + habSeleccionada.getNombre() + "!", 
                                                      "Maná Insuficiente", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // --- LÓGICA: INVENTARIO EN COMBATE ---
        btnInventario.addActionListener(e -> {
            new InventarioGUI(heroe, this).setVisible(true);
            actualizarBarrasEstiloVisual();
        });

        // --- LÓGICA: HUIR ---
        btnHuir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Has huido del combate con éxito!");
            regresarAlMundoPlano();
        });
    }

    public void actualizarBarrasEstiloVisual() {
        Stats s = heroe.getStats();
        Stats se = enemigo.getStats();

        // Configurar barra de Vida del Héroe
        barraVidaHeroe.setMaximum(Math.max(1, s.getVidaMaxima()));
        barraVidaHeroe.setValue(Math.max(0, s.getVida()));
        int porcVidaH = (int) (((double) s.getVida() / s.getVidaMaxima()) * 100);
        barraVidaHeroe.setString(s.getVida() + " / " + s.getVidaMaxima() + " (" + porcVidaH + "%)");

        // Configurar barra de Maná del Héroe
        barraManaHeroe.setMaximum(Math.max(1, s.getManaMaximo()));
        barraManaHeroe.setValue(Math.max(0, s.getMana()));
        int porcManaH = (int) (((double) s.getMana() / s.getManaMaximo()) * 100);
        barraManaHeroe.setString(s.getMana() + " / " + s.getManaMaximo() + " (" + porcManaH + "%)");

        // Configurar barra de Vida del Enemigo
        barraVidaEnemigo.setMaximum(Math.max(1, se.getVidaMaxima()));
        barraVidaEnemigo.setValue(Math.max(0, se.getVida()));
        int porcVidaE = (int) (((double) se.getVida() / se.getVidaMaxima()) * 100);
        barraVidaEnemigo.setString(se.getVida() + " / " + se.getVidaMaxima() + " (" + porcVidaE + "%)");
    }

    private void verificarEstadoCombate() {
        actualizarBarrasEstiloVisual();

        if (enemigo.getStats().getVida() <= 0) {
            JOptionPane.showMessageDialog(this, "¡Has derrotado a " + enemigo.getNombre() + "!\n¡Ganaste " + xpRecompensa + " XP!");
            if (ventanaPadre instanceof MundoPlanoGUI) {
                soltarBotin();
                ((MundoPlanoGUI) ventanaPadre).ganarExperiencia(xpRecompensa);
                ((MundoPlanoGUI) ventanaPadre).setVisible(true);
            }
            dispose();
            return;
        }

        turnoEnemigo();
    }
       private void soltarBotin() {
        MundoPlanoGUI mundo = (MundoPlanoGUI) ventanaPadre;
        Random r = new Random();

        // En peleas de práctica (0 XP) no cae nada
        if (xpRecompensa <= 0) {
            return;
        }

        // 65% de probabilidad de que suelte algo
       int probabilidad = 40;
        if ("Media".equals(mundo.getDificultad())) {
            probabilidad = 60;
        }
        if ("Difícil".equals(mundo.getDificultad())) {
            probabilidad = 80;
        }
        if (r.nextInt(100) >= probabilidad) {
            return;
        }

        // El bono base crece con tu nivel; la dificultad lo mejora
        int base = 2 + mundo.getNivel() * 2;
        int extraSuerte = 0;
        if ("Media".equals(mundo.getDificultad())) {
            base = base * 3 / 2;
            extraSuerte = 5;
        }
        if ("Difícil".equals(mundo.getDificultad())) {
            base = base * 2;
            extraSuerte = 10;
        }

        // Calidad al azar
        int suerte = r.nextInt(100) + extraSuerte;
        String calidad;
        int bono;
        if (suerte < 25) {
            calidad = "Oxidada";
            bono = base / 3;
        } else if (suerte < 75) {
            calidad = "Común";
            bono = base;
        } else if (suerte < 95) {
            calidad = "Rara";
            bono = base + base / 2;
        } else {
            calidad = "Legendaria";
            bono = base * 2;
        }

        // Tipo al azar: 0 = arma, 1 = armadura, 2 y 3 = poción
        Item drop;
        int tipo = r.nextInt(4);

         int nivelArma = 8;
        int nivelArmadura = 4;
        if ("Difícil".equals(mundo.getDificultad())) {
            nivelArma = 4;
            nivelArmadura = 2;
        }

        if (tipo == 0 && mundo.getNivel() < nivelArma) {
            tipo = 2;
        }
        if (tipo == 1 && mundo.getNivel() < nivelArmadura) {
            tipo = 2;
        }

        // Si el equipo que cae no mejora el que ya llevas puesto, suelta una poción
        if (tipo == 0 && heroe.getArmaEquipada() != null) {
            if (bono <= heroe.getArmaEquipada().getBonusFuerza()) {
                tipo = 2;
            }
        }
        if (tipo == 1 && heroe.getArmaduraEquipada() != null) {
            if (bono <= heroe.getArmaduraEquipada().getBonusDefensa()) {
                tipo = 2;
            }
        }

                if (tipo == 0) {
            String nombre = nombreArma();
            drop = new Arma(nombre + " (" + calidad + ")", 20, bono);
        } else if (tipo == 1) {
            String nombre = nombreArmadura();
            drop = new Armadura(nombre + " (" + calidad + ")", 20, bono);
        } else {
            // Desde el nivel 3 puede caer el Elixir (1 de cada 5 pociones)
            if (mundo.getNivel() >= 3 && r.nextInt(5) == 0) {
                drop = new Pocion("Elixir de Vida", 45, 100);
            } else {
                int total = 3;
                int deSalud = 2;
                if ("Media".equals(mundo.getDificultad())) {
                    total = 4;
                    deSalud = 3;
                }
                if ("Difícil".equals(mundo.getDificultad())) {
                    total = 5;
                    deSalud = 4;
                }
                int p = r.nextInt(total);
                if (p < deSalud) {
                    drop = new Pocion("Poción de Salud Menor", 20, 30);
                } else {
                    drop = new Pocion("Poción de Maná", 15, 30);
                }
            }
        }

        heroe.getInventario().agregarItem(drop);
        JOptionPane.showMessageDialog(this, "¡El enemigo soltó: " + drop.getNombre() + "!");
    }
    private String nombreArma() {
        Random r = new Random();
        String nombre;

        if (heroe instanceof Mago) {
            if (r.nextBoolean()) {
                nombre = "Bastón Arcano";
            } else {
                nombre = "Cetro Mágico";
            }
        } else if (heroe instanceof Golem) {
            if (r.nextBoolean()) {
                nombre = "Puños de Acero";
            } else {
                nombre = "Martillo de Piedra";
            }
        } else if (heroe instanceof Arquero) {
            if (r.nextBoolean()) {
                nombre = "Arco Largo";
            } else {
                nombre = "Arco Compuesto";
            }
        } else if (heroe instanceof Cazador) {
            if (r.nextBoolean()) {
                nombre = "Dagas Dobles";
            } else {
                nombre = "Dagas Curvas";
            }
        } else {
            if (r.nextBoolean()) {
                nombre = "Espada de Acero";
            } else {
                nombre = "Hacha de Guerra";
            }
        }
        return nombre;
    }
       
    private String nombreArmadura() {
        Random r = new Random();
        String nombre;

        if (heroe instanceof Mago) {
            if (r.nextBoolean()) {
                nombre = "Túnica Mágica";
            } else {
                nombre = "Manto Arcano";
            }
        } else if (heroe instanceof Golem) {
            if (r.nextBoolean()) {
                nombre = "Placas de Piedra";
            } else {
                nombre = "Coraza de Roca";
            }
        } else if (heroe instanceof Arquero) {
            if (r.nextBoolean()) {
                nombre = "Capa de Cuero";
            } else {
                nombre = "Chaleco Ligero";
            }
        } else if (heroe instanceof Cazador) {
            if (r.nextBoolean()) {
                nombre = "Armadura de Cuero";
            } else {
                nombre = "Capa de Sombras";
            }
        } else {
            if (r.nextBoolean()) {
                nombre = "Coraza de Gladiador";
            } else {
                nombre = "Coraza de Hierro";
            }
        }
        return nombre;
    }
    
    private void turnoEnemigo() {
        int dañoEnemigo = Math.max(3, enemigo.getStats().getFuerza() - (heroe.getStats().getDefensa() / 2));
        heroe.getStats().setVida(heroe.getStats().getVida() - dañoEnemigo);
        txtLogCombate.append(enemigo.getNombre() + " te atacó e infligió " + dañoEnemigo + " de daño.\n");

        actualizarBarrasEstiloVisual();

        if (heroe.getStats().getVida() <= 0) {
            JOptionPane.showMessageDialog(this, "Has sido derrotado por " + enemigo.getNombre() + "...", "Game Over", JOptionPane.ERROR_MESSAGE);
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        }
    }

    private void regresarAlMundoPlano() {
        if (ventanaPadre instanceof MundoPlanoGUI) {
            ((MundoPlanoGUI) ventanaPadre).setVisible(true);
        }
        dispose();
    }
}