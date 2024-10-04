
package Modelo;
import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombreUsuario, contraseña) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContraseña());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario validarUsuario(String nombreUsuario, String contraseña) {
        String sql = "SELECT * FROM Usuarios WHERE nombreUsuario = ? AND contraseña = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                usuario.setContraseña(rs.getString("contraseña"));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Usuario> obtenerListaUsuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                usuario.setContraseña(rs.getString("contraseña"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }
}

