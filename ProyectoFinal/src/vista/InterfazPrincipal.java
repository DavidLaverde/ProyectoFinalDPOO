package vista;

import controlador.LogicaVentas;
import modelo.ModeloVenta;
import modelo.ModeloTiposPago;
import modelo.ModeloTipoDeVenta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class InterfazPrincipal extends JFrame {
    private LogicaVentas logicaVentas;

    public InterfazPrincipal(LogicaVentas logicaVentas) {
        this.logicaVentas = logicaVentas;

        // Configuración de la ventana principal
        setTitle("Interfaz Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Panel de formulario de ventas
        JPanel panelFormulario = new JPanel(new GridLayout(9, 2));
        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        JLabel lblTitulo = new JLabel("Título Pieza:");
        JTextField txtTitulo = new JTextField();
        JLabel lblEmailAnt = new JLabel("Email Dueño Anterior:");
        JTextField txtEmailAnt = new JTextField();
        JLabel lblEmailNuevo = new JLabel("Email Nuevo Dueño:");
        JTextField txtEmailNuevo = new JTextField();
        JLabel lblPrecio = new JLabel("Precio:");
        JTextField txtPrecio = new JTextField();
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        JTextField txtFecha = new JTextField();
        JLabel lblTipoPago = new JLabel("Tipo Pago:");
        JComboBox<ModeloTiposPago> cbTipoPago = new JComboBox<>(ModeloTiposPago.values());
        JLabel lblTipoVenta = new JLabel("Tipo Venta:");
        JComboBox<ModeloTipoDeVenta> cbTipoVenta = new JComboBox<>(ModeloTipoDeVenta.values());
        JLabel lblConfirmado = new JLabel("Confirmado:");
        JCheckBox cbConfirmado = new JCheckBox();

        panelFormulario.add(lblId);
        panelFormulario.add(txtId);
        panelFormulario.add(lblTitulo);
        panelFormulario.add(txtTitulo);
        panelFormulario.add(lblEmailAnt);
        panelFormulario.add(txtEmailAnt);
        panelFormulario.add(lblEmailNuevo);
        panelFormulario.add(txtEmailNuevo);
        panelFormulario.add(lblPrecio);
        panelFormulario.add(txtPrecio);
        panelFormulario.add(lblFecha);
        panelFormulario.add(txtFecha);
        panelFormulario.add(lblTipoPago);
        panelFormulario.add(cbTipoPago);
        panelFormulario.add(lblTipoVenta);
        panelFormulario.add(cbTipoVenta);
        panelFormulario.add(lblConfirmado);
        panelFormulario.add(cbConfirmado);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);

        // Gráfico de barras para representar las ventas
        JPanel panelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Aquí puedes agregar la lógica para dibujar el gráfico de barras
                // Por ahora, solo dibujaremos un ejemplo simple
                g.setColor(Color.blue);
                g.fillRect(50, 200, 50, 150); // Barra para enero
                g.fillRect(150, 200, 50, 100); // Barra para febrero
                g.fillRect(250, 200, 50, 200); // Barra para marzo
                // Continúa dibujando las barras para los demás meses...
            }
        };
        panelPrincipal.add(panelGrafico, BorderLayout.CENTER);

        // Botón para agregar una venta
        JButton btnAgregarVenta = new JButton("Agregar Venta");
        btnAgregarVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer id = Integer.parseInt(txtId.getText());
                    String titulo = txtTitulo.getText();
                    String emailAnt = txtEmailAnt.getText();
                    String emailNuevo = txtEmailNuevo.getText();
                    Double precio = Double.parseDouble(txtPrecio.getText());
                    LocalDate fecha = LocalDate.parse(txtFecha.getText());
                    ModeloTiposPago tipoPago = (ModeloTiposPago) cbTipoPago.getSelectedItem();
                    ModeloTipoDeVenta tipoVenta = (ModeloTipoDeVenta) cbTipoVenta.getSelectedItem();
                    Boolean confirmado = cbConfirmado.isSelected();

                    ModeloVenta nuevaVenta = new ModeloVenta(id, titulo, emailAnt, emailNuevo, precio, fecha, tipoPago, tipoVenta, confirmado);
                    logicaVentas.crear(nuevaVenta);
                    JOptionPane.showMessageDialog(InterfazPrincipal.this, "Venta agregada exitosamente!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(InterfazPrincipal.this, "Error al agregar la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Botón para actualizar el gráfico (solo para demostración)
        JButton btnActualizarGrafico = new JButton("Actualizar Gráfico");
        btnActualizarGrafico.addActionListener(e -> panelGrafico.repaint());

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarVenta);
        panelBotones.add(btnActualizarGrafico);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Agregar el panel principal a la ventana
        add(panelPrincipal);

        // Mostrar la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        // Crear una instancia de la lógica de ventas
        LogicaVentas logicaVentas = new LogicaVentas();

        // Crear una instancia de la interfaz principal
        SwingUtilities.invokeLater(() -> new InterfazPrincipal(logicaVentas));
    }
}