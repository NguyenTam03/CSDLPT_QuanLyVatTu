package views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;

import dao.KhoDao;
import dao.PhieuLapDao;
import main.Program;

import model.PhieuLapModel;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;

import javax.swing.table.DefaultTableModel;
import controller.PhieuLapController;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.SwingConstants;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import common.method.Formatter;

import javax.swing.SpinnerNumberModel;

public class PhieuLapForm extends CommonView<PhieuLapModel, PhieuLapDao> {
	private static final long serialVersionUID = 1L;
	private JButton btnCTDHOption, btnDHOption;
	private JTextField TFMaPN;
	private JTextField TFMaNV;
	private JTextField TFMaKho;
	private JTextField TFMaVT;
	private JTable tableCTPN;
	private JSpinner SoLuong, DonGia;
	private DefaultTableModel ctplModel;
	private JMenuItem MenuItemPN, MenuItemCTPN;
	private JTextField TFMaDDH;
	private JMenu mnOption;
	private JDateChooser Ngay;
	private JLabel lbTenVatTu, lbTenKho,lbHoTenNV ;
	private JButton btnKho;
	// String MaPN: MaNV | TenKho
	public static HashMap<String, Integer> maPN_NV;
	public static HashMap<Integer, String> maNV_TenNV;
	public static HashMap<String, String> maPN_Kho;
	public static HashMap<String, String> maKho_TenKho;

	public PhieuLapForm() {
		super();
		comboBox.setEnabled(false);
		scrollPane.setEnabled(false);
		getBtnThem().setEnabled(false);
		getBtnXoa().setEnabled(false);
		getBtnGhi().setEnabled(false);
		getBtnLamMoi().setEnabled(false);
		this.setEnabled(false);

		JPanel panel_2 = new JPanel();
		panel_2.setForeground(new Color(207, 207, 207));
		add(panel_2, BorderLayout.SOUTH);

		JPanel panelInfo = new JPanel();
		panelInfo.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);

		JLabel lblNewLabel = new JLabel("Thông tin");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setForeground(Color.DARK_GRAY);

		JLabel lblNewLabel_1 = new JLabel("Chi Tiết Phiếu Nhập");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(panelInfo, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)))
				.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, 0, 0, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(panelInfo, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
							.addGap(10))))
		);

		tableCTPN = new JTable();
		tableCTPN.setEnabled(false);
		scrollPane.setViewportView(tableCTPN);

		JPanel panelPhieuNhap = new JPanel();
		panelPhieuNhap.setBorder(
				new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Phi\u1EBFu Nh\u1EADp", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelPhieuNhap.setLayout(null);

		JLabel lblMaPN = new JLabel("Mã Phiếu Nhập");
		lblMaPN.setBounds(10, 15, 86, 14);
		panelPhieuNhap.add(lblMaPN);

		TFMaPN = new JTextField();
		TFMaPN.setEditable(false);
		TFMaPN.setBounds(101, 13, 114, 19);
		panelPhieuNhap.add(TFMaPN);
		TFMaPN.setColumns(10);

		JLabel lblNgay = new JLabel("Ngày");
		lblNgay.setBounds(225, 15, 49, 14);
		panelPhieuNhap.add(lblNgay);

		JLabel lblMaNV = new JLabel("Mã Nhân Viên");
		lblMaNV.setBounds(10, 39, 103, 14);
		panelPhieuNhap.add(lblMaNV);

		TFMaNV = new JTextField();
		TFMaNV.setEditable(false);
		TFMaNV.setColumns(10);
		TFMaNV.setBounds(101, 37, 114, 20);
		panelPhieuNhap.add(TFMaNV);

		JLabel lblMaKho = new JLabel("Mã Kho");
		lblMaKho.setBounds(10, 65, 46, 14);
		panelPhieuNhap.add(lblMaKho);

		TFMaKho = new JTextField();
		TFMaKho.setEditable(false);
		TFMaKho.setBounds(101, 63, 114, 20);
		panelPhieuNhap.add(TFMaKho);
		TFMaKho.setColumns(10);

		JPanel panelCTDH = new JPanel();
		panelCTDH.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Chi Ti\u1EBFt Phi\u1EBFu Nh\u1EADp", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout gl_panelInfo = new GroupLayout(panelInfo);
		gl_panelInfo.setHorizontalGroup(
			gl_panelInfo.createParallelGroup(Alignment.LEADING)
				.addComponent(panelPhieuNhap, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelCTDH, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		gl_panelInfo.setVerticalGroup(
			gl_panelInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelInfo.createSequentialGroup()
					.addComponent(panelPhieuNhap, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelCTDH, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
		);
		panelCTDH.setLayout(null);

		JLabel lblMaVT = new JLabel("Mã Vật Tư");
		lblMaVT.setBounds(10, 53, 59, 14);		
		panelCTDH.add(lblMaVT);

		TFMaVT = new JTextField();
		TFMaVT.setEditable(false);
		TFMaVT.setBounds(111, 51, 86, 20);
		panelCTDH.add(TFMaVT);
		TFMaVT.setColumns(10);

		btnCTDHOption = new JButton("Chọn Chi Tiết Đơn Hàng");
		btnCTDHOption.setEnabled(false);
		btnCTDHOption.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnCTDHOption.setBounds(223, 10, 144, 23);
		panelCTDH.add(btnCTDHOption);

		JLabel lblSoLuong = new JLabel("Số Lượng");
		lblSoLuong.setBounds(10, 83, 59, 14);
		panelCTDH.add(lblSoLuong);

		SoLuong = new JSpinner();
		SoLuong.setModel(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
		SoLuong.setEnabled(false);
		SoLuong.setBounds(111, 81, 86, 20);
		panelCTDH.add(SoLuong);

		JLabel lblDonGia = new JLabel("Đơn Giá");
		lblDonGia.setBounds(208, 83, 63, 14);
		panelCTDH.add(lblDonGia);

		DonGia = new JSpinner();
		DonGia.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(0), null, Float.valueOf(10000)));
		DonGia.setEnabled(false);
		DonGia.setBounds(281, 81, 86, 20);
		panelCTDH.add(DonGia);
		
		JLabel lbMaDDH = new JLabel("Mã Đơn Đặt Hàng");
		lbMaDDH.setBounds(10, 26, 109, 13);
		panelCTDH.add(lbMaDDH);
		
		TFMaDDH = new JTextField();
		TFMaDDH.setEditable(false);
		TFMaDDH.setBounds(111, 23, 86, 19);
		panelCTDH.add(TFMaDDH);
		TFMaDDH.setColumns(10);
		
		lbTenVatTu = new JLabel("");
		lbTenVatTu.setBounds(223, 54, 133, 13);
		panelCTDH.add(lbTenVatTu);

		btnDHOption = new JButton("Chọn Đơn Hàng");
		btnDHOption.setEnabled(false);
		btnDHOption.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnDHOption.setBounds(224, 86, 144, 23);
		panelPhieuNhap.add(btnDHOption);
		
		Ngay = new JDateChooser();
		Ngay.getCalendarButton().setEnabled(false);
		Ngay.setBounds(255, 15, 103, 20);
		panelPhieuNhap.add(Ngay);
		
		lbHoTenNV = new JLabel("");
		lbHoTenNV.setBounds(225, 39, 133, 13);
		panelPhieuNhap.add(lbHoTenNV);
		
		lbTenKho = new JLabel("");
		lbTenKho.setBounds(225, 66, 133, 13);
		panelPhieuNhap.add(lbTenKho);
		
		btnKho = new JButton("Chọn Kho");
		btnKho.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnKho.setEnabled(false);
		btnKho.setBounds(101, 86, 104, 23);
		panelPhieuNhap.add(btnKho);
		panelInfo.setLayout(gl_panelInfo);
		panel_2.setLayout(gl_panel_2);

		panel_4.remove(getBtnChuyenChiNhanh());

		JMenuBar menuBar = new JMenuBar();
		panel_4.add(menuBar);

		mnOption = new JMenu("Chọn chế độ");
		mnOption.setBackground(Color.LIGHT_GRAY);
		mnOption.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		menuBar.add(mnOption);

		MenuItemPN = new JMenuItem("Phiếu Nhập");
		mnOption.add(MenuItemPN);

		MenuItemCTPN = new JMenuItem("Chi Tiết Phiếu Nhập");
		mnOption.add(MenuItemCTPN);

//      load chi nhánh lên combobox
		loadChiNhanh();

//		Phiếu Lập table
		dao = PhieuLapDao.getInstance();
		loadDataIntoTable();

//		Chi tiết phiếu nhập table
		
		ctplModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCTPN.setModel(ctplModel);
		ctplModel = (DefaultTableModel) tableCTPN.getModel();
		

//		CONGTY có thể chọn chi nhánh để xem dữ liệu
		comboBox.addItemListener(l -> loadDataOtherServer(l));
		//	chỉ cho chọn 1 dòng
		tableCTPN.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setEnabled(false);
		PhieuLapController ac = new PhieuLapController(this);
		ac.initController();
	}



	public JButton getBtnCTDHOption() {
		return btnCTDHOption;
	}

	public JButton getBtnDHOption() {
		return btnDHOption;
	}

	public JTextField getTFMaPN() {
		return TFMaPN;
	}

	public JTextField getTFMaNV() {
		return TFMaNV;
	}

	public JDateChooser getNgay() {
		return Ngay;
	}



	public JTextField getTFMaKho() {
		return TFMaKho;
	}

	public JTextField getTFMaVT() {
		return TFMaVT;
	}

	public JSpinner getSoLuong() {
		return SoLuong;
	}

	public JSpinner getDonGia() {
		return DonGia;
	}

	public JMenuItem getMenuItemPN() {
		return MenuItemPN;
	}

	public JMenuItem getMenuItemCTPN() {
		return MenuItemCTPN;
	}

	public JTextField getTFMaDDH() {
		return TFMaDDH;
	}

	public JTable getTableCTPN() {
		return tableCTPN;
	}

	public JMenu getMnOption() {
		return mnOption;
	}
	
	public JLabel getLbTenVatTu() {
		return lbTenVatTu;
	}

	public JLabel getLbTenKho() {
		return lbTenKho;
	}

	public JLabel getLbHoTenNV() {
		return lbHoTenNV;
	}
	
	
	public DefaultTableModel getCtplModel() {
		return ctplModel;
	}

	public JButton getBtnKho() {
		return btnKho;
	}

	public void loadDataIntoTable() {
		maPN_NV = new HashMap<>();
		maPN_Kho = new HashMap<>();
		maNV_TenNV = new HashMap<>();
		maKho_TenKho = new HashMap<>();
		loadData();
		String hoTenNV = "";
		String tenKho = "";
		String sql;
		for (PhieuLapModel pl : list) {
			sql = "SELECT Ho +' '+Ten FROM NHANVIEN WHERE MANV = ?";
			Program.myReader = Program.ExecSqlDataReader(sql, pl.getManv());
			try {
				Program.myReader.next();
				hoTenNV = Program.myReader.getString(1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			maPN_NV.put(pl.getMapn(), pl.getManv());
			maNV_TenNV.put(pl.getManv(), hoTenNV);
			sql = "SELECT TENKHO FROM KHO WHERE MAKHO = ?";
			Program.myReader = Program.ExecSqlDataReader(sql,pl.getMaKho());
			try {
				Program.myReader.next();
				tenKho = Program.myReader.getString(1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			maPN_Kho.put(pl.getMapn(), pl.getMaKho());
			maKho_TenKho.put(pl.getMaKho(), tenKho);
			Object[] rowData = { pl.getMapn(),Formatter.formatterDate(pl.getNgay()) , pl.getMaSoDDH(),hoTenNV ,tenKho };
			model.addRow(rowData);
		}
	}
	
	private void loadDataOtherServer(ItemEvent l) {
		if (l.getStateChange() == ItemEvent.SELECTED) {
			if (Program.conn != null) {
				try {
					Program.conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
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
			
			if (Program.Connect() == 0)
				return;
			Program.mChinhanh = comboBox.getSelectedIndex();
			table.getSelectionModel().removeListSelectionListener(selectionListener);
			model.setRowCount(0);
			loadDataIntoTable();
			table.getSelectionModel().addListSelectionListener(selectionListener);
		}
	}
}
