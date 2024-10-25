/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Modelo.Equipo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {
    private Connection conexion;

    public EquipoDAO(Connection conexion) {
        this.conexion = conexion;
    }

// Método para obtener un equipo por su ID
public Equipo obtenerEquipoPorId(int idEquipo) throws SQLException {
    String query = "SELECT id_equipo, nombre, descripcion FROM Equipo WHERE id_equipo = ?";
    try (PreparedStatement stmt = conexion.prepareStatement(query)) {
        stmt.setInt(1, idEquipo);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Crear un objeto Equipo con los datos obtenidos de la base de datos
            return new Equipo(
                rs.getInt("id_equipo"),
                rs.getString("nombre"),
                rs.getString("descripcion")
            );
        } else {
            return null; // Si no se encuentra un equipo con el ID proporcionado, devolver null
        }
    }
}


       // Método en tu DAO para obtener todos los equipos
public List<Equipo> obtenerTodosLosEquipos() throws SQLException {
    String query = "SELECT id_equipo, nombre, descripcion FROM Equipo";
    List<Equipo> listaEquipos = new ArrayList<>();

    try (Statement stmt = conexion.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            // Crear el objeto Equipo con los datos del ResultSet
            Equipo equipo = new Equipo(
                rs.getInt("id_equipo"),
                rs.getString("nombre"),
                rs.getString("descripcion")
            );

            listaEquipos.add(equipo);
        }
    }
    return listaEquipos;
}

}
