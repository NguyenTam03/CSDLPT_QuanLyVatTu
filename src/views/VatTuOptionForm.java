package views;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import common.method.ISearcher;
import common.method.Searcher;
import dao.VatTuDao;
import model.VattuModel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import java.awt.SystemColor;
import java.util.List;
import java.awt.Color;

public class VatTuOptionForm extends JFrame implements ISearcher {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldSearch;
	private JPanel panel;
	private JButton btnAccept;
	private JButton btnCancel;
	private JTable table;
	private DefaultTableModel model;
	private List<VattuModel> vatTuList;
	private VatTuDao vtDao;
	private String masoddh;
	public static boolean isVisible = false;
	
	public VatTuOptionForm(String masoddh) {
		// giao diện
		super("Chọn Vật Tư");
		this.masoddh = masoddh;

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 554, 367);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		textFieldSearch = new JTextField();
		contentPane.add(textFieldSearch, BorderLayout.NORTH);
		textFieldSearch.setColumns(10);
		Searcher.focusInput(textFieldSearch);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		btnAccept = new JButton("Xác nhận");
		btnAccept.setBackground(SystemColor.textHighlight);
		// lắng nghe sự kiện btn
		btnAccept.addActionListener(l -> btnAcceptListener());

		btnCancel = new JButton("Hủy");
		btnCancel.setBackground(Color.RED);
		// lắng nghe sự kiện btn hủy
		btnCancel.addActionListener(l -> btnCancelListener());
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(87)
						.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addGap(90)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAccept, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
								.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
						.addContainerGap()));
		panel.setLayout(gl_panel);
		setLocationRelativeTo(null);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);
		model = (DefaultTableModel) table.getModel();
		vtDao = VatTuDao.getInstance();
		loadData();
		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}

		// Chi duoc select 1 hang khong dc select nhieu hang
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void loadData() {
		String sql = "SELECT MAVT, TENVT, DVT FROM Vattu WHERE MAVT NOT IN (SELECT MAVT FROM CTDDH WHERE MasoDDH = ?)";
		vatTuList = vtDao.selectByCondition(sql, masoddh);
		model.setColumnIdentifiers(vtDao.getColName().toArray());
		for (VattuModel vt : vatTuList) {
			Object[] rowData = { vt.getMavt(), vt.getTenVT(), vt.getDvt() };
			model.addRow(rowData);
		}
	}

	private void btnAcceptListener() {
		if (table.getSelectedRow() != -1) {
			DatHangForm.getTextFieldMaVT().setText(table.getValueAt(table.getSelectedRow(), 0).toString());
			DatHangForm.getTextFieldTenVT().setText(table.getValueAt(table.getSelectedRow(), 1).toString());
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Hãy chọn vật tư!", "Thông báo", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void btnCancelListener() {
		this.dispose();
	}

	@Override
	public void search() {
		String input = textFieldSearch.getText().trim().toLowerCase();
		model.setRowCount(0);

		for (VattuModel vt : vatTuList) {
			if (vt.getTenVT().toLowerCase().contains(input)) {
				Object[] rowData = { vt.getMavt(), vt.getTenVT(), vt.getDvt() };
				model.addRow(rowData);
			}
		}

		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}

	}
}
