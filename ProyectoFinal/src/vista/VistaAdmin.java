package vista;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.LogicaPiezas;
import controlador.LogicaUsuarios;
import controlador.LogicaVentas;
import modelo.ModeloPiezas;
import modelo.ModeloRol;
import modelo.ModeloUsuario;
import modelo.ModeloVenta;
import utilidades.Utilidades;

public class VistaAdmin {
    private static final Logger logger = Logger.getLogger(VistaAdmin.class.getName());
    private static final String RUTA = "./src/datos/";
    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String ROL_CLIENTE = "CLIENTE";
    
    private LogicaUsuarios logicaUsuarios = new LogicaUsuarios();
    private VistaPiezas vistaPiezas = new VistaPiezas();

    public static void main(String[] args) {
        VistaAdmin vista = new VistaAdmin();
        boolean continuar = true;

        Utilidades.cargarDatos(RUTA);
        while (continuar) {
            boolean autenticado = vista.iniciarSesion();
            if (autenticado) {
                vista.ejecutarOpciones();
            }
        }
    }

    private boolean iniciarSesion() {
        System.out.println("--------------------");
        System.out.println("BIENVENIDO A LA APP PARA ADMINISTRADORES DE GALERIANDES");
        String correo = Utilidades.lectorConsola("Ingrese su correo: ");
        String contrasena = Utilidades.lectorConsola("Ingrese su contraseña: ");
        System.out.println("--------------------");
        ModeloUsuario autenticado = logicaUsuarios.iniciarSesion(correo, contrasena, ModeloRol.valueOf(ROL_ADMINISTRADOR));
        if (autenticado == null) {
            System.out.println("Autenticación fallida");
            return false;
        }
        System.out.println("Bienvenido");
        return true;
    }

    private void imprimirOpciones() {
        System.out.println("--------------------");
        System.out.println("1. Consultar Usuarios");
        System.out.println("2. Consultar Clientes");
        System.out.println("3. Registrar Usuario");
        System.out.println("4. Eliminar Usuario");
        System.out.println("5. Autorizar comprador");
        System.out.println("6. Cambiar límite de compra de usuario");
        System.out.println("7. Consultar piezas");
        System.out.println("8. Registrar pieza");
        System.out.println("9. Devolver pieza");
        System.out.println("10. Mover pieza");
        System.out.println("11. Consultar subastas");
        System.out.println("12. Iniciar subasta");
        System.out.println("13. Abortar subasta");
        System.out.println("14. Consultar ofertas de compra");
        System.out.println("15. Aprobar compra");
        System.out.println("16. Confirmar venta");
        System.out.println("17. Denegar compra");
        System.out.println("18. Consultar historial de una pieza");
        System.out.println("19. Consultar perfil de un artista");
        System.out.println("20. Consultar perfil de un cliente");
        System.out.println("21. Salir");
        System.out.println("--------------------");
    }

    private void ejecutarOpciones() {
        boolean continuar = true;
        while (continuar) {
            Utilidades.lectorConsola("Enter para continuar ->");
            imprimirOpciones();
            int opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    imprimirUsuarios(logicaUsuarios.consultarTodos());
                    break;
                case 2:
                    imprimirUsuarios(logicaUsuarios.consultarClientes());
                    break;
                case 3:
                    registrarUsuario();
                    break;
                case 4:
                    eliminarUsuario();
                    break;
                case 5:
                    cambiarAutorizacionCliente();
                    break;
                case 6:
                    cambiarLimiteCliente();
                    break;
                case 7:
                    vistaPiezas.imprimirTodasLasPiezas();
                    break;
                case 8:
                    vistaPiezas.registrarPieza();
                    break;
                case 9:
                    vistaPiezas.devolverPieza();
                    break;
                case 10:
                    vistaPiezas.moverPieza();
                    break;
                case 11:
                    vistaPiezas.consultarPiezasEnSubasta();
                    break;
                case 12:
                    vistaPiezas.iniciarSubasta();
                    break;
                case 13:
                    vistaPiezas.abortarSubasta();
                    break;
                case 14:
                    vistaPiezas.consultarSolicitudesDeCompra();
                    break;
                case 15:
                    vistaPiezas.aprobarCompra();
                    break;
                case 16:
                    vistaPiezas.confirmarVenta();
                    break;
                case 17:
                    vistaPiezas.desbloquearPieza();
                    break;
                case 18:
                    vistaPiezas.consultarHistorialPieza();
                    break;
                case 19:
                    vistaPiezas.consultarPerfilArtista();
                    break;
                case 20:
                    consultarPerfilCliente();
                    break;
                case 21:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
                    break;
            }
        }
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(Utilidades.lectorConsola("Ingrese opción a escoger: "));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void consultarPerfilCliente() {
        try {
            LogicaVentas logicaVentas = new LogicaVentas();
            LogicaPiezas logicaPiezas = new LogicaPiezas();
            String correo = Utilidades.lectorConsola("Ingrese el correo del cliente: ");
            ModeloUsuario u = logicaUsuarios.consultar(correo);
            if (!u.getRol().equals(ModeloRol.valueOf(ROL_CLIENTE))) {
                throw new Exception("El usuario no es un cliente");
            }
            imprimirPerfil(u);
            imprimirComprasCliente(logicaVentas, u);
            imprimirPiezasCliente(logicaPiezas, u);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error consultando el perfil del cliente", e);
        }
    }

    private void imprimirComprasCliente(LogicaVentas logicaVentas, ModeloUsuario u) {
        System.out.println("Compras realizadas: ");
        logicaVentas.consultarTodasLasVentas().values().stream()
                .filter(v -> v.getEmailNuevoDueno().equals(u.getCorreo()))
                .forEach(v -> System.out.println("-> " + v.getId() + ", " + v.getTituloPieza() + ", " + v.getEmailNuevoDueno() + ", " + v.getPrecio() + ", " + v.getFecha()));
    }

    private void imprimirPiezasCliente(LogicaPiezas logicaPiezas, ModeloUsuario u) {
        System.out.println("Piezas de las que es dueño: ");
        logicaPiezas.consultarTodos().values().stream()
                .filter(p -> p.getEmailDueno().equals(u.getCorreo()))
                .forEach(vistaPiezas::imprimirPieza);
    }

    private void imprimirUsuarios(HashMap<String, ModeloUsuario> usuarios) {
        usuarios.values().forEach(u -> {
            System.out.println("-> " + u.getNombre() + ", " + u.getCorreo() + ", " + u.getRol() + ", " + u.getTelefono());
            if (u.getRol().equals(ModeloRol.valueOf(ROL_CLIENTE))) {
                System.out.println("    -> Autorizado: " + u.getAutorizado() + ", límite de pagos: " + u.getLimitePago());
            }
        });
    }

    private void imprimirPerfil(ModeloUsuario u) {
        System.out.println("-> " + u.getNombre() + ", " + u.getCorreo() + ", " + u.getRol() + ", " + u.getTelefono());
    }

    private void eliminarUsuario() {
        String correo = Utilidades.lectorConsola("Inserte el correo del usuario: ");
        try {
            logicaUsuarios.eliminar(correo);
            System.out.println("Usuario eliminado exitosamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error eliminando el usuario", e);
        }
    }

    private void registrarUsuario() {
        try {
            String nombre = Utilidades.lectorConsola("Ingrese nombre: ");
            String correo = Utilidades.lectorConsola("Ingrese correo: ");
            String contrasena = Utilidades.lectorConsola("Ingrese contraseña: ");
            ModeloRol rol = obtenerRol();
            String telefono = obtenerTelefono();
            ModeloUsuario nuevoUsuario = new ModeloUsuario(nombre, correo, rol, contrasena, telefono);
            if (nuevoUsuario.getRol().equals(ModeloRol.valueOf(ROL_CLIENTE))) {
                nuevoUsuario.setAutorizado(false);
                nuevoUsuario.setLimitePago(0.0);
            }
            logicaUsuarios.crear(nuevoUsuario);
            System.out.println("Usuario creado exitosamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creando usuario", e);
        }
    }

    private ModeloRol obtenerRol() {
        System.out.println("Roles disponibles: ADMINISTRADOR, CLIENTE, EMPLEADO");
        while (true) {
            try {
                return ModeloRol.valueOf(Utilidades.lectorConsola("Ingrese rol: "));
            } catch (IllegalArgumentException e) {
                System.out.println("Rol incorrecto, intente de nuevo.");
            }
        }
    }

    private String obtenerTelefono() {
        while (true) {
            try {
                String telefono = Utilidades.lectorConsola("Ingrese Telefono (10 dígitos): ");
                if (telefono.length() == 10 && Utilidades.esNumerico(telefono)) {
                    return telefono;
                }
                throw new IllegalArgumentException("El teléfono debe tener 10 dígitos y solo contener caracteres numéricos");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void cambiarAutorizacionCliente() {
        try {
            String correo = Utilidades.lectorConsola("Ingrese el correo del cliente: ");
            ModeloUsuario usuario = logicaUsuarios.consultar(correo);
            if (!usuario.getRol().equals(ModeloRol.valueOf(ROL_CLIENTE))) {
                throw new Exception("El usuario debe ser un cliente");
            }
            Boolean estadoPrevio = usuario.getAutorizado();
            usuario.setAutorizado(!estadoPrevio);
            logicaUsuarios.editar(usuario);
            System.out.println("Autorización modificada de " + estadoPrevio + " a -> " + usuario.getAutorizado());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cambiando la autorización del cliente", e);
        }
    }

    private void cambiarLimiteCliente() {
        try {
            String correo = Utilidades.lectorConsola("Ingrese el correo del cliente: ");
            ModeloUsuario usuario = logicaUsuarios.consultar(correo);
            if (!usuario.getRol().equals(ModeloRol.valueOf(ROL_CLIENTE))) {
                throw new Exception("El usuario debe ser un cliente");
            }
            Double estadoPrevio = usuario.getLimitePago();
            System.out.println("Estado actual -> " + estadoPrevio);
            Double nuevo = Double.parseDouble(Utilidades.lectorConsola("Ingrese el nuevo monto máximo (con punto decimal): "));
            usuario.setLimitePago(nuevo);
            logicaUsuarios.editar(usuario);
            System.out.println("Autorización modificada de " + estadoPrevio + " a -> " + usuario.getLimitePago());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cambiando el límite de pago del cliente", e);
        }
    }
}
