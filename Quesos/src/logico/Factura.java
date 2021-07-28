package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Factura implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private Cliente cliente;
	private ArrayList<Queso> quesos;
	public static int codigo = 1;
	
	public Factura(String id, Cliente cliente, ArrayList<Queso> quesos) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.quesos = quesos;
		Factura.codigo++;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ArrayList<Queso> getQuesos() {
		return quesos;
	}

	public void setQuesos(ArrayList<Queso> quesos) {
		this.quesos = quesos;
	}
	
	public float precioTotal() {
		float precio = 0;
		for (Queso q : quesos) {
			precio += q.precio();
		}
		return precio;
	}
}
