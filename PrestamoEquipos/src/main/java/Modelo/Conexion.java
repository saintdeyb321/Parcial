package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ACER
 */
public class Conexion {
    // Cambia la URL de conexión con los datos de tu servidor de Azure SQL Database
    private static final String URL = "jdbc:sqlserver://parionaservidor.database.windows.net:1433;databaseName=PRESTAMOS;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static final String USER = "deyvid"; // Cambia por tu usuario de Azure SQL Database
    private static final String PASSWORD = "Asd 3612"; // Cambia por tu contraseña de Azure SQL Database

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return connection;
    }
}