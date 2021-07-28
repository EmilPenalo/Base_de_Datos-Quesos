package logico;

import java.io.Serializable;

public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String cedula;
	private String nombre;
	private String direccion;
	private String telefono;
	public static int codigo = 1;
	
	
	public Cliente(String id, String cedula, String nombre, String direccion, String telefono) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		Cliente.codigo++;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String id) {
		this.cedula = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
