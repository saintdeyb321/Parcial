/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prestamoequipos;

import Controlador.ControladorPrestamo;
import Controlador.ControladorUsuario;
import DAO.EquipoDAO;
import DAO.PrestamoDAO;
import DAO.UsuarioDAO;
import Modelo.Conexion;
import Vista.IInicio;
import Vista.IPrestamo;
import Vista.IUsuario;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import java.sql.Connection;

import javax.swing.UIManager;

public class PrestamoEquipos {
    public static void main(String[] args) {
        // Establecer el Look and Feel
        try {
            UIManager.setLookAndFeel(new HiFiLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear conexión a la base de datos
        Connection conexion = Conexion.getConnection();

        // Crear instancias de los DAOs pasando la conexión
        PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
        EquipoDAO equipoDAO = new EquipoDAO(conexion);
        UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

        // Crear las vistas
        IPrestamo vistaPrestamo = new IPrestamo(null); // Inicializa sin controlador aún
        IUsuario vistaUsuario = new IUsuario(null); // Inicializa sin controlador aún

        // Crear los controladores y pasar las vistas y DAOs
        ControladorPrestamo controladorPrestamo = new ControladorPrestamo(vistaPrestamo, prestamoDAO, equipoDAO, usuarioDAO);
        vistaPrestamo.setControlador(controladorPrestamo);

        ControladorUsuario controladorUsuario = new ControladorUsuario(vistaUsuario, conexion);
        vistaUsuario.setControlador(controladorUsuario);

        // Mostrar la ventana principal (IInicio) y pasar los controladores y vistas
        IInicio vistaInicio = new IInicio(controladorPrestamo, controladorUsuario, vistaPrestamo, vistaUsuario);
        vistaInicio.setVisible(true);
        vistaInicio.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }
}
