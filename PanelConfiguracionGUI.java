package view;

import javax.swing.*;

public class PanelConfiguracionGUI extends JFrame {
    private JComboBox<String> cbTipoEntidad;
    private JTextField txtNombre, txtVida, txtFuerza, txtDefensa, txtInteligencia;
    private JButton btnGuardar;

    public PanelConfiguracionGUI() {
        setTitle("Panel de Configuración y Balanceo");
        setSize(400, 380);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTipo = new JLabel("Entidad:");
        lblTipo.setBounds(30, 30, 140, 25);
        add(lblTipo);

        String[] entidades = {"Gladiador", "Mago", "Golem", "Arquero", "Cazador", "Goblin", "Orco", "Esqueleto", "Gargola", "Dragon"};
        cbTipoEntidad = new JComboBox<>(entidades);
        cbTipoEntidad.setBounds(150, 30, 190, 25);
        add(cbTipoEntidad);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 75, 100, 25);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, 75, 190, 25);
        add(txtNombre);

        JLabel lblVida = new JLabel("Vida Base:");
        lblVida.setBounds(30, 115, 100, 25);
        add(lblVida);
        txtVida = new JTextField();
        txtVida.setBounds(150, 115, 190, 25);
        add(txtVida);

        JLabel lblFuerza = new JLabel("Fuerza:");
        lblFuerza.setBounds(30, 155, 100, 25);
        add(lblFuerza);
        txtFuerza = new JTextField();
        txtFuerza.setBounds(150, 155, 190, 25);
        add(txtFuerza);

        JLabel lblDefensa = new JLabel("Defensa:");
        lblDefensa.setBounds(30, 195, 100, 25);
        add(lblDefensa);
        txtDefensa = new JTextField();
        txtDefensa.setBounds(150, 195, 190, 25);
        add(txtDefensa);

        JLabel lblInteligencia = new JLabel("Inteligencia:");
        lblInteligencia.setBounds(30, 235, 100, 25);
        add(lblInteligencia);
        txtInteligencia = new JTextField();
        txtInteligencia.setBounds(150, 235, 190, 25);
        add(txtInteligencia);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBounds(90, 285, 210, 35);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Configuración guardada con éxito!");
            dispose();
        });
    }
}