package vista;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap; // Importación añadida
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.LogicaOfertas;
import controlador.LogicaPiezas;
import controlador.LogicaSubastas;
import controlador.LogicaVentas;
import modelo.*;
import utilidades.Utilidades;

public class VistaPiezas {

    private static final Logger logger = Logger.getLogger(VistaPiezas.class.getName());

    private final LogicaPiezas logicaPiezas = new LogicaPiezas();
    private final LogicaVentas logicaVentas = new LogicaVentas();
    private final LogicaSubastas logicaSubastas = new LogicaSubastas();
    private final LogicaOfertas logicaOfertas = new LogicaOfertas();

    // GENERAL

    public void imprimirTodasLasPiezas() {
        Map<String, ModeloPiezas> piezas = logicaPiezas.consultarTodos();
        imprimirPiezas(piezas);
    }

    private void imprimirPiezas(Map<String, ModeloPiezas> piezas) {
        if (piezas.isEmpty()) {
            logger.info("Resultado vacío");
        } else {
            piezas.values().forEach(this::imprimirPieza);
        }
    }

    public void imprimirPieza(ModeloPiezas p) {
        logger.info("-> " + p.getTitulo() + ", " + p.getTipo() + ", Autor: " + p.getNombreAutor() + ", Dueño: "
                + p.getEmailDueno());
        logger.info("    -> Materiales: " + p.getMateriales());
        logger.info("    -> Bloqueada: " + p.getBloqueada()); // Corregido de getBloqueda a getBloqueada
        logger.info("    -> Estado: " + p.getEstado());
        logger.info("    -> Precio mínimo: " + p.getPrecioMinimo());
        logger.info("    -> Tipo de venta permitido: " + p.getTipoDeVenta());
        if (p.getBloqueada()) { // Corregido de getBloqueda a getBloqueada
            logger.info("    -> Bloqueada por: " + p.getEmailOfertante());
        }
    }

    // ADMINISTRADOR

    public void registrarPieza() {
        try {
            String titulo = Utilidades.lectorConsola("Ingrese nombre -> ");
            String nombreAutor = Utilidades.lectorConsola("Ingrese el nombre del autor -> ");
            String emailDueno = Utilidades.lectorConsola("Ingrese el email del dueño -> ");

            System.out.print("Tipos de pieza: ");
            Arrays.stream(ModeloTiposPieza.values()).forEach(p -> System.out.print(" " + p));
            System.out.print("\n");

            ModeloTiposPieza tipo = ModeloTiposPieza.valueOf(Utilidades.lectorConsola("Ingrese el tipo de pieza -> "));
            ArrayList<String> materiales = new ArrayList<>(
                    Arrays.asList(Utilidades.lectorConsola("Ingrese los materiales separados por espacios -> ").split(" ")));
            boolean bloqueada = false;
            double precioMinimo = Double.parseDouble(Utilidades.lectorConsola("Ingrese el precio mínimo con punto decimal -> "));
            System.out.println("Estados posibles: BODEGA, EXHIBICION");
            ModeloEstado estado = ModeloEstado.valueOf(Utilidades.lectorConsola("Ingrese el estado inicial de la pieza -> "));
            System.out.println("Tipos de venta posibles: " + Arrays.asList(ModeloTipoDeVenta.values()));
            ModeloTipoDeVenta tipoDeVenta = ModeloTipoDeVenta
                    .valueOf(Utilidades.lectorConsola("Ingrese el tipo de venta de la pieza -> "));
            ModeloPiezas pieza = new ModeloPiezas(titulo, nombreAutor, emailDueno, tipo, materiales, bloqueada,
                    precioMinimo, estado, null, tipoDeVenta);
            logicaPiezas.crear(pieza);
            logger.info("Pieza creada correctamente");
            imprimirPieza(pieza);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error registrando una pieza", e);
        }
    }

    public void devolverPieza() {
        try {
            System.out.println("Devolver una pieza: Seleccione una pieza que no esté bloqueada ni en subasta");
            String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza que desea devolver -> ");
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            logicaPiezas.verificarDisponible(pieza);
            pieza.setEstado(ModeloEstado.DEVUELTA);
            logicaPiezas.editar(pieza);
            logger.info("La pieza ha sido devuelta a su dueño exitosamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al editar la información de una pieza", e);
        }
    }

    public void moverPieza() {
        ArrayList<ModeloEstado> estadosValidos = new ArrayList<>(
                Arrays.asList(ModeloEstado.BODEGA, ModeloEstado.EXHIBICION));

        try {
            String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza que desea mover -> ");
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            logicaPiezas.verificarDisponible(pieza);

            System.out.println("Mover una Pieza. Los posibles estados son: " + estadosValidos);
            ModeloEstado estado = obtenerEstadoValido(estadosValidos);

            pieza.setEstado(estado);
            logicaPiezas.editar(pieza);
            logger.info("La pieza fue movida exitosamente a -> " + estado);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error moviendo la pieza", e);
        }
    }

    private ModeloEstado obtenerEstadoValido(ArrayList<ModeloEstado> estadosValidos) {
        ModeloEstado estado = null;
        while (estado == null) {
            try {
                estado = ModeloEstado.valueOf(Utilidades.lectorConsola("Ingrese el estado -> "));
                if (!estadosValidos.contains(estado)) {
                    throw new Exception("Estado inválido");
                }
            } catch (Exception e) {
                logger.warning("Estado inválido");
                estado = null;
            }
        }
        return estado;
    }

    public void desbloquearPieza() {
        String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza a desbloquear -> ");
        try {
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            pieza.setBloqueada(false); // Corregido de setBloqueda a setBloqueada
            pieza.setEstado(ModeloEstado.BODEGA);
            pieza.setEmailOfertante(null);
            logicaPiezas.editar(pieza);
            logger.info("Pieza desbloqueada exitosamente");
            logger.info("La pieza fue regresada a la bodega");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error desbloqueando la pieza", e);
        }
    }

    public void consularPiezasEnSubasta() {
        Map<String, ModeloPiezas> filtro = new HashMap<>(); // Importación de HashMap
        for (ModeloPiezas p : PersistenciaPieza.piezas.values()) {
            boolean condicion = p.getEstado().equals(ModeloEstado.EN_SUBASTA)
                    && (p.getTipoDeVenta().equals(ModeloTipoDeVenta.SUBASTA)
                            || p.getTipoDeVenta().equals(ModeloTipoDeVenta.TODO));

            if (condicion) {
                filtro.put(p.getTitulo(), p);
            }
        }
        imprimirPiezas(filtro);
    }

    public void iniciarSubasta() {
        String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza a subastar -> ");
        try {
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            if (!(pieza.getTipoDeVenta().equals(ModeloTipoDeVenta.SUBASTA)
                    || pieza.getTipoDeVenta().equals(ModeloTipoDeVenta.TODO))) {
                throw new Exception("Esta pieza no se puede subastar.");
            }
            logicaPiezas.verificarDisponible(pieza);
            pieza.setEstado(ModeloEstado.EN_SUBASTA);
            logicaPiezas.editar(pieza);

            double inicial = Double.parseDouble(Utilidades.lectorConsola("Ingrese el valor inicial de la subasta con punto decimal -> "));
            ModeloSubasta subasta = new ModeloSubasta(LocalDate.now(), inicial, pieza);
            logicaSubastas.crear(subasta);

            logger.info("Subasta creada con éxito. Id -> " + subasta.getId());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al iniciar subasta", e);
        }
    }
    
 // Método para consultar las solicitudes de compra de una pieza en la clase VistaPiezas
    public void consultarSolicitudesDeCompra() {
        String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza para consultar las solicitudes de compra: ");
        try {
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            if (pieza != null) {
                Map<Integer, ModeloVenta> solicitudes = logicaVentas.consultarSolicitudesDeCompra(pieza);
                if (!solicitudes.isEmpty()) {
                    System.out.println("Solicitudes de compra para la pieza '" + titulo + "':");
                    for (ModeloVenta solicitud : solicitudes.values()) {
                        System.out.println("ID: " + solicitud.getId() + ", Comprador: " + solicitud.getNombreComprador() +
                                ", Precio Ofrecido: " + solicitud.getPrecioOfrecido() + ", Fecha: " + solicitud.getFecha());
                    }
                } else {
                    System.out.println("No hay solicitudes de compra para la pieza '" + titulo + "'.");
                }
            } else {
                System.out.println("La pieza con el título '" + titulo + "' no existe.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al consultar las solicitudes de compra de la pieza", e);
        }
    }

    // Método para aprobar una solicitud de compra en la clase VistaPiezas
    public void aprobarCompra() {
        int ventaId = Integer.parseInt(Utilidades.lectorConsola("Ingrese el ID de la venta a aprobar: "));
        try {
            ModeloVenta venta = logicaVentas.consultarVenta(ventaId);
            if (venta != null) {
                logicaVentas.aprobarVenta(venta);
                System.out.println("La venta con ID " + ventaId + " ha sido aprobada.");
            } else {
                System.out.println("No se encontró ninguna venta con el ID " + ventaId + ".");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al aprobar la venta", e);
        }
    }


    public void abortarSubasta() {
        String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza en subasta -> ");
        try {
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            ModeloSubasta subasta = logicaSubastas.consultarPorPieza(pieza);
            logicaPiezas.verificarBloqueada(pieza);
            subasta.setEstado(ModeloEstadoSubasta.ABORTADA);
            logicaSubastas.editar(subasta);
            pieza.setEstado(ModeloEstado.BODEGA);
            logicaPiezas.editar(pieza);
            logger.info("Subasta abortada exitosamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al abortar la subasta", e);
        }
    }

    // CLIENTE

    public void eliminarPieza() {
        try {
            String titulo = Utilidades.lectorConsola("Ingrese el título de la pieza que desea eliminar -> ");
            ModeloPiezas pieza = logicaPiezas.consultar(titulo);
            logicaPiezas.verificarBloqueada(pieza);
            logicaPiezas.eliminar(pieza.getTitulo());
            logger.info("Pieza eliminada exitosamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error eliminando la pieza", e);
        }
    }
}