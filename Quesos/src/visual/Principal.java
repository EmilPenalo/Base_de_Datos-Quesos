package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logico.Empresa;

import javax.swing.border.BevelBorder;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Dimension dim;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {	    
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			}
		});
		
		Empresa.database = Empresa.getSqlConnection();
		
		setTitle("Registro de Quesos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 398);
		
		dim = getToolkit().getScreenSize();
		setSize(dim.width, dim.height-35);
		
		setLocationRelativeTo(null);
		
		setResizable(false);
		
		if(Empresa.database!=null) {
			Empresa.getInstance().loadQuesos();
			Empresa.getInstance().loadClientes();
			//Empresa.getInstance().loadFactura();
		}else {
			JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnClientes = new JMenu("Clientes");
		menuBar.add(mnClientes);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Listar");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListCliente listc = new ListCliente();
				listc.setModal(true);
				listc.setVisible(true);
			}
		});
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Registrar");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegCliente regc = new RegCliente(null);
				regc.setModal(true);
				regc.setVisible(true);
			}
		});
		mnClientes.add(mntmNewMenuItem_5);
		mnClientes.add(mntmNewMenuItem);
		
		JMenu mnQuesos = new JMenu("Quesos");
		menuBar.add(mnQuesos);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Registrar");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegQueso regQ = new RegQueso(null);
				regQ.setModal(true);
				regQ.setVisible(true);
			}
		});
		mnQuesos.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Listar");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListQueso listQ = new ListQueso(null);
				listQ.setModal(true);
				listQ.setVisible(true);
			}
		});
		mnQuesos.add(mntmNewMenuItem_3);
		
		JMenu mnPedidos = new JMenu("Pedidos");
		menuBar.add(mnPedidos);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Hacer pedido");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HacerPedido pedido = new HacerPedido();
				pedido.setModal(true);
				pedido.setVisible(true);
			}
		});
		mnPedidos.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Listado de Facturas");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListFactura fact = new ListFactura(null);
				fact.setModal(true);
				fact.setVisible(true);
			}
		});
		mnPedidos.add(mntmNewMenuItem_4);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(10);
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.add(panel_1, BorderLayout.SOUTH);
		
	}

}
