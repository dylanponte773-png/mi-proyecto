package view;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ejecutamos la interfaz gráfica de forma segura en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalGUI menu = new MenuPrincipalGUI();
            menu.setVisible(true);
        });
    }
}