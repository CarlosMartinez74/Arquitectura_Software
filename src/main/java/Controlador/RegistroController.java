package Controlador;

import Modelo.Usuario;
import Modelo.UsuarioDAO;
import Vista.LoginForm;
import Vista.RegistroForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class RegistroController {
    private RegistroForm registroForm;
    private UsuarioDAO usuarioDAO;
    private LoginForm loginForm;

    public RegistroController(RegistroForm registroForm, UsuarioDAO usuarioDAO, LoginForm loginForm) {
        this.registroForm = registroForm;
        this.usuarioDAO = usuarioDAO;
        this.loginForm = loginForm;

        // Configurar listeners
        this.registroForm.addRegistroListener(new RegistroListener());
    }

    // ActionListener para el botón de registro
    class RegistroListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombreUsuario = registroForm.getNombreUsuario();
            String contraseña = registroForm.getContraseña();

            // Validar que el nombre de usuario termine en "@gmail.com"
            if (!nombreUsuario.toLowerCase().endsWith("@gmail.com")) {
                registroForm.mostrarMensaje("El nombre de usuario debe terminar en @gmail.com");
                return;
            }

            // Validar que la contraseña cumpla con los requisitos
            if (!validarContraseña(contraseña)) {
                registroForm.mostrarMensaje("La contraseña debe tener al menos una mayúscula, una minúscula, un número y un caracter especial como '@'");
                return;
            }

            // Crear un nuevo usuario
            Usuario nuevoUsuario = new Usuario(nombreUsuario, contraseña);

            // Agregar usuario a la base de datos
            usuarioDAO.agregarUsuario(nuevoUsuario);

            // Mostrar mensaje de éxito
            registroForm.mostrarMensaje("¡Usuario registrado exitosamente!");

            // Cerrar el formulario de registro
            registroForm.dispose();

            // Mostrar el formulario de login
            loginForm.setVisible(true);
        }

        // Método para validar la contraseña
        private boolean validarContraseña(String contraseña) {
            boolean tieneMayuscula = false;
            boolean tieneMinuscula = false;
            boolean tieneNumero = false;
            boolean tieneCaracterEspecial = false;

            for (char c : contraseña.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    tieneMayuscula = true;
                } else if (Character.isLowerCase(c)) {
                    tieneMinuscula = true;
                } else if (Character.isDigit(c)) {
                    tieneNumero = true;
                } else if (c == '@') { // Caracter especial como '@'
                    tieneCaracterEspecial = true;
                }
            }

            return tieneMayuscula && tieneMinuscula && tieneNumero && tieneCaracterEspecial;
        }
    }
}
