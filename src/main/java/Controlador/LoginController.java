/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.ClienteDAO;
import Modelo.ProductoDAO;
import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Modelo.VentaDAO;
import Vista.LoginForm;
import Vista.RegistroForm;
import Vista.VentaForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginController {

    private LoginForm loginForm;
    private UsuarioDAO usuarioDAO;
    private RegistroForm registroForm;

    public LoginController(LoginForm loginForm, RegistroForm registroForm, UsuarioDAO usuarioDAO) {
        this.loginForm = loginForm;
        this.registroForm = registroForm;
        this.usuarioDAO = usuarioDAO;

        // Configurar listeners
        this.loginForm.addLoginListener(new LoginListener());
        this.loginForm.addRegistroListener(new RegistroListener()); // Listener para ir al formulario de registro

    }

    // ActionListener para el botón de login
    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String nombreUsuario = loginForm.getNombreUsuario();
            String contraseña = loginForm.getContraseña();

            // Validar usuario
            Usuario usuario = usuarioDAO.validarUsuario(nombreUsuario, contraseña);

            if (usuario != null) {
                // Login exitoso: mostrar el formulario de ventas
                loginForm.mostrarMensaje("BIENVENIDO !! <3");
                mostrarVentaForm(); // Método para mostrar el formulario de ventas
                loginForm.dispose();
            } else {
                // Login fallido
                loginForm.mostrarMensaje("Usuario o contraseña incorrectos");
                // Aquí puedes manejar la lógica para mostrar un mensaje de error o hacer alguna acción adicional
            }
        }
    }

    // ActionListener para el botón de registro
    class RegistroListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Mostrar el formulario de registro
            registroForm.setVisible(true);
            // Opcionalmente, puedes ocultar el formulario de login si lo deseas
            loginForm.setVisible(false);
        }
    }

    private void mostrarVentaForm() {
        Connection connection = establecerConexion();
        //connection = establecerConexion();
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        ProductoDAO productoDAO = new ProductoDAO(connection);
        VentaDAO ventaDAO = new VentaDAO(connection);
        //UsuarioDAO usuarioDAO = new UsuarioDAO(connection); // DAO para usuarios

        // Crear formularios y controladores
        //LoginForm loginForm = new LoginForm();
        //RegistroForm registroForm = new RegistroForm();
        VentaForm ventaForm = new VentaForm();
        VentaController ventaController = new VentaController(ventaForm, ventaDAO, productoDAO, clienteDAO);
        ventaController.mostrar();

    }

    private static Connection establecerConexion() {
        Connection connection = null;
        try {
            // Configurar la conexión con la base de datos (ejemplo para MySQL)
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
