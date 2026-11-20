package view;

import modelo.*;
import javax.swing.*;

public class HabilidadesGUI extends JFrame {
    private Heroe heroe;
    private JFrame ventanaPadre;
    
    private DefaultListModel<String> modeloLista;
    private JList<String> listaHabilidades;
    private JButton btnCerrar;
    private JLabel lblManaInfo;

    public HabilidadesGUI(Heroe heroe, JFrame ventanaPadre) {
        this.heroe = heroe;
        this.ventanaPadre = ventanaPadre;

        setTitle("Habilidades de " + heroe.getNombre());
        setSize(420, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        lblManaInfo = new JLabel("Maná Actual: " + heroe.getStats().getMana() + " / " + heroe.getStats().getManaMaximo());
        lblManaInfo.setBounds(20, 15, 350, 25);
        add(lblManaInfo);

        modeloLista = new DefaultListModel<>();
        cargarHabilidades();

        listaHabilidades = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaHabilidades);
        scrollPane.setBounds(20, 50, 360, 200);
        add(scrollPane);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(150, 265, 120, 30);
        add(btnCerrar);

        btnCerrar.addActionListener(e -> dispose());
    }

    public HabilidadesGUI(Heroe heroe) {
        this(heroe, null);
    }

    private void cargarHabilidades() {
        modeloLista.clear();
        if (heroe.getHabilidades() != null) {
            for (Habilidad h : heroe.getHabilidades()) {
                String info = h.getNombre() + " | Daño: " + h.getDañoBase() + 
                              " | Costo Maná: " + h.getCostoMana() + " | Tipo: " + h.getTipo();
                modeloLista.addElement(info);
            }
        }
    }
}