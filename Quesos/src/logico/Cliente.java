package logico;

import java.io.Serializable;

public class Cliente implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String cedula;
	private String nombre;
	private String cuidad;
	private String pais;
	private String telefono;
	public static int codigo = 1;
	
	
	public Cliente(String id, String cedula, String nombre, String telefono, String cuidad, String pais) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombre = nombre;
		this.cuidad = cuidad;
		this.pais = pais;
		this.telefono = telefono;
		Cliente.codigo++;
	}
	
	public String getPais() {
		return pais;
	}
	
	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCuidad() {
		return cuidad;
	}

	public void setCuidad(String cuidad) {
		this.cuidad = cuidad;
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
