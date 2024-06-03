package modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import controlador.LogicaPiezas;


public class PersistenciaSubastas implements Persistencia {

    public static HashMap<Integer, ModeloSubasta> subastas = new HashMap<>();
    public static String cabecera;
    private static String nombreArchivo = "Subastas.csv";
    private static String ruta;

    private final LogicaPiezas logicaPiezas = new LogicaPiezas();

    @Override
    public HashMap<Integer, ModeloSubasta> cargar(String nuevaRuta) throws FileNotFoundException, IOException {
        ruta = nuevaRuta;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ruta + nombreArchivo))) {
            String fila = bufferedReader.readLine(); // Leer cabecera
            cabecera = fila;
            fila = bufferedReader.readLine();
            while (fila != null) {
                String[] partes = fila.split(",");
                // Parsear los datos de la fila
                Integer id = Integer.parseInt(partes[0].strip());
                LocalDate fecha = LocalDate.parse(partes[1].strip());
                String tituloPieza = partes[2].strip();
                Double minimo = Double.parseDouble(partes[3].strip());
                ModeloEstadoSubasta estado = ModeloEstadoSubasta.valueOf(partes[4].strip());

                // Crear la instancia de ModeloSubasta y agregarla al HashMap
                ModeloSubasta subasta = new ModeloSubasta(id, fecha, tituloPieza, estado, minimo, logicaPiezas);
                subastas.put(id, subasta);
                fila = bufferedReader.readLine();
            }
        }
        return subastas;
    }

    @Override
    public void actualizar() throws IOException {
        try (FileWriter editor = new FileWriter(ruta + nombreArchivo)) {
            ArrayList<String[]> filas = new ArrayList<>();
            // Generar las filas para escribir en el archivo
            for (ModeloSubasta s : subastas.values()) {
                String[] datos = { 
                    s.getId().toString(), 
                    s.getFechaInicio().toString(), 
                    s.getTituloPieza(),
                    String.valueOf(s.getMinimo()), // Convertir el valor mínimo a String correctamente
                    s.getEstado().toString() 
                };
                filas.add(datos);
            }

            editor.write(cabecera + "\n");

            // Escribir las filas en el archivo
            for (String[] fila : filas) {
                String filaStr = String.join(",", fila);
                editor.append(filaStr + "\n");
            }
        }
    }
}
