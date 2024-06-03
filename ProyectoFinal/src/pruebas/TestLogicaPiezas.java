package pruebas;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controlador.LogicaPiezas;


class TestLogicaPiezas {

    private LogicaPiezas logicaPiezas;

    @BeforeEach
    void setUp() {
        logicaPiezas = new LogicaPiezas();
    }



    @Test
    void testCargarDatos() {
        int cantidadPiezasCargadas = 0;
        try {
            cantidadPiezasCargadas = logicaPiezas.cargarDatos("datos_piezas.csv");
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            fail("Error de lectura/escritura: " + e.getMessage());
        }
        assertTrue(cantidadPiezasCargadas > 0, "Se esperaba cargar al menos una pieza desde el archivo");
    }


}
