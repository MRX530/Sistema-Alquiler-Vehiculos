package menu;

import services.MantenimientoService;
import models.Mantenimiento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MantenimientoPanel extends JPanel {
    private MantenimientoService mantService = new MantenimientoService();

    private JTextField txtVehiculoId, txtTipo, txtKm, txtProximoKm, txtCosto, txtNotas;
    private JTable tabla;
    private DefaultTableModel modelo;

    public MantenimientoPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Vehículo ID:"));
        txtVehiculoId = new JTextField();
        formPanel.add(txtVehiculoId);

        formPanel.add(new JLabel("Tipo:"));
        txtTipo = new JTextField();
        formPanel.add(txtTipo);

        formPanel.add(new JLabel("Km actual:"));
        txtKm = new JTextField();
        formPanel.add(txtKm);

        formPanel.add(new JLabel("Próximo Km:"));
        txtProximoKm = new JTextField();
        formPanel.add(txtProximoKm);

        formPanel.add(new JLabel("Costo RD$:"));
        txtCosto = new JTextField();
        formPanel.add(txtCosto);

        formPanel.add(new JLabel("Notas:"));
        txtNotas = new JTextField();
        formPanel.add(txtNotas);

        JButton btnGuardar = new JButton("Guardar Mantenimiento");
        JButton btnEliminar = new JButton("Eliminar");

        btnGuardar.addActionListener(this::guardarMantenimiento);
        btnEliminar.addActionListener(this::eliminarMantenimiento);

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEliminar);

        modelo = new DefaultTableModel(new String[]{"ID", "Vehículo", "Tipo", "Km", "Próximo Km", "Costo"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(formPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);

        cargarTabla();
    }

    private void guardarMantenimiento(ActionEvent e) {
        try {
            Mantenimiento m = new Mantenimiento(
                    Integer.parseInt(txtVehiculoId.getText()),
                    txtTipo.getText(),
                    Integer.parseInt(txtKm.getText()),
                    Integer.parseInt(txtProximoKm.getText()),
                    new java.sql.Date(System.currentTimeMillis()),
                    null,
                    Double.parseDouble(txtCosto.getText()),
                    txtNotas.getText()
            );
            mantService.registrarMantenimiento(m);
            cargarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarMantenimiento(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modelo.getValueAt(fila, 0);
            mantService.eliminarMantenimiento(id);
            cargarTabla();
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Mantenimiento> lista = mantService.listarMantenimientos();
        for (Mantenimiento m : lista) {
            modelo.addRow(new Object[]{m.getId(), m.getVehiculoId(), m.getTipo(), m.getKm(), m.getProximoKm(), m.getCosto()});
        }
    }

    private void limpiarFormulario() {
        txtVehiculoId.setText("");
        txtTipo.setText("");
        txtKm.setText("");
        txtProximoKm.setText("");
        txtCosto.setText("");
        txtNotas.setText("");
    }
}