package modelo;

import java.time.LocalDate;
import java.util.HashMap;

public class ModeloVenta {
    private Integer id;
    private String tituloPieza;
    private String emailDuenoAnterior;
    private String emailNuevoDueno;
    private Double precio;
    private LocalDate fecha;
    private ModeloTiposPago tipoPago;
    private ModeloTipoDeVenta tipoVenta;
    private Boolean confirmadoAdmin;

    public ModeloVenta(Integer id, String tituloPieza, String emailDuenoAnterior, String emailNuevoDueno, Double precio,
            LocalDate fecha, ModeloTiposPago tipoPago, ModeloTipoDeVenta tipoDeVenta, Boolean confirmadoAdmin) {
        this.id = id;
        this.tituloPieza = tituloPieza;
        this.emailDuenoAnterior = emailDuenoAnterior;
        this.emailNuevoDueno = emailNuevoDueno;
        this.precio = precio;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.tipoVenta = tipoDeVenta;
        this.confirmadoAdmin = confirmadoAdmin;
    }

    public ModeloVenta(String tituloPieza, String emailDuenoAnterior, String emailNuevoDueno, Double precio,
            LocalDate fecha, ModeloTiposPago tipoPago, ModeloTipoDeVenta tipoDeVenta, Boolean confirmadoAdmin) {
        this.asignarIdUnico();
        this.tituloPieza = tituloPieza;
        this.emailDuenoAnterior = emailDuenoAnterior;
        this.emailNuevoDueno = emailNuevoDueno;
        this.precio = precio;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.tipoVenta = tipoDeVenta;
        this.confirmadoAdmin = confirmadoAdmin;
    }


    private void asignarIdUnico() {
        HashMap<Integer, ModeloVenta> ventas = PersistenciaVentas.ventas;
        Integer max = 0;
        for (ModeloVenta m : ventas.values()) {
            if (m.getId() > max) {
                max = m.getId();
            }
        }
        this.id = max + 1;
    }

    // Getters y setters
    public String getTituloPieza() {
        return tituloPieza;
    }

    public void setTituloPieza(String tituloPieza) {
        this.tituloPieza = tituloPieza;
    }

    public String getEmailDuenoAnterior() {
        return emailDuenoAnterior;
    }

    public void setEmailDuenoAnterior(String emailDuenoAnterior) {
        this.emailDuenoAnterior = emailDuenoAnterior;
    }

    public String getEmailNuevoDueno() {
        return emailNuevoDueno;
    }

    public void setEmailNuevoDueno(String emailNuevoDueno) {
        this.emailNuevoDueno = emailNuevoDueno;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ModeloTiposPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(ModeloTiposPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ModeloTipoDeVenta getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(ModeloTipoDeVenta tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public Boolean getConfirmadoAdmin() {
        return confirmadoAdmin;
    }

    public void setConfirmadoAdmin(Boolean confirmadoAdmin) {
        this.confirmadoAdmin = confirmadoAdmin;
    }

}
