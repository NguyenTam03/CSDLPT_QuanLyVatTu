package views;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import common.method.ISearcher;
import common.method.Searcher;
import dao.KhoDao;
import model.KhoModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.awt.Color;
import javax.swing.JTextField;

public class KhoOptionForm extends JFrame implements ISearcher {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableKho;
	private DefaultTableModel model;
	private JPanel panel;
	private JButton btnAccept;
	private JButton btnCancel;
	private KhoDao khoDao;
	private List<KhoModel> khoList;
	private JTextField textFieldSearch;
	public static boolean isVisible = false;
	
	/**
	 * Create the frame.
	 */
	public KhoOptionForm() {
		super("Chọn Kho");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 378);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tableKho = new JTable();
//      Chi duoc select 1 hang khong dc select nhieu hang
		tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(tableKho);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		btnAccept = new JButton("Xác nhận");
		btnAccept.setBackground(SystemColor.textHighlight);
		btnAccept.addActionListener(l -> btnAcceptListener());

		btnCancel = new JButton("Hủy");
		btnCancel.setBackground(Color.RED);
		btnCancel.addActionListener(l -> btnCancelListener());

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel.createSequentialGroup().addGap(93)
						.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
						.addGap(84)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(3)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
								.addComponent(btnAccept, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
						.addContainerGap()));
		panel.setLayout(gl_panel);

		textFieldSearch = new JTextField();
		contentPane.add(textFieldSearch, BorderLayout.NORTH);
		textFieldSearch.setColumns(10);
		Searcher.focusInput(textFieldSearch);

		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				search();
			}
		});

		setLocationRelativeTo(null);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableKho.setModel(model);
		model = (DefaultTableModel) tableKho.getModel();
		khoDao = KhoDao.getInstance();
		loadData();

		if (tableKho.getRowCount() > 0) {
			tableKho.getSelectionModel().setSelectionInterval(0, 0);
		}
	}
	
	private void loadData() {
		khoList = khoDao.selectAll();
		model.setColumnIdentifiers(khoDao.getColName().toArray());
		
		for (KhoModel kho : khoList) {
			Object[] rowData = { kho.getMaKho(), kho.getTenKho(), kho.getDiaChi() };
			model.addRow(rowData);
		}
	}

	private void btnAcceptListener() {
		if (tableKho.getSelectedRow() != -1) {
			String makho = tableKho.getValueAt(tableKho.getSelectedRow(), 0).toString();
			String tenKho = tableKho.getValueAt(tableKho.getSelectedRow(), 1).toString();
			DatHangForm.getTextFieldMaKho().setText(makho);
			DatHangForm.getTextFieldTenKho().setText(tenKho);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Hãy chọn kho!", "Thông bán", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void btnCancelListener() {
		this.dispose();
	}

	@Override
	public void search() {
		String input = textFieldSearch.getText().trim().toLowerCase();
		model.setRowCount(0);

		for (KhoModel kho : khoList) {
			if (kho.getTenKho().toLowerCase().contains(input)) {
				Object[] rowData = { kho.getMaKho(), kho.getTenKho(), kho.getDiaChi() };
				model.addRow(rowData);
			}
		}

		if (tableKho.getRowCount() > 0) {
			tableKho.getSelectionModel().setSelectionInterval(0, 0);
		}
	}
}
