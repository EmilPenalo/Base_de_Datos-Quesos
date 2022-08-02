package logico;

public class Cliente{

	private String id;
	private String cedula;
	private String nombre;
	private String apellido;
	private String cuidad;
	private String pais;
	private String telefono;
	public static int codigo = 1;
	
	
	public Cliente(String id, String cedula, String nombre, String telefono, String cuidad, String pais, String apellido) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombre = nombre;
		this.cuidad = cuidad;
		this.pais = pais;
		this.telefono = telefono;
		this.apellido = apellido;
		Cliente.codigo++;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
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
