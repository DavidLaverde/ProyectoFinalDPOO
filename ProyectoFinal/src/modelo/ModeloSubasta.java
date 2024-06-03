package modelo;

import java.time.LocalDate;
import controlador.LogicaPiezas; // Importar la clase LogicaPiezas

public class ModeloSubasta {
    private Integer id;
    private LocalDate fechaInicio;
    private double valorInicial;
    private ModeloPiezas pieza;
    private ModeloEstadoSubasta estado;

    // Constructor original
    public ModeloSubasta(LocalDate fechaInicio, double valorInicial, ModeloPiezas pieza) {
        this.fechaInicio = fechaInicio;
        this.valorInicial = valorInicial;
        this.pieza = pieza;
        this.estado = ModeloEstadoSubasta.ABIERTA; // Estado inicial por defecto
    }

    // Nuevo constructor que incluye id
    public ModeloSubasta(Integer id, LocalDate fechaInicio, String tituloPieza, ModeloEstadoSubasta estado, double valorInicial, LogicaPiezas logicaPiezas) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.valorInicial = valorInicial;
        try {
            this.pieza = logicaPiezas.consultar(tituloPieza); 
        } catch (Exception e) {
            // Manejar la excepción de alguna manera apropiada, por ejemplo, imprimir un mensaje de error
            System.err.println("Error al consultar la pieza: " + e.getMessage());
        }
        this.estado = estado;
    }

    // Método para asignar un nuevo ID
    public void asignarId() {
        // Lógica para asignar un nuevo ID a la subasta
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public ModeloPiezas getPieza() {
        return pieza;
    }

    public void setPieza(ModeloPiezas pieza) {
        this.pieza = pieza;
    }

    public ModeloEstadoSubasta getEstado() {
        return estado;
    }

    public void setEstado(ModeloEstadoSubasta estado) {
        this.estado = estado;
    }

    // Métodos adicionales
    public String getTituloPieza() {
        return pieza.getTitulo();
    }

    public double getMinimo() {
        return valorInicial;
    }
}
