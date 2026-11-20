package view;

import javax.swing.*;

/**
 * Pantalla de selección de personaje. Cada personaje tiene un botón
 * que abre su pantalla de detalle (stats, habilidades y equipo).
 */
public class CreacionHeroeGUI extends JFrame {
    // Se conservan al ir y volver entre pantallas
    private String nombreGuardado = "";
    private String dificultadGuardada = "Fácil";

    public CreacionHeroeGUI() {
        setTitle("Selección de Personaje");
        setSize(480, 470);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Elige tu personaje");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(java.awt.Font.BOLD, 20f));
        lblTitulo.setBounds(20, 12, 400, 30);
        add(lblTitulo);

        int y = 55;
        for (String clase : PersonajeInfo.CLASES) {
            JLabel lblClase = new JLabel(clase);
            lblClase.setFont(lblClase.getFont().deriveFont(java.awt.Font.BOLD, 14f));
            lblClase.setBounds(20, y, 120, 22);
            add(lblClase);

            JLabel lblDesc = new JLabel("<html>" + PersonajeInfo.descripcion(clase) + "</html>");
            lblDesc.setBounds(20, y + 22, 300, 32);
            add(lblDesc);

            JButton btnVer = new JButton("Ver detalles");
            btnVer.setBounds(335, y + 8, 120, 34);
            add(btnVer);

            btnVer.addActionListener(e -> {
                DetalleHeroeGUI detalle = new DetalleHeroeGUI(this, clase, nombreGuardado, dificultadGuardada);
                detalle.setVisible(true);
                setVisible(false);
            });

            y += 65;
        }

        JButton btnMenu = new JButton("Volver al Menú");
        btnMenu.setBounds(140, y + 5, 200, 32);
        add(btnMenu);

        btnMenu.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });
    }

    /** Lo llama la pantalla de detalle al volver, para no perder lo que ya escribió el jugador. */
    public void recordarDatos(String nombre, String dificultad) {
        this.nombreGuardado = nombre;
        this.dificultadGuardada = dificultad;
    }
}
