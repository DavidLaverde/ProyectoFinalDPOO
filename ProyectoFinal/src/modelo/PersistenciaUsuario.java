package modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PersistenciaUsuario implements Persistencia {

    public static HashMap<String, ModeloUsuario> usuarios = new HashMap<>();
    public static String cabecera;
    private static String nombreArchivo = "Usuarios.csv";
    private static String ruta;

    @Override
    public HashMap<String, ModeloUsuario> cargar(String nuevaRuta) throws IOException, FileNotFoundException {
        ruta = nuevaRuta;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ruta + nombreArchivo))) {
            String fila = bufferedReader.readLine(); // Leer cabecera
            cabecera = fila;
            fila = bufferedReader.readLine();
            while (fila != null) {
                String[] partes = fila.split(",");
                // Parsear los datos de la fila
                String email = partes[0].strip();
                String nombre = partes[1].strip();
                ModeloRol rol = ModeloRol.valueOf(partes[2].strip());
                String contrasena = partes[3].strip();
                String telefono = partes[4].strip();
                ModeloUsuario usuario = new ModeloUsuario(nombre, email, rol, contrasena, telefono);
                
                // Verificar si el usuario es un cliente y actualizar sus atributos correspondientes
                if (usuario.getRol().equals(ModeloRol.CLIENTE)) {
                    Boolean autorizado = Boolean.parseBoolean(partes[5].strip());
                    Double limitePago = Double.parseDouble(partes[6].strip());
                    usuario.setAutorizado(autorizado);
                    usuario.setLimitePago(limitePago);
                }

                // Agregar el usuario al HashMap
                usuarios.put(email, usuario);
                fila = bufferedReader.readLine();
            }
        }
        return usuarios;
    }

    @Override
    public void actualizar() throws IOException, FileNotFoundException {
        try (FileWriter editor = new FileWriter(ruta + nombreArchivo)) {
            ArrayList<String[]> filas = new ArrayList<>();
            // Generar las filas para escribir en el archivo
            for (ModeloUsuario u : usuarios.values()) {
                String autorizado = u.getAutorizado() != null ? u.getAutorizado().toString() : "null";
                String limitePago = u.getLimitePago() != null ? u.getLimitePago().toString() : "null";
                String[] fila = { u.getCorreo(), u.getNombre(), u.getRol().toString(), u.getContrasena(),
                        u.getTelefono(), autorizado, limitePago };
                filas.add(fila);
            }

            // Escribir la cabecera en el archivo
            editor.write(cabecera + "\n");

            // Escribir las filas en el archivo
            for (String[] fila : filas) {
                String row = String.join(",", fila);
                editor.append(row + "\n");
            }
        }
    }
}