package utilidades;

import java.io.BufferedReader;
import java.io.FileNotFoundException; // Importamos FileNotFoundException
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.LogicaOfertas;
import controlador.LogicaPiezas;
import controlador.LogicaSubastas;
import controlador.LogicaUsuarios;
import controlador.LogicaVentas;

public class Utilidades {

    private static final Logger logger = Logger.getLogger(Utilidades.class.getName());

    public static void cargarDatos(String ruta) {
        LogicaUsuarios logicaUsuarios = new LogicaUsuarios();
        LogicaPiezas logicaPiezas = new LogicaPiezas();
        LogicaVentas logicaVentas = new LogicaVentas();
        LogicaSubastas logicaSubastas = new LogicaSubastas();
        LogicaOfertas logicaOfertas = new LogicaOfertas();
        try {
            Integer usuariosCargados = logicaUsuarios.cargarDatos(ruta);
            logger.info("Se cargaron " + usuariosCargados + " usuarios.");
            Integer piezasCargadas = logicaPiezas.cargarDatos(ruta);
            logger.info("Se cargaron " + piezasCargadas + " piezas.");
            Integer ventasCargadas = logicaVentas.cargarDatos(ruta);
            logger.info("Se cargaron " + ventasCargadas + " ventas.");
            Integer subastasCargadas = logicaSubastas.cargarDatos(ruta);
            logger.info("Se cargaron " + subastasCargadas + " subastas.");
            Integer ofertasCargadas = logicaOfertas.cargarDatos(ruta);
            logger.info("Se cargaron " + ofertasCargadas + " ofertas.");

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Archivo no encontrado: " + ruta, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error de lectura/escritura al cargar datos desde: " + ruta, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando los datos desde: " + ruta, e);
        }
    }

    public static String lectorConsola(String mensaje) {
        System.out.print(mensaje);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            return bufferedReader.readLine().strip();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error leyendo entrada de datos", e);
            return null;
        }
    }

    public static boolean esNumerico(String str) {
        try {
            Long.parseLong(str); // Soporta Long
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
