package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import logico.Cliente;
import logico.Empresa;
import javax.swing.JComboBox;

public class RegCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private Cliente selected;
	private JTextField txtCedula;
	private JTextField txtTelefono;
	private JComboBox cbxPais;
	private JComboBox cbxCuidad;
	private JTextField txtApellido;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegCliente dialog = new RegCliente(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegCliente(Cliente cliente) {
		selected = cliente;
		
		if (selected == null) {
			setTitle("Registrar Cliente");

		} else {
			setTitle("Modificar Cliente");
		}
		
		setBounds(100, 100, 471, 317);
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
			
			JLabel lblCodigo = new JLabel("Codigo:");
			lblCodigo.setBounds(15, 20, 69, 20);
			panel.add(lblCodigo);
			
			txtCodigo = new JTextField();
			txtCodigo.setEditable(false);
			txtCodigo.setBounds(81, 15, 146, 30);
			if (selected == null) {
				txtCodigo.setText("C-" + Cliente.codigo);
			} else {
				txtCodigo.setText(selected.getId());
			}
			panel.add(txtCodigo);
			txtCodigo.setColumns(10);
			{
				JLabel lblNombre = new JLabel("Nombre:");
				lblNombre.setBounds(15, 102, 69, 20);
				panel.add(lblNombre);
			}
			{
				txtNombre = new JTextField();
				txtNombre.setColumns(10);
				txtNombre.setBounds(81, 97, 146, 30);
				panel.add(txtNombre);
			}
			{
				JLabel lblPais = new JLabel("Pais:");
				lblPais.setBounds(15, 142, 49, 20);
				panel.add(lblPais);
			}
			
			JLabel lblCedula = new JLabel("Cedula:");
			lblCedula.setBounds(15, 61, 69, 20);
			panel.add(lblCedula);
			
			txtCedula = new JTextField();
			txtCedula.setColumns(10);
			txtCedula.setBounds(81, 56, 146, 30);
			panel.add(txtCedula);
			
			JLabel lblTelefono = new JLabel("Telefono:");
			lblTelefono.setBounds(15, 183, 112, 20);
			panel.add(lblTelefono);
			
			txtTelefono = new JTextField();
			txtTelefono.setColumns(10);
			txtTelefono.setBounds(81, 178, 357, 30);
			panel.add(txtTelefono);
			
			JLabel lblCuidad = new JLabel("Cuidad:");
			lblCuidad.setBounds(237, 145, 49, 14);
			panel.add(lblCuidad);
			
			cbxPais = new JComboBox();
			cbxPais.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						cbxCuidad.setEnabled(true);
						loadCuidades(cbxPais.getSelectedItem().toString());
					}
				}
			);
			cbxPais.setBounds(81, 138, 146, 30);
			panel.add(cbxPais);
			
			cbxCuidad = new JComboBox();
			cbxCuidad.setEnabled(false);
			cbxCuidad.setBounds(292, 137, 146, 30);
			panel.add(cbxCuidad);
			
			JLabel lblApellido = new JLabel("Apellido:");
			lblApellido.setBounds(237, 102, 69, 20);
			panel.add(lblApellido);
			
			txtApellido = new JTextField();
			txtApellido.setColumns(10);
			txtApellido.setBounds(292, 97, 146, 30);
			panel.add(txtApellido);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("");
				if (selected == null) {
					okButton.setText("Registrar");
				} else {
					okButton.setText("Modificar");
				}
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (selected == null) {
							if(Empresa.getInstance().validarDatosCliente(txtCedula.getText(), txtTelefono.getText())==true) {
							Cliente c = new Cliente(txtCodigo.getText(), txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), cbxCuidad.getSelectedItem().toString(), cbxPais.getSelectedItem().toString(), txtApellido.getText());
							
							if (Empresa.getInstance().insertBdCliente(c)) {
								Empresa.getInstance().insertarCliente(c);
							} else {
								JOptionPane.showMessageDialog(null, "No se pudo insertar el cliente en la base de datos", "Registro de cliente", JOptionPane.ERROR_MESSAGE);
							}
							
							JOptionPane.showMessageDialog(null, "Registrado satisfactoriamente", "Registro de cliente", JOptionPane.INFORMATION_MESSAGE);
							clean();
							}else {
								JOptionPane.showMessageDialog(null, "Cedula o numero de telefono invalidos", "Error", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							selected.setCedula(txtCedula.getText());
							selected.setNombre(txtNombre.getText());
							selected.setCuidad(cbxCuidad.getSelectedItem().toString());
							selected.setPais(cbxPais.getSelectedItem().toString());
							selected.setTelefono(txtTelefono.getText());
							selected.setApellido(txtApellido.getText());
							ListCliente.loadTable();
							dispose();
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
			loadPaises();
			loadCliente();
		}
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

	private void loadCliente() {
		if (selected != null) {
			Integer pais = Empresa.getInstance().getPaisbyNombre(selected.getPais());
			loadCuidades(selected.getPais());

			txtCedula.setText(selected.getCedula());
			txtNombre.setText(selected.getNombre());
			cbxPais.setSelectedIndex(pais);
			cbxCuidad.setSelectedItem(selected.getCuidad());
			txtTelefono.setText(selected.getTelefono());
			txtApellido.setText(selected.getApellido());
		}
	}

	protected void clean() {
		txtCodigo.setText("C-" + Cliente.codigo);
		txtCedula.setText("");
		txtNombre.setText("");
		cbxCuidad.setSelectedIndex(0);
		cbxPais.setSelectedIndex(0);
		txtTelefono.setText("");
		txtApellido.setText("");
		
	}
}
