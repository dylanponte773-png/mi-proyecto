package view;

import modelo.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Pantalla de detalle de un personaje: muestra estadísticas, habilidades y
 * equipo inicial. Permite volver a la selección o elegirlo y comenzar a jugar.
 */
public class DetalleHeroeGUI extends JFrame {
    private final CreacionHeroeGUI ventanaAnterior;
    private final String clase;
    private JTextField txtNombre;
    private JComboBox<String> cbDificultad;

    public DetalleHeroeGUI(CreacionHeroeGUI ventanaAnterior, String clase, String nombreGuardado, String dificultadGuardada) {
        this.ventanaAnterior = ventanaAnterior;
        this.clase = clase;

        // Héroe de vista previa: las habilidades y stats salen de las clases reales del modelo
        Heroe previa = PersonajeInfo.crearHeroe(clase, "Vista previa");
        Stats s = previa.getStats();

        setTitle("Detalle de personaje - " + clase);
        setSize(490, 610);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                volver();
            }
        });

        JLabel lblTitulo = new JLabel(clase.toUpperCase());
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(java.awt.Font.BOLD, 22f));
        lblTitulo.setBounds(20, 12, 440, 30);
        add(lblTitulo);

        JLabel lblDesc = new JLabel("<html>" + PersonajeInfo.descripcion(clase) + "</html>");
        lblDesc.setBounds(20, 45, 440, 35);
        add(lblDesc);

        // ---------- ESTADÍSTICAS ----------
        JLabel lblStats = new JLabel("Estadísticas");
        lblStats.setFont(lblStats.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        lblStats.setBounds(20, 88, 200, 22);
        add(lblStats);

        agregarBarra("Vida", s.getVidaMaxima(), 150, 115);
        agregarBarra("Maná", s.getManaMaximo(), 100, 141);
        agregarBarra("Fuerza", s.getFuerza(), 30, 167);
        agregarBarra("Defensa", s.getDefensa(), 30, 193);
        agregarBarra("Inteligencia", s.getInteligencia(), 30, 219);

        JLabel lblDano = new JLabel("Daño base por ataque: " + previa.calcularDano());
        lblDano.setBounds(20, 247, 300, 22);
        add(lblDano);

        // ---------- HABILIDADES ----------
        JLabel lblHab = new JLabel("Habilidades");
        lblHab.setFont(lblHab.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        lblHab.setBounds(20, 278, 200, 22);
        add(lblHab);

        DefaultListModel<String> modeloHab = new DefaultListModel<>();
        for (Habilidad h : previa.getHabilidades()) {
            modeloHab.addElement(h.getNombre() + "  |  Daño: " + h.getDañoBase()
                    + "  |  Maná: " + h.getCostoMana() + "  |  " + h.getTipo());
        }
        JScrollPane spHab = new JScrollPane(new JList<>(modeloHab));
        spHab.setBounds(20, 302, 440, 60);
        add(spHab);

        // ---------- EQUIPO INICIAL ----------
        JLabel lblEq = new JLabel("Equipo inicial");
        lblEq.setFont(lblEq.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        lblEq.setBounds(20, 372, 200, 22);
        add(lblEq);

        DefaultListModel<String> modeloEq = new DefaultListModel<>();
        for (String linea : PersonajeInfo.resumenEquipo(clase)) {
            modeloEq.addElement(linea);
        }
        JScrollPane spEq = new JScrollPane(new JList<>(modeloEq));
        spEq.setBounds(20, 396, 440, 70);
        add(spEq);

        // ---------- NOMBRE Y DIFICULTAD ----------
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 482, 60, 25);
        add(lblNombre);

        txtNombre = new JTextField(nombreGuardado);
        txtNombre.setBounds(80, 482, 150, 25);
        add(txtNombre);

        JLabel lblDif = new JLabel("Dificultad:");
        lblDif.setBounds(245, 482, 70, 25);
        add(lblDif);

        cbDificultad = new JComboBox<>(new String[]{"Fácil", "Media", "Difícil"});
        cbDificultad.setSelectedItem(dificultadGuardada);
        cbDificultad.setBounds(320, 482, 140, 25);
        add(cbDificultad);

        // ---------- BOTONES ----------
        JButton btnVolver = new JButton("← Elegir otro");
        btnVolver.setBounds(20, 525, 200, 36);
        add(btnVolver);

        JButton btnElegir = new JButton("Elegir y Comenzar");
        btnElegir.setBounds(260, 525, 200, 36);
        add(btnElegir);

        btnVolver.addActionListener(e -> volver());
        btnElegir.addActionListener(e -> elegir());
    }

    private void agregarBarra(String etiqueta, int valor, int maximo, int y) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setBounds(20, y, 100, 20);
        add(lbl);

        JProgressBar barra = new JProgressBar(0, maximo);
        barra.setValue(valor);
        barra.setStringPainted(true);
        barra.setString(String.valueOf(valor));
        barra.setBounds(125, y, 335, 20);
        add(barra);
    }

    private void volver() {
        ventanaAnterior.recordarDatos(txtNombre.getText(), (String) cbDificultad.getSelectedItem());
        ventanaAnterior.setVisible(true);
        dispose();
    }

    private void elegir() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa un nombre válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String dificultad = (String) cbDificultad.getSelectedItem();

        try {
            Heroe heroe = PersonajeInfo.crearHeroe(clase, nombre);
            JOptionPane.showMessageDialog(this, "¡Héroe creado con éxito!\nSe te ha otorgado equipo y pociones iniciales.");

            MundoPlanoGUI mundo = new MundoPlanoGUI(heroe, dificultad);
            mundo.setVisible(true);
            ventanaAnterior.dispose();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el héroe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
