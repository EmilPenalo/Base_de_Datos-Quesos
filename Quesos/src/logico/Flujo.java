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
		Servidor.archivo=new File("factura/factura.txt");
        boolean eof=false;
        String linea="";
        FileWriter escritor=null;
		try {
			escritor=new FileWriter("respaldo/factura.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		try {
			 while(!eof) {
		            linea=FlujoLectura.readUTF();
		           
		            if(linea!=null) {
		            	try {
		          	      escritor.write(linea);
		          	      FlujoEscritura.writeUTF(linea);
		          	      FlujoEscritura.flush();
		          	      
		          	    } catch (IOException e) {
		          	      e.printStackTrace();
		          	    }
		            }else {
		              eof=true;
		            }
		        }
			 	escritor.close();
		        FlujoEscritura.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error de escritura o lectura de archivo","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	/*public void enviarArchivoRespaldo(File archivo) {
		
		synchronized(Servidor.archivo) {
			
			try {
				FlujoEscritura.writeBytes(Servidor.archivo.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
}
