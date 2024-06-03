package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;

import modelo.ModeloEstado;
import modelo.ModeloEstadoSubasta;
import modelo.ModeloOferta;
import modelo.ModeloPiezas;
import modelo.ModeloSubasta;
import modelo.ModeloUsuario;
import modelo.PersistenciaOfertas;

public class LogicaOfertas {
    private final PersistenciaOfertas persistenciaOfertas = new PersistenciaOfertas();
    private final LogicaUsuarios logicaUsuarios = new LogicaUsuarios();
    private final LogicaPiezas logicaPiezas = new LogicaPiezas();
    private final LogicaSubastas logicaSubastas = new LogicaSubastas();

    public Integer cargarDatos(String ruta) throws FileNotFoundException, IOException {
        return persistenciaOfertas.cargarOfertas(ruta).size();
    }

    public ModeloOferta crearOferta(ModeloOferta oferta) throws Exception {
        validarOferta(oferta);
        PersistenciaOfertas.ofertas.add(oferta);
        persistenciaOfertas.actualizar();
        return oferta;
    }

    private void validarOferta(ModeloOferta oferta) throws Exception {
        ModeloUsuario usuario = logicaUsuarios.consultar(oferta.getEmailOfertante());
        ModeloSubasta subasta = logicaSubastas.consultar(oferta.getIdSubasta().toString());
        ModeloPiezas pieza = logicaPiezas.consultar(subasta.getTituloPieza());
        ModeloOferta ofertaMayor = consultarOfertaMayor(oferta.getIdSubasta());

        validarUsuarioAutorizado(usuario);
        validarPiezaEnSubasta(pieza);
        validarSubastaAbierta(subasta);
        validarMontoOferta(oferta, usuario, subasta, ofertaMayor);
    }

    private void validarUsuarioAutorizado(ModeloUsuario usuario) throws Exception {
        if (!usuario.getAutorizado()) {
            throw new Exception("No se puede crear la oferta: Usuario no autorizado");
        }
    }

    private void validarPiezaEnSubasta(ModeloPiezas pieza) throws Exception {
        if (!pieza.getEstado().equals(ModeloEstado.EN_SUBASTA)) {
            throw new Exception("No se puede crear la oferta: La pieza no está en subasta");
        }
    }

    private void validarSubastaAbierta(ModeloSubasta subasta) throws Exception {
        if (!subasta.getEstado().equals(ModeloEstadoSubasta.ABIERTA)) {
            throw new Exception("No se puede crear la oferta: La subasta no está abierta");
        }
    }

    private void validarMontoOferta(ModeloOferta oferta, ModeloUsuario usuario, ModeloSubasta subasta, ModeloOferta ofertaMayor) throws Exception {
        if (usuario.getLimitePago() < oferta.getCantidad()) {
            throw new Exception("No se puede crear la oferta: El monto excede el límite de pago del usuario");
        }

        if (oferta.getCantidad() < subasta.getMinimo()) {
            throw new Exception("No se puede crear la oferta: El monto es menor al monto mínimo de la subasta");
        }

        if (ofertaMayor != null && ofertaMayor.getCantidad() >= oferta.getCantidad()) {
            throw new Exception("No se puede crear la oferta: El monto es menor o igual a la oferta mayor existente");
        }
    }

    public ModeloOferta consultarOfertaMayor(Integer idSubasta) {
        return PersistenciaOfertas.ofertas.stream()
            .filter(oferta -> oferta.getIdSubasta().equals(idSubasta))
            .max((o1, o2) -> o1.getCantidad().compareTo(o2.getCantidad()))
            .orElse(null);
    }
}
