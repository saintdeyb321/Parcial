package Controlador;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import DAO.UsuarioDAO;
import Modelo.Usuario;
import Vista.IUsuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorUsuario {
    private IUsuario vistaUsuario;
    private UsuarioDAO usuarioDAO;

    public ControladorUsuario(IUsuario vista, Connection conexion) {
        this.vistaUsuario = vista;
        this.usuarioDAO = new UsuarioDAO(conexion);

        this.vistaUsuario.getBtnGuardar().addActionListener(e -> agregarUsuario());
        this.vistaUsuario.getBtnEliminar().addActionListener(e -> eliminarUsuario());

        listarUsuarios(); // Carga usuarios al inicio
    }
    
    public void setVista(IUsuario vistaUsuario) {
        this.vistaUsuario = vistaUsuario;
        listarUsuarios(); // Carga usuarios en la tabla cuando la vista está disponible
    }
    
    private void listarUsuarios() {
    try {
        System.out.println("Cargando usuarios...");
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();
        System.out.println("Usuarios cargados: " + usuarios.size());
        vistaUsuario.actualizarTablaUsuarios(usuarios);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(vistaUsuario, "Error al cargar los usuarios: " + e.getMessage());
        e.printStackTrace(); // Imprimir la traza de la excepción
    }
}
    
    
    
    private void agregarUsuario() {
    try {
        String codigo = vistaUsuario.getTxtCodigo().getText();
        String nombre = vistaUsuario.getTxtNombre().getText();
        String apellido = vistaUsuario.getTxtApellido().getText();
        String tipo = vistaUsuario.getCboTipo().getSelectedItem().toString();

        Usuario nuevoUsuario = new Usuario(codigo, nombre, apellido, tipo);
        usuarioDAO.guardarUsuario(nuevoUsuario);
        JOptionPane.showMessageDialog(vistaUsuario, "Usuario agregado con éxito.");

        // Llama a listarUsuarios() para actualizar la tabla después de agregar
        listarUsuarios();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(vistaUsuario, "Error al agregar el usuario: " + e.getMessage());
    }
}

private void eliminarUsuario() {
    try {
        String codigo = vistaUsuario.getTxtCodigo().getText();
        usuarioDAO.eliminarUsuario(codigo);
        JOptionPane.showMessageDialog(vistaUsuario, "Usuario eliminado con éxito.");

        // Llama a listarUsuarios() para actualizar la tabla después de eliminar
        listarUsuarios();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(vistaUsuario, "Error al eliminar el usuario: " + e.getMessage());
    }
}
}
