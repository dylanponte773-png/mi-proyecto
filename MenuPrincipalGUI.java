package view;

import modelo.*;
import data.GestorArchivo;
import javax.swing.*;
import java.io.File;
import java.util.List;

public class MenuPrincipalGUI extends JFrame {
    private JButton btnIniciarJuego, btnCargarPartida, btnEliminarPartida, btnConfiguracion, btnSalir;

    public MenuPrincipalGUI() {
        setTitle("Motor RPG - Menú Principal");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        btnIniciarJuego = new JButton("Jugar / Nueva Partida");
        btnIniciarJuego.setBounds(80, 30, 240, 40);
        add(btnIniciarJuego);

        btnCargarPartida = new JButton("Cargar Partida Guardada");
        btnCargarPartida.setBounds(80, 90, 240, 40);
        add(btnCargarPartida);

        btnEliminarPartida = new JButton("Eliminar Partida Guardada");
        btnEliminarPartida.setBounds(80, 150, 240, 40);
        add(btnEliminarPartida);

        btnConfiguracion = new JButton("Panel de Configuración (Admin)");
        btnConfiguracion.setBounds(80, 210, 240, 40);
        add(btnConfiguracion);

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(80, 270, 240, 40);
        add(btnSalir);

        btnIniciarJuego.addActionListener(e -> {
            CreacionHeroeGUI creacion = new CreacionHeroeGUI();
            creacion.setVisible(true);
            dispose();
        });

        btnCargarPartida.addActionListener(e -> cargarPartidaAccion());

        btnEliminarPartida.addActionListener(e -> {
            File carpetaActual = new File(".");
            File[] archivosGuardados = carpetaActual.listFiles((dir, name) -> name.startsWith("partida_") && name.endsWith(".txt"));

            if (archivosGuardados == null || archivosGuardados.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay partidas guardadas para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<String> comboPartidas = new JComboBox<>();
            for (File archivo : archivosGuardados) {
                comboPartidas.addItem(archivo.getName());
            }

            int seleccion = JOptionPane.showConfirmDialog(this, comboPartidas, "Selecciona la partida a eliminar", JOptionPane.OK_CANCEL_OPTION);
            if (seleccion == JOptionPane.OK_OPTION) {
                String archivoSeleccionado = (String) comboPartidas.getSelectedItem();
                File archivoAEliminar = new File(archivoSeleccionado);
                if (archivoAEliminar.delete()) {
                    JOptionPane.showMessageDialog(this, "¡Partida eliminada con éxito!");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnConfiguracion.addActionListener(e -> new LoginAdminGUI().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));
    }

private void cargarPartidaAccion() {
        File carpetaActual = new File(".");
        File[] archivosGuardados = carpetaActual.listFiles((dir, name) -> name.startsWith("partida_") && name.endsWith(".txt"));

        if (archivosGuardados == null || archivosGuardados.length == 0) {
            JOptionPane.showMessageDialog(this, "No se encontró ninguna partida guardada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<String> comboPartidas = new JComboBox<>();
        for (File archivo : archivosGuardados) {
            comboPartidas.addItem(archivo.getName());
        }

        int seleccion = JOptionPane.showConfirmDialog(this, comboPartidas, "Selecciona una Partida para Cargar", JOptionPane.OK_CANCEL_OPTION);
        
        if (seleccion == JOptionPane.OK_OPTION) {
            String archivoSeleccionado = (String) comboPartidas.getSelectedItem();
            try {
                List<String> datos = GestorArchivo.leerDatos(archivoSeleccionado);
                if (datos == null || datos.isEmpty()) return;

                String nombreCargado = datos.get(0);
                String claseCargada = datos.get(1);
                int vidaActual = Integer.parseInt(datos.get(2));
                int vidaMaxima = Integer.parseInt(datos.get(3));
                int manaActual = Integer.parseInt(datos.get(4));
                int fuerzaBaseCargada = Integer.parseInt(datos.get(5));
                int defensaBaseCargada = Integer.parseInt(datos.get(6));
                int inteligenciaCargada = Integer.parseInt(datos.get(7));
                String dificultadCargada = datos.get(8);
                int nivelCargado = Integer.parseInt(datos.get(9));
                int expCargada = Integer.parseInt(datos.get(10));

                Stats stats = new Stats(vidaMaxima, fuerzaBaseCargada, defensaBaseCargada, inteligenciaCargada);
                stats.setVida(vidaActual);
                stats.setMana(manaActual);

                Heroe heroeCargado;
                switch (claseCargada) {
                    case "Gladiador": heroeCargado = new Gladiador(nombreCargado, stats); break;
                    case "Mago": heroeCargado = new Mago(nombreCargado, stats); break;
                    case "Golem": heroeCargado = new Golem(nombreCargado, stats); break;
                    case "Arquero": heroeCargado = new Arquero(nombreCargado, stats); break;
                    case "Cazador": heroeCargado = new Cazador(nombreCargado, stats); break;
                    default: heroeCargado = new Gladiador(nombreCargado, stats); break;
                }


 // Cargar inventario y restaurar equipo guardado
if (datos.size() > 11 && !datos.get(11).isEmpty()) {
    String[] itemsArray = datos.get(11).split(";");
    for (String itemStr : itemsArray) {
        if (!itemStr.isEmpty()) {
            String[] partes = itemStr.split(":");
            if (partes.length >= 4) {
                String tipo = partes[0];
                String nomItem = partes[1];
                boolean estaEquipado = Boolean.parseBoolean(partes[2]);
                int bonoGuardado = Integer.parseInt(partes[3]);

                Item nuevoItem = null;
                if (tipo.equals("Arma")) {
                    // Reconstruimos el arma con su bono exacto guardado
                    nuevoItem = new Arma(nomItem, 10, bonoGuardado);
                    heroeCargado.getInventario().agregarItem(nuevoItem);
                    if (estaEquipado) {
                        heroeCargado.equiparArma((Arma) nuevoItem);
                    }
                } else if (tipo.equals("Armadura")) {
                    // Reconstruimos la armadura con su bono exacto guardado
                    nuevoItem = new Armadura(nomItem, 10, bonoGuardado);
                    heroeCargado.getInventario().agregarItem(nuevoItem);
                    if (estaEquipado) {
                        heroeCargado.equiparArmadura((Armadura) nuevoItem);
                    }
                } else if (tipo.equals("Pocion")) {
                    nuevoItem = new Pocion(nomItem, 10, 20);
                    heroeCargado.getInventario().agregarItem(nuevoItem);
                }
            }
        }
    }
}

                JOptionPane.showMessageDialog(this, "¡Partida de " + nombreCargado + " cargada con éxito!");
                new MundoPlanoGUI(heroeCargado, dificultadCargada, nivelCargado, expCargada).setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la partida: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}