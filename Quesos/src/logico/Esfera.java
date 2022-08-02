package logico;

public class Esfera extends Queso {

	private int radio;
	
	public Esfera(String id, String nombre, float precioBase, float precioUnitario, int radio) {
		super(id, nombre, precioBase, precioUnitario);
		this.radio = radio;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	@Override
	public float volumen() {
		return (float) ((4*(Math.PI*Math.pow(radio, 3)))/3);
	}

}
