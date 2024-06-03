package modelo;

public class ModeloOferta {
    private Integer idSubasta;
    private String emailOfertante;
    private Double cantidad;

    public ModeloOferta(Integer idSubasta, String emailOfertante, Double cantidad) {
        this.idSubasta = idSubasta;
        this.emailOfertante = emailOfertante;
        this.cantidad = cantidad;
    }

    public Integer getIdSubasta() {
        return idSubasta;
    }

    public void setIdSubasta(Integer idSubasta) {
        this.idSubasta = idSubasta;
    }

    public String getEmailOfertante() {
        return emailOfertante;
    }

    public void setEmailOfertante(String emailOfertante) {
        this.emailOfertante = emailOfertante;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ModeloOferta{" +
                "idSubasta=" + idSubasta +
                ", emailOfertante='" + emailOfertante + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModeloOferta that = (ModeloOferta) o;
        if (!idSubasta.equals(that.idSubasta)) return false;
        if (!emailOfertante.equals(that.emailOfertante)) return false;
        return cantidad.equals(that.cantidad);
    }

    @Override
    public int hashCode() {
        int result = idSubasta.hashCode();
        result = 31 * result + emailOfertante.hashCode();
        result = 31 * result + cantidad.hashCode();
        return result;
    }
}