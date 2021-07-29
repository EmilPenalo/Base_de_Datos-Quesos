package logico;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class Flujo extends Thread {
	private Socket nsfd;
	private DataInputStream FlujoLectura;
	private DataOutputStream FlujoEscritura;
	
	public Flujo(Socket sfd) {
		this.nsfd=sfd;
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
		Servidor.archivo=new File("respaldo/factura.txt");
        boolean eof=false;
        String linea="";
        BufferedReader lector;
        
		try {
			lector=new BufferedReader(new FileReader(Servidor.archivo));
			 while(!eof) {
		            linea=lector.readLine();
		           
		            if(linea!=null) {
		            	try {
		        			
		          	      FileWriter escritor = new FileWriter(Servidor.archivo.getPath());
		          	      escritor.write(linea);
		          	      escritor.close();
		          	      
		          	    } catch (IOException e) {
		          	      e.printStackTrace();
		          	    }
		            }else {
		              eof=true;
		            }
		        }

		        lector.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
