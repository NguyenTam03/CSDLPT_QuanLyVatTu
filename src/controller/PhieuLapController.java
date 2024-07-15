package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import common.method.Formatter;
import dao.CTDDHDao;
import dao.CTPLDao;
import dao.DatHangDao;
import dao.KhoDao;
import main.Program;
import model.CTDDHModel;
import model.CTPLModel;
import model.DatHangModel;
import model.KhoModel;
import model.PhieuLapModel;
import views.DonHangOptionForm;
import views.PhieuLapForm;

public class PhieuLapController {
	private PhieuLapForm PLForm;
	private ListSelectionListener selectionListener_CTPN, selectionListener;
	private CTPLDao ctplDao;
	private ArrayList<DatHangModel> dhList;
	private ArrayList<CTDDHModel> ctddhList;
	private ArrayList<CTPLModel> ctplList;
	private ArrayList<KhoModel> khoList;
	private DefaultTableModel dhModel;
	private DonHangOptionForm DHOptionView;
	private PhieuLapModel plModel;
	private boolean isThem, isPhieuNhap;
	private int rowSelectedPN, rowSelectedCTPN;
	private Stack<String> undoList;

	public PhieuLapController(PhieuLapForm PLForm) {
		this.PLForm = PLForm;
		plModel = new PhieuLapModel();
		rowSelectedPN = rowSelectedCTPN = 0;
		undoList = new Stack<String>();
		isThem = isPhieuNhap = false;
	}
	
	public void initController() {
		PLForm.getMenuItemPN().addActionListener(l -> initPhieuLap());
		PLForm.getMenuItemCTPN().addActionListener(l -> initCTPhieuLap());
		PLForm.getBtnThoat().addActionListener(l -> exitPhieuLap());
		PLForm.getBtnDHOption().addActionListener(l -> ChonDonHang());
		PLForm.getBtnCTDHOption().addActionListener(l -> ChonCTDonHang());
		PLForm.getBtnKho().addActionListener(l -> ChonKho());
		PLForm.getBtnThem().addActionListener(l -> themPhieuNhap());
		PLForm.getBtnGhi().addActionListener(l -> ghiPhieuNhap());
		PLForm.getBtnXoa().addActionListener(l -> deletePhieuNhap());
		PLForm.getBtnHoanTac().addActionListener(l -> undoData());
		PLForm.getBtnLamMoi().addActionListener(l -> refreshData());
		autoSearchPhieuNhap();
	}

	private void autoSearchPhieuNhap() {
		PLForm.getTextFieldTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchPhieuNhap();
			}
		});
	}

	private void searchPhieuNhap() {
		String input = PLForm.getTextFieldTim().getText().trim().toLowerCase();
		PLForm.getTable().getSelectionModel().removeListSelectionListener(selectionListener);
		PLForm.getModel().setRowCount(0);

		for (PhieuLapModel pl : PLForm.getList()) {
			if (pl.getMapn().toLowerCase().contains(input) || pl.getMaSoDDH().toLowerCase().contains(input)
					|| PhieuLapForm.maKho_TenKho.get(pl.getMaKho()).toLowerCase().contains(input) || PhieuLapForm.maNV_TenNV.get(pl.getManv()).toLowerCase().contains(input)
					|| Formatter.formatterDate(pl.getNgay()).toString().contains(input)) {
				Object[] rowData = { pl.getMapn(),Formatter.formatterDate(pl.getNgay()), pl.getMaSoDDH(), PhieuLapForm.maNV_TenNV.get(pl.getManv()), PhieuLapForm.maKho_TenKho.get(pl.getMaKho()) };
				PLForm.getModel().addRow(rowData);
			}
		}
		PLForm.getTable().getSelectionModel().addListSelectionListener(selectionListener);
		if (PLForm.getTable().getRowCount() > 0) {
			PLForm.getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	private void exitPhieuLap() {
		Program.frmMain.getTabbedPane_Main().removeTabAt(Program.frmMain.getTabbedPane_Main().getSelectedIndex());
		Program.frmMain.getPanel_dathang().remove(PLForm);
	}

// ------------------------------------ Phiếu Nhập ------------------------------------
	private void initPhieuLap() {
		isPhieuNhap = true;
		selectionListener = e -> {
			if (PLForm.getTable().getSelectedRow() != -1) {
				if(Program.username.equals(PhieuLapForm.maPN_NV.get(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 0)).toString() )) {
					PLForm.getBtnXoa().setEnabled(true);
					PLForm.getBtnGhi().setEnabled(true);
					PLForm.getBtnDHOption().setEnabled(true);
					PLForm.getBtnKho().setEnabled(true);
				} else {
					PLForm.getBtnXoa().setEnabled(false);
					PLForm.getBtnGhi().setEnabled(false);
					PLForm.getBtnDHOption().setEnabled(false);
					PLForm.getBtnKho().setEnabled(false);
				}
				
				PLForm.getTFMaPN()
						.setText(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 0).toString());
				try {
					PLForm.getNgay().setDate(new SimpleDateFormat("dd-MM-yyyy").parse((String) PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 1)));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				PLForm.getTFMaDDH()
						.setText(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 2).toString());
				PLForm.getTFMaNV().
				setText(String.valueOf(PhieuLapForm.maPN_NV.get(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 0))));
				PLForm.getLbHoTenNV().setText(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 3).toString());
				PLForm.getTFMaKho().setText(PhieuLapForm.maPN_Kho.get(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 0)));
				PLForm.getLbTenKho().setText(PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 4).toString());				

				ctplDao = CTPLDao.getInstance();
				// .setColumnIdentifiers to set column name
				try {
					PLForm.getCtplModel().setColumnIdentifiers(ctplDao.getColName().toArray());
					PLForm.getCtplModel().addColumn("TRIGIA");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				// .setRowCount to remove all rows
				PLForm.getCtplModel().setRowCount(0);
				ctplList = ctplDao.selectAllCTPN(PLForm.getTFMaPN().getText());
				for (CTPLModel pl : ctplList) {
					Object[] rowData = { pl.getMaPN(), pl.getMavt(), pl.getSoLuong(),
							Formatter.formatObjecttoMoney(pl.getDonGia()), Formatter.formatObjecttoMoney(pl.getSoLuong() * pl.getDonGia()) };
					PLForm.getCtplModel().addRow(rowData);
				}
			}
		};
		PLForm.getTableCTPN().setEnabled(false);
		PLForm.getTable().setEnabled(true);
		PLForm.getTable().getSelectionModel().addListSelectionListener(selectionListener);
		PLForm.getTableCTPN().getSelectionModel().removeListSelectionListener(selectionListener_CTPN);
		if (Program.mGroup.equals("CONGTY")) {
			PLForm.getComboBox().setEnabled(true);
		} else {
			PLForm.getComboBox().setEnabled(false);
			
			PLForm.getMnOption().setText("Phiếu Nhập");
			PLForm.getBtnThem().setEnabled(true);
			PLForm.getBtnXoa().setEnabled(true);
			PLForm.getBtnGhi().setEnabled(true);
			if (undoList.isEmpty()) {
				PLForm.getBtnHoanTac().setEnabled(false);
			} else {
				PLForm.getBtnHoanTac().setEnabled(true);
			}
			PLForm.getBtnLamMoi().setEnabled(true);
			PLForm.getBtnDHOption().setEnabled(true);
			PLForm.getBtnCTDHOption().setEnabled(false);

			PLForm.getNgay().setEnabled(false);
			PLForm.getBtnKho().setEnabled(true);

			PLForm.getTFMaVT().setText("");
			PLForm.getLbTenVatTu().setText("");
			PLForm.getSoLuong().setValue(0);
			PLForm.getDonGia().setValue(0);
			PLForm.getSoLuong().setEnabled(false);
			PLForm.getDonGia().setEnabled(false);
		}
	}

//------------------------------------ Chọn Button Đơn Hàng ------------------------------------
	private void ChonDonHang() {
		DHOptionView = new DonHangOptionForm();
		DatHangDao dhDao;
		dhModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dhDao = DatHangDao.getInstance();
		dhModel = (DefaultTableModel) DHOptionView.getTableDH().getModel();
		dhDao.selectAll();
		dhModel.setColumnIdentifiers(dhDao.getColName().toArray());
		dhList = loadDHList();

		for (DatHangModel dh : dhList) {
			Object[] rowData = { dh.getMaSoDDH(), dh.getNgay(), dh.getNhaCC(), dh.getManv(), dh.getMakho() };
			dhModel.addRow(rowData);
		}
		DHOptionView.getTableDH().setModel(dhModel);
		dhModel = (DefaultTableModel) DHOptionView.getTableDH().getModel();
		DHOptionView.setVisible(true);
		DHOptionView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DHOptionView.getBtnChon().addActionListener(e -> clickBtnChon_DH());
		DHOptionView.getBtnThoat().addActionListener(e -> DHOptionView.dispose());
		autoSearchDonHang();
	}

	private void clickBtnChon_DH() {
		PLForm.getTFMaDDH().setText(dhModel.getValueAt(DHOptionView.getTableDH().getSelectedRow(), 0).toString());
		DHOptionView.dispose();
	}

	private void autoSearchDonHang() {
		DHOptionView.getTFTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchDonHang();
			}
		});
	}

	private void searchDonHang() {
		String input = DHOptionView.getTFTim().getText().trim().toLowerCase();
//		DHOptionView.getTableDH().getSelectionModel().removeListSelectionListener(selectionListener);
		dhModel.setRowCount(0);

		for (DatHangModel dh : dhList) {
			if (dh.getMaSoDDH().toLowerCase().contains(input) || dh.getNhaCC().toLowerCase().contains(input)) {
				Object[] rowData = { dh.getMaSoDDH(), dh.getNgay(), dh.getNhaCC(), dh.getManv(), dh.getMakho() };
				dhModel.addRow(rowData);
			}
		}
//		DHOptionView.getTableDH().getSelectionModel().addListSelectionListener(selectionListener);
		if (DHOptionView.getTableDH().getRowCount() > 0) {
			DHOptionView.getTableDH().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	// Load danh sách đơn hàng chưa nhập
	private ArrayList<DatHangModel> loadDHList() {
		ArrayList<DatHangModel> datHangList = new ArrayList<DatHangModel>();
		String sql = "select * from DatHang where MasoDDH not in (select MasoDDH from PhieuNhap)";
		Program.myReader = Program.ExecSqlDataReader(sql);

		try {
			while (Program.myReader.next()) {
				DatHangModel datHang = new DatHangModel(Program.myReader.getString(1), Program.myReader.getDate(2),
						Program.myReader.getString(3), Program.myReader.getInt(4), Program.myReader.getString(5));

				datHangList.add(datHang);
			}
			return datHangList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// ------------------------------------ Chọn Button Kho
	// ------------------------------------
	private void ChonKho() {
		DHOptionView = new DonHangOptionForm();
		KhoDao khoDao;
		dhModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		khoDao = KhoDao.getInstance();
		try {
			dhModel.setColumnIdentifiers(khoDao.getColName().toArray());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		khoDao = KhoDao.getInstance();
		dhModel = (DefaultTableModel) DHOptionView.getTableDH().getModel();
		Object[] objects = khoDao.getColName().toArray();
		if (objects.length >= 2) {
		    String[] ColName = new String[2];
		    ColName[0] = String.valueOf(objects[0]);
		    ColName[1] = String.valueOf(objects[1]);
		    dhModel.setColumnIdentifiers(ColName);
		} else {
		    JOptionPane.showMessageDialog(null, "Không có dữ liệu kho");
		}
		khoList = loadKhoList();

		for (KhoModel kho : khoList) {
			Object[] rowData = { kho.getMaKho(), kho.getTenKho() };
			dhModel.addRow(rowData);
		}
		DHOptionView.getTableDH().setModel(dhModel);
		dhModel = (DefaultTableModel) DHOptionView.getTableDH().getModel();
		DHOptionView.setVisible(true);
		DHOptionView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DHOptionView.getBtnChon().addActionListener(e -> clickBtnChonKho());
		DHOptionView.getBtnThoat().addActionListener(e -> DHOptionView.dispose());
		autoSearchKho();
	}

	private void clickBtnChonKho() {
		PLForm.getTFMaKho().setText(dhModel.getValueAt(DHOptionView.getTableDH().getSelectedRow(), 0).toString());
		PLForm.getLbTenKho().setText(dhModel.getValueAt(DHOptionView.getTableDH().getSelectedRow(), 1).toString());
		DHOptionView.dispose();
	}

	private void autoSearchKho() {
		DHOptionView.getTFTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchKho();
			}
		});
	}

	private void searchKho() {
		String input = DHOptionView.getTFTim().getText().trim().toLowerCase();
		dhModel.setRowCount(0);
		for (KhoModel kho : khoList) {
			if (kho.getMaKho().toLowerCase().contains(input) || kho.getTenKho().toLowerCase().contains(input)) {
				Object[] rowData = { kho.getMaKho(), kho.getTenKho() };
				dhModel.addRow(rowData);
			}
		}
		if (DHOptionView.getTableDH().getRowCount() > 0) {
			DHOptionView.getTableDH().getSelectionModel().setSelectionInterval(0, 0);
		}

	}

	private ArrayList<KhoModel> loadKhoList() {
		ArrayList<KhoModel> khoList = new ArrayList<KhoModel>();
		String sql = "select * from Kho";
		Program.myReader = Program.ExecSqlDataReader(sql);

		try {
			while (Program.myReader.next()) {
				KhoModel kho = new KhoModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getString(3), Program.myReader.getString(4));

				khoList.add(kho);
			}
			return khoList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//------------------------------------ Phiếu Nhập ------------------------------------
	private void initCTPhieuLap() {
		if (PLForm.getTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "Chưa chọn phiếu nhập");
			return;
		}
		rowSelectedPN = PLForm.getTable().getSelectedRow();
		PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(0, 0);
		isPhieuNhap = false;
		selectionListener_CTPN = e -> {
			if (PLForm.getTableCTPN().getSelectedRow() != -1) {
				PLForm.getTFMaVT().setText(
						PLForm.getTableCTPN().getValueAt(PLForm.getTableCTPN().getSelectedRow(), 1).toString());
				String sql = "SELECT TENVT FROM VATTU WHERE MAVT = ?";
				Program.myReader = Program.ExecSqlDataReader(sql, PLForm.getTFMaVT().getText());
				try {
					Program.myReader.next();
					PLForm.getLbTenVatTu().setText(Program.myReader.getString(1));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				PLForm.getSoLuong()
						.setValue(PLForm.getTableCTPN().getValueAt(PLForm.getTableCTPN().getSelectedRow(), 2));
				PLForm.getDonGia().setValue(Formatter.formatMoneyToFloat(
						PLForm.getTableCTPN().getValueAt(PLForm.getTableCTPN().getSelectedRow(), 3)));
			}
		};
//		PLForm.getTable().setEnabled(false);
		PLForm.getTableCTPN().setEnabled(true);
		PLForm.getTableCTPN().getSelectionModel().addListSelectionListener(selectionListener_CTPN);
		PLForm.getTableCTPN().getSelectionModel().removeListSelectionListener(selectionListener);
		if (Program.mGroup.equals("CONGTY")) {
			PLForm.getComboBox().setEnabled(true);
		} else {
			PLForm.getComboBox().setEnabled(false);
			PLForm.getMnOption().setText("Chi Tiết Phiếu Nhập");
			PLForm.getBtnThem().setEnabled(true);
			PLForm.getBtnXoa().setEnabled(true);
			PLForm.getBtnGhi().setEnabled(true);
			if (undoList.isEmpty()) {
				PLForm.getBtnHoanTac().setEnabled(false);
			} else {
				PLForm.getBtnHoanTac().setEnabled(true);
			}
			PLForm.getBtnLamMoi().setEnabled(true);
			PLForm.getBtnDHOption().setEnabled(false);
			PLForm.getBtnCTDHOption().setEnabled(false);

			PLForm.getNgay().setEnabled(false);
			PLForm.getBtnKho().setEnabled(false);

			PLForm.getSoLuong().setEnabled(true);
			PLForm.getDonGia().setEnabled(true);
		}

	}

//------------------------------------ Chọn Button Chi Tiết Đơn Hàng ------------------------------------
	private void ChonCTDonHang() {
		if (PLForm.getTFMaDDH().getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Chưa chọn đơn hàng");
			return;
		}
		DHOptionView = new DonHangOptionForm();
		CTDDHDao ctddhDao;
		dhModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		ctddhDao = CTDDHDao.getInstance();
		try {
			dhModel.setColumnIdentifiers(ctddhDao.getColName().toArray());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ctddhDao = CTDDHDao.getInstance();
		dhModel = (DefaultTableModel) DHOptionView.getTableDH().getModel();
		dhModel.setColumnIdentifiers(ctddhDao.getColName().toArray());
		ctddhList = loadCTDHList();

		for (CTDDHModel ctddh : ctddhList) {
			Object[] rowData = { ctddh.getMaSoDDH(), ctddh.getMavt(), ctddh.getSoLuong(),
					Formatter.formatObjecttoMoney(ctddh.getDonGia()) };
			dhModel.addRow(rowData);
		}
		DHOptionView.getTableDH().setModel(dhModel);
		dhModel = (DefaultTableModel) DHOptionView.getTableDH().getModel();
		DHOptionView.setVisible(true);
		DHOptionView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DHOptionView.getBtnChon().addActionListener(e -> clickBtnChon_CTDDH());
		DHOptionView.getBtnThoat().addActionListener(e -> DHOptionView.dispose());
		autoSearchCTDonHang();
	}

	private ArrayList<CTDDHModel> loadCTDHList() {
		ArrayList<CTDDHModel> CTDHList = new ArrayList<CTDDHModel>();
		String sql = "select * from CTDDH where MasoDDH = ? and mavt not in (select mavt from ctpn where MAPN = ?)";
		Program.myReader = Program.ExecSqlDataReader(sql, PLForm.getTFMaDDH().getText(), PLForm.getTFMaPN().getText());

		try {
			while (Program.myReader.next()) {
				CTDDHModel ctdh = new CTDDHModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getInt(3), Program.myReader.getFloat(4));

				CTDHList.add(ctdh);
			}
			return CTDHList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void clickBtnChon_CTDDH() {
		if (DHOptionView.getTableDH().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "Chưa chọn vật tư");
			return;
		}
		PLForm.getTFMaVT().setText(
				DHOptionView.getTableDH().getValueAt(DHOptionView.getTableDH().getSelectedRow(), 1).toString());
		PLForm.getSoLuong()
				.setValue(DHOptionView.getTableDH().getValueAt(DHOptionView.getTableDH().getSelectedRow(), 2));
		PLForm.getDonGia().setValue(Formatter.formatMoneyToFloat(
				DHOptionView.getTableDH().getValueAt(DHOptionView.getTableDH().getSelectedRow(), 3)));
		String sql = "SELECT TENVT FROM VATTU WHERE MAVT = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, PLForm.getTFMaVT().getText());
		try {
			Program.myReader.next();
			PLForm.getLbTenVatTu().setText(Program.myReader.getString(1));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		DHOptionView.dispose();
	}

	private void autoSearchCTDonHang() {
		DHOptionView.getTFTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchCTDonHang();
			}
		});
	}

	private void searchCTDonHang() {
		String input = DHOptionView.getTFTim().getText().trim().toLowerCase();
//		DHOptionView.getTableDH().getSelectionModel().removeListSelectionListener(selectionListener);

		dhModel.setRowCount(0);

		for (CTDDHModel ctddh : ctddhList) {
			if (ctddh.getMavt().toLowerCase().contains(input)) {
				Object[] rowData = { ctddh.getMaSoDDH(), ctddh.getMavt(), ctddh.getSoLuong(), ctddh.getDonGia() };
				dhModel.addRow(rowData);
			}
		}
//		DHOptionView.getTableDH().getSelectionModel().addListSelectionListener(selectionListener);
		if (DHOptionView.getTableDH().getRowCount() > 0) {
			DHOptionView.getTableDH().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

//------------------------------------ Thêm Đơn Hàng/CT Đơn Hàng ------------------------------------
	private void themPhieuNhap() {
		isThem = true;
		PLForm.getTable().setEnabled(false);
		PLForm.getTableCTPN().setEnabled(false);
		PLForm.getBtnThem().setEnabled(false);
		PLForm.getBtnXoa().setEnabled(false);
		PLForm.getBtnHoanTac().setEnabled(true);
		PLForm.getBtnLamMoi().setEnabled(false);
		PLForm.getMnOption().setEnabled(false);

		PLForm.getTFMaVT().setText("");
		PLForm.getLbTenVatTu().setText("");
		PLForm.getSoLuong().setValue(0);
		PLForm.getDonGia().setValue(0);
		if (isPhieuNhap) {
			PLForm.getTFMaPN().setText("");			
			java.util.Date currentDate = Calendar.getInstance().getTime();
			PLForm.getNgay().setDate(currentDate);
			PLForm.getNgay().setEnabled(false);
			PLForm.getTFMaDDH().setText("");
			PLForm.getTFMaNV().setText(Program.username);
			PLForm.getLbHoTenNV().setText(Program.mHoten);
			PLForm.getTFMaKho().setText("");
			PLForm.getLbTenKho().setText("");

			rowSelectedPN = PLForm.getTable().getSelectedRow();
			PLForm.getTFMaDDH().setEditable(false);
			PLForm.getTFMaVT().setEditable(false);
			PLForm.getSoLuong().setEnabled(false);
			PLForm.getDonGia().setEnabled(false);
			PLForm.getBtnCTDHOption().setEnabled(false);

			PLForm.getBtnDHOption().setEnabled(true);
			PLForm.getTFMaPN().setEditable(true);
//			PLForm.getNgay().setEnabled(true);
			PLForm.getBtnKho().setEnabled(true);

			PLForm.getTable().getSelectionModel().removeListSelectionListener(selectionListener);
			PLForm.getTable().getSelectionModel().clearSelection();
		} else {
			rowSelectedCTPN = PLForm.getTableCTPN().getSelectedRow();
			PLForm.getTFMaDDH().setEditable(false);
			PLForm.getTFMaVT().setEditable(false);
			PLForm.getSoLuong().setEnabled(true);
			PLForm.getDonGia().setEnabled(true);
			PLForm.getBtnCTDHOption().setEnabled(true);

			PLForm.getBtnDHOption().setEnabled(false);
			PLForm.getTFMaPN().setEditable(false);
			PLForm.getNgay().setEnabled(false);
			PLForm.getBtnKho().setEnabled(false);
			PLForm.getTableCTPN().getSelectionModel().removeListSelectionListener(selectionListener_CTPN);
			PLForm.getTableCTPN().getSelectionModel().clearSelection();
		}

	}

	private void ghiPhieuNhap() {
		if (isPhieuNhap) {
			rowSelectedPN = PLForm.getTable().getSelectedRow();
			String MaPN, MaDDH, MaNV, MaKho;
			java.util.Date Ngay = null;
			try {
				MaPN = PLForm.getTFMaPN().getText().trim();
				Ngay = PLForm.getNgay().getDate();
				MaDDH = PLForm.getTFMaDDH().getText().trim();
				MaNV = PLForm.getTFMaNV().getText().trim();
				MaKho = PLForm.getTFMaKho().getText().trim();
				if (MaPN.isEmpty() || MaDDH.isEmpty() || MaNV.isEmpty() || MaKho.isEmpty() || Ngay == null) {
					JOptionPane.showMessageDialog(null, "Chưa nhập đủ thông tin");
					return;
				}
				if (checkMaPN(MaPN) && isThem) {
					JOptionPane.showMessageDialog(null, "Mã phiếu nhập đã tồn tại");
					return;
				}
				int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn ghi dữ liệu vào bảng không?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (reply == JOptionPane.YES_NO_OPTION) {
					PhieuLapForm.maPN_NV.put(MaPN, Integer.parseInt(MaNV));
					PhieuLapForm.maNV_TenNV.put(Integer.parseInt(MaNV), PLForm.getLbHoTenNV().getText());
					PhieuLapForm.maPN_Kho.put(MaPN, PLForm.getLbTenKho().getText());
					PhieuLapForm.maKho_TenKho.put(MaKho, PLForm.getLbTenKho().getText());
					plModel.setMapn(MaPN);
					plModel.setNgay(new Date(((java.util.Date) Ngay).getTime()));
					plModel.setMaSoDDH(MaDDH);
					plModel.setManv(Integer.parseInt(MaNV));
					plModel.setMaKho(MaKho);
					rowSelectedPN = PLForm.getTable().getSelectedRow();
					if (isThem) {
						addDataPNToDB(plModel);
						String sql = "autoAddCTPN ?, ?";
						try {
							Program.ExecSqlDML(sql, MaDDH, MaPN);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						String sqlUndo = "delete from CTPN where MaPN = '"+MaPN +"'";
						undoList.push(sqlUndo);
						isThem = false;
						PLForm.getTable().setEnabled(true);
						PLForm.getTableCTPN().setEnabled(false);
						PLForm.getBtnThem().setEnabled(true);
						PLForm.getBtnXoa().setEnabled(true);
						PLForm.getBtnHoanTac().setEnabled(true);
						PLForm.getBtnLamMoi().setEnabled(true);
						PLForm.getMnOption().setEnabled(true);

//						PLForm.getBtnDHOption().setEnabled(false);
						PLForm.getTFMaPN().setEditable(false);
						PLForm.getNgay().setEnabled(false);
//						PLForm.getBtnKho().setEnabled(false);
						
						PLForm.getTable().getSelectionModel().addListSelectionListener(selectionListener);
						// .getSelectionModel().setSelectionInterval() : chọn dòng cuối cùng trong table
						PLForm.getTable().getSelectionModel().setSelectionInterval(PLForm.getTable().getRowCount() - 1,
								PLForm.getTable().getRowCount() - 1);

						rowSelectedPN = PLForm.getTable().getRowCount() - 1;
					} else {
						updateDataPNToDB(plModel);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else {
			rowSelectedCTPN = PLForm.getTableCTPN().getSelectedRow();
			String MaPN, MaVT;
			int SoLuong;
			float DonGia = 0;
			try {
				MaPN = PLForm.getTFMaPN().getText().trim();
				MaVT = PLForm.getTFMaVT().getText().trim();
				SoLuong = (int) PLForm.getSoLuong().getValue();
				Object donGiaObject = PLForm.getDonGia().getValue();
				if (donGiaObject instanceof Integer) {
				    Integer intValue = (Integer) donGiaObject;
				    DonGia = intValue.floatValue();
				} else if (donGiaObject instanceof Float) {
					DonGia = (Float) donGiaObject;
				}
				if (MaPN.isEmpty() || MaVT.isEmpty() || SoLuong == 0 || DonGia == 0) {
					JOptionPane.showMessageDialog(null, "Chưa nhập đủ thông tin");
					return;
				}
				int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn ghi dữ liệu vào bảng không?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (reply == JOptionPane.YES_NO_OPTION) {
					CTPLModel ctplModel = new CTPLModel(MaPN, MaVT, SoLuong, (float) DonGia);
					if (isThem) {
						addDataCTPNToDB(ctplModel);
						isThem = false;
					} else {
						updateDataCTPNToDB(ctplModel);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

	}

	private boolean checkMaPN(String MaPN) {
		// Kiểm tra site đang đứng trước
		String sql = "SELECT CASE WHEN ? IN (SELECT MAPN FROM PhieuNhap) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaPN);
		try {
			Program.myReader.next();
			if (Program.myReader.getBoolean(1))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Kiểm tra site khác
		sql = "SELECT CASE WHEN ? IN (SELECT MAPN FROM Link1.QLVT_DATHANG.dbo.PhieuNhap) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaPN);
		try {
			Program.myReader.next();
			if (Program.myReader.getBoolean(1))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void addDataPNToDB(PhieuLapModel plModel) {
		try {
			Object[] newRow = { plModel.getMapn(),Formatter.formatterDate(plModel.getNgay()) , plModel.getMaSoDDH(), PLForm.getLbHoTenNV().getText(),
					PLForm.getLbTenKho().getText() };
			PLForm.getModel().addRow(newRow);
			PLForm.getDao().insert(plModel);
			PLForm.getList().add(plModel);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi thêm phiếu nhập!! \n " + e.getMessage(), "THông Báo",
					JOptionPane.WARNING_MESSAGE);
			refreshData();
			PLForm.getTable().getSelectionModel().setSelectionInterval(rowSelectedPN, rowSelectedPN);
			e.printStackTrace();
			return;
		}
		String sqlUndo = "DELETE FROM PhieuNhap WHERE MAPN = '" + plModel.getMapn() + "'";
		undoList.push(sqlUndo);
		JOptionPane.showConfirmDialog(null, "Ghi Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
	}

	private void updateDataPNToDB(PhieuLapModel pl) {
		String Ngay = PLForm.getTable().getValueAt(rowSelectedPN, 1).toString();
		String MaDDH = PLForm.getTable().getValueAt(rowSelectedPN, 2).toString();
		String MaKho = PLForm.getTable().getValueAt(rowSelectedPN, 4).toString();

		try {
			PLForm.getTable().setValueAt(Formatter.formatterDate(pl.getNgay()) , rowSelectedPN, 1);
			PLForm.getTable().setValueAt(pl.getMaSoDDH(), rowSelectedPN, 2);
			PLForm.getTable().setValueAt(pl.getMaKho(), rowSelectedPN, 4);
			PLForm.getDao().update(pl);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Lỗi cập nhật phiếu nhập!! \n " + e.getMessage(), "THông Báo",
					JOptionPane.WARNING_MESSAGE);
			refreshData();
			PLForm.getTable().getSelectionModel().setSelectionInterval(rowSelectedPN, rowSelectedPN);
			e.printStackTrace();
		}
		PLForm.getTable().getSelectionModel().setSelectionInterval(rowSelectedPN, rowSelectedPN);
		String sqlUndo = "UPDATE PhieuNhap SET Ngay = '" + Ngay + "', MasoDDH = '" + MaDDH + "', Makho = '" + MaKho
				+ "' WHERE MAPN = '" + pl.getMapn() + "'";
		undoList.push(sqlUndo);
		PLForm.getBtnHoanTac().setEnabled(true);
		JOptionPane.showConfirmDialog(null, "Cập Nhật Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
	}

	private void addDataCTPNToDB(CTPLModel ctplModel) {
		try {
			Object[] newRow = { ctplModel.getMaPN(), ctplModel.getMavt(), ctplModel.getSoLuong(),
					Formatter.formatObjecttoMoney(ctplModel.getDonGia()) };
			PLForm.getCtplModel().addRow(newRow);
			ctplDao.insert(ctplModel);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi thêm chi tiết phiếu nhập!! \n " + e.getMessage(), "THông Báo",
					JOptionPane.WARNING_MESSAGE);
			refreshData();
			PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(rowSelectedCTPN, rowSelectedCTPN);
			e.printStackTrace();
			return;
		}
		String sqlUndo = "DELETE FROM CTPN WHERE MAPN = '" + ctplModel.getMaPN() + "' AND MAVT = '"
				+ ctplModel.getMavt() + "'";
		undoList.push(sqlUndo);

		PLForm.getTable().setEnabled(true);
		PLForm.getTableCTPN().setEnabled(true);
		PLForm.getBtnThem().setEnabled(true);
		PLForm.getBtnXoa().setEnabled(true);
		PLForm.getBtnHoanTac().setEnabled(true);
		PLForm.getBtnLamMoi().setEnabled(true);
		PLForm.getMnOption().setEnabled(true);

		PLForm.getBtnDHOption().setEnabled(false);
		PLForm.getBtnCTDHOption().setEnabled(false);
		PLForm.getTFMaPN().setEditable(false);
		PLForm.getNgay().setEnabled(false);
		PLForm.getBtnKho().setEnabled(false);

		PLForm.getTableCTPN().getSelectionModel().addListSelectionListener(selectionListener_CTPN);
		PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(PLForm.getTableCTPN().getRowCount() - 1,
				PLForm.getTableCTPN().getRowCount() - 1);

		rowSelectedCTPN = PLForm.getTableCTPN().getRowCount() - 1;
		JOptionPane.showConfirmDialog(null, "Ghi Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
	}

	private void updateDataCTPNToDB(CTPLModel ctplmodel) {
		String MaVT = PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 1).toString();
		int SoLuong = (int) PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 2);
		float DonGia = Formatter.formatMoneyToFloat(PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 3));

		try {
			PLForm.getTableCTPN().setValueAt(ctplmodel.getMavt(), rowSelectedCTPN, 1);
			PLForm.getTableCTPN().setValueAt(ctplmodel.getSoLuong(), rowSelectedCTPN, 2);
			PLForm.getTableCTPN().setValueAt(Formatter.formatObjecttoMoney(ctplmodel.getDonGia()),
					rowSelectedCTPN, 3);
			ctplDao.update(ctplmodel);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Lỗi cập nhật chi tiết phiếu nhập!! \n " + e.getMessage(), "THông Báo",
					JOptionPane.WARNING_MESSAGE);
			refreshData();
			PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(rowSelectedCTPN, rowSelectedCTPN);
			e.printStackTrace();
		}
		PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(rowSelectedCTPN, rowSelectedCTPN);
		String sqlUndo = "UPDATE CTPN SET SoLuong = " + SoLuong + ", DonGia = " + DonGia + " WHERE MAPN = '"
				+ ctplmodel.getMaPN() + "' AND MAVT = '" + MaVT + "'";
		undoList.push(sqlUndo);
		PLForm.getBtnHoanTac().setEnabled(true);
		JOptionPane.showConfirmDialog(null, "Cập Nhật Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
	}

	private void refreshData() {
		try {
			PLForm.getTableCTPN().getSelectionModel().removeListSelectionListener(selectionListener_CTPN);
			PLForm.getTable().getSelectionModel().removeListSelectionListener(selectionListener);
			// .setRowCount(0) : xóa hết dữ liệu
			PLForm.getCtplModel().setRowCount(0);
			PLForm.getModel().setRowCount(0);
			PLForm.loadDataIntoTable();
			PLForm.getTableCTPN().getSelectionModel().addListSelectionListener(selectionListener_CTPN);
			PLForm.getTable().getSelectionModel().addListSelectionListener(selectionListener);
			if (isPhieuNhap) {
				PLForm.getTable().getSelectionModel().setSelectionInterval(0, 0);
			} else {
				PLForm.getTable().getSelectionModel().setSelectionInterval(rowSelectedPN, rowSelectedPN);
			}
			PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(-1, -1);
			PLForm.getTFMaVT().setText("");
			PLForm.getLbTenVatTu().setText("");
			PLForm.getSoLuong().setValue(0);
			PLForm.getDonGia().setValue(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deletePhieuNhap() {
		if (isPhieuNhap) {

			if (PLForm.getTable().getRowCount() == 0)
				PLForm.getBtnXoa().setEnabled(false);
			rowSelectedPN = PLForm.getTable().getSelectedRow();
			if (rowSelectedPN == -1) {
				JOptionPane.showMessageDialog(null, "Chưa chọn phiếu nhập");
				return;
			}
			plModel.setMapn(PLForm.getTable().getValueAt(rowSelectedPN, 0).toString());
			try {
				plModel.setNgay(new java.sql.Date(new SimpleDateFormat("dd-MM-yyyy").parse((String) PLForm.getTable().getValueAt(PLForm.getTable().getSelectedRow(), 1)).getTime()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			plModel.setMaSoDDH(PLForm.getTable().getValueAt(rowSelectedPN, 2).toString());
			plModel.setManv(Integer.parseInt(PLForm.getTFMaNV().getText()));
			plModel.setMaKho(PLForm.getTFMaKho().getText());
			if (checkPhieuNhap(plModel.getMapn())) {
				JOptionPane.showMessageDialog(null, "Không thể xóa \n Phiếu nhập đã tồn tại trong chi tiết phiếu nhập");
				return;
			}
			int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa phiếu nhập này không?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (reply == JOptionPane.YES_OPTION) {
				try {
					PLForm.getDao().delete(plModel);
					String sqlUndo = "INSERT INTO PhieuNhap (MAPN, Ngay, MasoDDH, MaNV, Makho) VALUES ('"
							+ plModel.getMapn() + "', '" + plModel.getNgay() + "', '" + plModel.getMaSoDDH() + "', '"
							+ plModel.getManv() + "', '" + plModel.getMaKho() + "')";
					undoList.push(sqlUndo);
					refreshData();
					PLForm.getBtnHoanTac().setEnabled(true);
					JOptionPane.showConfirmDialog(null, "Xóa Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
					return;
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Lỗi xóa phiếu nhập!! \n " + e.getMessage(), "THông Báo",
							JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					return;
				}
			} else
				return;
		} else {
			if (PLForm.getTableCTPN().getRowCount() == 0)
				PLForm.getBtnXoa().setEnabled(false);
			rowSelectedCTPN = PLForm.getTableCTPN().getSelectedRow();
			if (rowSelectedCTPN == -1) {
				JOptionPane.showMessageDialog(null, "Chưa chọn chi tiết phiếu nhập");
				return;
			}
			CTPLModel ctplModel = new CTPLModel();
			ctplModel.setMaPN(PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 0).toString());
			ctplModel.setMavt(PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 1).toString());
			ctplModel.setSoLuong((int) PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 2));
			ctplModel.setDonGia((float) Formatter.formatMoneyToFloat(PLForm.getTableCTPN().getValueAt(rowSelectedCTPN, 3)));
			int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa chi tiết phiếu nhập này không?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (reply == JOptionPane.YES_OPTION) {
				try {
					ctplDao.delete(ctplModel);
					String sqlUndo = "INSERT INTO CTPN (MAPN, MAVT, SoLuong, DonGia) VALUES ('" + ctplModel.getMaPN()
							+ "', '" + ctplModel.getMavt() + "', " + ctplModel.getSoLuong() + ", "
							+ ctplModel.getDonGia() + ")";
					undoList.push(sqlUndo);
					refreshData();
					PLForm.getBtnHoanTac().setEnabled(true);
					JOptionPane.showConfirmDialog(null, "Xóa Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
					return;
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Lỗi xóa chi tiết phiếu nhập!! \n " + e.getMessage(),
							"THông Báo", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					return;
				}
			} else
				return;
		}

	}

	private boolean checkPhieuNhap(String MaPN) {
		String sql = "SELECT CASE WHEN ? IN (SELECT MAPN FROM CTPN) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaPN);
		try {
			Program.myReader.next();
			if (Program.myReader.getBoolean(1))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void undoData() {
		if (isThem) {
			if (isPhieuNhap) {
				PLForm.getTable().getSelectionModel().addListSelectionListener(selectionListener);
				PLForm.getTable().getSelectionModel().setSelectionInterval(rowSelectedPN, rowSelectedPN);
				PLForm.getTable().setEnabled(true);
				PLForm.getBtnDHOption().setEnabled(false);
			} else {
				PLForm.getTFMaVT().setText("");
				PLForm.getLbTenVatTu().setText("");
				PLForm.getSoLuong().setValue(0);
				PLForm.getDonGia().setValue(0);
				PLForm.getTableCTPN().getSelectionModel().addListSelectionListener(selectionListener_CTPN);
				PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(rowSelectedCTPN, rowSelectedCTPN);
				PLForm.getTableCTPN().setEnabled(true);
				PLForm.getBtnCTDHOption().setEnabled(false);
			}

			PLForm.getBtnXoa().setEnabled(true);
			PLForm.getBtnThem().setEnabled(true);
			PLForm.getBtnHoanTac().setEnabled(true);
			PLForm.getBtnLamMoi().setEnabled(true);
			PLForm.getMnOption().setEnabled(true);

//			PLForm.getBtnDHOption().setEnabled(false);
			PLForm.getTFMaPN().setEditable(false);
			PLForm.getNgay().setEnabled(false);
//			PLForm.getBtnKho().setEnabled(false);
			isThem = false;
			if (undoList.isEmpty()) {
				PLForm.getBtnHoanTac().setEnabled(false);
			}

			return;
		}
		if (undoList.isEmpty()) {
			JOptionPane.showConfirmDialog(null, "Hết thao tác để khôi phục", "Thông Báo", JOptionPane.CLOSED_OPTION);
			PLForm.getBtnHoanTac().setEnabled(false);
			return;
		}
		String queryUndo = undoList.pop().toString();
		if (Program.ExecSqlNonQuery(queryUndo) == -1) {
			JOptionPane.showConfirmDialog(null, "Khôi phục thất bại", "Thông Báo", JOptionPane.CLOSED_OPTION);
			return;
		}
		refreshData();
		if (rowSelectedPN <= PLForm.getTable().getRowCount() - 1) {
			PLForm.getTable().getSelectionModel().setSelectionInterval(rowSelectedPN, rowSelectedPN);
		} else {
			PLForm.getTable().getSelectionModel().setSelectionInterval(0, 0);
			rowSelectedPN = 0;
		}
		if (rowSelectedCTPN <= PLForm.getTableCTPN().getRowCount() - 1) {
			PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(rowSelectedCTPN, rowSelectedCTPN);
		} else {
			PLForm.getTableCTPN().getSelectionModel().setSelectionInterval(-1, -1);
			rowSelectedCTPN = 0;
		}
		if (undoList.isEmpty()) {
			PLForm.getBtnHoanTac().setEnabled(false);
			return;
		}
	}
}
