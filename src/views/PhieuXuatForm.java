package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
//import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import common.method.Formatter;
import controller.PhieuXuatController;
import dao.CTPXDao;
import dao.PhieuXuatDao;
import main.Program;
import model.CTPXModel;
import model.PhieuXuatModel;
import javax.swing.border.EtchedBorder;

public class PhieuXuatForm extends CommonView<PhieuXuatModel, PhieuXuatDao> {
	private static final long serialVersionUID = 1L;
	private JButton btnVatTuOption, btnKhoOption;
	private JTextField textFieldMaPX;
	private JTextField textFieldTenKH;
	private JTextField textFieldMaNV;
	private static JTextField textFieldMaKho;
	private static JTextField textFieldMaVT;
	private static JTextField textFieldTenKho;
	private static JTextField textFieldTenVT;
	private JTable tableCTPX;
	private JSpinner spinnerSoLuong, spinnerDonGia;
	private JDateChooser ngay;
	public DefaultTableModel ctpxModel;
	private CTPXDao ctpxDao;
	private ArrayList<CTPXModel> ctpxList;
	private JMenuItem mntmPhieuXuat, mntmCTPX;
	private JMenu mnOption;
	private ListSelectionListener selectionListenerCTPX;
	private KhoOptionFormForPX khoOptionFormPx;
	private VatTuOptionFormForPX vatTuOptionFormPx;
	private Map<String, List<Object>> maNhanVienKho;
	private Map<Integer, String> maVT;

	private JTextField textFieldTenNV;
	private PhieuXuatController ac;

	private boolean isSelectedCTPX = false;
	private boolean isSelectedPX = false;

	/*
	 * Những map này phục vụ cho chức năng search key là khóa, value là tên
	 */
	private Map<Integer, String> nhanVienInfo;
	private Map<String, String> khoInfo;
	private Map<String, String> vatTuInfo;

	public PhieuXuatForm() {
		ac = new PhieuXuatController(this);
		table.setEnabled(false);
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

		JLabel lblNewLabel_1 = new JLabel("Chi Tiết Phiếu Xuất");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
						.addComponent(panelInfo, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)))
				.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1244, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_2
				.createSequentialGroup().addGap(2)
				.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE).addGap(2)
				.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_2.createSequentialGroup()
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
								.addGap(2).addComponent(scrollPane, 0, 0, Short.MAX_VALUE))
						.addComponent(panelInfo, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE))));

		tableCTPX = new JTable();
		tableCTPX.setEnabled(false);
		scrollPane.setViewportView(tableCTPX);

		JPanel panelDatHang = new JPanel();
		panelDatHang.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Phi\u1EBFu Xu\u1EA5t", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDatHang.setLayout(null);

		JLabel lblMaPX = new JLabel("Mã Phiếu Xuất");
		lblMaPX.setBounds(20, 15, 92, 14);
		panelDatHang.add(lblMaPX);

		textFieldMaPX = new JTextField();
		textFieldMaPX.setBounds(122, 12, 71, 20);
		panelDatHang.add(textFieldMaPX);
		textFieldMaPX.setColumns(10);
		textFieldMaPX.setEditable(false);

		JLabel lblNgay = new JLabel("Ngày");
		lblNgay.setBounds(210, 15, 39, 14);
		panelDatHang.add(lblNgay);

		ngay = new JDateChooser();
		ngay.setBounds(299, 15, 116, 20);
		panelDatHang.add(ngay);
		ngay.setEnabled(false);
		ngay.setDateFormatString("dd-MM-yyyy");

		JLabel lblTenKH = new JLabel("Tên Khách Hàng");
		lblTenKH.setBounds(20, 42, 105, 14);
		panelDatHang.add(lblTenKH);

		textFieldTenKH = new JTextField();
		textFieldTenKH.setBounds(122, 39, 131, 20);
		panelDatHang.add(textFieldTenKH);
		textFieldTenKH.setColumns(10);
		textFieldTenKH.setEditable(false);

		JLabel lblMaNV = new JLabel("Mã Nhân Viên");
		lblMaNV.setBounds(20, 70, 82, 14);
		panelDatHang.add(lblMaNV);

		textFieldMaNV = new JTextField();
		textFieldMaNV.setColumns(10);
		textFieldMaNV.setBounds(122, 67, 71, 20);
		panelDatHang.add(textFieldMaNV);
		textFieldMaNV.setEditable(false);

		JLabel lblMaKho = new JLabel("Mã Kho");
		lblMaKho.setBounds(20, 100, 46, 14);
		panelDatHang.add(lblMaKho);

		textFieldMaKho = new JTextField();
		textFieldMaKho.setEditable(false);
		textFieldMaKho.setBounds(122, 97, 71, 20);
		panelDatHang.add(textFieldMaKho);
		textFieldMaKho.setColumns(10);

		JPanel panelCTPX = new JPanel();
		panelCTPX.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Chi Ti\u1EBFt Phi\u1EBFu Xu\u1EA5t", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		GroupLayout gl_panelInfo = new GroupLayout(panelInfo);
		gl_panelInfo.setHorizontalGroup(gl_panelInfo.createParallelGroup(Alignment.LEADING)
				.addComponent(panelDatHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_panelInfo.createSequentialGroup().addGap(3)
						.addComponent(panelCTPX, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(3)));
		gl_panelInfo.setVerticalGroup(gl_panelInfo.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_panelInfo.createSequentialGroup()
						.addComponent(panelDatHang, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
						.addGap(3).addComponent(panelCTPX, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(3, Short.MAX_VALUE)));
		panelCTPX.setLayout(null);

		JLabel lblMaVT = new JLabel("Mã Vật Tư");
		lblMaVT.setBounds(20, 25, 59, 14);
		panelCTPX.add(lblMaVT);

		textFieldMaVT = new JTextField();
		textFieldMaVT.setEditable(false);
		textFieldMaVT.setBounds(114, 22, 86, 20);
		panelCTPX.add(textFieldMaVT);
		textFieldMaVT.setColumns(10);

		btnVatTuOption = new JButton("Chọn Vật Tư");
		btnVatTuOption.setEnabled(false);
		btnVatTuOption.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnVatTuOption.setBounds(301, 21, 103, 23);
		panelCTPX.add(btnVatTuOption);

		JLabel lblSoLuong = new JLabel("Số Lượng");
		lblSoLuong.setBounds(20, 91, 59, 14);
		panelCTPX.add(lblSoLuong);

		spinnerSoLuong = new JSpinner();
		spinnerSoLuong
				.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spinnerSoLuong.setEnabled(false);
		spinnerSoLuong.setBounds(114, 88, 86, 20);
		panelCTPX.add(spinnerSoLuong);

		JLabel lblDonGia = new JLabel("Đơn Giá");
		lblDonGia.setBounds(239, 91, 52, 14);
		panelCTPX.add(lblDonGia);

		spinnerDonGia = new JSpinner();
		spinnerDonGia.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(0), null, Float.valueOf(100)));
		spinnerDonGia.setEnabled(false);
		spinnerDonGia.setBounds(318, 88, 86, 20);
		panelCTPX.add(spinnerDonGia);

		JLabel lblTenVT = new JLabel("Tên Vật Tư");
		lblTenVT.setBounds(20, 60, 84, 14);
		panelCTPX.add(lblTenVT);

		textFieldTenVT = new JTextField();
		textFieldTenVT.setText((String) null);
		textFieldTenVT.setEditable(false);
		textFieldTenVT.setColumns(10);
		textFieldTenVT.setBounds(114, 57, 160, 20);
		panelCTPX.add(textFieldTenVT);

		btnKhoOption = new JButton("Chọn Kho Hàng");
		btnKhoOption.setEnabled(false);
		btnKhoOption.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnKhoOption.setBounds(299, 96, 105, 23);
		panelDatHang.add(btnKhoOption);

		JLabel lblTenNV = new JLabel("Tên Nhân Viên");
		lblTenNV.setBounds(210, 70, 89, 14);
		panelDatHang.add(lblTenNV);

		textFieldTenNV = new JTextField();
		textFieldTenNV.setText((String) null);
		textFieldTenNV.setEditable(false);
		textFieldTenNV.setColumns(10);
		textFieldTenNV.setBounds(299, 67, 116, 20);
		panelDatHang.add(textFieldTenNV);

		JLabel lblTenKho = new JLabel("Tên Kho");
		lblTenKho.setBounds(20, 125, 61, 14);
		panelDatHang.add(lblTenKho);

		textFieldTenKho = new JTextField();
		textFieldTenKho.setText((String) null);
		textFieldTenKho.setEditable(false);
		textFieldTenKho.setColumns(10);
		textFieldTenKho.setBounds(122, 122, 131, 20);
		panelDatHang.add(textFieldTenKho);
		panelInfo.setLayout(gl_panelInfo);
		panel_2.setLayout(gl_panel_2);

		panel_4.remove(getBtnChuyenChiNhanh());

		JMenuBar menuBar = new JMenuBar();
		panel_4.add(menuBar);

		mnOption = new JMenu("Chọn chế độ");
		mnOption.setBackground(Color.LIGHT_GRAY);
		mnOption.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		menuBar.add(mnOption);

		mntmPhieuXuat = new JMenuItem("Phiếu xuất");
		mnOption.add(mntmPhieuXuat);

		mntmCTPX = new JMenuItem("Chi tiết phiếu xuất");
		mnOption.add(mntmCTPX);

		getBtnThoat().addActionListener(l -> exitPhieuXuat());

//      load chi nhánh lên combobox
		loadChiNhanh();

//		Phiếu xuất table
		dao = PhieuXuatDao.getInstance();
		loadDataIntoTable();

//		Chi tiết phiếu xuất table
		ctpxDao = CTPXDao.getInstace();
		ctpxModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCTPX.setModel(ctpxModel);
		ctpxModel = (DefaultTableModel) tableCTPX.getModel();
		loadDataIntoTableCTPX();

//		CONGTY có thể chọn chi nhánh để xem dữ liệu
		comboBox.addItemListener(l -> loadDataOtherServer(l));
//		lắng nghe sự kiện chọn row đồng thời in dữ liệu ra textfield cho bảng ctpx
		selectionListenerCTPX = e -> {

			textFieldMaVT.setText(maVT.get(tableCTPX.getSelectedRow()));
			textFieldTenVT.setText(tableCTPX.getValueAt(tableCTPX.getSelectedRow(), 1).toString());
			spinnerSoLuong.setValue(tableCTPX.getValueAt(tableCTPX.getSelectedRow(), 2));
			spinnerDonGia.setValue(
					Formatter.formatMoneyToFloat(tableCTPX.getValueAt(tableCTPX.getSelectedRow(), 3).toString()));

			if (isSelectedCTPX && !Program.mGroup.equals("CONGTY")) {
				if (!Program.username.equals(getTextFieldMaNV().getText())) {
					getBtnXoa().setEnabled(false);
					getBtnGhi().setEnabled(false);
//					getBtnVatTuOption().setEnabled(false);
					spinnerSoLuong.setEnabled(false);
					spinnerDonGia.setEnabled(false);
				} else {
					getBtnXoa().setEnabled(true);
					getBtnGhi().setEnabled(true);
//					getBtnVatTuOption().setEnabled(true);
					spinnerSoLuong.setEnabled(false);
					spinnerDonGia.setEnabled(false);
				}
			}

		};
//		lắng nghe sự kiện chọn row đồng thời in dữ liệu ra textfield cho bảng phiếu xuất

		selectionListener = e -> {
			textFieldMaPX.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
			try {
				ngay.setDate(new SimpleDateFormat("dd-MM-yyyy")
						.parse(table.getValueAt(table.getSelectedRow(), 1).toString()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			textFieldTenKH.setText(table.getValueAt(table.getSelectedRow(), 2).toString());

			textFieldMaNV.setText(maNhanVienKho.get(table.getValueAt(table.getSelectedRow(), 0)).get(0).toString());
			textFieldTenNV.setText(table.getValueAt(table.getSelectedRow(), 3).toString());

			textFieldMaKho.setText(maNhanVienKho.get(table.getValueAt(table.getSelectedRow(), 0)).get(1).toString());
			textFieldTenKho.setText(table.getValueAt(table.getSelectedRow(), 4).toString());

			if (isSelectedPX && !Program.mGroup.equals("CONGTY")) {
				if (!Program.username.equals(getTextFieldMaNV().getText())) {
					getBtnXoa().setEnabled(false);
					getBtnGhi().setEnabled(false);
					getBtnKhoOption().setEnabled(false);
					getTextFieldTenKH().setEditable(false);
				} else {
					getBtnXoa().setEnabled(true);
					getBtnGhi().setEnabled(true);
					getBtnKhoOption().setEnabled(true);
					getTextFieldTenKH().setEditable(true);
				}
			}

			tableCTPX.getSelectionModel().removeListSelectionListener(selectionListenerCTPX);
			ctpxModel.setRowCount(0);
			loadDataIntoTableCTPX();
		};
		table.getSelectionModel().addListSelectionListener(selectionListener);

		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}
		ac.initController();
	}

	public JButton getBtnVatTuOption() {
		return btnVatTuOption;
	}

	public JButton getBtnKhoOption() {
		return btnKhoOption;
	}

	public JTextField getTextFieldMaPX() {
		return textFieldMaPX;
	}

	public JTextField getTextFieldTenKH() {
		return textFieldTenKH;
	}

	public JTextField getTextFieldMaNV() {
		return textFieldMaNV;
	}

	public static JTextField getTextFieldMaKho() {
		return textFieldMaKho;
	}

	public static JTextField getTextFieldMaVT() {
		return textFieldMaVT;
	}

	public JTextField getTextFieldTenNV() {
		return textFieldTenNV;
	}

	public static JTextField getTextFieldTenKho() {
		return textFieldTenKho;
	}

	public static JTextField getTextFieldTenVT() {
		return textFieldTenVT;
	}

	public JDateChooser getNgay() {
		return ngay;
	}

	public JTable getTableCTPX() {
		return tableCTPX;
	}

	public JMenuItem getMntmPhieuXuat() {
		return mntmPhieuXuat;
	}

	public JMenuItem getMntmCTPX() {
		return mntmCTPX;
	}

	public JSpinner getSpinnerSoLuong() {
		return spinnerSoLuong;
	}

	public JSpinner getSpinnerDonGia() {
		return spinnerDonGia;
	}

	public DefaultTableModel getCtpxModel() {
		return ctpxModel;
	}

	public JMenu getMnOption() {
		return mnOption;
	}

	public CTPXDao getCtpxDao() {
		return ctpxDao;
	}

	public ListSelectionListener getSelectionListenerCTPX() {
		return selectionListenerCTPX;
	}

	public KhoOptionFormForPX getKhoOptionFormPx() {
		khoOptionFormPx = new KhoOptionFormForPX();
		return khoOptionFormPx;
	}

	public VatTuOptionFormForPX getVatTuOptionFormPx() {
		String mapx = textFieldMaPX.getText();
		vatTuOptionFormPx = new VatTuOptionFormForPX(mapx);
		return vatTuOptionFormPx;
	}

	public ArrayList<CTPXModel> getCtpxList() {
		return ctpxList;
	}

	public Map<String, List<Object>> getMaNhanVienKho() {
		return maNhanVienKho;
	}

	public Map<Integer, String> getMaVT() {
		return maVT;
	}

	public Map<Integer, String> getNhanVienInfo() {
		return nhanVienInfo;
	}

	public Map<String, String> getKhoInfo() {
		return khoInfo;
	}

	public Map<String, String> getVatTuInfo() {
		return vatTuInfo;
	}

	public void setSelectedCTPX(boolean isSelectedCTPX) {
		this.isSelectedCTPX = isSelectedCTPX;
	}

	public void setSelectedPX(boolean isSelectedPX) {
		this.isSelectedPX = isSelectedPX;
	}

	public boolean isSelectedCTPX() {
		return isSelectedCTPX;
	}

	public boolean isSelectedPX() {
		return isSelectedPX;
	}

	public void loadDataIntoTable() {
		loadData();
		maNhanVienKho = new HashMap<String, List<Object>>();
		nhanVienInfo = new HashMap<Integer, String>();
		khoInfo = new HashMap<String, String>();
		String sql = "";
		String hoTenNV = "";
		String tenKho = "";
		for (PhieuXuatModel px : list) {
			/* hiển thị tên nhân viên thay cho mã */
			sql = "SELECT Ho + ' ' + Ten FROM NhanVien WHERE MANV = ?";
			Program.myReader = Program.ExecSqlDataReader(sql, px.getManv());
			try {
				if (Program.myReader.next())
					hoTenNV = Program.myReader.getString(1);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			/* hiển thị tên kho thay cho mã */
			sql = "SELECT TENKHO FROM KHO WHERE MAKHO = ?";
			Program.myReader = Program.ExecSqlDataReader(sql, px.getMaKho());
			try {
				Program.myReader.next();
				tenKho = Program.myReader.getString(1);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			nhanVienInfo.put(px.getManv(), hoTenNV);
			khoInfo.put(px.getMaKho(), tenKho);

			List<Object> values = new ArrayList<Object>();
			values.add(px.getManv());
			values.add(px.getMaKho());
			maNhanVienKho.put(px.getMapx(), values);
			Object[] rowData = { px.getMapx(), Formatter.formatterDate(px.getNgay()), px.getHoTenKH(), hoTenNV,
					tenKho };
			model.addRow(rowData);
		}
	}

	public void loadDataIntoTableCTPX() {
//		String mapx = table.getValueAt(table.getSelectedRow(), 0).toString();
		String mapx = getTextFieldMaPX().getText();
		ctpxModel.setColumnIdentifiers(ctpxDao.getColName().toArray());
		String sql = "SELECT MAPX, MAVT, SOLUONG, DONGIA FROM CTPX WHERE MAPX = ?";
		ctpxList = ctpxDao.selectByCondition(sql, mapx);
		maVT = new HashMap<Integer, String>();
		String tenVT = "";
		Integer key = 0;

		vatTuInfo = new HashMap<String, String>();
		for (CTPXModel px : ctpxList) {
			sql = "SELECT TENVT FROM Vattu WHERE MAVT = ?";
			Program.myReader = Program.ExecSqlDataReader(sql, px.getMavt());
			try {
				if (Program.myReader.next())
					tenVT = Program.myReader.getString(1);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			vatTuInfo.put(px.getMavt(), tenVT);

			maVT.put(key, px.getMavt());
			key += 1;
			Object[] rowData = { px.getMapx(), tenVT, px.getSoLuong(), Formatter.formatObjecttoMoney(px.getDonGia()),
					Formatter.formatObjecttoMoney(px.getSoLuong() * px.getDonGia()) };
			ctpxModel.addRow(rowData);
		}

		if (tableCTPX.getRowCount() > 0) {
			tableCTPX.getSelectionModel().addListSelectionListener(selectionListenerCTPX);
			tableCTPX.getSelectionModel().setSelectionInterval(0, 0);

		} else {
			textFieldMaVT.setText("");
			textFieldTenVT.setText("");
			spinnerSoLuong.setValue(0);
			spinnerDonGia.setValue(0);
			spinnerSoLuong.setEnabled(false);
			spinnerDonGia.setEnabled(false);
			btnVatTuOption.setEnabled(false);
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

			if (model.getRowCount() > 0) {
				table.getSelectionModel().setSelectionInterval(0, 0);
			} else {
				getTextFieldMaPX().setText("");
				getNgay().setDate(null);
				getTextFieldTenKH().setText("");
				getTextFieldMaNV().setText("");
				getTextFieldTenNV().setText("");
				getTextFieldMaKho().setText("");
				getTextFieldTenKho().setText("");
				getTextFieldMaVT().setText("");
				getTextFieldTenVT().setText("");
				getSpinnerSoLuong().setValue(0);
				getSpinnerDonGia().setValue(0);
				tableCTPX.getSelectionModel().removeListSelectionListener(selectionListenerCTPX);
				ctpxModel.setRowCount(0);
			}
		}
	}

	private void exitPhieuXuat() {
		Program.frmMain.getTabbedPane_Main().removeTabAt(Program.frmMain.getTabbedPane_Main().getSelectedIndex());
		Program.frmMain.getPanel_phieuxuat().remove(this);
	}
}
