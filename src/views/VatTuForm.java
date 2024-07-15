package views;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import dao.VatTuDao;
import main.Program;
import model.VattuModel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import controller.VatTuController;
import javax.swing.JSpinner;


public class VatTuForm extends CommonView<VattuModel, VatTuDao> {
	private static final long serialVersionUID = 1L;
	private JTextField textFieldMaVT;
	private JTextField textFieldTenVT;
	private JTextField textFieldDonVi;
	private JSpinner spinner;

	public VatTuForm() {
		super();
		dao = VatTuDao.getInstance();
		JPanel panel_2 = new JPanel();
		panel_2.setForeground(new Color(207, 207, 207));
		add(panel_2, BorderLayout.SOUTH);

		JLabel lblMVtT = new JLabel("Mã Vật Tư");
		lblMVtT.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldMaVT = new JTextField();
		textFieldMaVT.setEditable(false);
		textFieldMaVT.setColumns(10);

		JLabel lblTenVT = new JLabel("Tên Vật Tư");
		lblTenVT.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldTenVT = new JTextField();
		textFieldTenVT.setColumns(10);

		JLabel lblDonVi = new JLabel("Đơn Vị Tính");
		lblDonVi.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldDonVi = new JTextField();
		textFieldDonVi.setColumns(10);

		JLabel lblSoLuong = new JLabel("Số Lượng Tồn");
		lblSoLuong.setFont(new Font("Tahoma", Font.PLAIN, 14));

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMVtT, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldMaVT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(37)
					.addComponent(lblTenVT, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldTenVT, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(lblDonVi, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldDonVi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addComponent(lblSoLuong, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMVtT, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldMaVT, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTenVT, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldTenVT, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDonVi, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldDonVi, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSoLuong, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(120, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
//		table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, },
//				new String[] { "M\u00E3 V\u1EADt T\u01B0", "T\u00EAn V\u1EADt T\u01B0",
//						"\u0110\u01A1n V\u1ECB T\u00EDnh", "S\u1ED1 L\u01B0\u1EE3ng T\u1ED3n" }));
		panel_4.remove(getBtnChuyenChiNhanh());
//		load chi nhánh lên combobox
		loadChiNhanh();
//		load data lên table
		dao = VatTuDao.getInstance();
		loadDataIntoTable();
		
		if (table.getRowCount() == 0) {
			getBtnXoa().setEnabled(false);
		}
		comboBox.addItemListener(l -> loadDataOtherServer(l));
//		lắng nghe sự kiện chọn row đồng thời in dữ liệu ra textfield
		selectionListener = e -> {
			textFieldMaVT.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
			textFieldTenVT.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
			textFieldDonVi.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
			spinner.setValue(table.getValueAt(table.getSelectedRow(), 3));
		};
		
//		thêm sự kiện chọn
		table.getSelectionModel().addListSelectionListener(selectionListener);

		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}
//		Listener event
		VatTuController ac = new VatTuController(this);
		ac.initController();
	}

	public JTextField getTextFieldMaVT() {
		return textFieldMaVT;
	}


	public JTextField getTextFieldTenVT() {
		return textFieldTenVT;
	}


	public JTextField getTextFieldDonVi() {
		return textFieldDonVi;
	}

	public JSpinner getSpinner() {
		return spinner;
	}

	public void loadDataIntoTable() {
		loadData();
		for (VattuModel vattuModel : list) {
			Object[] rowData = {vattuModel.getMavt(), vattuModel.getTenVT(), vattuModel.getDvt(), vattuModel.getSoLuongTon()};
			model.addRow(rowData);
		}
	}
	
	private void loadDataOtherServer(ItemEvent l) {
		if (l.getStateChange() == ItemEvent.SELECTED) {
			if (Program.conn != null) {
				try {
					Program.conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Program.servername = Program.servers.get(comboBox.getSelectedItem());
			if (!Program.mlogin.equals(Program.mloginDN)) {
				Program.mlogin = Program.mloginDN;
				Program.password = Program.passwordDN;
			} else {
				Program.mlogin = Program.remotelogin;
				Program.password = Program.remotepassword;
			}
			
			if (Program.Connect() == 0) {
				JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi kết nối với chi nhánh hiện tại", "Thông báo", JOptionPane.OK_OPTION);
				return;
			}
			Program.mChinhanh = comboBox.getSelectedIndex();
			table.getSelectionModel().removeListSelectionListener(selectionListener);
			model.setRowCount(0);
			loadDataIntoTable();
			table.getSelectionModel().addListSelectionListener(selectionListener);
		}
	}
	

}

//setLayout(new BorderLayout(0, 0));
//
//JPanel panel = new JPanel();
//add(panel, BorderLayout.NORTH);
//
//JLabel lblChiNhanh = new JLabel("Chi Nhánh");
//lblChiNhanh.setFont(new Font("Tahoma", Font.BOLD, 16));
//
//JComboBox comboBox = new JComboBox();
//
//JPanel panel_4 = new JPanel();
////lblSearch.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/search.png")));
//GroupLayout gl_panel = new GroupLayout(panel);
//gl_panel.setHorizontalGroup(
//	gl_panel.createParallelGroup(Alignment.LEADING)
//		.addGroup(gl_panel.createSequentialGroup()
//			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_panel.createSequentialGroup()
//					.addGap(201)
//					.addComponent(lblChiNhanh, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
//					.addPreferredGap(ComponentPlacement.UNRELATED)
//					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE))
//				.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 877, GroupLayout.PREFERRED_SIZE))
//			.addContainerGap(45, Short.MAX_VALUE))
//);
//gl_panel.setVerticalGroup(
//	gl_panel.createParallelGroup(Alignment.TRAILING)
//		.addGroup(gl_panel.createSequentialGroup()
//			.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
//			.addPreferredGap(ComponentPlacement.RELATED)
//			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
//				.addComponent(lblChiNhanh)
//				.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//			.addContainerGap())
//);
//panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
//
//JButton btnThem = new JButton("Thêm");
//btnThem.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/add.png")));
//btnThem.addActionListener(new ActionListener() {
//	public void actionPerformed(ActionEvent e) {
//	}
//});
//panel_4.add(btnThem);
//
//JButton btnXoa = new JButton("Xóa");
//btnXoa.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/delete.png")));
//panel_4.add(btnXoa);
//
//JButton btnGhi = new JButton("Ghi");
//btnGhi.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/write.png")));
//btnGhi.addActionListener(new ActionListener() {
//	public void actionPerformed(ActionEvent e) {
//	}
//});
//panel_4.add(btnGhi);
//
//JButton btnHoanTac = new JButton("Hoàn Tác");
//btnHoanTac.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/undo.png")));
//panel_4.add(btnHoanTac);
//
//JButton btnLamMoi = new JButton("Làm Mới");
//btnLamMoi.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/refresh.png")));
//panel_4.add(btnLamMoi);
//
//JButton btnChuyenChiNhanh = new JButton("Chuyển Chi Nhánh");
//btnChuyenChiNhanh.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/building.png")));
//panel_4.add(btnChuyenChiNhanh);
//
//JButton btnThoat = new JButton("Thoát");
//btnThoat.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/log-out_12572185.png")));
//panel_4.add(btnThoat);
//panel.setLayout(gl_panel);
//
//JPanel panel_1 = new JPanel();
//panel_1.setBackground(new Color(148, 148, 148));
//add(panel_1, BorderLayout.CENTER);
//panel_1.setLayout(new BorderLayout(0, 0));
//
//JScrollPane scrollPane = new JScrollPane();
//panel_1.add(scrollPane, BorderLayout.CENTER);
//
//table = new JTable();
//table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, },
//		new String[] { "M\u00E3 V\u1EADt T\u01B0", "T\u00EAn V\u1EADt T\u01B0",
//				"\u0110\u01A1n V\u1ECB T\u00EDnh", "S\u1ED1 L\u01B0\u1EE3ng T\u1ED3n" }));
//scrollPane.setViewportView(table);
//
//JPanel panel_3 = new JPanel();
//panel_1.add(panel_3, BorderLayout.NORTH);
//
//textFieldTim = new JTextField();
//
//textFieldTim.setColumns(15);
//
//JButton btnTim = new JButton("Tìm");
//btnTim.setIcon(new ImageIcon(VatTuForm.class.getResource("/imgs/find.png")));
//
//JSeparator separator = new JSeparator();
//separator.setForeground(new Color(240, 240, 240));
//panel_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
//panel_3.add(textFieldTim);
//panel_3.add(btnTim);
//panel_3.add(separator);
//
//JPanel panel_2 = new JPanel();
//panel_2.setForeground(new Color(207, 207, 207));
//add(panel_2, BorderLayout.SOUTH);
//
//JLabel lblMVtT = new JLabel("Mã Vật Tư");
//lblMVtT.setFont(new Font("Tahoma", Font.PLAIN, 14));
//
//textFieldMaVT = new JTextField();
//textFieldMaVT.setColumns(10);
//
//JLabel lblTenVT = new JLabel("Tên Vật Tư");
//lblTenVT.setFont(new Font("Tahoma", Font.PLAIN, 14));
//
//textFieldTenVT = new JTextField();
//textFieldTenVT.setColumns(10);
//
//JLabel lblDonVi = new JLabel("Đơn Vị Tính");
//lblDonVi.setFont(new Font("Tahoma", Font.PLAIN, 14));
//
//textFieldDonVi = new JTextField();
//textFieldDonVi.setColumns(10);
//
//JLabel lblSoLuong = new JLabel("Số Lượng Tồn");
//lblSoLuong.setFont(new Font("Tahoma", Font.PLAIN, 14));
//
//JSpinner spinner = new JSpinner();
//
//GroupLayout gl_panel_2 = new GroupLayout(panel_2);
//gl_panel_2.setHorizontalGroup(
//	gl_panel_2.createParallelGroup(Alignment.LEADING)
//		.addGroup(gl_panel_2.createSequentialGroup()
//			.addContainerGap()
//			.addComponent(lblMVtT, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
//			.addPreferredGap(ComponentPlacement.RELATED)
//			.addComponent(textFieldMaVT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//			.addGap(37)
//			.addComponent(lblTenVT, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
//			.addPreferredGap(ComponentPlacement.RELATED)
//			.addComponent(textFieldTenVT, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
//			.addGap(43)
//			.addComponent(lblDonVi, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
//			.addPreferredGap(ComponentPlacement.RELATED)
//			.addComponent(textFieldDonVi, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//			.addGap(31)
//			.addComponent(lblSoLuong, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
//			.addPreferredGap(ComponentPlacement.RELATED)
//			.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
//			.addGap(19))
//);
//gl_panel_2.setVerticalGroup(
//	gl_panel_2.createParallelGroup(Alignment.LEADING)
//		.addGroup(gl_panel_2.createSequentialGroup()
//			.addGap(26)
//			.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
//				.addComponent(lblMVtT, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//				.addComponent(textFieldMaVT, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//				.addComponent(lblTenVT, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//				.addComponent(textFieldTenVT, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//				.addComponent(lblDonVi, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//				.addComponent(textFieldDonVi, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
//				.addComponent(lblSoLuong, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
//				.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
//			.addContainerGap(120, Short.MAX_VALUE))
//);
//panel_2.setLayout(gl_panel_2);