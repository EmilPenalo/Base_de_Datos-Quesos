package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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

public class RegCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JTextField txtDireccion;
	private Cliente selected;
	private JTextField txtCedula;
	private JTextField txtTelefono;

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
		
		setBounds(100, 100, 450, 317);
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
				txtNombre.setBounds(81, 97, 330, 30);
				panel.add(txtNombre);
			}
			{
				JLabel lblDireccion = new JLabel("Direccion:");
				lblDireccion.setBounds(15, 142, 112, 20);
				panel.add(lblDireccion);
			}
			{
				txtDireccion = new JTextField();
				txtDireccion.setColumns(10);
				txtDireccion.setBounds(81, 137, 330, 30);
				panel.add(txtDireccion);
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
			txtTelefono.setBounds(81, 178, 330, 30);
			panel.add(txtTelefono);
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
							Cliente c = new Cliente(txtCodigo.getText(), txtCedula.getText(), txtNombre.getText(), txtDireccion.getText(), txtTelefono.getText());
							Empresa.getInstance().insertarCliente(c);
							JOptionPane.showMessageDialog(null, "Registrado satisfactoriamente", "Registro de cliente", JOptionPane.INFORMATION_MESSAGE);
							clean();
						} else {
							selected.setCedula(txtCedula.getText());
							selected.setNombre(txtNombre.getText());
							selected.setDireccion(txtDireccion.getText());
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

	private void loadCliente() {
		if (selected != null) {
			txtCedula.setText(selected.getCedula());
			txtNombre.setText(selected.getNombre());
			txtDireccion.setText(selected.getDireccion());
			txtTelefono.setText(selected.getTelefono());
		}
	}

	protected void clean() {
		txtCodigo.setText("C-" + Cliente.codigo);
		txtCedula.setText("");
		txtNombre.setText("");
		txtDireccion.setText("");
		txtTelefono.setText("");
	}
}
