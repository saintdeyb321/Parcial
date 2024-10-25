/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Equipo;
import Modelo.Prestamo;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ACER
 */
public class PrestamoDAO {
    private Connection conexion;

    public PrestamoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para agregar un préstamo a la base de datos
    public void agregarPrestamo(Prestamo prestamo) throws SQLException {
        String query = "INSERT INTO Prestamo (id_equipo, codigo_usuario, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, prestamo.getEquipo().getIdEquipo());
            stmt.setString(2, prestamo.getUsuario().getCodigo());
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            stmt.setDate(4, new java.sql.Date(prestamo.getFechaDevolucion().getTime()));
            stmt.executeUpdate();
        }
    }

    public void actualizarPrestamo(Prestamo prestamo) throws SQLException {
    String query = "UPDATE Prestamo SET id_equipo = ?, codigo_usuario = ?, fecha_devolucion = ? WHERE id_prestamo = ?";
    try (PreparedStatement stmt = conexion.prepareStatement(query)) {
        stmt.setInt(1, prestamo.getEquipo().getIdEquipo());
        stmt.setString(2, prestamo.getUsuario().getCodigo());
        stmt.setDate(3, new java.sql.Date(prestamo.getFechaDevolucion().getTime()));
        stmt.setInt(4, prestamo.getIdPrestamo());

        stmt.executeUpdate();
    }
}


    // Método para eliminar un préstamo de la base de datos
    public void eliminarPrestamo(int idPrestamo) throws SQLException {
        String query = "DELETE FROM Prestamo WHERE id_prestamo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, idPrestamo);
            stmt.executeUpdate();
        }
    }

    // Método para obtener todos los préstamos de la base de datos
public List<Prestamo> obtenerTodosLosPrestamos() throws SQLException {
    String query = "SELECT p.id_prestamo, p.fecha_prestamo, p.fecha_devolucion, " +
                   "u.codigo, u.nombre AS nombre_usuario, u.apellido, u.tipo, " +
                   "e.id_equipo, e.nombre AS nombre_equipo, e.descripcion " +
                   "FROM Prestamo p " +
                   "JOIN Usuario u ON p.codigo_usuario = u.codigo " +
                   "JOIN Equipo e ON p.id_equipo = e.id_equipo";
    List<Prestamo> listaPrestamos = new ArrayList<>();

    try (Statement stmt = conexion.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            // Crear el objeto Usuario con todos los atributos necesarios
            Usuario usuario = new Usuario(
                rs.getString("codigo"),
                rs.getString("nombre_usuario"),
                rs.getString("apellido"),
                rs.getString("tipo")
            );

            // Crear el objeto Equipo con los datos del ResultSet
            Equipo equipo = new Equipo(
                rs.getInt("id_equipo"),
                rs.getString("nombre_equipo"),
                rs.getString("descripcion")
            );

            // Crear el objeto Prestamo con los datos
            Prestamo prestamo = new Prestamo(
                rs.getInt("id_prestamo"),
                equipo,
                usuario,
                rs.getDate("fecha_prestamo"),
                rs.getDate("fecha_devolucion")
            );

            listaPrestamos.add(prestamo);
        }
    }
    return listaPrestamos;
}

}