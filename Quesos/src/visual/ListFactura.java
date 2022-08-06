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
import logico.Factura;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ListFactura extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object rows[];
	private String selected = null;
	private JButton btnSelect;
	private static Cliente cliente = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ListFactura dialog = new ListFactura(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListFactura(Cliente clienteDado) {
		cliente = clienteDado;
		
		setTitle("Listado de Facturas");
		setBounds(100, 100, 600, 351);
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
					String headers[] = {"Codigo", "Cliente", "Quesos", "Monto", "Fecha"};
					model = new DefaultTableModel();
					model.setColumnIdentifiers(headers);
					table = new JTable();
					table.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							int index = -1;
							index = table.getSelectedRow();
							
							if (index != -1) {
								btnSelect.setEnabled(true);
								selected = (String)(model.getValueAt(index, 0));
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
					{
						btnSelect = new JButton("Seleccionar");
						btnSelect.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Empresa.getInstance().loadQuesos(selected);
								ListQueso aux = new ListQueso(Empresa.getInstance().getQuesos());
								aux.setModal(true);
								aux.setVisible(true);
								Empresa.getInstance().clearQuesos();
							}
						});
						btnSelect.setEnabled(false);
						buttonPane.add(btnSelect);
					}
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
		
		if (cliente == null) {
			for (String[] res : Empresa.getInstance().getAllFacturas()) {
				
				rows[0] = res[0];
				rows[1] = res[1];
				rows[2] = res[2];
				rows[3] = res[3];
				rows[4] = res[4];
						
				model.addRow(rows);
			}
		} else {
			for (String[] res : Empresa.getInstance().getAllFacturas()) {
				
				rows[0] = res[0];
				rows[1] = res[1];
				rows[2] = res[2];
				rows[3] = res[3];
				rows[4] = res[4];
						
				model.addRow(rows);
			}
		}
	}

}
