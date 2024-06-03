package modelo;

import java.util.ArrayList;

public class ModeloPiezas {

    private String titulo;
    private String nombreAutor;
    private String emailDueno;
    private ModeloTiposPieza tipo;
    private ArrayList<String> materiales;
    private Boolean bloqueada;
    private Double precioMinimo;
    private ModeloEstado estado;
    private String emailOfertante;
    private ModeloTipoDeVenta tipoDeVenta;

    public ModeloPiezas(String titulo, String nombreAutor, String emailDueno, ModeloTiposPieza tipo,
                        ArrayList<String> materiales, Boolean bloqueada, Double precioMinimo, ModeloEstado estado,
                        String emailOfertante, ModeloTipoDeVenta tipoDeVenta) throws Exception {
        validarUsuarioDueno(emailDueno);
        validarUsuarioOfertante(emailOfertante, bloqueada);
        validarEstadoYTipoDeVenta(estado, tipoDeVenta);

        this.titulo = titulo;
        this.nombreAutor = nombreAutor;
        this.emailDueno = emailDueno;
        this.tipo = tipo;
        this.materiales = materiales;
        this.bloqueada = bloqueada;
        this.precioMinimo = precioMinimo;
        this.estado = estado;
        this.emailOfertante = emailOfertante;
        this.tipoDeVenta = tipoDeVenta;
    }

    private void validarUsuarioDueno(String emailDueno) throws Exception {
        ModeloUsuario usuarioDueno = PersistenciaUsuario.usuarios.get(emailDueno);
        if (usuarioDueno == null || !usuarioDueno.getRol().equals(ModeloRol.CLIENTE)) {
            throw new Exception("El usuario dueño no existe o no es un cliente");
        }
    }
    public ModeloPiezas(String titulo) {
        this.titulo = titulo;
        this.estado = ModeloEstado.BODEGA; // Estado por defecto o como sea necesario
    }

    private void validarUsuarioOfertante(String emailOfertante, Boolean bloqueada) throws Exception {
        if (emailOfertante != null && bloqueada) {
            ModeloUsuario usuarioOfertante = PersistenciaUsuario.usuarios.get(emailOfertante);
            if (usuarioOfertante == null || !usuarioOfertante.getRol().equals(ModeloRol.CLIENTE)) {
                throw new Exception("El usuario ofertante no existe o no es un cliente");
            }
        }
    }

    private void validarEstadoYTipoDeVenta(ModeloEstado estado, ModeloTipoDeVenta tipoDeVenta) throws Exception {
        if (tipoDeVenta != null && (estado.equals(ModeloEstado.DEVUELTA) || estado.equals(ModeloEstado.VENDIDA))) {
            throw new Exception("La pieza no puede tener un tipo de venta permitido si ya ha sido devuelta o vendida");
        }
    }

    // Getters y setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getEmailDueno() {
        return emailDueno;
    }

    public void setEmailDueno(String emailDueno) throws Exception {
        validarUsuarioDueno(emailDueno);
        this.emailDueno = emailDueno;
    }

    public ModeloTiposPieza getTipo() {
        return tipo;
    }

    public void setTipo(ModeloTiposPieza tipo) {
        this.tipo = tipo;
    }

    public ArrayList<String> getMateriales() {
        return materiales;
    }

    public void setMateriales(ArrayList<String> materiales) {
        this.materiales = materiales;
    }

    public Boolean getBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(Boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    public Double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(Double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public ModeloEstado getEstado() {
        return estado;
    }

    public void setEstado(ModeloEstado estado) {
        this.estado = estado;
    }

    public String getEmailOfertante() {
        return emailOfertante;
    }

    public void setEmailOfertante(String emailOfertante) throws Exception {
        validarUsuarioOfertante(emailOfertante, bloqueada);
        this.emailOfertante = emailOfertante;
    }

    public ModeloTipoDeVenta getTipoDeVenta() {
        return tipoDeVenta;
    }

    public void setTipoDeVenta(ModeloTipoDeVenta tipoDeVenta) {
        this.tipoDeVenta = tipoDeVenta;
    }
}