package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import modelo.ModeloRol;
import modelo.ModeloUsuario;
import modelo.PersistenciaUsuario;
import utilidades.Utilidades;

public class LogicaUsuarios implements Logica<ModeloUsuario> {

    private final PersistenciaUsuario persistenciaUsuario = new PersistenciaUsuario();

    public ModeloUsuario iniciarSesion(String correo, String contrasena, ModeloRol rol) {
        ModeloUsuario modeloUsuario = PersistenciaUsuario.usuarios.get(correo);
        if (modeloUsuario != null && modeloUsuario.getRol().equals(rol) && modeloUsuario.getContrasena().equals(contrasena)) {
            return modeloUsuario;
        }
        return null;
    }
    
    @Override
    public ModeloUsuario crear(ModeloUsuario nuevo) throws Exception {
        validarNuevoUsuario(nuevo);
        PersistenciaUsuario.usuarios.put(nuevo.getCorreo(), nuevo);
        persistenciaUsuario.actualizar();
        return nuevo;
    }

    @Override
    public ModeloUsuario consultar(String identificador) throws Exception {
        ModeloUsuario usuario = PersistenciaUsuario.usuarios.get(identificador);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }
        return usuario;
    }

    @Override
    public ModeloUsuario editar(ModeloUsuario usuario) throws Exception {
        if (!PersistenciaUsuario.usuarios.containsKey(usuario.getCorreo())) {
            throw new Exception("Usuario no encontrado");
        }
        PersistenciaUsuario.usuarios.put(usuario.getCorreo(), usuario);
        persistenciaUsuario.actualizar();
        return usuario;
    }

    @Override
    public void eliminar(String identificador) throws Exception {
        if (!PersistenciaUsuario.usuarios.containsKey(identificador)) {
            throw new Exception("Usuario no encontrado");
        }
        PersistenciaUsuario.usuarios.remove(identificador);
        persistenciaUsuario.actualizar();
    }

    @Override
    public HashMap<String, ModeloUsuario> consultarTodos() {
        return new HashMap<>(PersistenciaUsuario.usuarios);
    }

    public HashMap<String, ModeloUsuario> consultarClientes() {
        HashMap<String, ModeloUsuario> filtro = new HashMap<>();
        for (ModeloUsuario usuario : PersistenciaUsuario.usuarios.values()) {
            if (usuario.getRol().equals(ModeloRol.CLIENTE)) {
                filtro.put(usuario.getCorreo(), usuario);
            }
        }
        return filtro;
    }

    @Override
    public Integer cargarDatos(String ruta) throws FileNotFoundException, IOException {
        persistenciaUsuario.cargar(ruta);
        return PersistenciaUsuario.usuarios.size();
    }

    private void validarNuevoUsuario(ModeloUsuario nuevoUsuario) throws Exception {
        if (PersistenciaUsuario.usuarios.containsKey(nuevoUsuario.getCorreo())) {
            throw new Exception("Correo duplicado");
        }
        if (nuevoUsuario.getTelefono().length() != 10) {
            throw new Exception("El teléfono debe tener 10 dígitos");
        }
        if (!Utilidades.esNumerico(nuevoUsuario.getTelefono())) {
            throw new Exception("El teléfono debe ser numérico");
        }
    }
}