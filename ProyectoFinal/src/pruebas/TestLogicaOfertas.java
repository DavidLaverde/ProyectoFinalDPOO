package pruebas;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controlador.LogicaOfertas;
import modelo.ModeloOferta;

class TestLogicaOfertas {

    private LogicaOfertas logicaOfertas;

    @BeforeEach
    void setUp() {
        logicaOfertas = new LogicaOfertas();
    }

    @Test
    void testCrearOferta() {
        try {
            ModeloOferta oferta = new ModeloOferta(3, "cliente@email.com", 200.0);
            ModeloOferta ofertaCreada = logicaOfertas.crearOferta(oferta);
            assertNotNull(ofertaCreada, "Se esperaba que se creara la oferta correctamente");
            assertEquals(oferta.getIdSubasta(), ofertaCreada.getIdSubasta(), "El id de subasta de la oferta creada no coincide");
            assertEquals(oferta.getEmailOfertante(), ofertaCreada.getEmailOfertante(), "El correo del ofertante de la oferta creada no coincide");
        } catch (Exception e) {
            fail("Se produjo un error al crear la oferta: " + e.getMessage());
        }
    }

    @Test
    void testConsultarOfertaMayor() {
        try {
            ModeloOferta ofertaMayor = logicaOfertas.consultarOfertaMayor(3);
            assertNotNull(ofertaMayor, "Se esperaba que se encontrara una oferta mayor");
        } catch (Exception e) {
            fail("Se produjo un error al consultar la oferta mayor: " + e.getMessage());
        }
    }
}
