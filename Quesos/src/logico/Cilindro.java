package logico;

public class Cilindro extends Queso {

	private static final long serialVersionUID = 1L;
	protected int radio;
	protected int longitud;
	
	public Cilindro(String id, float precioBase, float precioUnitario, int radio, int longitud) {
		super(id, precioBase, precioUnitario);
		this.radio = radio;
		this.longitud = longitud;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	public int getLongitud() {
		return longitud;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	@Override
	public float volumen() {
		return areaBase()*longitud;
	}
	
	public float areaBase() {
		return (float) (Math.PI*Math.pow(radio, 2));
	}

}
