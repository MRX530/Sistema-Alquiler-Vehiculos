package dao;

import models.Dano;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DanoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Dano d) throws SQLException {
        String sql = """
            INSERT INTO danos
              (vehiculo_id, reserva_id, cliente_id, descripcion, costo, fecha, estado)
            VALUES (?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    d.getVehiculoId());
            ps.setObject(2, d.getReservaId() == 0 ? null : d.getReservaId());
            ps.setObject(3, d.getClienteId() == 0 ? null : d.getClienteId());
            ps.setString(4, d.getDescripcion());
            ps.setDouble(5, d.getCosto());
            ps.setDate(6,   d.getFecha());
            ps.setString(7, d.getEstado());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) d.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public Dano buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM danos WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Dano> listarPorVehiculo(int vehiculoId) throws SQLException {
        List<Dano> lista = new ArrayList<>();
        String sql = "SELECT * FROM danos WHERE vehiculo_id=? ORDER BY fecha DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, vehiculoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Dano> listarPendientes() throws SQLException {
        List<Dano> lista = new ArrayList<>();
        String sql = "SELECT * FROM danos WHERE estado='PENDIENTE' ORDER BY fecha DESC";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Dano> listarPorCliente(int clienteId) throws SQLException {
        List<Dano> lista = new ArrayList<>();
        String sql = "SELECT * FROM danos WHERE cliente_id=? ORDER BY fecha DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE danos SET estado=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Dano mapear(ResultSet rs) throws SQLException {
        Dano d = new Dano();
        d.setId(rs.getInt("id"));
        d.setVehiculoId(rs.getInt("vehiculo_id"));
        d.setReservaId(rs.getInt("reserva_id"));
        d.setClienteId(rs.getInt("cliente_id"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setCosto(rs.getDouble("costo"));
        d.setFecha(rs.getDate("fecha"));
        d.setEstado(rs.getString("estado"));
        return d;
    }
}