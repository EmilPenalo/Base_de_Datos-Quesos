package logico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;
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

	public void crearFactura(Cliente c, ArrayList<Queso> compra, ArrayList<Integer> cants) {
		Factura f = new Factura("F-" + Factura.codigo, c, compra, cants);
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
	+  "loginTimeout=10;"
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
	
	public void loadQuesos() {
		if(database!=null) {
			String query = "SELECT Cilindro.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, Cilindro.radio, Cilindro.longitud FROM Queso,Cilindro WHERE Queso.id_queso = Cilindro.id_queso";
			try {
				Statement sql = database.createStatement();
				ResultSet QC = sql.executeQuery(query);
				
				while(QC.next()) {
					String id = QC.getString(1);
					String nombre = QC.getString(2);
					float precioBase = new Float(QC.getFloat(3));
					float precioUnitario = new Float(QC.getFloat(4));
					int radio = new Integer(QC.getInt(5));
					int longitud = new Integer(QC.getInt(6));
					Cilindro qc = new Cilindro(id, nombre, precioBase, precioUnitario, radio, longitud);
					insettarQueso(qc);
				}
				String qchQuery ="SELECT CilindroHueco.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, CilindroHueco.radio,CilindroHueco.longitud,CilindroHueco.radio_interior FROM Queso,CilindroHueco WHERE Queso.id_queso = CilindroHueco.id_queso";
				ResultSet QCH = sql.executeQuery(qchQuery);
				
				while(QCH.next()) {
					String id = QCH.getString(1);
					String nombre = QCH.getString(2);
					float precioBase = new Float(QCH.getFloat(3));
					float precioUnitario = new Float(QCH.getFloat(4));
					int radio = new Integer(QCH.getInt(5));
					int longitud = new Integer(QCH.getInt(6));
					int radioInterno = new Integer(QCH.getInt(7));
					CilindroHueco qch = new CilindroHueco(id, nombre, precioBase, precioUnitario, radio, longitud, radioInterno);
					insettarQueso(qch);
				}
				
				String qeQuery ="SELECT Esfera.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, Esfera.radio  FROM Queso,Esfera WHERE Queso.id_queso = Esfera.id_queso";
				ResultSet QE = sql.executeQuery(qeQuery);
				
				while(QE.next()) {
					String id = QE.getString(1);
					String nombre = QE.getString(2);
					float precioBase = new Float(QE.getFloat(3));
					float precioUnitario = new Float(QE.getFloat(4));
					int radio = new Integer(QE.getInt(5));
					Esfera qe = new Esfera(id, nombre, precioBase, precioUnitario, radio);
					insettarQueso(qe);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al cargar los datos de los quesos", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	public void loadFactura() {
		if(database!=null) {
			String query = "SELECT * FROM Factura";
			try {
				Statement sql = database.createStatement();
				ResultSet f = sql.executeQuery(query);
				while(f.next()) {
					String id = f.getString(1);
					Cliente cliente = buscarClientebyId(f.getString(2));
					Date fecha = f.getDate(3);
					String detalleQuery = "SELECT Detalle_Factura.id_queso, Detalle_Factura.cantidad FROM  Factura,Detalle_Factura WHERE Detalle_Factura.id_queso="+"'"+id+"'";
					ResultSet df = sql.executeQuery(detalleQuery);
					
					ArrayList<Queso> listQuesos = new ArrayList();
					ArrayList<Integer> cantidades = new ArrayList();
					while(df.next()) {
					Queso q = findQuesoById(df.getString(1));
					listQuesos.add(q);
					Integer i = new Integer(df.getInt(2));
					cantidades.add(i);
					}
					Factura factura = new Factura(id, cliente, listQuesos, cantidades);
					factura.setFecha(fecha);
					insertarFactura(factura);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al cargar los datos de las facturas", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	private Cliente buscarClientebyId(String id) {
		for (Cliente c : clientes) {
			if (c.getId().equalsIgnoreCase(id)) {
				return c;
			}
		}
		return null;
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
	
	public Integer getCiudadbyNobre(String nombre) {
		Integer id = -1;
		String query ="SELECT id_ciudad FROM Ciudad WHERE nombre LIKE" + "'" + nombre + "'";
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

	public String getNombrePaisbyId(Integer id) {
		String nombre = "";
		String query ="SELECT nombre FROM Pais WHERE id_pais = " + id.toString();
		try {
			Statement sql = database.createStatement();
			ResultSet res = sql.executeQuery(query);
			while(res.next()) {
				nombre = res.getString("nombre");
			}
			return nombre;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
