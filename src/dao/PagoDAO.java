package dao;

import models.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Pago p) throws SQLException {
        String sql = """
            INSERT INTO pagos (reserva_id, monto, metodo_pago, fecha, notas)
            VALUES (?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    p.getReservaId());
            ps.setDouble(2, p.getMonto());
            ps.setString(3, p.getMetodoPago());
            ps.setDate(4,   p.getFecha());
            ps.setString(5, p.getNotas());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) p.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public Pago buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Pago> listarPorReserva(int reservaId) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE reserva_id=? ORDER BY fecha";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, reservaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public double sumarPagado(int reservaId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE reserva_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, reservaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public List<Pago> listarPorMes(int mes, int anio) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE MONTH(fecha)=? AND YEAR(fecha)=? ORDER BY fecha";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public double sumarIngresosTotales() throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public double sumarIngresosPorMes(int mes, int anio) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE MONTH(fecha)=? AND YEAR(fecha)=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    private Pago mapear(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setId(rs.getInt("id"));
        p.setReservaId(rs.getInt("reserva_id"));
        p.setMonto(rs.getDouble("monto"));
        p.setMetodoPago(rs.getString("metodo_pago"));
        p.setFecha(rs.getDate("fecha"));
        p.setNotas(rs.getString("notas"));
        return p;
    }
}