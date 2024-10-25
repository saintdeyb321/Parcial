/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {
    private Connection conexion;

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
    List<Usuario> usuarios = new ArrayList<>();
    String query = "SELECT * FROM Usuario"; // Asegúrate de que el nombre de la tabla sea correcto
    try (PreparedStatement stmt = conexion.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Usuario usuario = new Usuario(
                rs.getString("codigo"), // Cambia esto según los nombres de tus columnas
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("tipo")
            );
            usuarios.add(usuario);
        }
    }
    return usuarios;
}


    public void guardarUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO Usuario (codigo, nombre, apellido, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, usuario.getCodigo());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellido());
            stmt.setString(4, usuario.getTipo());
            stmt.executeUpdate();
        }
    }

    public void eliminarUsuario(String codigo) throws SQLException {
        String query = "DELETE FROM Usuario WHERE codigo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, codigo);
            stmt.executeUpdate();
        }
    }
    
    public Usuario obtenerUsuarioPorCodigo(String codigoUsuario) throws SQLException {
        String query = "SELECT codigo, nombre, apellido, tipo FROM Usuario WHERE codigo = ?";
        Usuario usuario = null;

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, codigoUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Crear el objeto Usuario con los datos obtenidos de la consulta
                    usuario = new Usuario(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("tipo")
                    );
                }
            }
        }

        return usuario; // Retorna el usuario encontrado o null si no se encontró
    }

}
