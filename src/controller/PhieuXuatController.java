package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JOptionPane;

import common.method.Formatter;
import common.method.ISearcher;
import main.Program;
import model.PhieuXuatModel;
import model.CTPXModel;
import views.PhieuXuatForm;

public class PhieuXuatController implements ISearcher {
	private PhieuXuatForm px;
	private PhieuXuatModel phieuXuatModel;
	private CTPXModel ctpxModel;
	private Stack<Object[]> undoList;
	private int row;

	private enum Mode {
		PHIEUXUAT, CTPX;
	}

	private Mode mode;

	public PhieuXuatController(PhieuXuatForm px) {
		this.px = px;
		phieuXuatModel = new PhieuXuatModel();
		ctpxModel = new CTPXModel();
		undoList = new Stack<>();
		row = 0;

	}

	public void initController() {
		if (Program.mGroup.equals("CONGTY")) {
			px.getComboBox().setEnabled(true);
		}
		px.getMntmPhieuXuat().addActionListener(l -> {
			if (!px.isSelectedPX()) {
				px.setSelectedPX(true);
				px.setSelectedCTPX(false);
				initPhieuXuat();
			}
		});

		px.getMntmCTPX().addActionListener(l -> {
			if (!px.isSelectedCTPX()) {
				px.setSelectedCTPX(true);
				px.setSelectedPX(false);
				initCTPX();
			}
		});

		px.getBtnThem().addActionListener(l -> addPhieuXuatCTPX());
		px.getBtnLamMoi().addActionListener(l -> reFreshData());
		px.getBtnHoanTac().addActionListener(l -> btnUndo());
		px.getBtnGhi().addActionListener(l -> btnGhi());
		px.getBtnXoa().addActionListener(l -> deleteData());

		px.getBtnKhoOption().addActionListener(l -> openKhoForm());
		px.getBtnVatTuOption().addActionListener(l -> openVatTuForm());

		px.getTextFieldTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				search();
			}
		});
	}

	private void initPhieuXuat() {
		if (mode == Mode.CTPX && px.isSelectedPX()) {
			px.getBtnHoanTac().setEnabled(true);
			undoList.push(new Object[] { mode, px.getTableCTPX().getSelectedRow() });
		}
		mode = Mode.PHIEUXUAT;
		px.getTable().setEnabled(true);
		px.getTableCTPX().setEnabled(true);
		px.getBtnVatTuOption().setEnabled(false);
		px.getSpinnerSoLuong().setEnabled(false);
		px.getSpinnerDonGia().setEnabled(false);

		px.getBtnLamMoi().setEnabled(true);
		px.getTextFieldTim().setEditable(true);

		px.getBtnXoa().setEnabled(false);
		px.getBtnGhi().setEnabled(false);

		if (!Program.mGroup.equals("CONGTY")) {
			px.getBtnThem().setEnabled(true);

			if (px.getTable().getRowCount() > 0 && Program.username.equals(px.getTextFieldMaNV().getText())) {
				px.getBtnXoa().setEnabled(true);
				px.getBtnKhoOption().setEnabled(true);
				px.getTextFieldTenKH().setEditable(true);
				px.getBtnGhi().setEnabled(true);

			}

		} else {
			px.getComboBox().setEnabled(true);

		}
		px.getMnOption().setText("Phiếu Xuất");
	}

	private void initCTPX() {
		if (mode == Mode.PHIEUXUAT && px.isSelectedCTPX()) {
			px.getBtnHoanTac().setEnabled(true);
			undoList.push(new Object[] { mode, px.getTable().getSelectedRow() });
		}

		mode = Mode.CTPX;
		if (px.getTable().getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "Chưa chọn phiếu xuất");
			return;
		}

		px.getTable().setEnabled(false);
		px.getTableCTPX().setEnabled(true);
		px.getBtnKhoOption().setEnabled(false);
		px.getTextFieldTenKH().setEditable(false);

		px.getBtnLamMoi().setEnabled(true);
		px.getBtnGhi().setEnabled(false);
		px.getBtnVatTuOption().setEnabled(false);
		if (!Program.mGroup.equals("CONGTY")) {
			px.getBtnThem().setEnabled(true);
			if (px.getTableCTPX().getRowCount() > 0 && Program.username.equals(px.getTextFieldMaNV().getText())) {
//				px.getBtnVatTuOption().setEnabled(true);
				px.getBtnXoa().setEnabled(true);
				px.getSpinnerSoLuong().setEnabled(true);
				px.getSpinnerDonGia().setEnabled(true);
				px.getBtnGhi().setEnabled(true);
			}
		} else {
			px.getComboBox().setEnabled(true);
		}
		px.getMnOption().setText("Chi Tiết Phiếu Xuất");
	}

	private void addPhieuXuatCTPX() {
		px.getTextFieldTim().setEnabled(false);
		px.getBtnHoanTac().setEnabled(true);
		px.getBtnThem().setEnabled(false);
		px.getBtnGhi().setEnabled(true);
		px.getBtnLamMoi().setEnabled(false);
		px.getBtnThoat().setEnabled(false);
		px.getBtnXoa().setEnabled(false);
		px.getMnOption().setEnabled(false);
		if (mode == Mode.PHIEUXUAT) {
			row = px.getTable().getSelectedRow();
			px.getTextFieldMaPX().setEditable(true);
			px.getTextFieldMaPX().setText("");
			Date currentDate = Calendar.getInstance().getTime();
			px.getNgay().setDate(currentDate);
			px.getNgay().setEnabled(false);

			px.getTextFieldTenKH().setEditable(true);
			px.getTextFieldTenKH().setText("");
			px.getTextFieldMaNV().setText(Program.username);
			px.getTextFieldTenNV().setText(Program.mHoten);

			px.getBtnKhoOption().setEnabled(true);
			PhieuXuatForm.getTextFieldMaKho().setText("");
			PhieuXuatForm.getTextFieldTenKho().setText("");

			px.getTable().getSelectionModel().removeListSelectionListener(px.getSelectionListener());
			px.getTable().getSelectionModel().clearSelection();

		}

		if (mode == Mode.CTPX) {

			if (!Program.username.equals(px.getTextFieldMaNV().getText())) {
				JOptionPane.showMessageDialog(null,
						"Không thể thêm chi tiết phiếu xuất trên phiếu không phải do mình tạo!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			row = px.getTableCTPX().getSelectedRow();
			PhieuXuatForm.getTextFieldMaVT().setText("");
			PhieuXuatForm.getTextFieldTenVT().setText("");
			px.getBtnVatTuOption().setEnabled(true);

			px.getSpinnerDonGia().setEnabled(true);
			px.getSpinnerDonGia().setValue(0);

			px.getSpinnerSoLuong().setEnabled(true);
			px.getSpinnerSoLuong().setValue(1);

			px.getTableCTPX().getSelectionModel().removeListSelectionListener(px.getSelectionListenerCTPX());
			px.getTableCTPX().getSelectionModel().clearSelection();
			px.getTableCTPX().setEnabled(false);

		}

		px.getTable().setEnabled(false);

	}

	private void btnUndo() {
		/* hoàn tác sự kiện click btnthem */
		if (!px.getBtnThem().isEnabled() && !Program.mGroup.equals("CONGTY")) {
			px.getTextFieldMaPX().setEditable(false);
			px.getNgay().setEnabled(false);
			px.getTextFieldTim().setEnabled(true);
			px.getTable().setEnabled(true);
			px.getTableCTPX().setEnabled(true);
			px.getBtnThem().setEnabled(true);
			px.getBtnXoa().setEnabled(true);
			px.getBtnLamMoi().setEnabled(true);
			px.getBtnThoat().setEnabled(true);
			px.getMnOption().setEnabled(true);

			if (Program.mGroup.equals("CONGTY")) {
				px.getBtnThem().setEnabled(false);
				px.getBtnXoa().setEnabled(false);
			}

//			trở về dòng được select hiện tại
			if (mode == Mode.PHIEUXUAT) {
				px.getTable().getSelectionModel().addListSelectionListener(px.getSelectionListener());
				px.getTable().getSelectionModel().setSelectionInterval(row, row);
			}

			if (mode == Mode.CTPX) {
				px.getTableCTPX().getSelectionModel().addListSelectionListener(px.getSelectionListenerCTPX());
				px.getTableCTPX().getSelectionModel().setSelectionInterval(row, row);
			}
			if (undoList.empty()) {
				px.getBtnHoanTac().setEnabled(false);
			}
			return;
		}

		Object[] st = undoList.pop();
		Integer preRow = Integer.valueOf(st[1].toString());
		if (st[0] == Mode.PHIEUXUAT) {
			initPhieuXuat();
			px.getMntmPhieuXuat().doClick();
			px.getMntmPhieuXuat().setSelected(true);
			px.getTable().getSelectionModel().setSelectionInterval(preRow, preRow);
			if (undoList.empty()) {
				px.getBtnHoanTac().setEnabled(false);
			}
			return;
		}
		if (st[0] == Mode.CTPX) {
			initCTPX();
			px.getMntmCTPX().doClick();
			px.getMntmCTPX().setSelected(true);
			px.getTableCTPX().getSelectionModel().setSelectionInterval(preRow, preRow);
			if (undoList.empty()) {
				px.getBtnHoanTac().setEnabled(false);
			}
			return;
		}

		String sqlUndo = "";
		try {
			sqlUndo = st[0].toString();
			preRow = Integer.valueOf(st[1].toString());
			/*
			 * if (preRow == -1) { preRow = 0; phần này để khắc phục lỗi preRow = -1 khi
			 * preRow chưa được gán giá trị }
			 */
			if (!sqlUndo.equals("")) {
				System.out.println("sqlUndo: " + sqlUndo);
				System.out.println("preRow: " + preRow);
				System.out.println("row: " + row);
				Program.ExecSqlDML(sqlUndo);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi, undo không thành công!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		reFreshData();
		if (row <= px.getTable().getRowCount() - 1) {
			px.getTable().getSelectionModel().setSelectionInterval(preRow, preRow);
		}
//		 else if (mode == Mode.CTPX && row <= px.getTable().getRowCount() - 1) {
//			px.getTableCTPX().getSelectionModel().setSelectionInterval(preRow, preRow);
//		}
		if (undoList.empty()) {
			px.getBtnHoanTac().setEnabled(false);
			return;
		}
	}

	private boolean checkInputData() {
		if (mode == Mode.PHIEUXUAT) {
			if (!Program.username.equals(px.getTextFieldMaNV().getText())) {
				JOptionPane.showMessageDialog(null, "Không thể chỉnh sửa phiếu xuất với phiếu xuất do người khác tạo.",
						"Thông báo", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			if (px.getTextFieldMaPX().getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Mã phiếu xuất không được bỏ trống.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getTextFieldMaPX().requestFocusInWindow();
				return false;
			}

			if (px.getTextFieldMaPX().getText().length() > 8) {
				JOptionPane.showMessageDialog(null, "Mã phiếu xuất không thể quá 8 kí tự.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getTextFieldMaPX().requestFocusInWindow();
				return false;
			}

			if (px.getTextFieldTenKH().getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Tên khách hàng không được bỏ trống.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getTextFieldTenKH().requestFocusInWindow();
				return false;
			}

			if (px.getTextFieldTenKH().getText().length() > 100) {
				JOptionPane.showMessageDialog(null, "Tên khách hàng không quá 100 kí tự.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getTextFieldTenKH().requestFocusInWindow();
				return false;
			}

			if (PhieuXuatForm.getTextFieldMaKho().getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Không bỏ trống mã kho.", "Thông báo", JOptionPane.WARNING_MESSAGE);
				PhieuXuatForm.getTextFieldMaKho().requestFocusInWindow();
				return false;
			}
		}

		if (mode == Mode.CTPX) {
			if (!Program.username.equals(px.getTextFieldMaNV().getText())) {
				JOptionPane.showMessageDialog(null,
						"Không thể thêm hoặc chỉnh sửa chi tiết phiếu xuất với phiếu xuất do người khác tạo.",
						"Thông báo", JOptionPane.WARNING_MESSAGE);
				return false;
			}

			if (px.getTextFieldMaPX().getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Mã phiếu xuất không được bỏ trống.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getTextFieldMaPX().requestFocusInWindow();
				return false;
			}

			if (px.getTextFieldMaPX().getText().length() > 8) {
				JOptionPane.showMessageDialog(null, "Mã phiếu xuất không thể quá 8 kí tự.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getTextFieldMaPX().requestFocusInWindow();
				return false;
			}

			if (PhieuXuatForm.getTextFieldMaVT().getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Mã vật tư không được bỏ trống.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				PhieuXuatForm.getTextFieldMaVT().requestFocusInWindow();
				return false;
			}

			if (PhieuXuatForm.getTextFieldMaVT().getText().length() > 4) {
				JOptionPane.showMessageDialog(null, "Mã vật tư không thể quá 4 kí tự.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				PhieuXuatForm.getTextFieldMaVT().requestFocusInWindow();
				return false;
			}
			String sql = "SELECT SOLUONGTON FROM VATTU WHERE MAVT = ?";
			Program.myReader = Program.ExecSqlDataReader(sql, PhieuXuatForm.getTextFieldMaVT().getText());
			int soLuongTon = 0;
			try {
				Program.myReader.next();
				soLuongTon = Program.myReader.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// đây là số lượng được chỉnh sửa
			int soLuongTrongSpinner = Integer.valueOf(px.getSpinnerSoLuong().getValue().toString());
			if (soLuongTrongSpinner > soLuongTon && !px.getBtnThem().isEnabled()) {
				JOptionPane.showMessageDialog(null,
						"Số lượng vật tư không thể lớn hơn số lượng vật tư đang có trong kho hàng.", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				px.getSpinnerSoLuong().requestFocusInWindow();
				return false;
			}

			// đây là số lượng lưu trong csdl
			if (px.getTableCTPX().getRowCount() > 0) {
				int soLuongTrongBangCTPX = (int) px.getTableCTPX().getValueAt(px.getTableCTPX().getSelectedRow(), 2);
				if (soLuongTrongSpinner > soLuongTrongBangCTPX
						&& (soLuongTrongSpinner - soLuongTrongBangCTPX) > soLuongTon && px.getBtnThem().isEnabled()) {
					JOptionPane.showMessageDialog(null,
							"Số lượng vật tư vừa thêm không thể lớn hơn số lượng vật tư đang có trong kho hàng.",
							"Thông báo", JOptionPane.WARNING_MESSAGE);
					px.getSpinnerSoLuong().requestFocusInWindow();
					return false;
				}
			}
		}
		return true;
	}

	private void btnGhi() {
		int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn ghi dữ liệu vào bảng không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (reply == JOptionPane.YES_OPTION) {
			if (mode == Mode.PHIEUXUAT) {
				row = px.getTable().getSelectedRow();
				String mapx = px.getTextFieldMaPX().getText().trim();
				Integer manv = Integer.parseInt(px.getTextFieldMaNV().getText());
				String tenKH = px.getTextFieldTenKH().getText();
				String makho = PhieuXuatForm.getTextFieldMaKho().getText();
				java.sql.Date ngay = new java.sql.Date(px.getNgay().getDate().getTime());
				phieuXuatModel.setMapx(mapx);
				phieuXuatModel.setNgay(ngay);
				phieuXuatModel.setManv(manv);
				phieuXuatModel.setMaKho(makho);
				phieuXuatModel.setHoTenKH(tenKH);
				if (checkInputData()) {
					if (!px.getBtnThem().isEnabled()) {
						addDataToDB();
					} else {
						upDateDataToDB();
					}
				}
			}

			if (mode == Mode.CTPX) {
				row = px.getTableCTPX().getSelectedRow();
				String mapx = px.getTextFieldMaPX().getText();
				String mavt = PhieuXuatForm.getTextFieldMaVT().getText();
				Integer soLuong = (Integer) px.getSpinnerSoLuong().getValue();
				Float donGia = Float.valueOf(px.getSpinnerDonGia().getValue().toString());
				ctpxModel.setMavt(mavt);
				ctpxModel.setSoLuong(soLuong);
				ctpxModel.setDonGia(donGia);
				ctpxModel.setMapx(mapx);
				if (checkInputData()) {
					if (!px.getBtnThem().isEnabled()) {
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
		if (mode == Mode.PHIEUXUAT) {
			// Execute query
			sql = "{? = call dbo.sp_KiemTraMaPhieuXuat(?)}";
			res = 0;
			try {
				res = Program.ExecSqlNoQuery(sql, phieuXuatModel.getMapx());
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi kiểm tra phiếu xuất!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// execute return 1, phieu xuat da ton tai
			if (res == 1) {
				JOptionPane.showMessageDialog(null,
						"Đã tồn tại mã phiếu xuất " + phieuXuatModel.getMapx() + ", vui lòng nhập lại", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				try {
					Object[] newRow = { phieuXuatModel.getMapx(), Formatter.formatterDate(phieuXuatModel.getNgay()),
							phieuXuatModel.getHoTenKH().trim(), px.getNhanVienInfo().get(phieuXuatModel.getManv()),
							phieuXuatModel.getMaKho() };

					List<Object> values = new ArrayList<Object>();
					values.add(phieuXuatModel.getManv());
					values.add(phieuXuatModel.getMaKho());
					px.getMaNhanVienKho().put(phieuXuatModel.getMapx(), values);
					px.getModel().addRow(newRow);
					px.getList().add(phieuXuatModel);
					px.getDao().insert(phieuXuatModel);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Lỗi thêm phiếu xuất!\n" + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					reFreshData();
					px.getTable().getSelectionModel().setSelectionInterval(row, row);
					return;
				}
			}

			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			px.getTable().getSelectionModel().addListSelectionListener(px.getSelectionListener());
			px.getTable().getSelectionModel().setSelectionInterval(px.getTable().getRowCount() - 1,
					px.getTable().getRowCount() - 1);

//			row = px.getTable().getRowCount() - 1;
			// Luu truy van de hoan tac yeu cau them
			String sqlUndo;
			sqlUndo = "DELETE FROM PhieuXuat WHERE MAPX = '" + phieuXuatModel.getMapx() + "'";
			undoList.push(new Object[] { sqlUndo, row });
			row = px.getTable().getSelectedRow() - 1;
		}

		if (mode == Mode.CTPX) {
			try {
				Object[] newRow = { ctpxModel.getMapx(), PhieuXuatForm.getTextFieldTenVT().getText(),
						ctpxModel.getSoLuong(), Formatter.formatObjecttoMoney(ctpxModel.getDonGia()),
						Formatter.formatObjecttoMoney(ctpxModel.getDonGia() * ctpxModel.getSoLuong()) };
				px.getCtpxModel().addRow(newRow);
				px.getMaVT().put(px.getTableCTPX().getRowCount() - 1, ctpxModel.getMavt());
				px.getCtpxDao().insert(ctpxModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi thêm chi tiết phiếu xuất!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				px.getTableCTPX().getSelectionModel().setSelectionInterval(row, row);
				return;
			}
			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			px.getTableCTPX().setEnabled(true);
			px.getTableCTPX().getSelectionModel().addListSelectionListener(px.getSelectionListenerCTPX());
			px.getTableCTPX().getSelectionModel().setSelectionInterval(px.getTableCTPX().getRowCount() - 1,
					px.getTableCTPX().getRowCount() - 1);
			px.getCtpxList().add(ctpxModel);

			// Luu truy van de hoan tac yeu cau them
			String sqlUndo = "";
			sqlUndo = "DELETE FROM CTPX WHERE MAPX = '" + ctpxModel.getMapx().trim() + "' AND MAVT = '"
					+ ctpxModel.getMavt() + "'";

//			sql = "EXEC sp_CapNhatSoLuongVatTu 'EXPORT','" + ctpxModel.getMavt() + "', " + ctpxModel.getSoLuong();
//			Program.ExecSqlNonQuery(sql);
			undoList.push(new Object[] { sqlUndo, row });
			row = px.getTableCTPX().getRowCount() - 1;

		}
		px.getTable().setEnabled(true);
		px.getBtnThem().setEnabled(true);
		px.getBtnXoa().setEnabled(true);
		px.getBtnLamMoi().setEnabled(true);
		px.getBtnThoat().setEnabled(true);
		px.getBtnHoanTac().setEnabled(true);
		px.getTextFieldTim().setEnabled(true);
		px.getMnOption().setEnabled(true);
	}

	private void upDateDataToDB() {
		if (mode == Mode.PHIEUXUAT) {
			row = px.getTable().getSelectedRow();
			String tenKH = px.getTable().getValueAt(px.getTable().getSelectedRow(), 2).toString().trim();
			String maKho = px.getKhoInfo().entrySet().stream()
					.filter(entry -> px.getTable().getValueAt(px.getTable().getSelectedRow(), 4).toString()
							.equals(entry.getValue()))
					.map(Map.Entry::getKey) // Chuyển đổi từ Entry<K, V> sang K (key)
					.findFirst() // Lấy phần tử đầu tiên tìm được
					.orElse(null); // Trả về null nếu không tìm thấy

			try {
				px.getTable().setValueAt(phieuXuatModel.getHoTenKH(), px.getTable().getSelectedRow(), 2);
				px.getTable().setValueAt(PhieuXuatForm.getTextFieldTenKho().getText(), px.getTable().getSelectedRow(),
						4);
				List<Object> values = new ArrayList<Object>();
				values.add(phieuXuatModel.getManv());
				values.add(phieuXuatModel.getMaKho());
				px.getMaNhanVienKho().put(phieuXuatModel.getMapx(), values);
				px.getDao().update(phieuXuatModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi update phiếu xuất!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				px.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			px.getBtnHoanTac().setEnabled(true);
			px.getTable().getSelectionModel().setSelectionInterval(row, row);
			px.getList().set(row, phieuXuatModel);
			// Luu truy van de hoan tac yeu cau update
			String sqlUndo;
			sqlUndo = "UPDATE PhieuXuat SET HOTENKH = N'" + tenKH + "'," + "MAKHO = '" + maKho + "' " + "WHERE MAPX = '"
					+ phieuXuatModel.getMapx() + "'";
			undoList.push(new Object[] { sqlUndo, row });
		}

		else if (mode == Mode.CTPX) {
			row = px.getTableCTPX().getSelectedRow();
			System.out.println("row được chọn là: " + row);
			String mavt = px.getMaVT().get(px.getTableCTPX().getSelectedRow());
			Integer soLuong = (Integer) px.getTableCTPX().getValueAt(px.getTableCTPX().getSelectedRow(), 2);
			Float donGia = Formatter
					.formatMoneyToFloat(px.getTableCTPX().getValueAt(px.getTableCTPX().getSelectedRow(), 3));

			try {
				px.getTableCTPX().setValueAt(PhieuXuatForm.getTextFieldTenVT().getText().trim(),
						px.getTableCTPX().getSelectedRow(), 1);
				px.getTableCTPX().setValueAt(ctpxModel.getSoLuong(), px.getTableCTPX().getSelectedRow(), 2);
				px.getTableCTPX().setValueAt(Formatter.formatObjecttoMoney(ctpxModel.getDonGia()),
						px.getTableCTPX().getSelectedRow(), 3);
				px.getTableCTPX().setValueAt(
						Formatter.formatObjecttoMoney(ctpxModel.getSoLuong() * ctpxModel.getDonGia()),
						px.getTableCTPX().getSelectedRow(), 4);
				px.getMaVT().put(px.getTableCTPX().getSelectedRow(), ctpxModel.getMavt());
				px.getCtpxDao().update(ctpxModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi update chi tiết đơn đặt hàng!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				px.getTableCTPX().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			px.getBtnHoanTac().setEnabled(true);
			px.getTableCTPX().getSelectionModel().setSelectionInterval(row, row);
			System.out.println("số phần tử trong ctpxlist: " + px.getCtpxList().size());
			px.getCtpxList().set(row, ctpxModel);
			// Luu truy van de hoan tac yeu cau update
			String sqlUndo = "";
			// Không sử dụng được phieuXuatModel.getMapx() vì bị null
			sqlUndo = "UPDATE CTPX SET SOLUONG = '" + soLuong + "', " + "DONGIA = '" + donGia + "' " + "WHERE MAPX = '"
					+ px.getTable().getValueAt(px.getTable().getSelectedRow(), 0).toString().trim() + "' AND MAVT = '"
					+ ctpxModel.getMavt() + "'";
			System.out.println(sqlUndo);
			undoList.push(new Object[] { sqlUndo, row });
		}
	}

	private void deleteData() {
		if (!Program.username.equals(px.getTextFieldMaNV().getText())) {
			JOptionPane.showMessageDialog(null,
					"Không thể xóa phiếu xuất/chi tiết phiếu xuất với phiếu xuất do người khác tạo.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa dữ liệu này không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
			return;
		}
		if (mode == Mode.PHIEUXUAT) {
			/* chưa chọn hàng thì không được xóa */
			if (px.getTable().getSelectedRow() == -1) {
				return;
			}

			if (px.getTable().getSelectedRow() != row) {
				undoList.push(new Object[] { "", row });
			}
			String sql = "SELECT * FROM CTPX WHERE MAPX = ?";
			Program.myReader = Program.ExecSqlDataReader(sql, px.getTextFieldMaPX().getText());
			try {
				if (Program.myReader.next()) {
					JOptionPane.showMessageDialog(null, "Không thể xóa phiếu xuất khi đã tồn tại vật tư", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

//			set dữ liệu của row đó
			String mapx = px.getTextFieldMaPX().getText().trim();
			Integer manv = Integer.parseInt(px.getTextFieldMaNV().getText());
			String tenKH = px.getTextFieldTenKH().getText();
			String makho = PhieuXuatForm.getTextFieldMaKho().getText();
			java.sql.Date ngay = new java.sql.Date(px.getNgay().getDate().getTime());
			phieuXuatModel.setMapx(mapx);
			phieuXuatModel.setNgay(ngay);
			phieuXuatModel.setManv(manv);
			phieuXuatModel.setMaKho(makho);
			phieuXuatModel.setHoTenKH(tenKH);

			row = px.getTable().getSelectedRow();
			try {
				px.getTable().getSelectionModel().removeListSelectionListener(px.getSelectionListener());
				px.getModel().removeRow(px.getTable().getSelectedRow());
				List<Object> values = new ArrayList<Object>();
				values.add(phieuXuatModel.getManv());
				values.add(phieuXuatModel.getMaKho());
				px.getMaNhanVienKho().remove(phieuXuatModel.getMapx(), values);
//				px.getList().remove(px.getTable().getSelectedRow());
				px.getDao().delete(phieuXuatModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi xóa phiếu xuất!\n" + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
				reFreshData();
				px.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			// listener selection row
//			reFreshData();
			px.getTable().getSelectionModel().addListSelectionListener(px.getSelectionListener());
			if (px.getTable().getRowCount() > 0) {
				if (row == 0) {
					px.getTable().getSelectionModel().setSelectionInterval(row + 1, row + 1);
				} else {
					px.getTable().getSelectionModel().setSelectionInterval(row - 1, row - 1);
				}
			}
			px.getBtnHoanTac().setEnabled(true);
			px.getList().remove(row);

			String sqlUndo = "INSERT INTO PHIEUXUAT (MAPX, NGAY, HOTENKH, MANV, MAKHO) VALUES ('"
					+ phieuXuatModel.getMapx() + "', '" + phieuXuatModel.getNgay() + "', N'"
					+ phieuXuatModel.getHoTenKH() + "', '" + phieuXuatModel.getManv() + "', '"
					+ phieuXuatModel.getMaKho() + "')";
			undoList.push(new Object[] { sqlUndo, row });

			if (px.getTable().getRowCount() == 0) {
				px.getBtnXoa().setEnabled(false);
			}

		}

		if (mode == Mode.CTPX) {
			if (px.getTable().getSelectedRow() == -1) {
				return;
			}

			if (px.getTableCTPX().getSelectedRow() != row) {
				undoList.push(new Object[] { "", row });
			}
//			set dữ liệu của row đó
			String mapx = px.getTableCTPX().getValueAt(px.getTableCTPX().getSelectedRow(), 0).toString();
			String mavt = px.getMaVT().get(px.getTableCTPX().getSelectedRow());
			Integer soLuong = (Integer) px.getTableCTPX().getValueAt(px.getTableCTPX().getSelectedRow(), 2);
			Float dongia = Formatter
					.formatMoneyToFloat(px.getTableCTPX().getValueAt(px.getTableCTPX().getSelectedRow(), 3).toString());
//			java.sql.Date ngay = new java.sql.Date(px.getNgay().getDate().getTime());
			ctpxModel.setMapx(mapx);
			ctpxModel.setMavt(mavt);
			ctpxModel.setSoLuong(soLuong);
			ctpxModel.setDonGia(dongia);

			row = px.getTableCTPX().getSelectedRow();
			try {
				px.getTableCTPX().getSelectionModel().removeListSelectionListener(px.getSelectionListenerCTPX());
				px.getCtpxModel().removeRow(px.getTableCTPX().getSelectedRow());
				px.getMaVT().remove(px.getTableCTPX().getSelectedRow(), mavt);
				px.getCtpxDao().delete(ctpxModel);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi xóa chi tiết phiếu xuất!", "Error", JOptionPane.ERROR_MESSAGE);
				reFreshData();
				px.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			JOptionPane.showMessageDialog(null, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			// listener selection row
			px.getTableCTPX().getSelectionModel().addListSelectionListener(px.getSelectionListenerCTPX());
			if (px.getTableCTPX().getRowCount() > 0) {
				if (row == 0) {
					px.getTableCTPX().getSelectionModel().setSelectionInterval(row + 1, row + 1);
				} else {
					px.getTableCTPX().getSelectionModel().setSelectionInterval(row - 1, row - 1);
				}
			}
			px.getBtnHoanTac().setEnabled(true);
			px.getCtpxList().remove(row);

			String sqlUndo = "INSERT INTO CTPX (MAPX, MAVT, SOLUONG, DONGIA) VALUES ('" + ctpxModel.getMapx().trim()
					+ "', '" + ctpxModel.getMavt().trim() + "', '" + ctpxModel.getSoLuong() + "', '"
					+ ctpxModel.getDonGia() + "')";
			undoList.push(new Object[] { sqlUndo, row });

			if (px.getTableCTPX().getRowCount() == 0) {
				px.getBtnXoa().setEnabled(false);
			}
		}

	}

	private void reFreshData() {

		// Refresh bảng phiếu xuất
		if (mode == Mode.PHIEUXUAT) {
			px.getTable().getSelectionModel().removeListSelectionListener(px.getSelectionListener());
			px.getModel().setRowCount(0);
			px.loadDataIntoTable();
			px.getTable().getSelectionModel().addListSelectionListener(px.getSelectionListener());
			px.getTable().getSelectionModel().setSelectionInterval(0, 0);

		}

		// Refresh bảng ctpx
		if (mode == Mode.CTPX) {
			px.getTableCTPX().getSelectionModel().removeListSelectionListener(px.getSelectionListenerCTPX());
			px.getCtpxModel().setRowCount(0);
			px.loadDataIntoTableCTPX();
		}
	}

	private void openKhoForm() {
		px.getKhoOptionFormPx().setVisible(true);
	}

	private void openVatTuForm() {
		px.getVatTuOptionFormPx().setVisible(true);
	}

	@Override
	public void search() {
		String input = px.getTextFieldTim().getText().trim().toLowerCase();
		if (mode == Mode.PHIEUXUAT) {
			px.getTable().getSelectionModel().removeListSelectionListener(px.getSelectionListener());
			px.getModel().setRowCount(0);

			for (PhieuXuatModel model : px.getList()) {
				if (model.getMapx().toLowerCase().contains(input)
						|| Formatter.formatterDate(model.getNgay()).toString().contains(input)
						|| model.getHoTenKH().toLowerCase().contains(input)
						|| px.getKhoInfo().get(model.getMaKho()).toLowerCase().contains(input)
						|| px.getNhanVienInfo().get(model.getManv()).toString().toLowerCase().contains(input)) {
					Object[] rowData = { model.getMapx(), Formatter.formatterDate(model.getNgay()), model.getHoTenKH(),
							px.getNhanVienInfo().get(model.getManv()), px.getKhoInfo().get(model.getMaKho()) };
					px.getModel().addRow(rowData);
				}
			}
			px.getTable().getSelectionModel().addListSelectionListener(px.getSelectionListener());
			if (px.getTable().getRowCount() > 0) {
				px.getTable().getSelectionModel().setSelectionInterval(0, 0);
			}

		} else if (mode == Mode.CTPX) {
			px.getTableCTPX().getSelectionModel().removeListSelectionListener(px.getSelectionListenerCTPX());
			px.getCtpxModel().setRowCount(0);

			for (CTPXModel model : px.getCtpxList()) {
				if (model.getMapx().toLowerCase().contains(input)
						|| px.getVatTuInfo().get(model.getMavt()).toLowerCase().contains(input)
						|| model.getSoLuong().toString().contains(input) || model.getDonGia().toString().contains(input)
						|| String.valueOf(model.getDonGia() * model.getSoLuong()).contains(input)) {
					Object[] rowData = { model.getMapx(), px.getVatTuInfo().get(model.getMavt()), model.getSoLuong(),
							Formatter.formatObjecttoMoney(model.getDonGia()),
							Formatter.formatObjecttoMoney(model.getSoLuong() * model.getDonGia()) };
					px.getCtpxModel().addRow(rowData);
				}
			}
			px.getTableCTPX().getSelectionModel().addListSelectionListener(px.getSelectionListenerCTPX());
			if (px.getTableCTPX().getRowCount() > 0) {
				px.getTableCTPX().getSelectionModel().setSelectionInterval(0, 0);
			}
		}

	}

}
