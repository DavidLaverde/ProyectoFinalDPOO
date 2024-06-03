package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface Logica<T> {
    T crear(T nuevo) throws Exception;
    T consultar(String identificador) throws Exception;
    Map<String, T> consultarTodos(); // Cambiado de Map<Integer, T> a Map<String, T>
    T editar(T entidad) throws Exception;
    void eliminar(String identificador) throws Exception;
    Integer cargarDatos(String ruta) throws FileNotFoundException, IOException;
}
