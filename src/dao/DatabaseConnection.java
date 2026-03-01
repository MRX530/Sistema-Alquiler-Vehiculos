package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static volatile DatabaseConnection instance;

    private Connection connection;
    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        loadProperties();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("[DB] Conexion establecida con: " + url);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] Conexion cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("[DB] Error al cerrar conexion: " + e.getMessage());
            }
        }
    }

    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                                           .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("[DB] No se encontro db.properties en el classpath.");
            }
            props.load(input);

            String host = props.getProperty("db.host",     "localhost");
            String port = props.getProperty("db.port",     "3306");
            String name = props.getProperty("db.name",     "rentacar_db");
            user         = props.getProperty("db.user",     "root");
            password     = props.getProperty("db.password", "");

            url = "jdbc:mysql://" + host + ":" + port + "/" + name
                + "?useSSL=false&serverTimezone=America/Santo_Domingo&allowPublicKeyRetrieval=true";

        } catch (IOException e) {
            throw new RuntimeException("[DB] Error leyendo db.properties: " + e.getMessage());
        }
    }
}