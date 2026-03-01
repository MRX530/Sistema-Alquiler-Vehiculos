package dao;

import models.Contrato;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ContratoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Contrato c) throws SQLException {
        String sql = """
            INSERT INTO contratos
              (reserva_id, fecha_generacion, km_inicial,
               nivel_combustible_inicial, notas_danos_preexistentes, estado)
            VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    c.getReservaId());
            ps.setDate(2,   c.getFechaGeneracion());
            ps.setInt(3,    c.getKmInicial());
            ps.setString(4, c.getNivelCombustibleInicial());
            ps.setString(5, c.getNotasDanosPreexistentes());
            ps.setString(6, c.getEstado());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) c.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public Contrato buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM contratos WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public Contrato buscarPorReserva(int reservaId) throws SQLException {
        String sql = "SELECT * FROM contratos WHERE reserva_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, reservaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Contrato> listarTodos() throws SQLException {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos ORDER BY fecha_generacion DESC";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Contrato> listarActivos() throws SQLException {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contratos WHERE estado = 'ACTIVO' ORDER BY fecha_generacion DESC";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE contratos SET estado=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Contrato c) throws SQLException {
        String sql = """
            UPDATE contratos SET
              reserva_id=?, fecha_generacion=?, km_inicial=?,
              nivel_combustible_inicial=?, notas_danos_preexistentes=?, estado=?
            WHERE id=?
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1,    c.getReservaId());
            ps.setDate(2,   c.getFechaGeneracion());
            ps.setInt(3,    c.getKmInicial());
            ps.setString(4, c.getNivelCombustibleInicial());
            ps.setString(5, c.getNotasDanosPreexistentes());
            ps.setString(6, c.getEstado());
            ps.setInt(7,    c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM contratos WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Contrato mapear(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        c.setId(rs.getInt("id"));
        c.setReservaId(rs.getInt("reserva_id"));
        c.setFechaGeneracion(rs.getDate("fecha_generacion"));
        c.setKmInicial(rs.getInt("km_inicial"));
        c.setNivelCombustibleInicial(rs.getString("nivel_combustible_inicial"));
        c.setNotasDanosPreexistentes(rs.getString("notas_danos_preexistentes"));
        c.setEstado(rs.getString("estado"));
        return c;
    }
}