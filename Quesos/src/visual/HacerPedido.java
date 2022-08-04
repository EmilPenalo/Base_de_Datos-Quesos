package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

import logico.Cilindro;
import logico.CilindroHueco;
import logico.Cliente;
import logico.Empresa;
import logico.Esfera;
import logico.Factura;
import logico.Queso;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSpinner;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class HacerPedido extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCedula;
	private JTextField txtNombre;
	private Cliente selected = null;
	private JButton btnDerecha;
	private JButton btnIzquierda;
	private JList<String> listDisponible;
	private DefaultListModel<String> listModelDisp;
	private JList<String> listCompra;
	private DefaultListModel<String> listModelCompra;
	private JTextField txtCodigo;
	private JTextField txtTelefono;
	private JSpinner spnMonto;
	private JComboBox cbxCuidad;
	private JComboBox cbxPais;
	private JTextField txtApellido;
	private JSpinner spnCantidad;
	
	private Hashtable<String, Integer> cantidades = new Hashtable<String, Integer>();
	private String selectedProduct;
	private JButton btnActualizar;
	
	/**
	  Launch the application.
	 */
	public static void main(String[] args) {
		try {
			HacerPedido dialog = new HacerPedido();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public HacerPedido() {
		setTitle("Manejo de Prestamos");
		setBounds(100, 100, 502, 628);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			{
				JLabel lblCedula = new JLabel("Cedula:");
				lblCedula.setBounds(15, 16, 127, 20);
				panel.add(lblCedula);
			}
			{
				txtCedula = new JTextField();
				txtCedula.setBounds(69, 11, 234, 30);
				panel.add(txtCedula);
				txtCedula.setColumns(10);
			}
			
			JButton btnContinuar = new JButton("Continuar");
			btnContinuar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selected = Empresa.getInstance().findClienteByCedula(txtCedula.getText());
					clean();
					if (selected != null) {
						loadCuidades(selected.getPais());

						txtCodigo.setText(selected.getId());
						txtNombre.setText(selected.getNombre());
						txtApellido.setText(selected.getApellido());
						cbxPais.setSelectedItem(selected.getPais());
						cbxCuidad.setSelectedItem(selected.getCuidad());
						txtTelefono.setText(selected.getTelefono());
						
						cbxCuidad.setEnabled(false);
					} else {
						txtCodigo.setText("C-" + Cliente.codigo);
						txtNombre.setEditable(true);
						txtApellido.setEditable(true);
						cbxPais.setEnabled(true);
						cbxCuidad.setEnabled(true);
						txtTelefono.setEditable(true);
					}
				}
			});
			btnContinuar.setBounds(318, 12, 115, 29);
			panel.add(btnContinuar);
			
			JPanel panel_1 = new JPanel();
			panel_1.setLayout(null);
			panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_1.setBounds(15, 52, 450, 180);
			panel.add(panel_1);
			
			JLabel label_1 = new JLabel("Nombre:");
			label_1.setBounds(15, 62, 69, 20);
			panel_1.add(label_1);
			
			txtNombre = new JTextField();
			txtNombre.setEditable(false);
			txtNombre.setColumns(10);
			txtNombre.setBounds(81, 57, 146, 30);
			panel_1.add(txtNombre);
			
			JLabel lblCodigo = new JLabel("Codigo:");
			lblCodigo.setBounds(15, 21, 69, 20);
			panel_1.add(lblCodigo);
			
			txtCodigo = new JTextField();
			txtCodigo.setEditable(false);
			txtCodigo.setColumns(10);
			txtCodigo.setBounds(81, 16, 146, 30);
			panel_1.add(txtCodigo);
			
			JLabel lblTelefono = new JLabel("Telefono:");
			lblTelefono.setBounds(15, 144, 112, 20);
			panel_1.add(lblTelefono);
			
			txtTelefono = new JTextField();
			txtTelefono.setEditable(false);
			txtTelefono.setColumns(10);
			txtTelefono.setBounds(81, 139, 357, 30);
			panel_1.add(txtTelefono);
			
			JLabel lblCuidad = new JLabel("Cuidad:");
			lblCuidad.setBounds(237, 106, 49, 14);
			panel_1.add(lblCuidad);
			
			cbxCuidad = new JComboBox<String>();
			cbxCuidad.setEnabled(false);
			cbxCuidad.setBounds(292, 98, 146, 30);
			panel_1.add(cbxCuidad);
			cbxCuidad.setModel(new DefaultComboBoxModel(new String[] {"<Seleccione>"}));
			
			JLabel lblPais = new JLabel("Pais:");
			lblPais.setBounds(15, 103, 49, 20);
			panel_1.add(lblPais);
			
			cbxPais = new JComboBox();
			cbxPais.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cbxPais.getSelectedIndex() != -1) {
						if (selected == null) {
							cbxCuidad.setEnabled(true);
							loadCuidades(cbxPais.getSelectedItem().toString());
						}
					}
				}
			});
			cbxPais.setBounds(81, 99, 146, 30);
			panel_1.add(cbxPais);
			cbxPais.setModel(new DefaultComboBoxModel(new String[] {"<Seleccione>"}));
			cbxPais.setEnabled(false);
			loadPaises();
			
			JLabel lblApellido = new JLabel("Apellido:");
			lblApellido.setBounds(237, 62, 69, 20);
			panel_1.add(lblApellido);
			
			txtApellido = new JTextField();
			txtApellido.setEditable(false);
			txtApellido.setColumns(10);
			txtApellido.setBounds(292, 57, 146, 30);
			panel_1.add(txtApellido);
			
			JLabel lblNewLabel = new JLabel("Disponibles:");
			lblNewLabel.setBounds(15, 265, 163, 20);
			panel.add(lblNewLabel);
			
			JLabel lblNewLabel_1 = new JLabel("A comprar:");
			lblNewLabel_1.setBounds(302, 270, 163, 20);
			panel.add(lblNewLabel_1);
			
			JSeparator separator = new JSeparator();
			separator.setBounds(15, 248, 450, 2);
			panel.add(separator);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setBounds(15, 301, 163, 180);
			panel.add(scrollPane);
			
			listDisponible = new JList<String>();
			listDisponible.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listDisponible.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int index = -1;
					index = listDisponible.getSelectedIndex();
					
					if (index != -1) {
						
						btnDerecha.setEnabled(true);
						spnCantidad.setEnabled(true);
						
						String aux = listModelDisp.getElementAt(index);
						updateCantidad(aux.substring(0, aux.indexOf('|')));
						selectedProduct = aux.substring(0, aux.indexOf('|'));
					}
				}
			});
			listDisponible.setLocation(15, 0);
			listModelDisp = new DefaultListModel<String>();
			listDisponible.setModel(listModelDisp);
			scrollPane.setViewportView(listDisponible);
			
			JScrollPane scrollPane_1 = new JScrollPane();
			scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane_1.setBounds(302, 301, 163, 180);
			panel.add(scrollPane_1);
			
			listCompra = new JList<String>();
			listCompra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listCompra.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int index = -1;
					index = listCompra.getSelectedIndex();
					
					if (index != -1) {
						
						btnIzquierda.setEnabled(true);
						spnCantidad.setEnabled(true);
						
						String aux = listModelCompra.getElementAt(index);
						updateCantidad(aux.substring(0, aux.indexOf('|')));
						selectedProduct = aux.substring(0, aux.indexOf('|'));
					}
				}
			});
			listModelCompra = new DefaultListModel<String>();
			listCompra.setModel(listModelCompra);
			scrollPane_1.setViewportView(listCompra);
			
			btnDerecha = new JButton(">>");
			btnDerecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					cantidades.replace(selectedProduct, (Integer) spnCantidad.getValue());
					
					String aux = listDisponible.getSelectedValue().toString();
					listModelCompra.addElement(aux);
					listModelDisp.remove(listDisponible.getSelectedIndex());
					btnDerecha.setEnabled(false);
					spnCantidad.setEnabled(false);
					spnCantidad.setValue(0);
					btnActualizar.setEnabled(false);

					
					selectedProduct = "";
					
					updateMonto();
				}
			});
			btnDerecha.setEnabled(false);
			btnDerecha.setBounds(204, 410, 70, 30);
			panel.add(btnDerecha);
			
			btnIzquierda = new JButton("<<");
			btnIzquierda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					cantidades.replace(selectedProduct, (Integer) spnCantidad.getValue());
					
					String aux = listCompra.getSelectedValue().toString();
					listModelDisp.addElement(aux);
					listModelCompra.remove(listCompra.getSelectedIndex());
					btnIzquierda.setEnabled(false);
					spnCantidad.setEnabled(false);
					spnCantidad.setValue(0);
					btnActualizar.setEnabled(false);
					
					selectedProduct = "";
					
					updateMonto();
				}
			});
			btnIzquierda.setEnabled(false);
			btnIzquierda.setBounds(204, 451, 70, 30);
			panel.add(btnIzquierda);
			
			JLabel lblNewLabel_2 = new JLabel("Monto a pagar:");
			lblNewLabel_2.setBounds(215, 497, 115, 20);
			panel.add(lblNewLabel_2);
			
			spnMonto = new JSpinner();
			spnMonto.setFont(new Font("Tahoma", Font.BOLD, 18));
			spnMonto.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
			spnMonto.setEnabled(false);
			spnMonto.setBounds(302, 492, 163, 30);
			panel.add(spnMonto);
			
			spnCantidad = new JSpinner();
			spnCantidad.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
			spnCantidad.setEnabled(false);
			spnCantidad.setBounds(204, 301, 70, 30);
			panel.add(spnCantidad);
			
			btnActualizar = new JButton("Actualizar");
			btnActualizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (selectedProduct != "") {
						cantidades.replace(selectedProduct, (Integer) spnCantidad.getValue());
						updateMonto();
					}
				}
			});
			btnActualizar.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnActualizar.setEnabled(false);
			btnActualizar.setBounds(195, 342, 88, 30);
			panel.add(btnActualizar);
			
			JLabel lblNewLabel_1_1 = new JLabel("Cantidad:");
			lblNewLabel_1_1.setBounds(205, 268, 88, 20);
			panel.add(lblNewLabel_1_1);
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Finalizar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (listModelCompra.size() != 0) {
							if (!txtCedula.getText().equals("")) {
								if ( Empresa.getInstance().validarDatosCliente(txtCedula.getText(), txtTelefono.getText())==true) {
								
									if (selected == null) {
										selected = new Cliente(txtCodigo.getText(), txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), cbxCuidad.getSelectedItem().toString(), cbxPais.getSelectedItem().toString(), txtApellido.getText());

										if (Empresa.getInstance().insertBdCliente(selected)) {
											Empresa.getInstance().insertarCliente(selected);
										} else {
											selected = null;
											JOptionPane.showMessageDialog(null, "No se pudo insertar el cliente en la base de datos", "Registro de cliente", JOptionPane.ERROR_MESSAGE);
										}
									}
									
									if (selected != null) {
										ArrayList<Queso> compra = new ArrayList<>();
										for (int i = 0; i < listModelCompra.size(); i++) {
											String aux = listModelCompra.getElementAt(i);
											String id = aux.substring(0, aux.indexOf('|'));
											
											Queso temp = Empresa.getInstance().findQuesoById(id);
											compra.add(temp);
											
										}
										Factura f = Empresa.getInstance().crearFactura(selected, compra, cantidades);
										if (Empresa.getInstance().insertBdFactura(f)) {
											Empresa.getInstance().insertarFactura(f);
										} else {
											JOptionPane.showMessageDialog(null, "No se pudo insertar la factura en la base de datos", "Registro de cliente", JOptionPane.ERROR_MESSAGE);
										}
										
										JOptionPane.showMessageDialog(null, "Operacion Satisfactoria", "Informacion", JOptionPane.INFORMATION_MESSAGE);
										txtCedula.setText("");
										selected = null;
										clean();
										updateMonto();
									}									
								} else {
									JOptionPane.showMessageDialog(null, "Datos invalidos: Verifique la cedula y/o el telefono", "Informacion", JOptionPane.WARNING_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(null, "Debe ingresar un cliente", "Informacion", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Debe seleccionar por un queso", "Informacion", JOptionPane.WARNING_MESSAGE);
						}	
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadDisponibles();
	}

	private void updateMonto() {
		float total = 0;
		for (int i = 0; i < listModelCompra.size(); i++) {
			
			String aux = listModelCompra.getElementAt(i);
			String id = aux.substring(0, aux.indexOf('|'));
			
			Queso q = Empresa.getInstance().findQuesoById(id);
			total += q.precio()*cantidades.get(id);
		}
		spnMonto.setValue(total);
	}

	private void clean() {
		listModelCompra.removeAllElements();
		listModelDisp.removeAllElements();
		
		txtNombre.setText("");
		txtApellido.setText("");
		txtCodigo.setText("");
		txtTelefono.setText("");
		spnCantidad.setValue(0);
		spnMonto.setValue(0);
		
		spnCantidad.setEnabled(false);
		txtNombre.setEditable(false);
		txtApellido.setEditable(false);
		txtTelefono.setEditable(false);
		cbxPais.setEnabled(false);
		cbxCuidad.setEnabled(false);
		
		btnActualizar.setEnabled(false);
		
		cantidades.clear();
		loadDisponibles();
	}

	private void loadDisponibles() {
		listModelDisp.removeAllElements();
		for (Queso q : Empresa.getInstance().getQuesos()) {
			if (q != null) {
				String aux = new String(q.getId());
				cantidades.put(aux, (Integer)0);
				
				if (q instanceof Esfera) {
					aux += "|Esfera";
				}
				if (q instanceof Cilindro) {
					aux += "|Cilindro";
				}
				if (q instanceof CilindroHueco) {
					aux += " Hueco";
				}
				aux += "|" + q.getNombre();
				listModelDisp.addElement(aux);
			}
		}
			
	}
	
	private void updateCantidad(String id) {
		spnCantidad.setValue(cantidades.get(id));
		btnActualizar.setEnabled(true);
	}
	
	private void loadPaises() {
		String query = "exec sp_obtener_paises";
		try {
			Statement sql = Empresa.database.createStatement();
			ResultSet p = sql.executeQuery(query);
			
			while(p.next()) {
				cbxPais.addItem(p.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCuidades(String pais) {
		String query = "exec sp_obtener_ciudades"+"'"+pais+"'";
		try {
			Statement sql = Empresa.database.createStatement();
			ResultSet c = sql.executeQuery(query);
			cbxCuidad.removeAllItems();
			while(c.next()) {
				cbxCuidad.addItem(c.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
