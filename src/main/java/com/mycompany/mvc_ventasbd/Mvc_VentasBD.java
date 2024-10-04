
package com.mycompany.mvc_ventasbd;

import Controlador.LoginController;
import Controlador.RegistroController;
import Controlador.VentaController;
import Modelo.ClienteDAO;
import Modelo.ProductoDAO;
import Modelo.UsuarioDAO;
import Modelo.VentaDAO;
import Vista.LoginForm;
import Vista.RegistroForm;
import Vista.VentaForm;

import javax.swing.*;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mvc_VentasBD {

    public static void main(String[] args) {
        // Configuración de la conexión a la base de datos
        Connection connection = establecerConexion();

        // Verificación de la conexión
        if (connection != null) {
            // Inicialización de los DAOs y las vistas
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            ProductoDAO productoDAO = new ProductoDAO(connection);
            VentaDAO ventaDAO = new VentaDAO(connection);
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection); // DAO para usuarios

            // Crear formularios y controladores
            LoginForm loginForm = new LoginForm();
            RegistroForm registroForm = new RegistroForm();
            VentaForm ventaForm = new VentaForm();

            // Crear controladores y vincularlos con las vistas y los DAOs
            LoginController loginController = new LoginController(loginForm, registroForm, usuarioDAO);
            RegistroController registroController = new RegistroController(registroForm, usuarioDAO, loginForm);
            VentaController ventaController = new VentaController(ventaForm, ventaDAO, productoDAO, clienteDAO);
            
            // Configurar acción para mostrar el formulario de registro desde el login
            loginForm.addRegistroListener(e -> {
                loginForm.setVisible(false);
                registroForm.setVisible(true);
            });

            // Mostrar primero el formulario de login
            EventQueue.invokeLater(() -> {
                loginForm.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.");
        }
    }

    private static Connection establecerConexion() {
        Connection connection = null;
        try {
            // Configurar la conexión con la base de datos (ejemplo para SQL Server)
            /*String url = "jdbc:sqlserver://localhost:1433;databaseName=MicroPC;encrypt=true;trustServerCertificate=true";
            String usuario = "user";
            String contraseña = "123";*/
            String url = "jdbc:mysql://localhost:3305/baseventas"; // Cambia el puerto y el nombre de la base de datos según sea necesario
            String usuario = "root";
            String contraseña = "1234";
            connection = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}

