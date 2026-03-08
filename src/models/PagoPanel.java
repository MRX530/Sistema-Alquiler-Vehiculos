package menu;

import services.PagoService;
import models.Pago;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PagosPanel extends JPanel {
    private PagoService pagoService = new PagoService();

    private JTextField txtReservaId, txtMonto, txtNotas;
    private JComboBox<String> cmbMetodo;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PagosPanel() {
        setLayout(new BorderLayout());

        // Formulario superior
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("Reserva ID:"));
        txtReservaId = new JTextField();
        formPanel.add(txtReservaId);

        formPanel.add(new JLabel("Monto RD$:"));
        txtMonto = new JTextField();
        formPanel.add(txtMonto);

        formPanel.add(new JLabel("Método:"));
        cmbMetodo = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA"});
        formPanel.add(cmbMetodo);

        formPanel.add(new JLabel("Notas:"));
        txtNotas = new JTextField();
        formPanel.add(txtNotas);

        // Botones
        JButton btnGuardar = new JButton("Guardar Pago");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");

        btnGuardar.addActionListener(this::guardarPago);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(this::eliminarPago);

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnLimpiar);
        botonesPanel.add(btnEliminar);

        // Tabla
        modelo = new DefaultTableModel(new String[]{"ID", "Reserva", "Monto", "Método", "Fecha"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(formPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);

        cargarTabla();
    }

    private void guardarPago(ActionEvent e) {
        try {
            int reservaId = Integer.parseInt(txtReservaId.getText());
            double monto = Double.parseDouble(txtMonto.getText());
            String metodo = (String) cmbMetodo.getSelectedItem();
            String notas = txtNotas.getText();

            Pago pago = new Pago(reservaId, monto, metodo, new java.sql.Date(System.currentTimeMillis()), notas);
            pagoService.registrarPago(pago);
            cargarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Pago registrado correctamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarPago(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modelo.getValueAt(fila, 0);
            pagoService.eliminarPago(id);
            cargarTabla();
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Pago> lista = pagoService.listarPagos();
        for (Pago p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getReservaId(), p.getMonto(), p.getMetodoPago(), p.getFecha()});
        }
    }

    private void limpiarFormulario() {
        txtReservaId.setText("");
        txtMonto.setText("");
        txtNotas.setText("");
    }
}