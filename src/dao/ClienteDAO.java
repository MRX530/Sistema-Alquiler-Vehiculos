package dao;

import models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection getConn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public boolean insertar(Cliente c) throws SQLException {
        String sql = """
            INSERT INTO clientes
              (nombre, cedula, pasaporte, licencia_conducir, telefono, email, direccion)
            VALUES (?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getCedula());
            ps.setString(3, c.getPasaporte());
            ps.setString(4, c.getLicenciaConducir());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getEmail());
            ps.setString(7, c.getDireccion());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) c.setId(rs.getInt(1));
                return true;
            }
        }
        return false;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public Cliente buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE cedula = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        }
        return null;
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Cliente> listarListaNegra() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE en_lista_negra = 1 ORDER BY nombre";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public boolean actualizar(Cliente c) throws SQLException {
        String sql = """
            UPDATE clientes SET
              nombre=?, cedula=?, pasaporte=?, licencia_conducir=?,
              telefono=?, email=?, direccion=?, notas=?
            WHERE id=?
            """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getCedula());
            ps.setString(3, c.getPasaporte());
            ps.setString(4, c.getLicenciaConducir());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getEmail());
            ps.setString(7, c.getDireccion());
            ps.setString(8, c.getNotas());
            ps.setInt(9,    c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarListaNegra(int id, boolean enListaNegra, String motivo) throws SQLException {
        String sql = "UPDATE clientes SET en_lista_negra=?, motivo_lista_negra=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setBoolean(1, enListaNegra);
            ps.setString(2, motivo);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNombre(rs.getString("nombre"));
        c.setCedula(rs.getString("cedula"));
        c.setPasaporte(rs.getString("pasaporte"));
        c.setLicenciaConducir(rs.getString("licencia_conducir"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        c.setEnListaNegra(rs.getBoolean("en_lista_negra"));
        c.setMotivoListaNegra(rs.getString("motivo_lista_negra"));
        c.setNotas(rs.getString("notas"));
        return c;
    }
}