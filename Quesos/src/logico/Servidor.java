package logico;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Servidor extends Thread {
	public static File archivo;
	
	public static void main(String[] args) {
		ServerSocket sfd=null;
		try {
		sfd=new ServerSocket(8000);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "La Comunicaci�n ha sido rechazada"+e,"conecxi�n al servidor", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
		
		while (true)
	    {
	      try
	      {
	        Socket nsfd=sfd.accept();
	        JOptionPane.showMessageDialog(null,"Conexion aceptada de: "+nsfd.getInetAddress(),"Conecxi�n aceptada",JOptionPane.INFORMATION_MESSAGE);
		    Flujo flujo=new Flujo(nsfd);
		    Thread t =new Thread(flujo);
	        t.start();
	      }
	      catch(IOException e)
	      {
	        JOptionPane.showMessageDialog(null,"Error: "+e,"Error",JOptionPane.INFORMATION_MESSAGE);
	      }

	}

}
}

//Delete this comment