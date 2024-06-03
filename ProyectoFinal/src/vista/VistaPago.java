package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VistaPago extends JFrame {
    public VistaPago() {
        // Configuración de la ventana de pago
        setTitle("Realizar Pago");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel para el formulario de pago
        JPanel panelPago = new JPanel(new GridLayout(3, 2));
        panelPago.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos de texto para ingresar la información de pago
        JTextField txtNumeroTarjeta = new JTextField();
        JTextField txtFechaVencimiento = new JTextField();
        JTextField txtCodigoSeguridad = new JTextField();

        // Etiquetas para los campos de texto
        JLabel lblNumeroTarjeta = new JLabel("Número de Tarjeta:");
        JLabel lblFechaVencimiento = new JLabel("Fecha de Vencimiento:");
        JLabel lblCodigoSeguridad = new JLabel("Código de Seguridad:");

        // Botón para confirmar el pago
        JButton btnConfirmarPago = new JButton("Confirmar Pago");
        btnConfirmarPago.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes agregar la lógica para procesar el pago
                JOptionPane.showMessageDialog(null, "Pago realizado con éxito");
                dispose(); // Cerrar la ventana de pago después de confirmar el pago
            }
        });

        // Agregar componentes al panel de pago
        panelPago.add(lblNumeroTarjeta);
        panelPago.add(txtNumeroTarjeta);
        panelPago.add(lblFechaVencimiento);
        panelPago.add(txtFechaVencimiento);
        panelPago.add(lblCodigoSeguridad);
        panelPago.add(txtCodigoSeguridad);

        // Agregar el panel de pago y el botón al marco de la ventana de pago
        add(panelPago, BorderLayout.CENTER);
        add(btnConfirmarPago, BorderLayout.SOUTH);

        // Mostrar la ventana de pago
        setVisible(true);
    }

    public static void main(String[] args) {
        // Crear una instancia de la ventana de pago
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new VistaPago();
            }
        });
    }
}
