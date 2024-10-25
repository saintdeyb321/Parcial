/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.EquipoDAO;
import DAO.PrestamoDAO;
import DAO.UsuarioDAO;
import Modelo.Equipo;
import Modelo.Prestamo;
import Modelo.Usuario;
import Vista.IPrestamo;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class ControladorPrestamo {
    private IPrestamo vistaPrestamo;
    private PrestamoDAO prestamoDAO;
    private EquipoDAO equipoDAO;
    private UsuarioDAO usuarioDAO;

    public ControladorPrestamo(IPrestamo vista, PrestamoDAO prestamoDAO, EquipoDAO equipoDAO, UsuarioDAO usuarioDAO) {
    this.vistaPrestamo = vista;
    this.prestamoDAO = prestamoDAO;
    this.equipoDAO = equipoDAO;
    this.usuarioDAO = usuarioDAO;
    initEventHandlers();
    listarPrestamos();
    cargarEquipos();
}
    
    public void setVista(IPrestamo vistaPrestamo) {
        this.vistaPrestamo = vistaPrestamo;
        initEventHandlers();
        listarPrestamos(); // Carga los datos en la tabla una vez que la vista está disponible
    }

    private void initEventHandlers() {
        vistaPrestamo.getBtnAgregar().addActionListener(e -> agregarPrestamo());
        vistaPrestamo.getBtnEliminar().addActionListener(e -> eliminarPrestamo());
        vistaPrestamo.getCboEquipo().addActionListener(e -> mostrarDescripcionEquipo());
        vistaPrestamo.getBtnActualizar().addActionListener(e -> actualizarPrestamoHandler());
        vistaPrestamo.getTblPrestamo().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = vistaPrestamo.getTblPrestamo().getSelectedRow();
                cargarDatosPrestamo(filaSeleccionada);
            }
        });
    }

    private void cargarDatosPrestamo(int fila) {
        DefaultTableModel modelo = (DefaultTableModel) vistaPrestamo.getTblPrestamo().getModel();

        if (fila >= 0 && fila < modelo.getRowCount()) {
            String codigoUsuario = (String) modelo.getValueAt(fila, 1); // Suponiendo que la columna 1 es el código del usuario
            String descripcionEquipo = (String) modelo.getValueAt(fila, 4); // Suponiendo que la columna 4 es la descripción del equipo

            // Rellenar los campos de la vista
            vistaPrestamo.getTxtCodigo().setText(codigoUsuario);
            vistaPrestamo.getTxtDescripcion().setText(descripcionEquipo);
        } else {

        }
    }

    private void cargarEquipos() {
        try {
            List<Equipo> listaEquipos = equipoDAO.obtenerTodosLosEquipos();
            llenarComboBoxEquipos(listaEquipos);
        } catch (SQLException e) {
            mostrarError("Error al cargar los equipos: " + e.getMessage());
        }
    }

    private void llenarComboBoxEquipos(List<Equipo> equipos) {
        DefaultComboBoxModel<Equipo> modelo = new DefaultComboBoxModel<>();
        for (Equipo equipo : equipos) {
            modelo.addElement(equipo);
        }
        vistaPrestamo.getCboEquipo().setModel(modelo);
    }

    private void mostrarDescripcionEquipo() {
        Equipo equipoSeleccionado = (Equipo) vistaPrestamo.getCboEquipo().getSelectedItem();
        if (equipoSeleccionado != null) {
            vistaPrestamo.getTxtDescripcion().setText(equipoSeleccionado.getDescripcion());
        } else {
            vistaPrestamo.getTxtDescripcion().setText("");
            System.out.println("No hay ningún equipo seleccionado.");
        }
    }

    private void agregarPrestamo() {
        try {
            Equipo equipoSeleccionado = (Equipo) vistaPrestamo.getCboEquipo().getSelectedItem();
            String codigoUsuario = vistaPrestamo.getTxtCodigo().getText();

            if (equipoSeleccionado == null) {
                mostrarError("Equipo no encontrado.");
                return;
            }

            Usuario usuario = usuarioDAO.obtenerUsuarioPorCodigo(codigoUsuario);
            if (usuario == null) {
                mostrarError("Error: No se realizó el préstamo, usuario no registrado.");
                return;
            }

            java.util.Date fechaPrestamo = new java.util.Date(); // Fecha actual
            java.util.Date fechaDevolucion = vistaPrestamo.getCalendario().getDate();
            Prestamo nuevoPrestamo = new Prestamo(0, equipoSeleccionado, usuario,
                    new java.sql.Date(fechaPrestamo.getTime()),
                    new java.sql.Date(fechaDevolucion.getTime()));

            prestamoDAO.agregarPrestamo(nuevoPrestamo);
            mostrarMensaje("Préstamo agregado con éxito.");
            limpiarCampos();
            listarPrestamos();
        } catch (SQLException e) {
            mostrarError("Error al agregar el préstamo: " + e.getMessage());
        }
    }

    private void eliminarPrestamo() {
        String input = JOptionPane.showInputDialog(vistaPrestamo, "Escriba el ID del préstamo a eliminar:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int idPrestamo = Integer.parseInt(input);
                prestamoDAO.eliminarPrestamo(idPrestamo);
                mostrarMensaje("Préstamo eliminado con éxito.");
                listarPrestamos();
            } catch (SQLException e) {
                mostrarError("Error al eliminar el préstamo: " + e.getMessage());
            } catch (NumberFormatException e) {
                mostrarError("Error: ID de préstamo inválido.");
            }
        } else {
            mostrarError("No se ha ingresado un ID válido.");
        }
    }

    private void actualizarPrestamoHandler() {
        try {
            actualizarPrestamo();
        } catch (SQLException ex) {
            mostrarError("Error al intentar actualizar: " + ex.getMessage());
        }
    }

    private void actualizarPrestamo() throws SQLException {
        int filaSeleccionada = vistaPrestamo.getTblPrestamo().getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Por favor, seleccione un préstamo para actualizar.");
            return;
        }

        String codigoUsuario = vistaPrestamo.getTxtCodigo().getText().trim();
        Usuario usuario = usuarioDAO.obtenerUsuarioPorCodigo(codigoUsuario);
        if (usuario == null) {
            mostrarError("Usuario no encontrado.");
            return;
        }

        Equipo equipo = (Equipo) vistaPrestamo.getCboEquipo().getSelectedItem();
        Date fechaDevolucion = vistaPrestamo.getCalendario().getDate();
        
        if (fechaDevolucion.before(new Date())) {
            mostrarError("La fecha de devolución no puede ser anterior a la fecha actual.");
            return;
        }

        int idPrestamo = (int) vistaPrestamo.getTblPrestamo().getValueAt(filaSeleccionada, 0);
        Prestamo prestamoActualizado = new Prestamo(idPrestamo, equipo, usuario, new Date(), fechaDevolucion);

        try {
            prestamoDAO.actualizarPrestamo(prestamoActualizado);
            listarPrestamos();
            mostrarMensaje("Préstamo actualizado exitosamente.");
        } catch (SQLException e) {
            mostrarError("Error al actualizar el préstamo: " + e.getMessage());
        }
    }

    private void listarPrestamos() {
        limpiarTabla();
        try {
            List<Prestamo> prestamos = prestamoDAO.obtenerTodosLosPrestamos();
            agregarFilaATabla(prestamos);
        } catch (SQLException e) {
            mostrarError("Error al listar los préstamos: " + e.getMessage());
        }
    }

    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vistaPrestamo.getTblPrestamo().getModel();
        modelo.setRowCount(0); // Elimina todas las filas
    }

    public void agregarFilaATabla(List<Prestamo> prestamos) {
        limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) vistaPrestamo.getTblPrestamo().getModel();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        for (Prestamo prestamo : prestamos) {
            if (prestamo != null) {
                String codigoUsuario = prestamo.getUsuario() != null ? prestamo.getUsuario().getCodigo() : "Desconocido";
                String nombreUsuario = prestamo.getUsuario() != null ? prestamo.getUsuario().getNombre() : "Desconocido";
                String nombreEquipo = prestamo.getEquipo() != null ? prestamo.getEquipo().getNombre() : "Desconocido";
                String descripcionEquipo = prestamo.getEquipo() != null ? prestamo.getEquipo().getDescripcion() : "Desconocido";

                modelo.addRow(new Object[]{
                        prestamo.getIdPrestamo(),
                        codigoUsuario,
                        nombreUsuario,
                        nombreEquipo,
                        descripcionEquipo,
                        formatoFecha.format(prestamo.getFechaPrestamo()),
                        formatoFecha.format(prestamo.getFechaDevolucion())
                });
            }
        }
    }

    private void limpiarCampos() {
        vistaPrestamo.getTxtCodigo().setText(""); // Limpiar campo de código de usuario
        vistaPrestamo.getCalendario().setDate(new java.util.Date()); // Restablecer el calendario a la fecha actual
        // Agrega más campos a limpiar si es necesario
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vistaPrestamo, mensaje);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vistaPrestamo, mensaje);
    }
}
