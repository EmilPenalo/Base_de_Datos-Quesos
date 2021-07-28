package logico;

import java.io.Serializable;

public abstract class Queso implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String id;
	protected float precioBase;
	protected float precioUnitario;
	public static int codigo = 1;
	
	public Queso(String id, float precioBase, float precioUnitario) {
		super();
		this.id = id;
		this.precioBase = precioBase;
		this.precioUnitario = precioUnitario;
		Queso.codigo++;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(float precioBase) {
		this.precioBase = precioBase;
	}

	public float getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(float precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	
	public abstract float volumen();
	
	public float precio() {
		return precioBase + precioUnitario * volumen();
	}
}
