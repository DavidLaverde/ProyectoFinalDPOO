package modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PersistenciaPieza implements Persistencia {

    public static HashMap<String, ModeloPiezas> piezas = new HashMap<>();
    public static String cabecera;
    private static String nombreArchivo = "Piezas.csv";
    private static String ruta;

    @Override
    public HashMap<String, ModeloPiezas> cargar(String nuevaRuta) throws FileNotFoundException, IOException {
        ruta = nuevaRuta;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ruta + nombreArchivo))) {
            String fila = bufferedReader.readLine(); // Leer cabecera
            cabecera = fila;
            fila = bufferedReader.readLine();
            while (fila != null) {
                String[] partes = fila.split(",");
                // Parsear los datos de la fila
                String titulo = partes[0].strip();
                String nombreAutor = partes[1].strip();
                String emailDueno = partes[2].strip();
                ModeloTiposPieza tipo = ModeloTiposPieza.valueOf(partes[3].strip());

                String materiales = partes[4].strip();
                ArrayList<String> materialesList = new ArrayList<>(Arrays.asList(materiales.split(" ")));

                Boolean bloqueada = Boolean.parseBoolean(partes[5].strip());
                Double precioMinimo = Double.parseDouble(partes[6].strip());

                ModeloEstado estado = ModeloEstado.valueOf(partes[7].strip());
                String emailOfertante = partes[8].strip();
                if (emailOfertante.equals("null")) {
                    emailOfertante = null;
                }
                ModeloTipoDeVenta tipoDeVenta = null;
                try {
                    if (!partes[9].strip().equals("null")) {
                        tipoDeVenta = ModeloTipoDeVenta.valueOf(partes[9].strip());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error interpretando tipo de venta. Debe tomar un valor entre: "
                            + Arrays.asList(ModeloTipoDeVenta.values()));
                    tipoDeVenta = null;
                    fila = bufferedReader.readLine();
                    continue;
                }

                try {
                    // Crear la instancia de ModeloPiezas y agregarla al HashMap
                    ModeloPiezas pieza = new ModeloPiezas(titulo, nombreAutor, emailDueno, tipo, materialesList,
                            bloqueada, precioMinimo, estado, emailOfertante, tipoDeVenta);
                    piezas.put(titulo, pieza);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                fila = bufferedReader.readLine();
            }
        }
        return piezas;
    }

    @Override
    public void actualizar() throws IOException {
        try (FileWriter editor = new FileWriter(ruta + nombreArchivo)) {
            ArrayList<String[]> filas = new ArrayList<>();
            // Generar las filas para escribir en el archivo
            for (ModeloPiezas p : piezas.values()) {
                String tipoDeVenta = (p.getTipoDeVenta() == null) ? "null" : p.getTipoDeVenta().toString();
                String[] datos = { p.getTitulo(), p.getNombreAutor(), p.getEmailDueno(), p.getTipo().toString(),
                        String.join(" ", p.getMateriales()), p.getBloqueada().toString(),
                        p.getPrecioMinimo().toString(), p.getEstado().toString(), p.getEmailOfertante(),
                        tipoDeVenta };
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
