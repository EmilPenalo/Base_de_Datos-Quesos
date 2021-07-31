package logico;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Flujo extends Thread {
	private Socket nsfd;
	private DataInputStream FlujoLectura;
	private DataOutputStream FlujoEscritura;
	
	public Flujo(Socket sfd) {
		this.nsfd=sfd;
		try
	    {
	      FlujoLectura = new DataInputStream(new BufferedInputStream(sfd.getInputStream()));
	      FlujoEscritura = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
	    }
	    catch(IOException ioe)
	   {
	      JOptionPane.showMessageDialog(null,"IOException(Flujo)","Error",JOptionPane.ERROR_MESSAGE);
	   }
	}
	
	public Socket getNsfd() {
		return nsfd;
	}


	public void setNsfd(Socket nsfd) {
		this.nsfd = nsfd;
	}


	public DataInputStream getFlujoLectura() {
		return FlujoLectura;
	}


	public void setFlujoLectura(DataInputStream flujoLectura) {
		FlujoLectura = flujoLectura;
	}

	public DataOutputStream getFlujoEscritura() {
		return FlujoEscritura;
	}

	public void setFlujoEscritura(DataOutputStream flujoEscritura) {
		FlujoEscritura = flujoEscritura;
	}

	public void run() {
		while(true) {
				try {
					String linea=FlujoLectura.readUTF();
					JOptionPane.showMessageDialog(null, "Guardando en respaldo","Generar archivo de respaldo",JOptionPane.INFORMATION_MESSAGE);
					FileWriter escritor=new FileWriter("respaldo/factura.txt");
					escritor.write(linea);
					escritor.close();
				} catch (IOException e) {
					break;
				}	
		}
		
	}
}
