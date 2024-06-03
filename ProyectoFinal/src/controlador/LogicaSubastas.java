package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import modelo.ModeloEstadoSubasta;
import modelo.ModeloOferta;
import modelo.ModeloPiezas;
import modelo.ModeloSubasta;
import modelo.PersistenciaSubastas;

public class LogicaSubastas implements Logica<ModeloSubasta> {

    private final PersistenciaSubastas persistenciaSubastas = new PersistenciaSubastas();
    private final LogicaPiezas logicaPiezas = new LogicaPiezas();
    private final LogicaOfertas logicaOfertas = new LogicaOfertas();
    
    public ModeloSubasta consultarPorPieza(ModeloPiezas pieza) throws Exception {
    	for (ModeloSubasta subasta : PersistenciaSubastas.subastas.values()) {
            if (subasta.getPieza().equals(pieza)) {
                return subasta;
            }
        }
        throw new Exception("No se encontró una subasta para la pieza indicada.");
    }

    @Override
    public ModeloSubasta crear(ModeloSubasta nuevo) throws Exception {
        if (nuevo.getId() == null) {
            nuevo.asignarId();
        } else if (PersistenciaSubastas.subastas.containsKey(nuevo.getId().toString())) { // Cambiado Integer a String
            throw new Exception("Ya existe una subasta con ese ID");
        }

        ModeloPiezas pieza = logicaPiezas.consultar(nuevo.getTituloPieza());
        validarMontoInicial(nuevo, pieza);

        PersistenciaSubastas.subastas.put(nuevo.getId(), nuevo);
        persistenciaSubastas.actualizar();
        return nuevo;
    }

    private void validarMontoInicial(ModeloSubasta nuevo, ModeloPiezas pieza) throws Exception {
        if (nuevo.getMinimo() >= pieza.getPrecioMinimo()) {
            throw new Exception("El monto inicial de la subasta no puede ser menor al precio mínimo de la pieza");
        }
    }

    @Override
    public ModeloSubasta consultar(String identificador) throws Exception {
        ModeloSubasta subasta = PersistenciaSubastas.subastas.get(identificador);
        if (subasta == null) {
            throw new Exception("Subasta no encontrada");
        }
        return subasta;
    }

    @Override
    public Map<String, ModeloSubasta> consultarTodos() {
        Map<String, ModeloSubasta> subastasMap = new HashMap<>();
        for (Map.Entry<Integer, ModeloSubasta> entry : PersistenciaSubastas.subastas.entrySet()) {
            subastasMap.put(entry.getKey().toString(), entry.getValue());
        }
        return subastasMap;
    }

    public Map<String, ModeloSubasta> consultarAbiertos() { // Cambiado de Integer a String
        Map<String, ModeloSubasta> filtro = new HashMap<>();
        for (ModeloSubasta subasta : PersistenciaSubastas.subastas.values()) {
            if (subasta.getEstado().equals(ModeloEstadoSubasta.ABIERTA)) {
                filtro.put(subasta.getId().toString(), subasta); // Cambiado Integer a String
            }
        }
        return filtro;
    }

    @Override
    public ModeloSubasta editar(ModeloSubasta model) throws Exception {
        if (!PersistenciaSubastas.subastas.containsKey(model.getId())) {
            throw new Exception("Subasta no encontrada");
        }
        PersistenciaSubastas.subastas.put(model.getId(), model);
        persistenciaSubastas.actualizar();
        return model;
    }

    @Override
    public void eliminar(String identificador) throws Exception {
        if (!PersistenciaSubastas.subastas.containsKey(identificador)) {
            throw new Exception("Subasta no encontrada");
        }
        PersistenciaSubastas.subastas.remove(identificador);
        persistenciaSubastas.actualizar();
    }

    @Override
    public Integer cargarDatos(String ruta) throws FileNotFoundException, IOException {
        Map<Integer, ModeloSubasta> subastas = persistenciaSubastas.cargar(ruta);
        PersistenciaSubastas.subastas.clear(); // Limpiamos el HashMap
        for (Map.Entry<Integer, ModeloSubasta> entry : subastas.entrySet()) {
            PersistenciaSubastas.subastas.put(entry.getKey(), entry.getValue());
        }
        return subastas.size();
    }
    public ModeloSubasta consultarPorTituloDePieza(String titulo) throws Exception {
        for (ModeloSubasta subasta : PersistenciaSubastas.subastas.values()) {
            if (subasta.getTituloPieza().equals(titulo) && subasta.getEstado().equals(ModeloEstadoSubasta.ABIERTA)) {
                return subasta;
            }
        }
        throw new Exception("No se encontró ninguna subasta abierta con ese título");
    }

    public String consultarEmailGanador(String tituloPieza) throws Exception {
        ModeloSubasta subasta = consultarPorTituloDePieza(tituloPieza);
        ModeloOferta ofertaMayor = logicaOfertas.consultarOfertaMayor(subasta.getId());
        if (ofertaMayor == null) {
            throw new Exception("No se encontró ninguna oferta ganadora");
        }
        return ofertaMayor.getEmailOfertante();
    }

    public Double consultarMontoGanador(String titulo) throws Exception {
        ModeloSubasta subasta = consultarPorTituloDePieza(titulo);
        ModeloOferta ofertaMayor = logicaOfertas.consultarOfertaMayor(subasta.getId());
        if (ofertaMayor == null) {
            throw new Exception("No se encontró ninguna oferta ganadora");
        }
        return ofertaMayor.getCantidad();
    }
}
