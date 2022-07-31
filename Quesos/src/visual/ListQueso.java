package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.Cilindro;
import logico.CilindroHueco;
import logico.Empresa;
import logico.Esfera;
import logico.Queso;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ListQueso extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object rows[];
	private Queso selected = null;
	private static ArrayList<Queso> quesos = null;
	private JButton btnEliminar;
	private JButton btnModificar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ListQueso dialog = new ListQueso(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListQueso(ArrayList<Queso> listQuesos) {
		if (listQuesos != null) {
			quesos = listQuesos;
		} else {
			quesos = null;
		}
		setTitle("Listado de Clientes");
		setBounds(100, 100, 444, 325);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBounds(5, 52, 418, 190);
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, BorderLayout.CENTER);
				{
					String headers[] = {"Codigo", "Nombre", "Volumen", "Precio", "Figura"};
					model = new DefaultTableModel();
					model.setColumnIdentifiers(headers);
					table = new JTable();
					table.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							int index = -1;
							index = table.getSelectedRow();
							
							if (index != -1) {
								if (quesos == null) {
									btnEliminar.setEnabled(true);
									btnModificar.setEnabled(true);
									
									String id = (String)(model.getValueAt(index, 0));
									selected = Empresa.getInstance().findQuesoById(id);
								}
							}
						}
					});
					table.setModel(model);
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPane.setViewportView(table);
				}
			}
		}
		
		JPanel panelTipo = new JPanel();
		panelTipo.setBounds(5, 0, 418, 51);
		contentPanel.add(panelTipo);
		panelTipo.setLayout(null);
		{
			JLabel label = new JLabel("Tipo de Queso:");
			label.setBounds(15, 21, 116, 20);
			panelTipo.add(label);
		}
		{
			JComboBox cbxTipo = new JComboBox();
			cbxTipo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selection = cbxTipo.getSelectedIndex();
					loadTable(selection);
				}
			});
			cbxTipo.setModel(new DefaultComboBoxModel(new String[] {"<Seleccione>", "Esfera", "Cilindro", "Cilindro Hueco"}));
			cbxTipo.setBounds(122, 16, 146, 30);
			panelTipo.add(cbxTipo);
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
							RegQueso aux = new RegQueso(selected);
							aux.setModal(true);
							aux.setVisible(true);
							
							btnEliminar.setEnabled(false);
							btnModificar.setEnabled(false);
						}
					});
					btnModificar.setEnabled(false);
					buttonPane.add(btnModificar);
				}
				{
					btnEliminar = new JButton("Eliminar");
					btnEliminar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int option = JOptionPane.showConfirmDialog(null, "Desea eliminar el queso seleccionado: " + selected.getId() + "?", "Eliminar cliente", JOptionPane.YES_NO_OPTION);
							if (option == JOptionPane.YES_OPTION) {
								Empresa.getInstance().eliminarQueso(selected);
								loadTable(0);
							}
							
							btnEliminar.setEnabled(false);
							btnModificar.setEnabled(false);
						}
					});
					btnEliminar.setEnabled(false);
					buttonPane.add(btnEliminar);
				}
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadTable(0);
	}

	public static void loadTable(int sel) {
		model.setRowCount(0);
		rows = new Object[model.getColumnCount()];
		
		switch (sel) {
		case 0:
			if (quesos == null) {
				for (Queso q : Empresa.getInstance().getQuesos()) {
					
					rows[0] = q.getId();
					rows[1] = q.getNombre();
					rows[2] = q.precio();
					rows[3] = q.volumen();
					
					if (q instanceof Esfera) {
						rows[4] = "Esfera";
					}
					
					if (q instanceof Cilindro) {
						rows[4] = "Cilindro";
					}
					
					if (q instanceof CilindroHueco) {
						rows[4] = "Cilindro Hueco";
					}
							
					model.addRow(rows);
				}
			} else {
				for (Queso q : quesos) {
					
					rows[0] = q.getId();
					rows[1] = q.getNombre();
					rows[2] = q.precio();
					rows[3] = q.volumen();
					
					if (q instanceof Esfera) {
						rows[4] = "Esfera";
					}
					
					if (q instanceof Cilindro) {
						rows[4] = "Cilindro";
					}
					
					if (q instanceof CilindroHueco) {
						rows[4] = "Cilindro Hueco";
					}
							
					model.addRow(rows);
				}
			}
			
			break;
		case 1:
			if (quesos == null) {
				for (Queso q : Empresa.getInstance().getQuesos()) {
					
					if (q instanceof Esfera) {
						rows[1] = q.getNombre();
						rows[2] = q.precio();
						rows[3] = q.volumen();
						rows[4] = "Esfera";
						
						model.addRow(rows);
					}	
				}
			} else {
				for (Queso q : quesos) {
					if (q instanceof Esfera) {
						rows[1] = q.getNombre();
						rows[2] = q.precio();
						rows[3] = q.volumen();
						rows[4] = "Esfera";
						
						model.addRow(rows);
					}
				}
			}
			break;
			
		case 2:
			if (quesos == null) {
				for (Queso q : Empresa.getInstance().getQuesos()) {

					if (q instanceof Cilindro && !(q instanceof CilindroHueco)) {
						rows[1] = q.getNombre();
						rows[2] = q.precio();
						rows[3] = q.volumen();
						rows[4] = "Cilindro";
						
						model.addRow(rows);
					}
				}
			} else {
				for (Queso q : quesos) {
					if (q instanceof Cilindro && !(q instanceof CilindroHueco)) {
						rows[1] = q.getNombre();
						rows[2] = q.precio();
						rows[3] = q.volumen();
						rows[4] = "Cilindro";
						
						model.addRow(rows);
					}
				}
			}
			break;
			
		case 3:
			if (quesos == null) {
				for (Queso q : Empresa.getInstance().getQuesos()) {

					if (q instanceof CilindroHueco) {
						rows[1] = q.getNombre();
						rows[2] = q.precio();
						rows[3] = q.volumen();
						rows[4] = "Cilindro Hueco";
						
						model.addRow(rows);
					}			
				}
			} else {
				for (Queso q : quesos) {
					if (q instanceof CilindroHueco) {
						rows[1] = q.getNombre();
						rows[2] = q.precio();
						rows[3] = q.volumen();
						rows[4] = "Cilindro Hueco";
						
						model.addRow(rows);
					}	
				}
			}
			break;
		}
	}
}
