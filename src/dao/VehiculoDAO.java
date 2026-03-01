package dao;

import models.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Vehiculo v) throws SQLException {
        String sql = """
            INSERT INTO vehiculos
              (marca, modelo, placa, color, anio, capacidad, km_inicial, km_actual,
               categoria, combustible, transmision, precio_base_dia, estado)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,  v.getMarca());
            ps.setString(2,  v.getModelo());
            ps.setString(3,  v.getPlaca());
            ps.setString(4,  v.getColor());
            ps.setInt(5,     v.getAnio());
            ps.setInt(6,     v.getCapacidad());
            ps.setInt(7,     v.getKmInicial());
            ps.setInt(8,     v.getKmActual());
            ps.setString(9,  v.getCategoria());
            ps.setString(10, v.getCombustible());
            ps.setString(11, v.getTransmision());
            ps.setDouble(12, v.getPrecioBaseDia());
            ps.setString(13, v.getEstado());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) v.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public Vehiculo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM vehiculos WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public Vehiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM vehiculos WHERE placa = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Vehiculo> listarTodos() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos ORDER BY id";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Vehiculo> listarPorEstado(String estado) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE estado = ? ORDER BY id";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean actualizar(Vehiculo v) throws SQLException {
        String sql = """
            UPDATE vehiculos SET
              marca=?, modelo=?, placa=?, color=?, anio=?, capacidad=?,
              km_actual=?, categoria=?, combustible=?, transmision=?,
              precio_base_dia=?, estado=?, notas=?
            WHERE id=?
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1,  v.getMarca());
            ps.setString(2,  v.getModelo());
            ps.setString(3,  v.getPlaca());
            ps.setString(4,  v.getColor());
            ps.setInt(5,     v.getAnio());
            ps.setInt(6,     v.getCapacidad());
            ps.setInt(7,     v.getKmActual());
            ps.setString(8,  v.getCategoria());
            ps.setString(9,  v.getCombustible());
            ps.setString(10, v.getTransmision());
            ps.setDouble(11, v.getPrecioBaseDia());
            ps.setString(12, v.getEstado());
            ps.setString(13, v.getNotas());
            ps.setInt(14,    v.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE vehiculos SET estado=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vehiculos WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Vehiculo mapear(ResultSet rs) throws SQLException {
      
        v.setId(rs.getInt("id"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setPlaca(rs.getString("placa"));
        v.setColor(rs.getString("color"));
        v.setAnio(rs.getInt("anio"));
        v.setCapacidad(rs.getInt("capacidad"));
        v.setKmInicial(rs.getInt("km_inicial"));
        v.setKmActual(rs.getInt("km_actual"));
        v.setCategoria(rs.getString("categoria"));
        v.setCombustible(rs.getString("combustible"));
        v.setTransmision(rs.getString("transmision"));
        v.setPrecioBaseDia(rs.getDouble("precio_base_dia"));
        v.setEstado(rs.getString("estado"));
        v.setNotas(rs.getString("notas"));
        return v;
    }
}