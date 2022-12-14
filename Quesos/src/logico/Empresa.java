package logico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

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
	
	public Boolean insertBdCliente(Cliente c) {
		String query = "INSERT INTO Cliente VALUES("+"'"+c.getId()+"'"+","+"'"+c.getNombre()+"'"+","+"'"+c.getApellido()+"'"+","+"'"+c.getCedula()+"'"+","+getCiudadbyNombre(c.getCuidad())+","+"'"+c.getTelefono()+"'"+")";
		try {
			Statement sql = Empresa.database.createStatement();
			sql.executeUpdate(query);
			return true;
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Error al insertar el cliente a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return false;
		}
	}
	
	public Boolean insertBdFactura(Factura f, Float total) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");  
		String strFecha = dateFormat.format(f.getFecha());
		
		String query = "INSERT INTO Factura VALUES("+"'"+f.getId()+"'"+",'"+f.getCliente().getId()+"'"+","+"'"+strFecha+"',"+total.toString()+")";
		try {
			Statement sql = Empresa.database.createStatement();
			sql.executeUpdate(query);
			
			//Agregando Quesos a detalle_factura
			for (Queso q : f.getQuesos()) {
				query = "INSERT INTO Detalle_Factura VALUES("+"'"+f.getId()+"'"+",'"+q.getId()+"'"+","+f.getCantidades().get(q.getId().toString())+")";
				
				sql = Empresa.database.createStatement();
				sql.executeUpdate(query);
			}
			return true;
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Error al insertar el queso a la BD", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return false;
		}
	}
	
	public void insertarQueso(Queso q) {
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

	public Factura crearFactura(Cliente c, ArrayList<Queso> compra, Hashtable<String, Integer> cants) {
		Factura f = new Factura(getCodFactura(), c, compra, cants);	
		return f;
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
			JOptionPane.showMessageDialog(null, e.toString(), "Error de conexi???n", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public void loadClientes(String cedula_dada) {
		if(database!=null) {
			clientes.clear();
			
			String query;
			if (cedula_dada == null) {
				query = "SELECT Cliente.id_cliente, Cliente.cedula, Cliente.nombre,Cliente.apellido,Ciudad.nombre AS ciudad,Pais.nombre AS pais,Cliente.telefono FROM Cliente, Pais, Ciudad WHERE Ciudad.id_ciudad = Cliente.id_ciudad AND ciudad.id_pais = Pais.id_pais";
			} else {
				query = "SELECT Cliente.id_cliente, Cliente.cedula, Cliente.nombre,Cliente.apellido,Ciudad.nombre AS ciudad,Pais.nombre AS pais,Cliente.telefono \r\n"
						+ "FROM Cliente, Pais, Ciudad \r\n"
						+ "WHERE Ciudad.id_ciudad = Cliente.id_ciudad AND \r\n"
						+ "ciudad.id_pais = Pais.id_pais AND\r\n"
						+ "Cliente.cedula = '"+cedula_dada+"'";
			}
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
	
	public void loadQuesos(String id_factura) {
		if(database!=null) {
			
			quesos.clear();
			
			String query;
			if (id_factura == null) {
				query = "SELECT Cilindro.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, Cilindro.radio, Cilindro.longitud FROM Queso,Cilindro WHERE Queso.id_queso = Cilindro.id_queso";
			} else {
				query = "SELECT Cilindro.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, Cilindro.radio, Cilindro.longitud \r\n"
						+ "FROM Queso,Cilindro,Detalle_Factura \r\n"
						+ "WHERE Queso.id_queso = Cilindro.id_queso AND \r\n"
						+ "Detalle_Factura.id_queso = Queso.id_queso AND\r\n"
						+ "Detalle_Factura.id_factura = '"+id_factura+"'";
			}
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
					insertarQueso(qc);
				}
				
				if (id_factura == null) {
					query ="SELECT CilindroHueco.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, CilindroHueco.radio,CilindroHueco.longitud,CilindroHueco.radio_interior FROM Queso,CilindroHueco WHERE Queso.id_queso = CilindroHueco.id_queso";
				} else {
					query = "SELECT CilindroHueco.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, CilindroHueco.radio,CilindroHueco.longitud,CilindroHueco.radio_interior \r\n"
							+ "FROM Queso,CilindroHueco,Detalle_Factura \r\n"
							+ "WHERE Queso.id_queso = CilindroHueco.id_queso AND\r\n"
							+ "Detalle_Factura.id_queso = Queso.id_queso AND\r\n"
							+ "Detalle_Factura.id_factura = '"+id_factura+"'";
				}
				ResultSet QCH = sql.executeQuery(query);
				
				while(QCH.next()) {
					String id = QCH.getString(1);
					String nombre = QCH.getString(2);
					float precioBase = new Float(QCH.getFloat(3));
					float precioUnitario = new Float(QCH.getFloat(4));
					int radio = new Integer(QCH.getInt(5));
					int longitud = new Integer(QCH.getInt(6));
					int radioInterno = new Integer(QCH.getInt(7));
					CilindroHueco qch = new CilindroHueco(id, nombre, precioBase, precioUnitario, radio, longitud, radioInterno);
					insertarQueso(qch);
				}
				
				if (id_factura == null) {
					query = "SELECT Esfera.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, Esfera.radio  FROM Queso,Esfera WHERE Queso.id_queso = Esfera.id_queso";
				} else {
					query = "SELECT Esfera.id_queso, Queso.nombre, Queso.precio_base,Queso.precio_unitario, Esfera.radio  \r\n"
							+ "FROM Queso,Esfera,Detalle_Factura \r\n"
							+ "WHERE Queso.id_queso = Esfera.id_queso AND\r\n"
							+ "Detalle_Factura.id_queso = Queso.id_queso AND\r\n"
							+ "Detalle_Factura.id_factura = '"+id_factura+"'";
				}
				ResultSet QE = sql.executeQuery(query);
				
				while(QE.next()) {
					String id = QE.getString(1);
					String nombre = QE.getString(2);
					float precioBase = new Float(QE.getFloat(3));
					float precioUnitario = new Float(QE.getFloat(4));
					int radio = new Integer(QE.getInt(5));
					Esfera qe = new Esfera(id, nombre, precioBase, precioUnitario, radio);
					insertarQueso(qe);
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
			String id = "-1";
			Cliente cliente = null;
			Date fecha = null;
			try {
				Statement sql = database.createStatement();
				ResultSet f = sql.executeQuery(query);
				while(f.next()) {
					id = f.getString(1);
					cliente = buscarClientebyId(f.getString(2));
					fecha = f.getDate(3);
					
					Factura factura = new Factura(id, cliente, null, null);
					factura.setFecha(fecha);
					insertarFactura(factura);
				}
				
				String detalleQuery = "SELECT id_factura, id_queso, cantidad FROM Detalle_Factura WHERE Detalle_Factura.id_factura="+"'"+id+"'";
				ResultSet df = sql.executeQuery(detalleQuery);
				ArrayList<Queso> listQuesos = new ArrayList<Queso>();
				Hashtable<String, Integer> cantidades = new Hashtable<String, Integer>();
				
				while(df.next()) {
					Queso q = findQuesoById(df.getString(1));
					listQuesos.add(q);
					Integer i = new Integer(df.getInt(2));
					cantidades.put(q.getId().toString(), i);
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
	
	public Integer getCiudadbyNombre(String nombre) {
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
	
	public boolean validarDatosCliente(String cedula,String telefono) {
		boolean b1 = validarCedula(cedula);
		boolean b2 = validarTelefono(telefono);

		if(b1==true && b2==true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean validarCedula(String cedula) {
		String query = "SELECT dbo.validar_cedula"+"("+"'"+cedula+"'"+")";
		try {
			Statement sql = database.createStatement();
			ResultSet s1 = sql.executeQuery(query);
			while(s1.next()) {
				if(s1.getBoolean(1) == true) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean validarTelefono(String telefono) {
		String query = "SELECT dbo.validar_telefono"+"("+"'"+telefono+"'"+")";
		try {
			Statement sql = database.createStatement();
			ResultSet s1 = sql.executeQuery(query);
			while(s1.next()) {
				if(s1.getBoolean(1) == true) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public ArrayList<String[]> getAllFacturas(String fechaini, String fechafin) {
		ArrayList<String[]> resList = new ArrayList<String[]>();
		String query ="exec sp_getFacturasByDate '"+fechaini+"','"+fechafin+"'";
		
		try {
			Statement sql = database.createStatement();
			ResultSet res = sql.executeQuery(query);
			while(res.next()) {
				String[] i = new String[5];
				
				i[0] = res.getString(1);
				i[1] = res.getString(2)+" "+res.getString(3);
				i[2] = res.getString(4);
				i[3] = res.getString(5);
				i[4] = res.getString(6);
				
				resList.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resList;
	}

	public void clearClientes() {
		clientes.clear();
	}

	public void clearQuesos() {
		quesos.clear();
	}

	public void clearFacturas() {
		facturas.clear();
	}

	public String getCodCliente() {
		try {
			String query = "SELECT COUNT(*) FROM Cliente";
			Statement sql = Empresa.database.createStatement();
			ResultSet cod = sql.executeQuery(query);
			
			while(cod.next()) {
				Cliente.codigo = cod.getInt(1)+1;
			}
			return "C-" + Cliente.codigo;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getCodFactura() {
		try {
			String query = "SELECT COUNT(*) FROM Factura";
			Statement sql = Empresa.database.createStatement();
			ResultSet cod = sql.executeQuery(query);
			
			while(cod.next()) {
				Factura.codigo = cod.getInt(1)+1;
			}
			return "F-" + Factura.codigo;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCodQueso() {
		try {
			String query = "SELECT COUNT(*) FROM Queso";
			Statement sql = Empresa.database.createStatement();
			ResultSet cod = sql.executeQuery(query);
			
			while(cod.next()) {
				Queso.codigo = cod.getInt(1)+1;
			}
			return "Q-" + Queso.codigo;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean eliminarQuesoBd(Queso q) {
		String query = "SELECT dbo.validar_elim_queso("+"'"+q.id+"'"+")";
		boolean b= false;
		try {
			Statement sql = database.createStatement();
			ResultSet res = sql.executeQuery(query);
			
			while(res.next()) {
				b = res.getBoolean(1);
			}
			
			if(b==true) {
				if(q instanceof Cilindro) {
					String delC = "DELETE FROM Cilindro WHERE id_queso="+"'"+q.id+"'";
					sql.executeUpdate(delC);
				}
				
				if(q instanceof CilindroHueco) {
					String delCH = "DELETE FROM CilindroHueco WHERE id_queso="+"'"+q.id+"'";
					sql.executeUpdate(delCH);
				}
				
				if(q instanceof Esfera) {
					String delE = "DELETE FROM Esfera WHERE id_queso="+"'"+q.id+"'";
					sql.executeUpdate(delE);
				}
				String delQ = "DELETE FROM Queso WHERE id_queso="+"'"+q.id+"'"; 
				sql.executeUpdate(delQ);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public boolean eliminarClieteBd(Cliente c) {
		String query = "SELECT dbo.validar_elim_cliente("+"'"+c.getId()+"'"+")";
		boolean b = false;
		try {
			Statement sql = database.createStatement();
			ResultSet res = sql.executeQuery(query);
			
			while(res.next()) {
				b = res.getBoolean(1);
			}
			
			if(b==true) {
				String del = "DELETE FROM Cliente WHERE id_cliente="+"'"+c.getId()+"'";
				sql.executeUpdate(del);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
