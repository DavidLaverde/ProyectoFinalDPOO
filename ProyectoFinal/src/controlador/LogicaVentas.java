package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import modelo.ModeloVenta;
import modelo.PersistenciaVentas;

public class LogicaVentas implements Logica<ModeloVenta> {

    private final PersistenciaVentas persistenciaVentas = new PersistenciaVentas();

    @Override
    public ModeloVenta crear(ModeloVenta nuevo) throws Exception {
        validarVenta(nuevo);
        PersistenciaVentas.ventas.put(nuevo.getId(), nuevo);
        persistenciaVentas.actualizar();
        return nuevo;
    }

    private void validarVenta(ModeloVenta nuevo) throws Exception {
        if (nuevo.getId() == null) {
            throw new IllegalArgumentException("Se requiere un id para registrar una venta");
        }

        if (PersistenciaVentas.ventas.containsKey(nuevo.getId())) {
            throw new IllegalArgumentException("Ya existe una venta con el id " + nuevo.getId());
        }
    }

    @Override
    public ModeloVenta consultar(String identificador) throws Exception {
        Integer id = Integer.parseInt(identificador);
        ModeloVenta venta = PersistenciaVentas.ventas.get(id);
        if (venta == null) {
            throw new IllegalArgumentException("No se encontró la venta con id " + id);
        }
        return venta;
    }

    @Override
    public HashMap<String, ModeloVenta> consultarTodos() {
        throw new UnsupportedOperationException("Unimplemented method 'consultarTodos'");
    }

    @Override
    public ModeloVenta editar(ModeloVenta venta) throws Exception {
        if (!PersistenciaVentas.ventas.containsKey(venta.getId())) {
            throw new IllegalArgumentException("Venta no encontrada");
        }
        PersistenciaVentas.ventas.put(venta.getId(), venta);
        persistenciaVentas.actualizar();
        return venta;
    }

    @Override
    public void eliminar(String identificador) throws Exception {
        Integer id = Integer.parseInt(identificador);
        if (!PersistenciaVentas.ventas.containsKey(id)) {
            throw new IllegalArgumentException("Venta no encontrada");
        }
        PersistenciaVentas.ventas.remove(id);
        persistenciaVentas.actualizar();
    }

    @Override
    public Integer cargarDatos(String ruta) throws FileNotFoundException, IOException {
        return persistenciaVentas.cargar(ruta).size();
    }

    public ModeloVenta consultarPagoMasRecientePorPieza(String titulo) {
        HashMap<Integer, ModeloVenta> ventas = consultarPorPieza(titulo);
        ModeloVenta masReciente = null;
        for (ModeloVenta venta : ventas.values()) {
            if (masReciente == null || venta.getFecha().isAfter(masReciente.getFecha())) {
                masReciente = venta;
            }
        }
        return masReciente;
    }

    public HashMap<Integer, ModeloVenta> consultarPorPieza(String titulo) {
        HashMap<Integer, ModeloVenta> filtro = new HashMap<>();
        for (ModeloVenta venta : PersistenciaVentas.ventas.values()) {
            if (venta.getTituloPieza().equals(titulo)) {
                filtro.put(venta.getId(), venta);
            }
        }
        return filtro;
    }

    public ArrayList<ModeloVenta> consultarHistorialDePieza(String tituloPieza) {
        HashMap<Integer, ModeloVenta> ventas = consultarPorPieza(tituloPieza);
        return new ArrayList<>(ventas.values());
    }

    public HashMap<Integer, ModeloVenta> consultarTodasLasVentas() {
        return PersistenciaVentas.ventas;
    }
}