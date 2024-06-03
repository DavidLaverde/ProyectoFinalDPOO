package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import modelo.ModeloPiezas;
import modelo.ModeloRol;
import modelo.ModeloUsuario;
import modelo.PersistenciaPieza;

public class LogicaPiezas implements Logica<ModeloPiezas> {

    private final PersistenciaPieza persistenciaPieza = new PersistenciaPieza();
    private final LogicaUsuarios logicaUsuarios = new LogicaUsuarios();

    public void verificarBloqueada(ModeloPiezas pieza) throws Exception {
        if (pieza.getBloqueada()) {
            throw new Exception("La pieza está bloqueada.");
        }
    }
    
    @Override
    public ModeloPiezas crear(ModeloPiezas nuevo) throws Exception {
        verificarExistenciaPieza(nuevo);
        validarPropietarioPieza(nuevo);

        PersistenciaPieza.piezas.put(nuevo.getTitulo(), nuevo);
        persistenciaPieza.actualizar();

        return nuevo;
    }

    private void verificarExistenciaPieza(ModeloPiezas nuevo) throws Exception {
        if (PersistenciaPieza.piezas.containsKey(nuevo.getTitulo())) {
            throw new Exception("Ya existe una pieza con ese nombre");
        }
    }

    private void validarPropietarioPieza(ModeloPiezas nuevo) throws Exception {
        ModeloUsuario usuario = logicaUsuarios.consultar(nuevo.getEmailDueno());
        if (usuario == null) {
            throw new Exception("No se encontró un cliente con el email asociado");
        } else if (!usuario.getRol().equals(ModeloRol.CLIENTE)) {
            throw new Exception("El usuario asociado al correo no es un cliente");
        }
    }

    @Override
    public ModeloPiezas consultar(String identificador) throws Exception {
        ModeloPiezas pieza = PersistenciaPieza.piezas.get(identificador);
        if (pieza == null) {
            throw new Exception("Pieza no encontrada");
        }
        return pieza;
    }

    @Override
    public Map<String, ModeloPiezas> consultarTodos() { // Cambiado de HashMap a Map
        return PersistenciaPieza.piezas;
    }

    @Override
    public ModeloPiezas editar(ModeloPiezas pieza) throws Exception {
        if (!PersistenciaPieza.piezas.containsKey(pieza.getTitulo())) {
            throw new Exception("Pieza no encontrada");
        }

        PersistenciaPieza.piezas.put(pieza.getTitulo(), pieza);
        persistenciaPieza.actualizar();
        return pieza;
    }

    @Override
    public void eliminar(String identificador) throws Exception {
        if (!PersistenciaPieza.piezas.containsKey(identificador)) {
            throw new Exception("Pieza no encontrada");
        }

        PersistenciaPieza.piezas.remove(identificador);
        persistenciaPieza.actualizar();
    }

    @Override
    public Integer cargarDatos(String ruta) throws FileNotFoundException, IOException {
        persistenciaPieza.cargar(ruta);
        return PersistenciaPieza.piezas.size();
    }

    public void verificarDisponible(ModeloPiezas pieza) throws Exception {
        if (pieza.getBloqueada()) {
            throw new Exception("La pieza está bloqueada");
        }

        switch (pieza.getEstado()) {
            case DEVUELTA:
                throw new Exception("La pieza ya ha sido devuelta con anterioridad");
            case EN_SUBASTA:
                throw new Exception("La pieza está en un proceso de subasta");
            case VENDIDA:
                throw new Exception("La pieza ya ha sido vendida");
            case EN_CHECKOUT:
                throw new Exception("La pieza está en checkout esperando el pago de compra");
            case PAGADA:
                throw new Exception("La pieza ya ha sido pagada y está esperando confirmación del administrador para ser retirada");
            default:
                break;
        }
    }

    public Map<String, ModeloPiezas> consultarPorAutor(String nombreAutor) { // Cambiado de HashMap a Map
        Map<String, ModeloPiezas> piezasPorAutor = new HashMap<>();
        for (ModeloPiezas pieza : PersistenciaPieza.piezas.values()) {
            if (pieza.getNombreAutor().equals(nombreAutor)) {
                piezasPorAutor.put(pieza.getTitulo(), pieza);
            }
        }
        return piezasPorAutor;
    }
}
