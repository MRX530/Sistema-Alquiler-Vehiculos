package dao;

import models.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReservaDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Reserva r) throws SQLException {
        String sql = """
            INSERT INTO reservas
              (cliente_id, vehiculo_id, fecha_inicio, fecha_fin,
               hora_recogida, hora_devolucion, lugar_recogida, extras, precio_total, estado)
            VALUES (?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    r.getClienteId());
            ps.setInt(2,    r.getVehiculoId());
            ps.setDate(3,   r.getFechaInicio());
            ps.setDate(4,   r.getFechaFin());
            ps.setTime(5,   r.getHoraRecogida());
            ps.setTime(6,   r.getHoraDevolucion());
            ps.setString(7, r.getLugarRecogida());
            ps.setString(8, r.getExtras());
            ps.setDouble(9, r.getPrecioTotal());
            ps.setString(10,r.getEstado());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) r.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public Reserva buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM reservas WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Reserva> listarPorCliente(int clienteId) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE cliente_id=? ORDER BY fecha_inicio DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Reserva> listarActivas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado IN ('ACTIVA','CONFIRMADA') ORDER BY fecha_inicio";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean existeSolapamiento(int vehiculoId, Date inicio, Date fin) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM reservas
            WHERE vehiculo_id = ?
              AND estado IN ('ACTIVA','CONFIRMADA')
              AND fecha_inicio <= ?
              AND fecha_fin    >= ?
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1,  vehiculoId);
            ps.setDate(2, fin);
            ps.setDate(3, inicio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE reservas SET estado=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Reserva r) throws SQLException {
        String sql = """
            UPDATE reservas SET
              cliente_id=?, vehiculo_id=?, fecha_inicio=?, fecha_fin=?,
              hora_recogida=?, hora_devolucion=?, lugar_recogida=?,
              extras=?, precio_total=?, estado=?, notas=?
            WHERE id=?
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1,    r.getClienteId());
            ps.setInt(2,    r.getVehiculoId());
            ps.setDate(3,   r.getFechaInicio());
            ps.setDate(4,   r.getFechaFin());
            ps.setTime(5,   r.getHoraRecogida());
            ps.setTime(6,   r.getHoraDevolucion());
            ps.setString(7, r.getLugarRecogida());
            ps.setString(8, r.getExtras());
            ps.setDouble(9, r.getPrecioTotal());
            ps.setString(10,r.getEstado());
            ps.setString(11,r.getNotas());
            ps.setInt(12,   r.getId());
            return ps.executeUpdate() > 0;
        }
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getInt("id"));
        r.setClienteId(rs.getInt("cliente_id"));
        r.setVehiculoId(rs.getInt("vehiculo_id"));
        r.setFechaInicio(rs.getDate("fecha_inicio"));
        r.setFechaFin(rs.getDate("fecha_fin"));
        r.setHoraRecogida(rs.getTime("hora_recogida"));
        r.setHoraDevolucion(rs.getTime("hora_devolucion"));
        r.setLugarRecogida(rs.getString("lugar_recogida"));
        r.setExtras(rs.getString("extras"));
        r.setPrecioTotal(rs.getDouble("precio_total"));
        r.setEstado(rs.getString("estado"));
        r.setNotas(rs.getString("notas"));
        return r;
    }
}