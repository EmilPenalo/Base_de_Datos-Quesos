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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;

public class ListFactura extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object rows[];
	private String selected = null;
	private JButton btnSelect;
	private static Cliente cliente = null;
	private JSpinner spnFin;
	private JSpinner spnIni;
	private JButton btnContinuar;

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
		setBounds(100, 100, 600, 450);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel);
			panel.setLayout(null);
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(2, 47, 572, 319);
				panel.add(scrollPane);
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
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel_1.setBounds(2, 2, 572, 40);
				panel.add(panel_1);
				panel_1.setLayout(null);
				
				JLabel lblFechaInicial = new JLabel("Fecha inicial:");
				lblFechaInicial.setBounds(10, 13, 89, 14);
				panel_1.add(lblFechaInicial);
				
				btnContinuar = new JButton("Continuar");
				btnContinuar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
						
					    String ini = dateFormat.format(spnIni.getValue());  					
					    String fin = dateFormat.format(spnFin.getValue());
					    
						loadTable(ini, fin);
					}
				});
				btnContinuar.setBounds(456, 8, 106, 23);
				panel_1.add(btnContinuar);
				
				spnIni = new JSpinner();
				spnIni.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
				spnIni.setEditor(new JSpinner.DateEditor(spnIni, "yyyy/MM/dd"));
				spnIni.setBounds(94, 10, 129, 20);
				panel_1.add(spnIni);
				
				JLabel lblFechaFin = new JLabel("Fecha final:");
				lblFechaFin.setBounds(238, 13, 89, 14);
				panel_1.add(lblFechaFin);
				
				spnFin = new JSpinner();
				spnFin.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
				spnFin.setEditor(new JSpinner.DateEditor(spnFin, "yyyy/MM/dd"));
				spnFin.setBounds(317, 10, 129, 20);
				panel_1.add(spnFin);
				
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
	}

	public static void loadTable(String fechaini, String fechafin) {
		Empresa.getInstance().clearFacturas();
		
		model.setRowCount(0);
		rows = new Object[model.getColumnCount()];
		
		if (cliente == null) {
			for (String[] res : Empresa.getInstance().getAllFacturas(fechaini, fechafin)) {
				
				rows[0] = res[0];
				rows[1] = res[1];
				rows[2] = res[2];
				rows[3] = res[3];
				rows[4] = res[4];
						
				model.addRow(rows);
			}
		} else {
			for (String[] res : Empresa.getInstance().getAllFacturas(fechaini, fechafin)) {
				
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
