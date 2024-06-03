package vista;

import java.util.logging.Level;
import java.util.logging.Logger;

import controlador.LogicaUsuarios;
import modelo.ModeloRol;
import modelo.ModeloUsuario;
import utilidades.Utilidades;

public class VistaCliente {
    private static final Logger logger = Logger.getLogger(VistaCliente.class.getName());
    private static final String RUTA_DATOS = "./src/datos/";
    
    private LogicaUsuarios logicaUsuarios = new LogicaUsuarios();
    private ModeloUsuario usuarioActual;
    private VistaPiezas vistasPiezas = new VistaPiezas();

    public static void main(String[] args) {
        VistaCliente vista = new VistaCliente();
        Utilidades.cargarDatos(RUTA_DATOS);
        
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
        System.out.println("BIENVENIDO A LA APP PARA CLIENTES DE GALERIANDES");
        String correo = Utilidades.lectorConsola("Ingrese su correo: ");
        String contrasena = Utilidades.lectorConsola("Ingrese su contraseña: ");
        System.out.println("--------------------");
        ModeloUsuario autenticado = logicaUsuarios.iniciarSesion(correo, contrasena, ModeloRol.CLIENTE);
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
        System.out.println("2. Consultar piezas en venta");
        System.out.println("3. Solicitar compra de pieza");
        System.out.println("4. Consultar mis piezas");
        System.out.println("5. Consultar piezas bloqueadas por mi");
        System.out.println("6. Consultar historial de una pieza");
        System.out.println("7. Consultar perfil de un artista");
        System.out.println("8. Salir");
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
                    vistasPiezas.consultarPiezasEnVenta();
                    break;
                case 3:
                    vistasPiezas.solicitarCompraDePieza(usuarioActual);
                    break;
                case 4:
                    vistasPiezas.consultarMisPiezas(usuarioActual);
                    break;
                case 5:
                    vistasPiezas.consultarPiezasBloqueadasPorCliente(usuarioActual);
                    break;
                case 6:
                    vistasPiezas.consultarHistorialPieza();
                    break;
                case 7:
                    vistasPiezas.consultarPerfilArtista();
                    break;
                case 8:
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
        System.out.println("    -> Autorizado: " + u.getAutorizado());
        System.out.println("    -> Límite de compras: " + u.getLimitePago());
    }
}
