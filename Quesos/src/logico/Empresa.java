package logico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import visual.Principal;

public class Empresa {
	
	private ArrayList<Cliente> clientes;
	private ArrayList<Factura> facturas;
	private ArrayList<Queso> quesos;
	private static Empresa empresa = null;
	public static Connection database = null;
	
	public Empresa() {
		super();
		this.clientes = new ArrayList<>();
		this.facturas = new ArrayList<>();
		this.quesos = new ArrayList<>();
	}
	
	public static Empresa getInstance() {
		if (empresa == null) {
			empresa = new Empresa();
		}
		return empresa;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(ArrayList<Cliente> clientes) {
		this.clientes = clientes;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}

	public ArrayList<Queso> getQuesos() {
		return quesos;
	}

	public void setQuesos(ArrayList<Queso> quesos) {
		this.quesos = quesos;
	}
	
	public float precioTotal(String idFactura) {
		Factura factura = findFacturaById(idFactura);
		if (factura != null) {
			return factura.precioTotal();
		}
		return -1;
	}

	public Factura findFacturaById(String id) {
		for (Factura f : facturas) {
			if (f.getId().equalsIgnoreCase(id)) {
				return f;
			}
		}
		return null;
	}
	
	public Queso findQuesoById(String id) {
		for (Queso q : quesos) {
			if (q.getId().equalsIgnoreCase(id)) {
				return q;
			}
		}
		return null;
	}
	
	public Cliente findClienteByCedula(String cedula) {
		for (Cliente c : clientes) {
			if (c.getCedula().equalsIgnoreCase(cedula)) {
				return c;
			}
		}
		return null;
	}
	
	public void insertarCliente(Cliente c) {
		clientes.add(c);
	}
	
	public void insertarFactura(Factura f) {
		facturas.add(f);
	}
	
	public void insettarQueso(Queso q) {
		quesos.add(q);
	}
	
	public float precioMayorEsferico() {
		Queso queso = null;
		float mayor = 0;
		
		for (Queso q : quesos) {
			if (q instanceof Esfera) {
				if (q.volumen() > mayor) {
					mayor = q.volumen();
					queso = q;
				}
			}
		}
		return queso.precio();
	}
	
	public ArrayList<Esfera> getEsferas() {
		ArrayList<Esfera> esferas = new ArrayList<>();
		for (Queso q : quesos) {
			if (q instanceof Esfera) {
				esferas.add((Esfera) q);
			}
		}
		return esferas;
	}
	
	public ArrayList<Cilindro> getCilindros() {
		ArrayList<Cilindro> cilindros = new ArrayList<>();
		for (Queso q : quesos) {
			if (q instanceof Cilindro) {
				cilindros.add((Cilindro) q);
			}
		}
		return cilindros;
	}
	
	public ArrayList<CilindroHueco> getCilindrosHuecos() {
		ArrayList<CilindroHueco> cilindros = new ArrayList<>();
		for (Queso q : quesos) {
			if (q instanceof CilindroHueco) {
				cilindros.add((CilindroHueco) q);
			}
		}
		return cilindros;
	}

	public void eliminarCliente(Cliente c) {
		clientes.remove(c);
	}

	public void eliminarQueso(Queso q) {
		quesos.remove(q);
	}

	public void crearFactura(Cliente c, ArrayList<Queso> compra) {
		Factura f = new Factura("F-" + Factura.codigo, c, compra);
		insertarFactura(f);	
	}
	
	public ArrayList<Factura> getFacturasOfCliente(String cedula) {
		ArrayList<Factura> deCliente = new ArrayList<>();
		for (Factura f : facturas) {
			if (f.getCliente().getCedula().equalsIgnoreCase(cedula)) {
				deCliente.add(f);
			}
		}
		return deCliente;
	}

	public static void setTienda(Empresa temp) {
		Empresa.empresa = temp;
	}
	
	public static Connection getSqlConnection() {
		String url = "jdbc:sqlserver://192.168.100.118:1433;" 
	+ "database=proyecto_final_grupo3;"
	+ "user=sa;"
	+ "password=Eict@2021;"
	+  "loginTimeout=30;"
	+ "encrypt=true;"
	+ "trustServerCertificate=true;";
		
		try {
			Connection con = DriverManager.getConnection(url);
			return con;
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error de conexi�n", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public void loadClientes() {
		if(database!=null) {
			String query = "SELECT Cliente.id_cliente, Cliente.cedula, Cliente.nombre,Cliente.apellido,Ciudad.nombre AS ciudad,Pais.nombre AS pais,Cliente.telefono FROM Cliente, Pais, Ciudad WHERE Ciudad.id_ciudad = Cliente.id_ciudad AND ciudad.id_pais = Pais.id_pais";
			try {
				Statement sql = database.createStatement();
				ResultSet clientes = sql.executeQuery(query);
				
				while(clientes.next()) {
					String id = clientes.getString(1);
					String cedula = clientes.getString(2);
					String nombre = clientes.getString(3);
					String apellido = clientes.getString(4);
					String ciudad = clientes.getString(5);
					String pais = clientes.getString(6);
					String telefono = clientes.getString(7);
					Cliente c = new Cliente(id, cedula, nombre, telefono, ciudad, pais, apellido);
					insertarCliente(c);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al cargar datos", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} 
		}
	}
	
	public Integer getPaisbyNombre(String nombre) {
		Integer id = -1;
		String query ="SELECT id_pais FROM Pais WHERE nombre LIKE" + "'" + nombre + "'";
		try {
			Statement sql = database.createStatement();
			ResultSet res = sql.executeQuery(query);
			while(res.next()) {
				id = res.getInt(1);
			}
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
