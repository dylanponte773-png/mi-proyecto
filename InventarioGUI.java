package view;

import modelo.*;
import javax.swing.*;
import java.awt.*;

public class InventarioGUI extends JFrame {
    private Heroe heroe;
    private JFrame ventanaPadre;
    
    private DefaultListModel<String> modeloLista; // Cambiado a String para mostrar etiquetas personalizadas
    private JList<Item> listaInventarioReal; // Guardamos los objetos de referencia
    private DefaultListModel<Item> modeloObjetosReal;
    
    private JList<String> listaInventarioVisual;
    private JButton btnEquipar, btnDesequiparUniversal, btnUsar, btnCerrar;
    private JLabel lblEstadoGeneral, lblEquipoActual;

    public InventarioGUI(Heroe heroe, JFrame ventanaPadre) {
        this.heroe = heroe;
        this.ventanaPadre = ventanaPadre;

        setTitle("Inventario y Equipo - " + heroe.getNombre());
        setSize(540, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        lblEstadoGeneral = new JLabel();
        lblEstadoGeneral.setBounds(20, 10, 480, 30);
        add(lblEstadoGeneral);

        lblEquipoActual = new JLabel();
        lblEquipoActual.setBounds(20, 45, 480, 30);
        add(lblEquipoActual);

        // Listas de soporte
        modeloObjetosReal = new DefaultListModel<>();
        modeloLista = new DefaultListModel<>();
        cargarItemsEnLista();
        
        listaInventarioVisual = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaInventarioVisual);
        scrollPane.setBounds(20, 85, 270, 270);
        add(scrollPane);

        // Botones
        btnUsar = new JButton("Usar Consumible");
        btnUsar.setBounds(305, 85, 195, 35);
        add(btnUsar);

        btnEquipar = new JButton("Equipar Seleccionado");
        btnEquipar.setBounds(305, 135, 195, 35);
        add(btnEquipar);

        btnDesequiparUniversal = new JButton("Desequipar Objeto");
        btnDesequiparUniversal.setBounds(305, 185, 195, 35);
        add(btnDesequiparUniversal);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(305, 320, 195, 35);
        add(btnCerrar);

        actualizarEtiquetasEstado();

        // --- LÓGICA: USAR CONSUMIBLE ---
        btnUsar.addActionListener(e -> {
            int index = listaInventarioVisual.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un ítem de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Item seleccionado = modeloObjetosReal.get(index);

             if (seleccionado instanceof Pocion) {
                Pocion pocion = (Pocion) seleccionado;
                Stats stats = heroe.getStats();
                String nom = pocion.getNombre().toLowerCase();

                if (nom.contains("elixir")) {
                    stats.setVida(Math.min(stats.getVidaMaxima(), stats.getVida() + stats.getVidaMaxima()*3/4));
                    JOptionPane.showMessageDialog(this, "¡El Elixir te devolvió mucha vida!");
                } else if (nom.contains("maná") || nom.contains("mana")) {
                    stats.setMana(Math.min(stats.getManaMaximo(), stats.getMana() + 30));
                    JOptionPane.showMessageDialog(this, "¡Has usado " + pocion.getNombre() + " y recuperado Maná!");
                } else {
                    stats.setVida(Math.min(stats.getVidaMaxima(), stats.getVida() + 50));
                    JOptionPane.showMessageDialog(this, "¡Has usado " + pocion.getNombre() + " y recuperado Vida!");
                }

                heroe.getInventario().removerItem(pocion); // Las pociones sí se consumen y desaparecen
                actualizarListaYEstado();
            } else {
                JOptionPane.showMessageDialog(this, "Las armas y armaduras no se consumen. Usa el botón 'Equipar'.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- LÓGICA: EQUIPAR ---
        btnEquipar.addActionListener(e -> {
            int index = listaInventarioVisual.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un arma o armadura de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Item seleccionado = modeloObjetosReal.get(index);

            if (seleccionado instanceof Arma) {
                Arma arma = (Arma) seleccionado;
                heroe.equiparArma(arma);
                JOptionPane.showMessageDialog(this, "¡Arma equipada con éxito!");
                actualizarListaYEstado();
            } else if (seleccionado instanceof Armadura) {
                Armadura armadura = (Armadura) seleccionado;
                heroe.equiparArmadura(armadura);
                JOptionPane.showMessageDialog(this, "¡Armadura equipada con éxito!");
                actualizarListaYEstado();
            } else {
                JOptionPane.showMessageDialog(this, "Este objeto no se puede equipar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- LÓGICA: DESEQUIPAR UNIVERSAL ---
        btnDesequiparUniversal.addActionListener(e -> {
            boolean tieneArma = (heroe.getArmaEquipada() != null);
            boolean tieneArmadura = (heroe.getArmaduraEquipada() != null);

            if (!tieneArma && !tieneArmadura) {
                JOptionPane.showMessageDialog(this, "No tienes ningún objeto equipado actualmente.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (tieneArma && tieneArmadura) {
                String[] opciones = {"Arma Equipada", "Armadura Equipada"};
                int eleccion = JOptionPane.showOptionDialog(this, "¿Qué objeto deseas desequipar?", "Desequipar",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                if (eleccion == 0) {
                    heroe.desequiparArma();
                    JOptionPane.showMessageDialog(this, "Arma desequipada.");
                } else if (eleccion == 1) {
                    heroe.desequiparArmadura();
                    JOptionPane.showMessageDialog(this, "Armadura desequipada.");
                }
            } else if (tieneArma) {
                heroe.desequiparArma();
                JOptionPane.showMessageDialog(this, "Arma desequipada.");
            } else if (tieneArmadura) {
                heroe.desequiparArmadura();
                JOptionPane.showMessageDialog(this, "Armadura desequipada.");
            }

            actualizarListaYEstado();
        });

        btnCerrar.addActionListener(e -> {
            if (ventanaPadre instanceof MundoPlanoGUI) {
                ((MundoPlanoGUI) ventanaPadre).actualizarInfoLabel();
            }
            dispose();
        });
    }

    private void cargarItemsEnLista() {
        modeloLista.clear();
        modeloObjetosReal.clear();

        // Verificamos de forma segura que el inventario y sus ítems no sean nulos
        if (heroe.getInventario() != null && heroe.getInventario().getItems() != null) {
            for (Item it : heroe.getInventario().getItems()) {
                modeloObjetosReal.addElement(it);

                String etiquetaVisual = it.toString();

                // Mostramos cuánto suma cada arma o armadura
                if (it instanceof Arma) {
                    etiquetaVisual += " +" + ((Arma) it).getBonusFuerza();
                } else if (it instanceof Armadura) {
                    etiquetaVisual += " +" + ((Armadura) it).getBonusDefensa();
                }

                // Si el ítem actual coincide con el arma o armadura equipada, le añadimos la etiqueta
                if (it.equals(heroe.getArmaEquipada()) || it.equals(heroe.getArmaduraEquipada())) {
                    etiquetaVisual += " [EQUIPADO]";
                }
                modeloLista.addElement(etiquetaVisual);
            }
        }
    }

    private Inventario visualGetInventarioNullCheck() {
        return heroe.getInventario();
    }

    private void actualizarEtiquetasEstado() {
        Stats s = heroe.getStats();
        lblEstadoGeneral.setText("<html><b>HP:</b> " + s.getVida() + "/" + s.getVidaMaxima() + 
                                  " | <b>Maná:</b> " + s.getMana() + "/" + s.getManaMaximo() + 
                                  " | <b>Fuerza:</b> " + s.getFuerza() + " | <b>Defensa:</b> " + s.getDefensa() + "</html>");

        String armaNombre = (heroe.getArmaEquipada() != null) ? heroe.getArmaEquipada().getNombre() + " (+" + heroe.getArmaEquipada().getBonusFuerza() + ")" : "Ninguna";
        String armaduraNombre = (heroe.getArmaduraEquipada() != null) ? heroe.getArmaduraEquipada().getNombre() + " (+" + heroe.getArmaduraEquipada().getBonusDefensa() + ")" : "Ninguna";
        
        lblEquipoActual.setText("<html><b>Arma:</b> " + armaNombre + " | <b>Armadura:</b> " + armaduraNombre + "</html>");
    }

    private void actualizarListaYEstado() {
        cargarItemsEnLista();
        actualizarEtiquetasEstado();
    }
}