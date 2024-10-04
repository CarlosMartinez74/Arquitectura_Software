package Controlador;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Venta;
import Modelo.VentaDAO;
import Vista.VentaForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class VentaController {
    private VentaForm view;
    private VentaDAO ventaDAO;
    private ProductoDAO productoDAO;
    private ClienteDAO clienteDAO;
    DefaultTableModel modelo;
    private String[] columnNames = {"N° Venta","Fecha","Cliente","DNI", "Producto", "Cantidad", "Subtotal", "Descuento", "Total a Pagar"};
    public VentaController(VentaForm view, VentaDAO ventaDAO, ProductoDAO productoDAO, ClienteDAO clienteDAO) {
        this.view = view;
        this.ventaDAO = ventaDAO;
        this.productoDAO = productoDAO;
        this.clienteDAO = clienteDAO;
        this.modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(columnNames);
        view.getTblVentas().setModel(modelo);
        // Agregar ActionListener para el botón btnNuevo
    this.view.getBtnNuevo().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            limpiarCampos();
        }
    });
    this.view.getBtnModificar().addActionListener(new ModificarListener());
        this.view.getBtnRegistrar().addActionListener(new RegistrarListener());
        this.view.getTblVentas().addMouseListener(new TablaListener());
        this.view.getBtnEliminar().addActionListener(new EliminarListener());
        // Suponiendo que tienes una instancia de JComboBox llamada comboBoxProductos
List<Producto> productos = productoDAO.obtenerTodosLosProductos();
for (Producto producto : productos) {
    view.getCbxProducto().addItem(producto.getNombre()); // Agrega el nombre del producto al ComboBox
    // Si necesitas tener el objeto Producto completo disponible, puedes usar addItem(producto) en lugar de addItem(producto.getNombre())
}
actualizarTabla();
    }

    private void limpiarCampos() {
        view.getTxtDniCliente().setText("");
        view.getTxtCantidad().setText("");
        view.getTxtNombreCliente().setText("");
    }

    private void actualizarTabla() {
        DefaultTableModel modelTabla = modelo;
        modelTabla.setRowCount(0);
        for (Venta venta : ventaDAO.obtenerTodasLasVentas()) {
            modelTabla.addRow(new Object[]{
                venta.getId(),
                venta.getFecha(),
                clienteDAO.obtenerClientePorId(venta.getClienteId()).getNombre(),
                clienteDAO.obtenerClientePorId(venta.getClienteId()).getDni(),
                productoDAO.obtenerProductoPorId(venta.getProductoId()).getNombre(),
                venta.getCantidad(),
                venta.calcularSubtotal(productoDAO.obtenerProductoPorId(venta.getProductoId()).getPrecio(), venta.getCantidad()),
                venta.calcularDescuento(venta.calcularSubtotal(productoDAO.obtenerProductoPorId(venta.getProductoId()).getPrecio(), venta.getCantidad())),
                venta.calcularTotal(venta.calcularSubtotal(productoDAO.obtenerProductoPorId(venta.getProductoId()).getPrecio(), venta.getCantidad()), venta.calcularDescuento(venta.calcularSubtotal(productoDAO.obtenerProductoPorId(venta.getProductoId()).getPrecio(), venta.getCantidad()))),
                new SimpleDateFormat("dd-MM-yyyy").format(venta.getFecha())
            });
        }
        view.getTblVentas().setModel(modelTabla);
    }

    class RegistrarListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Cliente cliente=new Cliente(1,view.getTxtNombreCliente().getText(),view.getTxtDniCliente().getText());
            clienteDAO.agregarCliente(cliente);
            
            int clienteId = clienteDAO.obtenerIdClientePorDni(view.getTxtDniCliente().getText());
            int productoId = productoDAO.obtenerIdProductoPorNombre(view.getCbxProducto().getSelectedItem().toString());
            int cantidad = Integer.parseInt(view.getTxtCantidad().getText());
            Date fecha = new SimpleDateFormat("dd-MM-yyyy").parse(view.getLblFecha().getText());

            // Obtener el producto por su ID para obtener su precio
            Producto producto = productoDAO.obtenerProductoPorId(productoId);
            if (producto == null) {
                // Manejar el caso donde no se encuentra el producto
                System.out.println("Producto no encontrado.");
                return;
            }

            // Obtener el precio del producto
            double precioProducto = producto.getPrecio();

            // Calcular subtotal
            double subtotal = precioProducto * cantidad;

            // Calcular descuento
            double descuento = calcularDescuento(subtotal);

            // Calcular total
            double total = subtotal - descuento;

            // Crear objeto Venta y registrar en la base de datos
            Venta venta = new Venta(1,clienteId, productoId, cantidad, fecha);
            
            ventaDAO.agregarVenta(venta);

            // Actualizar tabla y limpiar campos
            actualizarTabla();
            limpiarCampos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private double calcularDescuento(double subtotal) {
        if (subtotal <= 300) {
            return subtotal * 0.05; // 5% de descuento
        } else if (subtotal > 300 && subtotal <= 500) {
            return subtotal * 0.10; // 10% de descuento
        } else {
            return subtotal * 0.12; // 12% de descuento
        }
    }
}
    

    class ModificarListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int selectedRow = view.getTblVentas().getSelectedRow();
            if (selectedRow == -1) {
                System.out.println("No se ha seleccionado ninguna venta para modificar.");
                return;
            }

            int ventaId = (int) modelo.getValueAt(selectedRow, 0);
            int clienteId = clienteDAO.obtenerIdClientePorDni(view.getTxtDniCliente().getText());
            int productoId = productoDAO.obtenerIdProductoPorNombre(view.getCbxProducto().getSelectedItem().toString());
            int cantidad = Integer.parseInt(view.getTxtCantidad().getText());
            Date fecha = new SimpleDateFormat("dd-MM-yyyy").parse(view.getLblFecha().getText());

            // Obtener el producto por su ID para obtener su precio
            Producto producto = productoDAO.obtenerProductoPorId(productoId);
            if (producto == null) {
                // Manejar el caso donde no se encuentra el producto
                System.out.println("Producto no encontrado.");
                return;
            }
            Cliente cliente = new Cliente(clienteId,view.getTxtNombreCliente().getText(),view.getTxtDniCliente().getText());
            clienteDAO.modificarCliente(cliente);
            // Crear objeto Venta y actualizar en la base de datos
            Venta venta = new Venta(ventaId, clienteId, productoId, cantidad, fecha);
            ventaDAO.modificarVenta(venta);

            // Actualizar tabla y limpiar campos
            actualizarTabla();
            limpiarCampos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

    class EliminarListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int selectedRow = view.getTblVentas().getSelectedRow();
            if (selectedRow != -1) {
                // Asumiendo que la columna 0 es el ID de la venta
                int ventaId = (int) modelo.getValueAt(selectedRow, 0);
                
                // Eliminar la venta de la base de datos
                ventaDAO.eliminarVenta(ventaId);
                
                // Actualizar tabla
                actualizarTabla();
            } else {
                System.out.println("Por favor seleccione una venta para eliminar.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
    public  void mostrar(){
        view.setVisible(true);
    }
    
    class TablaListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            int selectedRow = view.getTblVentas().getSelectedRow();
            if (selectedRow != -1) {
                // Obtener los datos de la fila seleccionada
                String nombreCliente = (String) modelo.getValueAt(selectedRow, 2);
                String dniCliente = (String) modelo.getValueAt(selectedRow, 3);
                String nombreProducto = (String) modelo.getValueAt(selectedRow, 4).toString();
                
                int cantidad = (int) modelo.getValueAt(selectedRow, 5);
                

                // Rellenar los JTextField y otros componentes con los valores correspondientes
                view.getTxtDniCliente().setText(dniCliente);
                view.getTxtNombreCliente().setText(nombreCliente);
                view.getCbxProducto().setSelectedItem(nombreProducto);
                view.getTxtCantidad().setText(String.valueOf(cantidad));
            }
        }
    }
}


}
