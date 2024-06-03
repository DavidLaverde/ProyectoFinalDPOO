package modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PersistenciaVentas implements Persistencia {

    public static HashMap<Integer, ModeloVenta> ventas = new HashMap<>();
    public static String cabecera;
    private static String nombreArchivo = "Ventas.csv";
    private static String ruta;

    @Override
    public HashMap<Integer, ModeloVenta> cargar(String nuevaRuta) throws FileNotFoundException, IOException {
        ruta = nuevaRuta;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ruta + nombreArchivo))) {
            String fila = bufferedReader.readLine(); // Leer cabecera
            cabecera = fila;
            fila = bufferedReader.readLine();
            while (fila != null) {
                String[] partes = fila.split(",");
                // Parsear los datos de la fila
                Integer id = Integer.parseInt(partes[0].strip());
                String tituloPieza = partes[1].strip();
                String emailDuenoAnterior = partes[2].strip();
                String emailNuevoDueno = partes[3].strip();
                Double precio = Double.parseDouble(partes[4].strip());
                LocalDate fecha = LocalDate.parse(partes[5].strip());
                ModeloTiposPago tipoPago = ModeloTiposPago.valueOf(partes[6].strip());
                ModeloTipoDeVenta tipoVenta = ModeloTipoDeVenta.valueOf(partes[7].strip());
                Boolean aprobadoAdmin = Boolean.parseBoolean(partes[8].strip());

                ModeloVenta venta = new ModeloVenta(id, tituloPieza, emailDuenoAnterior, emailNuevoDueno, precio, fecha,
                        tipoPago, tipoVenta, aprobadoAdmin);
                ventas.put(id, venta);
                fila = bufferedReader.readLine();
            }
        }
        return ventas;
    }

    @Override
    public void actualizar() throws IOException {
        try (FileWriter editor = new FileWriter(ruta + nombreArchivo)) {
            ArrayList<String[]> filas = new ArrayList<>();
            // Generar las filas para escribir en el archivo
            for (ModeloVenta v : ventas.values()) {
                String[] datos = {
                    v.getId().toString(),
                    v.getTituloPieza(),
                    v.getEmailDuenoAnterior(),
                    v.getEmailNuevoDueno(),
                    v.getPrecio().toString(),
                    v.getFecha().toString(),
                    v.getTipoPago().toString(),
                    v.getTipoVenta().toString(),
                    v.getConfirmadoAdmin().toString()
                };
                filas.add(datos);
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