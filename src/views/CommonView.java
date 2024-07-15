package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import common.method.Searcher;
import dao.IAbstractDao;
import main.Program;

// Generic E: Model, T: DAO

public class CommonView<E, T> extends JPanel {

	private static final long serialVersionUID = 1L;
	protected JTable table;
	protected JPanel panel_4;
	private JTextField textFieldTim;
	protected JComboBox<String> comboBox;
	private JButton btnThem, btnXoa, btnGhi, btnLamMoi, btnHoanTac, btnChuyenChiNhanh, btnThoat;
	protected JScrollPane scrollPane;
	protected ArrayList<E> list;
	protected DefaultTableModel model;
	protected ListSelectionListener selectionListener;
	protected T dao;

	public CommonView() {
		setLayout(new BorderLayout(0, 0));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		JLabel lblChiNhanh = new JLabel("Chi Nhánh");
		lblChiNhanh.setFont(new Font("Tahoma", Font.BOLD, 16));

		comboBox = new JComboBox<>();
		comboBox.setEditable(false);
		comboBox.setEnabled(false);

		panel_4 = new JPanel();

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(201)
								.addComponent(lblChiNhanh, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 877, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(45, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addComponent(lblChiNhanh).addComponent(
						comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		btnThem = new JButton("Thêm");
		btnThem.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/add.png")));
		panel_4.add(btnThem);

		btnXoa = new JButton("Xóa");
		btnXoa.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/delete.png")));
		panel_4.add(btnXoa);

		btnGhi = new JButton("Ghi");
		btnGhi.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/write.png")));
		panel_4.add(btnGhi);

		btnHoanTac = new JButton("Hoàn Tác");
		btnHoanTac.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/undo.png")));
		btnHoanTac.setEnabled(false);
		panel_4.add(btnHoanTac);

		btnLamMoi = new JButton("Làm Mới");
		btnLamMoi.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/refresh.png")));
		panel_4.add(btnLamMoi);

		btnChuyenChiNhanh = new JButton("Chuyển Chi Nhánh");
		btnChuyenChiNhanh.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/building.png")));
		panel_4.add(btnChuyenChiNhanh);

		btnThoat = new JButton("Thoát");
		btnThoat.setIcon(new ImageIcon(CommonView.class.getResource("/imgs/log-out_12572185.png")));
		panel_4.add(btnThoat);
		panel.setLayout(gl_panel);

//		Nếu user là CONGTY thì không được thêm xóa sửa ghi chỉ được xem dữ liệu
		if (Program.mGroup.equals("CONGTY")) {
			btnThem.setEnabled(false);
			btnXoa.setEnabled(false);
			btnGhi.setEnabled(false);
		}

		JPanel panelLookforTable = new JPanel();
		panelLookforTable.setBackground(new Color(148, 148, 148));
		add(panelLookforTable, BorderLayout.CENTER);
		panelLookforTable.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panelLookforTable.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();

		scrollPane.setViewportView(table);

		JPanel panel_3 = new JPanel();
		panelLookforTable.add(panel_3, BorderLayout.NORTH);

		textFieldTim = new JTextField();
		textFieldTim.setColumns(15);
		textFieldTim.setText("Search");
		textFieldTim.setForeground(Color.LIGHT_GRAY);
		Searcher.focusInput(textFieldTim);
		
		panel_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		panel_3.add(textFieldTim);
		// Không cho phép chỉnh sửa dữ liệu trực tiếp ở bảng
		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);
		model = (DefaultTableModel) table.getModel();
		formLoad();

       //Chi duoc select 1 hang khong dc select nhieu hang
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@SuppressWarnings({ "unchecked" })
	protected void loadData() {
		list = ((IAbstractDao<E>) dao).selectAll();
		model.setColumnIdentifiers(((IAbstractDao<E>) dao).getColName().toArray());
	}

	protected void loadChiNhanh() {
		for (String key : Program.servers.keySet()) {
			comboBox.addItem(key);
		}
		comboBox.setSelectedIndex(Program.mChinhanh);
	}

	private void formLoad() {
//		CONGTY chi xem du lieu
		if (Program.mGroup.equals("CONGTY")) {
			comboBox.setEnabled(true);
			btnThem.setEnabled(false);
			btnXoa.setEnabled(false);
			btnGhi.setEnabled(false);
			btnHoanTac.setEnabled(false);
			btnLamMoi.setEnabled(false);
			btnChuyenChiNhanh.setEnabled(false);
			btnThoat.setEnabled(true);
		}

		if (Program.mGroup.equals("CHINHANH") || Program.mGroup.equals("USER")) {
			comboBox.setEnabled(false);
			btnThem.setEnabled(true);
			btnXoa.setEnabled(true);
			btnGhi.setEnabled(true);
			btnHoanTac.setEnabled(false);
			btnLamMoi.setEnabled(true);
			btnChuyenChiNhanh.setEnabled(true);
			btnThoat.setEnabled(true);
		}
	}

	public JTextField getTextFieldTim() {
		return textFieldTim;
	}

	public JButton getBtnThem() {
		return btnThem;
	}

	public JButton getBtnXoa() {
		return btnXoa;
	}

	public JButton getBtnGhi() {
		return btnGhi;
	}

	public JButton getBtnLamMoi() {
		return btnLamMoi;
	}

	public JButton getBtnHoanTac() {
		return btnHoanTac;
	}

	public JButton getBtnChuyenChiNhanh() {
		return btnChuyenChiNhanh;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public JTable getTable() {
		return table;
	}

	public ArrayList<E> getList() {
		return list;
	}

	public ListSelectionListener getSelectionListener() {
		return selectionListener;
	}

	public T getDao() {
		return dao;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

}
