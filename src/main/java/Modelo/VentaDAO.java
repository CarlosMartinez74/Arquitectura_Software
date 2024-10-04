package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    private Connection connection;

    public VentaDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarVenta(Venta venta) {
        String sql = "INSERT INTO Ventas (cliente_id, producto_id, cantidad, fecha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venta.getClienteId());
            stmt.setInt(2, venta.getProductoId());
            stmt.setInt(3, venta.getCantidad());
            stmt.setTimestamp(4, new java.sql.Timestamp(venta.getFecha().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificarVenta(Venta venta) {
        String sql = "UPDATE Ventas SET  producto_id = ?, cantidad = ?, fecha = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venta.getProductoId());
            stmt.setInt(2, venta.getCantidad());
            stmt.setTimestamp(3, new java.sql.Timestamp(venta.getFecha().getTime()));
            stmt.setInt(4, venta.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarVenta(int ventaId) {
        String sql = "DELETE FROM Ventas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ventaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Venta obtenerVentaPorId(int ventaId) {
        String sql = "SELECT * FROM Ventas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ventaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setClienteId(rs.getInt("cliente_id"));
                venta.setProductoId(rs.getInt("producto_id"));
                venta.setCantidad(rs.getInt("cantidad"));
                venta.setFecha(rs.getTimestamp("fecha"));
                return venta;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Venta> obtenerTodasLasVentas() {
        String sql = "SELECT * FROM Ventas";
        List<Venta> ventas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setClienteId(rs.getInt("cliente_id"));
                venta.setProductoId(rs.getInt("producto_id"));
                venta.setCantidad(rs.getInt("cantidad"));
                venta.setFecha(rs.getTimestamp("fecha"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }
}
