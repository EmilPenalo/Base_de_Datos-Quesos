package logico;

public class CilindroHueco extends Cilindro {

	private static final long serialVersionUID = 1L;
	private int radioInterno;

	public CilindroHueco(String id, String nombre, float precioBase, float precioUnitario, int radio, int longitud, int radioInterno) {
		super(id, nombre, precioBase, precioUnitario, radio, longitud);
		this.radioInterno = radioInterno;
	}

	public int getRadioInterno() {
		return radioInterno;
	}

	public void setRadioInterno(int radioInterno) {
		this.radioInterno = radioInterno;
	}
	
	public float areaBase() {
		return (float) (Math.PI* (Math.pow(radio, 2) - Math.pow(radioInterno, 2)));
	}
}
