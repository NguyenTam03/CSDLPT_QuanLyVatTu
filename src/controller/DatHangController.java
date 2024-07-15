package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JOptionPane;

import common.method.Authorization;
import common.method.Formatter;
import common.method.ISearcher;
import common.method.IValidation;
import main.Program;
import model.CTDDHModel;
import model.DatHangModel;
import views.DatHangForm;
import views.KhoOptionForm;
import views.VatTuOptionForm;

public class DatHangController implements ISearcher {
	private DatHangForm dh;
	private DatHangModel dhModel;
	private CTDDHModel ctdhModel;
	private Stack<Object[]> undoList;
	private int row;

	private enum Mode {
		DON_DAT_HANG, CTDDH
	}

	private Mode mode;

	public DatHangController(DatHangForm dh) {
		this.dh = dh;
		dhModel = new DatHangModel();
		ctdhModel = new CTDDHModel();
		undoList = new Stack<>();
		row = 0;
	}

	public void initController() {
		dh.getMntmDatHang().addActionListener(l -> {
			if (!dh.isSelectedDH()) {
				dh.setSelectedDH(true);
				dh.setSelectedCTDH(false);
				initDatHang();
			}

		});
		dh.getMntmCTDH().addActionListener(l -> {
			if (!dh.isSelectedCTDH()) {
				dh.setSelectedCTDH(true);
				dh.setSelectedDH(false);
				initCTDatHang();
			}

		});
		dh.getBtnThem().addActionListener(l -> addDatHang());
		dh.getBtnHoanTac().addActionListener(l -> btnUndo());
		dh.getBtnKhoOption().addActionListener(l -> openKhoForm());
		dh.getBtnVatTuOption().addActionListener(l -> openVatTuForm());
		dh.getBtnGhi().addActionListener(l -> btnGhiListener());
		dh.getBtnLamMoi().addActionListener(l -> reFreshData());
		dh.getBtnXoa().addActionListener(l -> deleteData());
		dh.getTextFieldTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				search();
			}
		});
	}

	private void initDatHang() {
		if (mode == Mode.CTDDH && dh.isSelectedDH()) {
			dh.getBtnHoanTac().setEnabled(true);
			undoList.push(new Object[] { mode, dh.getTableCTDH().getSelectedRow(), dh.getTable().getSelectedRow() });
		}
		mode = Mode.DON_DAT_HANG;
		dh.getTable().setEnabled(true);
		dh.getTableCTDH().setEnabled(true);
		dh.getBtnVatTuOption().setEnabled(false);
		dh.getSpinnerDonGia().setEnabled(false);
		dh.getSpinnerSoLuong().setEnabled(false);
		dh.getBtnLamMoi().setEnabled(true);
		dh.getMnOption().setText("Đặt hàng");
		dh.getTextFieldTim().setEnabled(true);
		if (Authorization.valueOf(Program.mGroup) != Authorization.CONGTY) {
			dh.getBtnThem().setEnabled(true);
			if (dh.getTable().getRowCount() > 0 && dh.getTextFieldMaNV().getText().equals(Program.username)) {
				dh.getBtnXoa().setEnabled(true);
				dh.getBtnKhoOption().setEnabled(true);
				dh.getTextFieldNCC().setEditable(true);
				dh.getBtnGhi().setEnabled(true);
			} else {
				dh.getBtnXoa().setEnabled(false);
				dh.getBtnGhi().setEnabled(false);
			}
		} else {
			dh.getComboBox().setEnabled(true);
		}
	}

	private void initCTDatHang() {
		if (mode == Mode.DON_DAT_HANG && dh.isSelectedCTDH()) {
			dh.getBtnHoanTac().setEnabled(true);
			undoList.push(new Object[] { mode, dh.getTable().getSelectedRow() });
		}
		mode = Mode.CTDDH;
		dh.getTable().setEnabled(true);
		dh.getTableCTDH().setEnabled(true);
		dh.getBtnKhoOption().setEnabled(false);
		dh.getTextFieldNCC().setEditable(false);
		dh.getBtnLamMoi().setEnabled(true);
		dh.getMnOption().setText("CT đặt hàng");
		if (Authorization.valueOf(Program.mGroup) != Authorization.CONGTY) {
			dh.getBtnThem().setEnabled(true);
			// có data thì mới được xóa
			if (dh.getTableCTDH().getRowCount() > 0 && dh.getTextFieldMaNV().getText().equals(Program.username)) {
				dh.getBtnVatTuOption().setEnabled(true);
				dh.getBtnXoa().setEnabled(true);
				dh.getBtnVatTuOption().setEnabled(true);
				dh.getSpinnerSoLuong().setEnabled(true);
				dh.getSpinnerDonGia().setEnabled(true);
				dh.getBtnGhi().setEnabled(true);
			} else {
				dh.getBtnXoa().setEnabled(false);
				dh.getBtnGhi().setEnabled(false);
			}

		} else {
			dh.getComboBox().setEnabled(true);
		}
	}

	private void addDatHang() {
		dh.getTextFieldTim().setEnabled(false);
		dh.getBtnHoanTac().setEnabled(true);
		dh.getBtnThem().setEnabled(false);
		dh.getBtnGhi().setEnabled(true);
		dh.getBtnLamMoi().setEnabled(false);
		dh.getBtnThoat().setEnabled(false);
		dh.getBtnXoa().setEnabled(false);
		dh.getMnOption().setEnabled(false);
		if (mode == Mode.DON_DAT_HANG) {
			row = dh.getTable().getSelectedRow();
			dh.getTextFieldMaDH().setText("");
			dh.getTextFieldMaDH().setEditable(true);
			dh.getNgayDat().setEnabled(false);
			dh.getNgayDat().setDate(new Date());
			dh.getTextFieldNCC().setEditable(true);
			dh.getTextFieldNCC().setText("");
			dh.getTextFieldMaNV().setText(Program.username);
			dh.getTextFieldTenNV().setText(Program.mHoten);
			DatHangForm.getTextFieldMaKho().setText("");
			DatHangForm.getTextFieldTenKho().setText("");
			dh.getBtnKhoOption().setEnabled(true);
			dh.getTable().getSelectionModel().removeListSelectionListener(dh.getSelectionListener());
			dh.getTable().getSelectionModel().clearSelection();
		}
		if (mode == Mode.CTDDH) {
			row = dh.getTableCTDH().getSelectedRow();
			DatHangForm.getTextFieldMaVT().setText("");
			DatHangForm.getTextFieldTenVT().setText("");
			dh.getBtnVatTuOption().setEnabled(true);
			dh.getSpinnerDonGia().setEnabled(true);
			dh.getSpinnerSoLuong().setEnabled(true);
			dh.getSpinnerSoLuong().setValue(0);
			dh.getSpinnerDonGia().setValue(0);

			dh.getTableCTDH().getSelectionModel().removeListSelectionListener(dh.getSelectionCTDHListener());
			dh.getTableCTDH().getSelectionModel().clearSelection();
			dh.getTableCTDH().setEnabled(false);
		}
		dh.getTable().setEnabled(false);
	}

	private void btnUndo() {
//		hoan tac su kien click btnthem
		if (!dh.getBtnThem().isEnabled() && Authorization.valueOf(Program.mGroup) != Authorization.CONGTY) {
			dh.getTextFieldMaDH().setEditable(false);
			dh.getNgayDat().setEnabled(false);
			dh.getTextFieldTim().setEnabled(true);
			dh.getTable().setEnabled(true);
			dh.getTableCTDH().setEnabled(true);
			dh.getBtnThem().setEnabled(true);
			dh.getBtnXoa().setEnabled(true);
			dh.getBtnLamMoi().setEnabled(true);
			dh.getBtnThoat().setEnabled(true);
			dh.getMnOption().setEnabled(true);

//			trở về dòng được select hiện tại
			if (mode == Mode.DON_DAT_HANG) {
				dh.getTable().getSelectionModel().addListSelectionListener(dh.getSelectionListener());
				dh.getTable().getSelectionModel().setSelectionInterval(row, row);
			}
			if (mode == Mode.CTDDH) {
				dh.getTableCTDH().getSelectionModel().addListSelectionListener(dh.getSelectionCTDHListener());
				dh.getTableCTDH().getSelectionModel().setSelectionInterval(row, row);
			}
			if (undoList.empty()) {
				dh.getBtnHoanTac().setEnabled(false);
			}
			return;
		}
		Object[] st = undoList.pop();
		Integer preRow1 = Integer.valueOf(st[1].toString());
		if (st[0] == Mode.DON_DAT_HANG) {
			initDatHang();
			dh.getMntmDatHang().doClick();
			dh.getMntmDatHang().setSelected(true);
			dh.getTable().getSelectionModel().setSelectionInterval(preRow1, preRow1);
			if (undoList.empty()) {
				dh.getBtnHoanTac().setEnabled(false);
			}
			return;
		}
		if (st[0] == Mode.CTDDH) {
			Integer preRow2 = Integer.valueOf(st[2].toString());
			initCTDatHang();
			dh.getMntmCTDH().doClick();
			dh.getMntmCTDH().setSelected(true);
			dh.getTable().getSelectionModel().setSelectionInterval(preRow2, preRow2);
			dh.getTableCTDH().getSelectionModel().setSelectionInterval(preRow1, preRow1);
			if (undoList.empty()) {
				dh.getBtnHoanTac().setEnabled(false);
			}
			return;
		}

		String sqlUndo = "";

		try {
			sqlUndo = st[0].toString();
			preRow1 = Integer.valueOf(st[1].toString());
			if (!sqlUndo.equals("")) {
				System.out.println(sqlUndo);
				Program.ExecSqlDML(sqlUndo);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi, undo không thành công!" + e.getMessage(), "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		reFreshData();
		ìf (mode == Mode.DON_DAT_HANG) {
			if (preRow1 <= dh.getTable().getRowCount() - 1) {
				dh.getTable().getSelectionModel().setSelectionInterval(preRow1, preRow1);
			}
		}else {
			if (preRow1 <= dh.getTableCTDH().getRowCount() - 1) {
				Integer preRow2 = Integer.valueOf(st[2].toString());
				dh.getTableCTDH().getSelectionModel().setSelectionInterval(preRow1, preRow1);
				dh.getTable().getSelectionModel().setSelectionInterval(preRow2, preRow2);
			}
		}
		

		if (undoList.empty()) {
			dh.getBtnHoanTac().setEnabled(false);
			return;
		}
	}

	private boolean validateObject(IValidation object) {
		boolean isValid = object.validate();
		if (mode == Mode.DON_DAT_HANG) {
			switch (DatHangModel.validateError) {
			case ERROR_MASODDH:
				dh.getTextFieldMaDH().requestFocusInWindow();
				break;
			case ERROR_NGAY:
				dh.getNgayDat().requestFocusInWindow();
				break;
			case ERROR_NHACC:
				dh.getTextFieldNCC().requestFocusInWindow();
				break;
			default:
				break;
			}
		}
		if (mode == Mode.CTDDH) {
			switch (CTDDHModel.validateError) {
			case ERROR_MAVT:
				DatHangForm.getTextFieldMaVT().requestFocusInWindow();
				break;
			case ERROR_SOLUONG:
				dh.getSpinnerSoLuong().requestFocusInWindow();
				break;
			case ERROR_DONGIA:
				dh.getSpinnerDonGia().requestFocusInWindow();
				break;
			default:
				break;
			}
		}

		return isValid;
	}

	private void btnGhiListener() {
		int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn ghi dữ liệu vào bảng không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (reply == JOptionPane.YES_OPTION) {
			if (mode == Mode.DON_DAT_HANG) {
				String maDDH = dh.getTextFieldMaDH().getText().trim();
				Integer manv = Integer.parseInt(dh.getTextFieldMaNV().getText());
				String nhacc = dh.getTextFieldNCC().getText();
				String makho = DatHangForm.getTextFieldMaKho().getText();
				java.sql.Date ngay = new java.sql.Date(dh.getNgayDat().getDate().getTime());
				dhModel.setMaSoDDH(maDDH);
				dhModel.setNgay(ngay);
				dhModel.setManv(manv);
				dhModel.setMakho(makho);
				dhModel.setNhaCC(nhacc);
				if (validateObject(dhModel)) {
					if (!dh.getBtnThem().isEnabled()) {
						addDataToDB();
					} else {
						upDateDataToDB();
					}
				}
			}
			if (mode == Mode.CTDDH) {
				String maDDH = dh.getTextFieldMaDH().getText();
				String mavt = DatHangForm.getTextFieldMaVT().getText();
				Integer soLuong = (Integer) dh.getSpinnerSoLuong().getValue();
				Float donGia = Float.valueOf(dh.getSpinnerDonGia().getValue().toString());
				ctdhModel.setMavt(mavt);
				ctdhModel.setSoLuong(soLuong);
				ctdhModel.setDonGia(donGia);
				ctdhModel.setMaSoDDH(maDDH);
				if (validateObject(ctdhModel)) {
					if (!dh.getBtnThem().isEnabled()) {
						addDataToDB();
					} else {
						upDateDataToDB();
					}
				}
			}

		}
	}

	private void addDataToDB() {
		String sql = "";
		int res = 0;
		if (mode == Mode.DON_DAT_HANG) {
			// Execute query
			sql = "{? = call dbo.sp_TraCuu_MADDH(?)}";
			res = 0;
			try {
				res = Program.ExecSqlNoQuery(sql, dhModel.getMaSoDDH());
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi kiểm tra đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// execute return 1, don da ton tai
			if (res == 1) {
				JOptionPane.showMessageDialog(null,
						"Đã tồn tại mã đơn đặt hàng " + dhModel.getMaSoDDH() + " này, vui lòng nhập lại", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				Object[] newRow = { dhModel.getMaSoDDH(), Formatter.formatterDate(dhModel.getNgay()),
						dhModel.getNhaCC(), dh.getTextFieldTenNV().getText(),
						DatHangForm.getTextFieldTenKho().getText() };
				dh.getModel().addRow(newRow);
				Map<String, Object> values = new HashMap<>();
				values.put("maNhanVien", dhModel.getManv());
				values.put("maKho", dhModel.getMakho());
				values.put("tenNhanVien", dh.getTextFieldTenNV().getText());
				values.put("tenKho", DatHangForm.getTextFieldTenKho().getText());
				dh.getMaNhanVienKho().put(dhModel.getMaSoDDH(), values);
				// insert 1 item into list
				dh.getList().add(dhModel);
				dh.getDao().insert(dhModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi thêm kho!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				dh.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

			dh.getTable().getSelectionModel().addListSelectionListener(dh.getSelectionListener());
			dh.getTable().getSelectionModel().setSelectionInterval(dh.getTable().getRowCount() - 1,
					dh.getTable().getRowCount() - 1);
			dh.getTextFieldMaDH().setEditable(false);
			// Luu truy van de hoan tac yeu cau them
			String sqlUndo;
			sqlUndo = "DELETE FROM DatHang WHERE MasoDDH = '" + dhModel.getMaSoDDH() + "'";
			undoList.push(new Object[] { sqlUndo, row });
			row = dh.getTable().getRowCount() - 1;
		}
		if (mode == Mode.CTDDH) {
			if (checkPhieuNhap(ctdhModel.getMaSoDDH())) {
				JOptionPane.showMessageDialog(null,
						"Không thể thêm vật tư vào đơn đặt hàng vì đơn hàng đã lập phiếu nhập.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			try {
				Object[] newRow = { ctdhModel.getMaSoDDH(), DatHangForm.getTextFieldTenVT().getText(),
						ctdhModel.getSoLuong(), Formatter.formatObjecttoMoney(ctdhModel.getDonGia()),
						Formatter.formatObjecttoMoney(ctdhModel.getDonGia() * ctdhModel.getSoLuong()) };
				dh.getCtdhModel().addRow(newRow);
				dh.getMaVT().put(dh.getTableCTDH().getRowCount() - 1, ctdhModel.getMavt());
				dh.getCtdhDao().insert(ctdhModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi thêm chi tiết đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				dh.getTableCTDH().getSelectionModel().setSelectionInterval(row, row);
				return;
			}
			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			dh.getTableCTDH().setEnabled(true);
			dh.getTableCTDH().getSelectionModel().addListSelectionListener(dh.getSelectionCTDHListener());
			dh.getTableCTDH().getSelectionModel().setSelectionInterval(dh.getTableCTDH().getRowCount() - 1,
					dh.getTableCTDH().getRowCount() - 1);
			// insert 1 item into list
			dh.getCtdhList().add(ctdhModel);

			String sqlUndo;
			sqlUndo = "DELETE FROM CTDDH WHERE MasoDDH = '" + ctdhModel.getMaSoDDH() + "'AND MAVT = '"
					+ ctdhModel.getMavt() + "'";
			undoList.push(new Object[] { sqlUndo, row, dh.getTable().getSelectedRow() });
			row = dh.getTableCTDH().getRowCount() - 1;
		}
		dh.getTable().setEnabled(true);
		dh.getBtnThem().setEnabled(true);
		dh.getBtnXoa().setEnabled(true);
		dh.getBtnLamMoi().setEnabled(true);
		dh.getBtnThoat().setEnabled(true);
		dh.getBtnHoanTac().setEnabled(true);
		dh.getTextFieldTim().setEnabled(true);
		dh.getMnOption().setEnabled(true);
	}

	private void upDateDataToDB() {
		if (checkPhieuNhap(dhModel.getMaSoDDH()) || checkPhieuNhap(ctdhModel.getMaSoDDH())) {
			JOptionPane.showMessageDialog(null, "Không thể sửa đơn đặt hàng này vì đơn hàng đã lập phiếu nhập.",
					"Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (mode == Mode.DON_DAT_HANG) {
			row = dh.getTable().getSelectedRow();
			String nhacc = dh.getTable().getValueAt(dh.getTable().getSelectedRow(), 2).toString();
			String makho = dh.getTable().getValueAt(dh.getTable().getSelectedRow(), 3).toString();

			try {
				dh.getTable().setValueAt(dhModel.getNhaCC(), dh.getTable().getSelectedRow(), 2);
				dh.getTable().setValueAt(DatHangForm.getTextFieldTenKho().getText(), dh.getTable().getSelectedRow(), 4);
				// log info update
				System.out.println(
						"update: " + dhModel.getMaSoDDH() + " " + dhModel.getNhaCC() + " " + dhModel.getMakho());
				// --------
				dh.getMaNhanVienKho().get(dhModel.getMaSoDDH()).put("maKho", dhModel.getMakho());
				dh.getMaNhanVienKho().get(dhModel.getMaSoDDH()).put("tenKho",
						DatHangForm.getTextFieldTenKho().getText());
				dh.getDao().update(dhModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi update đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				dh.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			dh.getBtnHoanTac().setEnabled(true);
			dh.getTable().getSelectionModel().setSelectionInterval(row, row);
			// update item for list
			dh.getList().set(row, dhModel);

			// Luu truy van de hoan tac yeu cau update
			String sqlUndo;
			sqlUndo = "UPDATE DatHang SET NhaCC = '" + nhacc + "', " + "MAKHO = '" + makho + "' " + "WHERE MasoDDH = '"
					+ dhModel.getMaSoDDH() + "'";
			undoList.push(new Object[] { sqlUndo, row });
		}
		if (mode == Mode.CTDDH) {
			row = dh.getTableCTDH().getSelectedRow();
			String mavt = dh.getMaVT().get(dh.getTableCTDH().getSelectedRow());
			Integer soLuong = (Integer) dh.getTableCTDH().getValueAt(dh.getTableCTDH().getSelectedRow(), 2);
			Float donGia = Formatter
					.formatMoneyToFloat(dh.getTableCTDH().getValueAt(dh.getTableCTDH().getSelectedRow(), 3));

			try {
				dh.getTableCTDH().setValueAt(DatHangForm.getTextFieldTenVT().getText(),
						dh.getTableCTDH().getSelectedRow(), 1);
				dh.getTableCTDH().setValueAt(ctdhModel.getSoLuong(), dh.getTableCTDH().getSelectedRow(), 2);
				dh.getTableCTDH().setValueAt(Formatter.formatObjecttoMoney(ctdhModel.getDonGia()),
						dh.getTableCTDH().getSelectedRow(), 3);
				dh.getTableCTDH().setValueAt(
						Formatter.formatObjecttoMoney(ctdhModel.getSoLuong() * ctdhModel.getDonGia()),
						dh.getTableCTDH().getSelectedRow(), 4);
				dh.getCtdhDao().update(ctdhModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi update chi tiết đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				dh.getTableCTDH().getSelectionModel().setSelectionInterval(row, row);
				return;
			}
			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			dh.getMaVT().put(dh.getTableCTDH().getSelectedRow(), ctdhModel.getMavt());
			dh.getBtnHoanTac().setEnabled(true);
			dh.getTableCTDH().getSelectionModel().setSelectionInterval(row, row);
			// update item for list
			dh.getCtdhList().set(row, ctdhModel);
			// Luu truy van de hoan tac yeu cau update
			String sqlUndo;
			sqlUndo = "UPDATE CTDDH SET MAVT = '" + mavt + "', " + "SOLUONG = '" + soLuong + "', " + "DONGIA = '"
					+ donGia + "' " + "WHERE MasoDDH = '" + ctdhModel.getMaSoDDH() + "' AND MAVT = '"
					+ ctdhModel.getMavt() + "'";
			undoList.push(new Object[] { sqlUndo, row, dh.getTable().getSelectedRow() });
		}

	}

	private void reFreshData() {
		if (mode == Mode.DON_DAT_HANG) {
			dh.getTable().getSelectionModel().removeListSelectionListener(dh.getSelectionListener());
			dh.getModel().setRowCount(0);
			dh.loadDataIntoTable();
			dh.getTable().getSelectionModel().addListSelectionListener(dh.getSelectionListener());
			dh.getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		if (mode == Mode.CTDDH) {
			dh.getTableCTDH().getSelectionModel().removeListSelectionListener(dh.getSelectionCTDHListener());
			dh.getCtdhModel().setRowCount(0);
			dh.loadDataCTDDH();
		}

	}

	private boolean checkPhieuNhap(String masoDDH) {
		String sql = "SELECT DISTINCT MasoDDH FROM PhieuNhap WHERE MasoDDH = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, masoDDH);

		try {
			Program.myReader.next();
			if (Program.myReader.getRow() > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	private void deleteData() {
		if (JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa dữ liệu này không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
			return;
		}
		String maDDH = dh.getTextFieldMaDH().getText().trim();
//		check exist
		if (checkPhieuNhap(dhModel.getMaSoDDH())) {
			JOptionPane.showMessageDialog(null, "Không thể xóa đơn đặt hàng này vì đã lập phiếu nhập.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (mode == Mode.DON_DAT_HANG) {
//			chưa chọn hàng thì không xóa được
			if (dh.getTable().getSelectedRow() == -1)
				return;
			if (dh.getTable().getSelectedRow() != row) {
				undoList.push(new Object[] { "", row });
			}
//			set dữ liệu của row đó

			Integer manv = Integer.parseInt(dh.getTextFieldMaNV().getText());
			String nhacc = dh.getTextFieldNCC().getText();
			String makho = DatHangForm.getTextFieldMaKho().getText();
			java.sql.Date ngay = new java.sql.Date(dh.getNgayDat().getDate().getTime());
			dhModel.setMaSoDDH(maDDH);
			dhModel.setNgay(ngay);
			dhModel.setManv(manv);
			dhModel.setMakho(makho);
			dhModel.setNhaCC(nhacc);

			row = dh.getTable().getSelectedRow();
			try {
				dh.getTable().getSelectionModel().removeListSelectionListener(dh.getSelectionListener());
				dh.getModel().removeRow(dh.getTable().getSelectedRow());
				List<Object> values = new ArrayList<Object>();
				values.add(dhModel.getManv());
				values.add(dhModel.getMakho());
				dh.getMaNhanVienKho().remove(dhModel.getMaSoDDH(), values);
				dh.getDao().delete(dhModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi xóa đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				dh.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			// listener selection row
			dh.getTable().getSelectionModel().addListSelectionListener(dh.getSelectionListener());
			if (dh.getTable().getRowCount() > 0) {
				if (row == 0) {
					dh.getTable().getSelectionModel().setSelectionInterval(row + 1, row + 1);
				} else {
					dh.getTable().getSelectionModel().setSelectionInterval(row - 1, row - 1);
				}
			}
			dh.getBtnHoanTac().setEnabled(true);
			// delete item in list
			dh.getList().remove(row);

			String sqlUndo = "INSERT INTO DatHang (MasoDDH, NGAY, NhaCC, MANV, MAKHO) VALUES ('" + dhModel.getMaSoDDH()
					+ "', '" + dhModel.getNgay() + "', '" + dhModel.getNhaCC() + "', '" + dhModel.getManv() + "', '"
					+ dhModel.getMakho() + "')";
			undoList.push(new Object[] { sqlUndo, row });

			if (dh.getTable().getRowCount() == 0) {
				dh.getBtnXoa().setEnabled(false);
			}
		}
		if (mode == Mode.CTDDH) {
			if (dh.getTableCTDH().getSelectedRow() == -1)
				return;
			if (dh.getTableCTDH().getSelectedRow() != row) {
				undoList.push(new Object[] { "", row });
			}
			String maVT = dh.getMaVT().get(dh.getTableCTDH().getSelectedRow());
			Integer soLuong = (Integer) dh.getTableCTDH().getValueAt(dh.getTableCTDH().getSelectedRow(), 2);
			Float donGia = Formatter
					.formatMoneyToFloat(dh.getTableCTDH().getValueAt(dh.getTableCTDH().getSelectedRow(), 3));
			ctdhModel.setMaSoDDH(maDDH);
			ctdhModel.setMavt(maVT);
			ctdhModel.setSoLuong(soLuong);
			ctdhModel.setDonGia(donGia);

			row = dh.getTableCTDH().getSelectedRow();
			try {
				dh.getTableCTDH().getSelectionModel().removeListSelectionListener(dh.getSelectionCTDHListener());
				dh.getCtdhModel().removeRow(dh.getTableCTDH().getSelectedRow());
				dh.getMaVT().remove(dh.getTableCTDH().getSelectedRow(), maVT);
				dh.getCtdhDao().delete(ctdhModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi xóa chi tiết đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				dh.getTableCTDH().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			// listener selection row
			dh.getTableCTDH().getSelectionModel().addListSelectionListener(dh.getSelectionCTDHListener());
			if (dh.getTableCTDH().getRowCount() > 0) {
				if (row == 0) {
					dh.getTableCTDH().getSelectionModel().setSelectionInterval(row + 1, row + 1);
				} else {
					dh.getTableCTDH().getSelectionModel().setSelectionInterval(row - 1, row - 1);
				}
			}
			dh.getBtnHoanTac().setEnabled(true);
			dh.getCtdhList().remove(row);

			String sqlUndo = "INSERT INTO CTDDH (MasoDDH, MAVT, SOLUONG, DONGIA) VALUES ('" + ctdhModel.getMaSoDDH()
					+ "', '" + ctdhModel.getMavt() + "', '" + ctdhModel.getSoLuong() + "', '" + ctdhModel.getDonGia()
					+ "')";
			undoList.push(new Object[] { sqlUndo, row, dh.getTable().getSelectedRow() });

			if (dh.getTableCTDH().getRowCount() == 0) {
				dh.getBtnXoa().setEnabled(false);
			}
		}

	}

	private void openKhoForm() {
		if (!KhoOptionForm.isVisible) {
			dh.getKhoOptionForm().setVisible(true);
		}
	}

	private void openVatTuForm() {
		if (!VatTuOptionForm.isVisible) {
			dh.getVtOptionForm().setVisible(true);
		}
	}

	@Override
	public void search() {
		String input = dh.getTextFieldTim().getText().trim().toLowerCase();
		dh.getTable().getSelectionModel().removeListSelectionListener(dh.getSelectionListener());
		dh.getModel().setRowCount(0);

		for (DatHangModel d : dh.getList()) {
			if (dh.getMaNhanVienKho().get(d.getMaSoDDH()).get("tenNhanVien").toString().contains(input)
					|| dh.getMaNhanVienKho().get(d.getMaSoDDH()).get("tenKho").toString().toLowerCase().contains(input)
					|| d.getMaSoDDH().toLowerCase().contains(input) || d.getNhaCC().toLowerCase().contains(input)
					|| Formatter.formatterDate(d.getNgay()).toString().contains(input)) {

				Object[] rowData = { d.getMaSoDDH(), Formatter.formatterDate(d.getNgay()), d.getNhaCC(),
						dh.getMaNhanVienKho().get(d.getMaSoDDH()).get("tenNhanVien"),
						dh.getMaNhanVienKho().get(d.getMaSoDDH()).get("tenKho") };
				dh.getModel().addRow(rowData);
			}
		}
		dh.getTable().getSelectionModel().addListSelectionListener(dh.getSelectionListener());
		if (dh.getTable().getRowCount() > 0) {
			dh.getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}
}
