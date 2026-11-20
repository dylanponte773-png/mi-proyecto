package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivo {

    // Guarda una lista de líneas de texto en un archivo especificado (ej. "personajes.txt")
    public static void guardarDatos(String rutaArchivo, List<String> datos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (String linea : datos) {
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    // Lee línea por línea un archivo de texto y retorna los registros en una lista
    public static List<String> leerDatos(String rutaArchivo) throws IOException {
        List<String> datos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        
        // Si el archivo no existe aún, retorna una lista vacía para evitar errores
        if (!archivo.exists()) {
            return datos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                datos.add(linea);
            }
        }
        
        return datos;
    }
}