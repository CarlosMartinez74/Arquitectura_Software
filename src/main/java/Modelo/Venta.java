
package Modelo;

import java.util.Date;

public class Venta {
    private int id;
    private int clienteId;
    private int productoId;
    private int cantidad;
    private Date fecha;

    public Venta(int id, int clienteId, int productoId, int cantidad, Date fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public Venta() {
    }

    
    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

   
    public double calcularSubtotal(double precio, int cantidad) {
        return precio * cantidad;
    }

    public double calcularDescuento(double subtotal) {
        if (subtotal <= 300) {
            return subtotal * 0.05;
        } else if (subtotal <= 500) {
            return subtotal * 0.10;
        } else {
            return subtotal * 0.12;
        }
    }

    public double calcularTotal(double subtotal, double descuento) {
        return subtotal - descuento;
    }
    
}
