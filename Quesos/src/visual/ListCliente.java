package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.Cliente;
import logico.Empresa;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ListCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object rows[];
	private Cliente selected = null;
	private JButton btnEliminar;
	private JButton btnModificar;
	private JButton btnSelect;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ListCliente dialog = new ListCliente();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListCliente() {
		setTitle("Listado de Clientes");
		setBounds(100, 100, 450, 300);
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
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, BorderLayout.CENTER);
				{
					String headers[] = {"Codigo", "Cedula", "Nombre", "Telefono"};
					model = new DefaultTableModel();
					model.setColumnIdentifiers(headers);
					table = new JTable();
					table.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							int index = -1;
							index = table.getSelectedRow();
							
							if (index != -1) {
								btnEliminar.setEnabled(true);
								btnModificar.setEnabled(true);
								btnSelect.setEnabled(true);
								String cedula = (String)(model.getValueAt(index, 1));
								selected = Empresa.getInstance().findClienteByCedula(cedula);
							}
						}
					});
					table.setModel(model);
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPane.setViewportView(table);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				{
					btnModificar = new JButton("Modificar");
					btnModificar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							RegCliente aux = new RegCliente(selected);
							aux.setModal(true);
							aux.setVisible(true);
							
							btnEliminar.setEnabled(false);
							btnModificar.setEnabled(false);
							btnSelect.setEnabled(false);
						}
					});
					{
						btnSelect = new JButton("Seleccionar");
						btnSelect.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {								
								ListFactura aux = new ListFactura(selected);
								aux.setModal(true);
								aux.setVisible(true);
							}
						});
						btnSelect.setEnabled(false);
						buttonPane.add(btnSelect);
					}
					btnModificar.setEnabled(false);
					buttonPane.add(btnModificar);
				}
				{
					btnEliminar = new JButton("Eliminar");
					btnEliminar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int option = JOptionPane.showConfirmDialog(null, "Desea eliminar el cliente seleccionado: " + selected.getId() + "|" + selected.getNombre() + "?", "Eliminar cliente", JOptionPane.YES_NO_OPTION);
							if (option == JOptionPane.YES_OPTION) {
								Empresa.getInstance().eliminarCliente(selected);
								loadTable();
							}
							
							btnEliminar.setEnabled(false);
							btnModificar.setEnabled(false);
							btnSelect.setEnabled(false);
						}
					});
					btnEliminar.setEnabled(false);
					buttonPane.add(btnEliminar);
				}
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadTable();
	}

	public static void loadTable() {
		model.setRowCount(0);
		rows = new Object[model.getColumnCount()];
		for (Cliente c : Empresa.getInstance().getClientes()) {
			
			rows[0] = c.getId();
			rows[1] = c.getCedula();
			rows[2] = c.getNombre();
			rows[3] = c.getTelefono();
					
			model.addRow(rows);
		}
	}

}
