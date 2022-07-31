package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
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
				txtNombre.setBounds(81, 97, 357, 30);
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
					if (cbxPais.getSelectedIndex() == 0) {
						cbxCuidad.setEnabled(false);
						cbxCuidad.setSelectedIndex(0);
					} else {
						cbxCuidad.setEnabled(true);
						loadCuidades(cbxPais.getSelectedIndex());
					}
				}
			});
			loadPaises();
			cbxPais.setBounds(81, 138, 146, 30);
			panel.add(cbxPais);
			
			cbxCuidad = new JComboBox();
			cbxCuidad.setEnabled(false);
			cbxCuidad.setBounds(292, 137, 146, 30);
			panel.add(cbxCuidad);
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
							Cliente c = new Cliente(txtCodigo.getText(), txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(), cbxCuidad.getSelectedItem().toString(), cbxPais.getSelectedItem().toString());
							Empresa.getInstance().insertarCliente(c);
							JOptionPane.showMessageDialog(null, "Registrado satisfactoriamente", "Registro de cliente", JOptionPane.INFORMATION_MESSAGE);
							clean();
						} else {
							selected.setCedula(txtCedula.getText());
							selected.setNombre(txtNombre.getText());
							selected.setCuidad(cbxCuidad.getSelectedItem().toString());
							selected.setPais(cbxPais.getSelectedItem().toString());
							selected.setTelefono(txtTelefono.getText());
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
			loadCliente();
		}
	}
	
// General call should be moved to Empresa for it to be used in HacerPedido as well
	private void loadPaises() {
//		Call DataBase and set Model to the available countries
//		Do we need to be able to add more countries?
		cbxPais.setModel(new DefaultComboBoxModel(new String[] {"<Seleccione>", "Esfera", "Cilindro", "Cilindro Hueco"}));
	}
	
	private void loadCuidades(Integer pais) {
//		Same as loadPaises()
		cbxCuidad.setModel(new DefaultComboBoxModel(new String[] {"<Seleccione>", "Esfera" + pais.toString(), "Cilindro", "Cilindro Hueco"}));
	}

	private void loadCliente() {
		if (selected != null) {
			Integer pais = Empresa.getInstance().getPaisbyNombre(selected.getPais());
			loadCuidades(pais);

			txtCedula.setText(selected.getCedula());
			txtNombre.setText(selected.getNombre());
			cbxPais.setSelectedIndex(pais);
			cbxCuidad.setSelectedItem(selected.getCuidad());
			txtTelefono.setText(selected.getTelefono());
		}
	}

	protected void clean() {
		txtCodigo.setText("C-" + Cliente.codigo);
		txtCedula.setText("");
		txtNombre.setText("");
		cbxCuidad.setSelectedIndex(0);
		cbxPais.setSelectedIndex(0);
		txtTelefono.setText("");
	}
}
