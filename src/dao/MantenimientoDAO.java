package dao;

import models.Mantenimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Mantenimiento m) throws SQLException {
        String sql = """
            INSERT INTO mantenimientos
              (vehiculo_id, tipo, fecha, km, proximo_km, proxima_fecha, costo, notas)
            VALUES (?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    m.getVehiculoId());
            ps.setString(2, m.getTipo());
            ps.setDate(3,   m.getFecha());
            ps.setInt(4,    m.getKm());
            ps.setObject(5, m.getProximoKm() == 0 ? null : m.getProximoKm());
            ps.setDate(6,   m.getProximaFecha());
            ps.setDouble(7, m.getCosto());
            ps.setString(8, m.getNotas());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) m.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public List<Mantenimiento> listarPorVehiculo(int vehiculoId) throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM mantenimientos WHERE vehiculo_id=? ORDER BY fecha DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, vehiculoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Mantenimiento> listarAlertasVencidas() throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = """
            SELECT m.* FROM mantenimientos m
            JOIN vehiculos v ON m.vehiculo_id = v.id
            WHERE m.proximo_km IS NOT NULL
              AND v.km_actual >= m.proximo_km
            ORDER BY m.vehiculo_id
            """;
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Mantenimiento> listarAlertasFechaVencida() throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = """
            SELECT * FROM mantenimientos
            WHERE proxima_fecha IS NOT NULL
              AND proxima_fecha <= CURDATE()
            ORDER BY proxima_fecha
            """;
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Mantenimiento> listarTodos() throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM mantenimientos ORDER BY fecha DESC";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Mantenimiento mapear(ResultSet rs) throws SQLException {
        Mantenimiento m = new Mantenimiento();
        m.setId(rs.getInt("id"));
        m.setVehiculoId(rs.getInt("vehiculo_id"));
        m.setTipo(rs.getString("tipo"));
        m.setFecha(rs.getDate("fecha"));
        m.setKm(rs.getInt("km"));
        m.setProximoKm(rs.getInt("proximo_km"));
        m.setProximaFecha(rs.getDate("proxima_fecha"));
        m.setCosto(rs.getDouble("costo"));
        m.setNotas(rs.getString("notas"));
        return m;
    }
}