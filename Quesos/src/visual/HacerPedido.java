package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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
	
	private Hashtable<String, Integer> cantidades;
	
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
						Integer pais = Empresa.getInstance().getPaisbyNombre(selected.getPais());
//						loadCuidades(pais);

						txtCodigo.setText(selected.getId());
						txtNombre.setText(selected.getNombre());
						txtApellido.setText(selected.getApellido());
						cbxPais.setSelectedIndex(pais);
						cbxCuidad.setSelectedItem(selected.getCuidad());
						txtTelefono.setText(selected.getTelefono());
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
			
			JLabel lblPais = new JLabel("Pais:");
			lblPais.setBounds(15, 103, 49, 20);
			panel_1.add(lblPais);
			
			cbxPais = new JComboBox();
			cbxPais.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cbxPais.getSelectedIndex() == 0) {
						cbxCuidad.setEnabled(false);
						cbxCuidad.setSelectedIndex(0);
					} else {
						cbxCuidad.setEnabled(true);
//						loadCuidades(cbxPais.getSelectedIndex());
					}
				}
			});
			cbxPais.setEnabled(false);
//			loadPaises();
			cbxPais.setBounds(81, 99, 146, 30);
			panel_1.add(cbxPais);
			
			JLabel lblCuidad = new JLabel("Cuidad:");
			lblCuidad.setBounds(237, 106, 49, 14);
			panel_1.add(lblCuidad);
			
			cbxCuidad = new JComboBox();
			cbxCuidad.setEnabled(false);
			cbxCuidad.setBounds(292, 98, 146, 30);
			panel_1.add(cbxCuidad);
			
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
						
						String aux = listModelCompra.getElementAt(index);
						updateCantidad(aux.substring(0, aux.indexOf('|')));
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
					}
				}
			});
			listModelCompra = new DefaultListModel<String>();
			listCompra.setModel(listModelCompra);
			scrollPane_1.setViewportView(listCompra);
			
			btnDerecha = new JButton(">>");
			btnDerecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String aux = listDisponible.getSelectedValue().toString();
					listModelCompra.addElement(aux);
					listModelDisp.remove(listDisponible.getSelectedIndex());
					btnDerecha.setEnabled(false);
					spnCantidad.setEnabled(false);
					spnCantidad.setValue(0);
					
					updateMonto();
				}
			});
			btnDerecha.setEnabled(false);
			btnDerecha.setBounds(204, 376, 70, 30);
			panel.add(btnDerecha);
			
			btnIzquierda = new JButton("<<");
			btnIzquierda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String aux = listCompra.getSelectedValue().toString();
					listModelDisp.addElement(aux);
					listModelCompra.remove(listCompra.getSelectedIndex());
					btnIzquierda.setEnabled(false);
					spnCantidad.setEnabled(false);
					spnCantidad.setValue(0);
					
					updateMonto();
				}
			});
			btnIzquierda.setEnabled(false);
			btnIzquierda.setBounds(204, 436, 70, 30);
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
			spnCantidad.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					//Update hashtable when value changes
				}
			});
			spnCantidad.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
			spnCantidad.setEnabled(false);
			spnCantidad.setBounds(204, 318, 70, 30);
			panel.add(spnCantidad);
			
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
								
								if (selected == null) {
									selected = new Cliente(txtCodigo.getText(), txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), cbxCuidad.getSelectedItem().toString(), cbxPais.getSelectedItem().toString(), txtApellido.getText());
									Empresa.getInstance().insertarCliente(selected);
								}
								ArrayList<Queso> compra = new ArrayList<>();
								ArrayList<Integer> cantsCompra = new ArrayList<>();
								for (int i = 0; i < listModelCompra.size(); i++) {
									String aux = listModelCompra.getElementAt(i);
									String id = aux.substring(0, aux.indexOf('|'));
									
									Queso temp = Empresa.getInstance().findQuesoById(id);
									compra.add(temp);
									cantsCompra.add(cantidades.get(id));
									
								}
								Empresa.getInstance().crearFactura(selected, compra, cantsCompra);
								
								JOptionPane.showMessageDialog(null, "Operacion Satisfactoria", "Informacion", JOptionPane.INFORMATION_MESSAGE);
								txtCedula.setText("");
								selected = null;
								clean();
								updateMonto();
								
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
		cbxPais.setSelectedIndex(0);
		cbxCuidad.setSelectedIndex(0);
		txtCodigo.setText("");
		txtTelefono.setText("");
		spnCantidad.setValue(0);
		
		spnCantidad.setEnabled(false);
		txtNombre.setEditable(false);
		txtApellido.setEditable(false);
		txtTelefono.setEditable(false);
		cbxPais.setEnabled(false);
		cbxCuidad.setEnabled(false);
		
		cantidades.clear();
		loadDisponibles();
	}

	private void loadDisponibles() {
		listModelDisp.removeAllElements();
		for (Queso q : Empresa.getInstance().getQuesos()) {
			String aux = new String(q.getId());
			cantidades.put(aux, 0);
			
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
	
	private void updateCantidad(String id) {
		spnCantidad.setValue(cantidades.get(id));
	}
}
