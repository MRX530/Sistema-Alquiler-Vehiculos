package menu;

import services.DanoService;
import models.Dano;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DanosPanel extends JPanel {
    private DanoService danoService = new DanoService();

    private JTextField txtVehiculoId, txtClienteId, txtDescripcion, txtCosto;
    private JComboBox<String> cmbEstado;
    private JTable tabla;
    private DefaultTableModel modelo;

    public DanosPanel() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.add(new JLabel("Vehículo ID:"));
        txtVehiculoId = new JTextField();
        formPanel.add(txtVehiculoId);

        formPanel.add(new JLabel("Cliente ID:"));
        txtClienteId = new JTextField();
        formPanel.add(txtClienteId);

        formPanel.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        formPanel.add(txtDescripcion);

        formPanel.add(new JLabel("Costo RD$:"));
        txtCosto = new JTextField();
        formPanel.add(txtCosto);

        formPanel.add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(new String[]{"PENDIENTE", "COBRADO"});
        formPanel.add(cmbEstado);

        JButton btnGuardar = new JButton("Guardar Daño");
        JButton btnCobrar = new JButton("Cobrar Seleccionado");
        JButton btnEliminar = new JButton("Eliminar");

        btnGuardar.addActionListener(this::guardarDano);
        btnCobrar.addActionListener(this::cobrarDano);
        btnEliminar.addActionListener(this::eliminarDano);

        JPanel botonesPanel = new JPanel();
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnCobrar);
        botonesPanel.add(btnEliminar);

        modelo = new DefaultTableModel(new String[]{"ID", "Vehículo", "Cliente", "Descripción", "Costo", "Estado"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(formPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);

        cargarTabla();
    }

    private void guardarDano(ActionEvent e) {
        try {
            Dano d = new Dano(
                    Integer.parseInt(txtVehiculoId.getText()),
                    Integer.parseInt(txtClienteId.getText()),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtCosto.getText()),
                    new java.sql.Date(System.currentTimeMillis()),
                    (String) cmbEstado.getSelectedItem()
            );
            danoService.registrarDano(d);
            cargarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void cobrarDano(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modelo.getValueAt(fila, 0);
            danoService.cobrarDano(id);
            cargarTabla();
        }
    }

    private void eliminarDano(ActionEvent e) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modelo.getValueAt(fila, 0);
            danoService.eliminarDano(id);
            cargarTabla();
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Dano> lista = danoService.listarDanos();
        for (Dano d : lista) {
            modelo.addRow(new Object[]{d.getId(), d.getVehiculoId(), d.getClienteId(), d.getDescripcion(), d.getCosto(), d.getEstado()});
        }
    }

    private void limpiarFormulario() {
        txtVehiculoId.setText("");
        txtClienteId.setText("");
        txtDescripcion.setText("");
        txtCosto.setText("");
    }
}