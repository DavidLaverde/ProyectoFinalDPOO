package modelo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public interface Persistencia {
    
    public HashMap cargar(String ruta) throws FileNotFoundException, IOException;

    public void actualizar() throws IOException;
}
