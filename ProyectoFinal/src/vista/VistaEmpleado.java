package vista;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.LogicaUsuarios;
import modelo.ModeloRol;
import modelo.ModeloUsuario;
import utilidades.Utilidades;

public class VistaEmpleado {
    private static final Logger logger = Logger.getLogger(VistaEmpleado.class.getName());
    private static final String RUTA_DATOS = "./src/datos/";

    private LogicaUsuarios logicaUsuarios = new LogicaUsuarios();
    private VistaPiezas vistasPiezas = new VistaPiezas();
    private ModeloUsuario usuarioActual;

    public static void main(String[] args) {
        Utilidades.cargarDatos(RUTA_DATOS);
        VistaEmpleado vista = new VistaEmpleado();
        boolean continuar = true;
        while (continuar) {
            boolean autenticado = vista.iniciarSesion();
            if (autenticado) {
                vista.ejecutarOpciones();
            }
        }
    }

    private boolean iniciarSesion() {
        System.out.println("--------------------");
        System.out.println("BIENVENIDO A LA APP PARA EMPLEADOS DE GALERIANDES");
        String correo = Utilidades.lectorConsola("Ingrese su correo: ");
        String contrasena = Utilidades.lectorConsola("Ingrese su contraseña: ");
        System.out.println("--------------------");
        ModeloUsuario autenticado = logicaUsuarios.iniciarSesion(correo, contrasena, ModeloRol.EMPLEADO);
        if (autenticado == null) {
            System.out.println("Autenticación fallida");
            return false;
        }
        System.out.println("Bienvenido");
        this.usuarioActual = autenticado;
        return true;
    }

    private void imprimirOpciones() {
        System.out.println("--------------------");
        System.out.println("1. Consultar perfil");
        System.out.println("2. Consultar clientes");
        System.out.println("3. Consultar piezas en check-out");
        System.out.println("4. Recibir pago por pieza");
        System.out.println("5. Consultar piezas en subasta");
        System.out.println("6. Consultar procesos de subasta");
        System.out.println("7. Registrar oferta en subasta");
        System.out.println("8. Finalizar subasta");
        System.out.println("9. Consultar historial de una pieza");
        System.out.println("10. Consultar historial de un artista");
        System.out.println("11. Salir");
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
                    imprimirPerfil();
                    break;
                case 2:
                    imprimirUsuarios(logicaUsuarios.consultarClientes());
                    break;
                case 3:
                    vistasPiezas.consultarPiezasEnCheckout();
                    break;
                case 4:
                    vistasPiezas.recibirPagoPorPieza();
                    break;
                case 5:
                    vistasPiezas.consultarPiezasEnSubasta();
                    break;
                case 6:
                    vistasPiezas.consultarProcesosDeSubasta();
                    break;
                case 7:
                    vistasPiezas.registrarOfertaEnSubasta();
                    break;
                case 8:
                    vistasPiezas.finalizarSubasta();
                    break;
                case 9:
                    vistasPiezas.consultarHistorialPieza();
                    break;
                case 10:
                    vistasPiezas.consultarPerfilArtista();
                    break;
                case 11:
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

    private void imprimirPerfil() {
        ModeloUsuario u = this.usuarioActual;
        System.out.println("-> " + u.getNombre() + ", " + u.getCorreo() + ", " + u.getRol() + ", " + u.getTelefono());
    }

    private void imprimirUsuarios(HashMap<String, ModeloUsuario> usuarios) {
        usuarios.values().forEach(u -> {
            System.out.println("-> " + u.getNombre() + ", " + u.getCorreo() + ", " + u.getRol() + ", " + u.getTelefono());
            if (u.getRol().equals(ModeloRol.CLIENTE)) {
                System.out.println("    -> Autorizado: " + u.getAutorizado() + ", límite de pagos: " + u.getLimitePago());
            }
        });
    }
}
