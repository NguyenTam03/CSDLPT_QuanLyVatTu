package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import common.method.Formatter;
import main.Program;
import views.ChuyenChiNhanhForm;
import views.NhanVienForm;
import model.NhanVienModel;

public class NhanVienController {
	private Stack<String> undoList;
	private NhanVienForm NhanVienFrm;
	private NhanVienModel NhanVienModel;
	private boolean isSelectThem;
	private int rowSelected;

	public NhanVienController(NhanVienForm NhanVienFrm) {
		this.NhanVienFrm = NhanVienFrm;
		NhanVienModel = new NhanVienModel();
		undoList = new Stack<String>();
		isSelectThem = false;
		rowSelected = 0;
	}

	public void initController() {
		if (Program.mGroup.equals("CONGTY")) {
			NhanVienFrm.getBtnThem().setEnabled(false);
			NhanVienFrm.getBtnXoa().setEnabled(false);
			NhanVienFrm.getBtnGhi().setEnabled(false);
			NhanVienFrm.getBtnHoanTac().setEnabled(false);
			NhanVienFrm.getBtnLamMoi().setEnabled(false);
			NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(false);
		}
		NhanVienFrm.getBtnThem().addActionListener(e -> addNhanVien());
		NhanVienFrm.getBtnGhi().addActionListener(e -> pushDataToDB());
		NhanVienFrm.getBtnLamMoi().addActionListener(e -> refreshData());
		NhanVienFrm.getBtnHoanTac().addActionListener(e -> undoData());
		NhanVienFrm.getBtnXoa().addActionListener(e -> deleteNhanVien());
		NhanVienFrm.getBtnChuyenChiNhanh().addActionListener(e -> chuyenChiNhanh());
		NhanVienFrm.getBtnThoat().addActionListener(l -> exitNhanVien());
		autoSearchNhanVien();
	}
	private void autoSearchNhanVien() {
		NhanVienFrm.getTextFieldTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchNhanVien();
			}
		});
	}

	private void searchNhanVien() {
		String input = NhanVienFrm.getTextFieldTim().getText().trim().toLowerCase();
		NhanVienFrm.getTable().getSelectionModel().removeListSelectionListener(NhanVienFrm.getSelectionListener());
		NhanVienFrm.getModel().setRowCount(0);

		for (NhanVienModel nv : NhanVienFrm.getList()) {
			if (nv.getTen().toLowerCase().contains(input) || nv.getManv().toString().contains(input)) {
				Object[] rowData = { nv.getManv(), nv.getHo(), nv.getTen(), nv.getSoCMND(), nv.getDiaChi(),
                        nv.getNgaySinh(), Formatter.formatObjecttoMoney(nv.getLuong()), nv.getMacn(), nv.getTrangThaiXoa()};
				NhanVienFrm.getModel().addRow(rowData);
			}
		}
		NhanVienFrm.getTable().getSelectionModel().addListSelectionListener(NhanVienFrm.getSelectionListener());
		if (NhanVienFrm.getTable().getRowCount() > 0) {
			NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}
	
	private void exitNhanVien() {
		Program.frmMain.getTabbedPane_Main().removeTabAt(Program.frmMain.getTabbedPane_Main().getSelectedIndex());
		Program.frmMain.getPanel_VT().remove(NhanVienFrm);
	}

	public void addNhanVien() {
		isSelectThem = true;
		NhanVienFrm.getBtnThem().setEnabled(false);
		NhanVienFrm.getBtnXoa().setEnabled(false);
		NhanVienFrm.getBtnLamMoi().setEnabled(false);
		NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(false);
		NhanVienFrm.getBtnThoat().setEnabled(false);
		NhanVienFrm.getBtnHoanTac().setEnabled(true);

		NhanVienFrm.getTFMaNV().setEditable(true);

		rowSelected = NhanVienFrm.getTable().getSelectedRow();

		NhanVienFrm.getTFMaNV().setText("");
		NhanVienFrm.getTFHo().setText("");
		NhanVienFrm.getTFTen().setText("");
		NhanVienFrm.getTFCMND().setText("");
		NhanVienFrm.getTFDiaChi().setText("");
		NhanVienFrm.getLuong().setValue(4000000);
		NhanVienFrm.getNgaySinh().setDate(null);
		NhanVienFrm.getTFMaCN().setText(Program.macn.get(Program.mChinhanh));
		// .removeListSelectionListener() : xóa sự kiện khi click vào dòng trong table
		NhanVienFrm.getTable().getSelectionModel().removeListSelectionListener(NhanVienFrm.getSelectionListener());
		// .setSelectionModel() : xóa dòng đang chọn trong table
		NhanVienFrm.getTable().getSelectionModel().clearSelection();
		NhanVienFrm.getTable().setEnabled(false);
	}

	// push or update info into Data
	public void pushDataToDB() {
		String MaNV, Ho, Ten, CMND, DiaChi, MaCN;
		float Luong;
		java.util.Date NgaySinh = null;
		boolean TrangThaiXoa = false;
		try {
			MaNV = NhanVienFrm.getTFMaNV().getText().trim();
			Ho = NhanVienFrm.getTFHo().getText().trim();
			Ten = NhanVienFrm.getTFTen().getText().trim();
			CMND = NhanVienFrm.getTFCMND().getText().trim();
			DiaChi = NhanVienFrm.getTFDiaChi().getText().trim();
			NgaySinh = NhanVienFrm.getNgaySinh().getDate();

			Object luongObject = NhanVienFrm.getLuong().getValue();
			if (luongObject instanceof Integer) {
			    Integer intValue = (Integer) luongObject;
			    Luong= intValue.floatValue();
			} else if (luongObject instanceof Float) {
				Luong = (Float) luongObject;
			} else {
				JOptionPane.showMessageDialog(null, "Lỗi Lương", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			LocalDate ngayHienTai = LocalDate.now();
			// Tuổi phải lớn hơn 16
			if (ngayHienTai.getYear() - NgaySinh.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() < 16) {
				JOptionPane.showMessageDialog(null, "Nhân viên phải trên 16 tuổi", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			MaCN = NhanVienFrm.getTFMaCN().getText().trim();
			TrangThaiXoa = NhanVienFrm.getTrangThaiXoa().isSelected();

			if (CheckMaNV(MaNV) && isSelectThem) {
				JOptionPane.showMessageDialog(null, "Mã nhân viên đã tồn tại", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (Ho.isEmpty() || Ten.isEmpty() || CMND.isEmpty() || DiaChi.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Hãy nhập đủ thông tin", "Thông Báo", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (CMND.length() != 9 && CMND.length() != 12) {
				JOptionPane.showMessageDialog(null, "Số CMND phải có 9 hoặc 12 chữ số", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// .*\\d.* : chứa số
			if (Ho.matches(".*\\d.*") || Ten.matches(".*\\d.*")) {
				JOptionPane.showMessageDialog(null, "Họ tên không được chứa số", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// .*\D.* : chứa chữ
			if (CMND.matches(".*\\D.*")) {
				JOptionPane.showMessageDialog(null, "Số CMND không được chứa chữ", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (Luong >= 40000000) {
				JOptionPane.showMessageDialog(null, "Lương phải trên 4.000.000đ", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (checkCMND(CMND,MaNV)) {
				JOptionPane.showMessageDialog(null, "Số CMND đã tồn tại", "Thông Báo", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn ghi dữ liệu vào bảng không?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (reply == JOptionPane.YES_NO_OPTION) {
				NhanVienModel.setManv(Integer.parseInt(MaNV));
				NhanVienModel.setHo(Ho);
				NhanVienModel.setTen(Ten);
				NhanVienModel.setSoCMND(CMND);
				NhanVienModel.setDiaChi(DiaChi);
				// convert java.util.Date to java.sql.Date
				NhanVienModel.setNgaySinh(new Date(((java.util.Date) NgaySinh).getTime()));
				NhanVienModel.setLuong(Luong);
				NhanVienModel.setMacn(MaCN);
				NhanVienModel.setTrangThaiXoa(TrangThaiXoa);
				// ghi lại dòng đang chọn
				rowSelected = NhanVienFrm.getTable().getSelectedRow();
				if (isSelectThem) {
					addDataToDB(NhanVienModel);
					isSelectThem = false;
				} else {
					updateDataToDB(NhanVienModel);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "THông Báo", JOptionPane.WARNING_MESSAGE);
			NhanVienFrm.getBtnThem().setEnabled(true);
			NhanVienFrm.getBtnXoa().setEnabled(true);
			NhanVienFrm.getBtnLamMoi().setEnabled(true);
			NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(true);
			NhanVienFrm.getBtnThoat().setEnabled(true);
			NhanVienFrm.getTable().setEnabled(true);	
			refreshData();
			NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
			if (undoList.isEmpty()) {
				NhanVienFrm.getBtnHoanTac().setEnabled(false);
			}
			e.printStackTrace();
			return;
		}

	}
	// Check CMND
	public boolean checkCMND(String CMND, String MaNV) {
		String sql = "SELECT CASE WHEN ? IN (SELECT SoCMND FROM Link2.QLVT_DATHANG.dbo.NhanVien where ? <> MaNv) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, CMND , MaNV);
		try {
			Program.myReader.next();
			return Program.myReader.getBoolean(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// push info into Data
	public void addDataToDB(NhanVienModel nhanVienModel) {
		
		try {
			Object[] newRow = { nhanVienModel.getManv(), nhanVienModel.getHo(), nhanVienModel.getTen(),
					nhanVienModel.getSoCMND(), nhanVienModel.getDiaChi(), Formatter.formatterDate(nhanVienModel.getNgaySinh()),
					Formatter.formatObjecttoMoney(nhanVienModel.getLuong()), nhanVienModel.getMacn(),
					nhanVienModel.getTrangThaiXoa() };
			NhanVienFrm.getModel().addRow(newRow);
			NhanVienFrm.getDao().insert(nhanVienModel);
			NhanVienFrm.getList().add(nhanVienModel);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Lỗi thêm nhân viên!! \n "+e.getMessage(), "THông Báo", JOptionPane.WARNING_MESSAGE);
			NhanVienFrm.getBtnThem().setEnabled(true);
			NhanVienFrm.getBtnXoa().setEnabled(true);
			NhanVienFrm.getBtnLamMoi().setEnabled(true);
			NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(true);
			NhanVienFrm.getBtnThoat().setEnabled(true);
			NhanVienFrm.getTable().setEnabled(true);	
			refreshData();
			NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
			if (undoList.isEmpty()) {
				NhanVienFrm.getBtnHoanTac().setEnabled(false);
			}
			return;
		}
		
		// sqlUndo câu lệnh để hoàn tác
		String sqlUndo = "Delete from NhanVien where MaNV = '" + nhanVienModel.getManv().toString() + "'";
		undoList.push(sqlUndo);
		NhanVienFrm.getBtnThem().setEnabled(true);
		NhanVienFrm.getBtnXoa().setEnabled(true);
		NhanVienFrm.getBtnLamMoi().setEnabled(true);
		NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(true);
		NhanVienFrm.getBtnThoat().setEnabled(true);
		NhanVienFrm.getTable().setEnabled(true);
		// .getSelectionModel().addListSelectionListener() : thêm sự kiện khi click vào dòng
		// .getSelectionListener() : lắng nghe sự kiện
		NhanVienFrm.getTable().getSelectionModel().addListSelectionListener(NhanVienFrm.getSelectionListener());
		// .getSelectionModel().setSelectionInterval() : chọn dòng cuối cùng trong table
		NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(NhanVienFrm.getTable().getRowCount() - 1,
				NhanVienFrm.getTable().getRowCount() - 1);
		
		rowSelected = NhanVienFrm.getTable().getRowCount() - 1;
		JOptionPane.showConfirmDialog(null, "Ghi Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
	}

	// update info Data
	public void updateDataToDB(NhanVienModel nhanVienModel) {

		String Ho = NhanVienFrm.getTable().getValueAt(rowSelected, 1).toString();
		String Ten = NhanVienFrm.getTable().getValueAt(rowSelected, 2).toString();
		String CMND = NhanVienFrm.getTable().getValueAt(rowSelected, 3).toString();
		String DiaChi = NhanVienFrm.getTable().getValueAt(rowSelected, 4).toString();
		String NgaySinh = NhanVienFrm.getTable().getValueAt(rowSelected, 5).toString();
		String Luong = NhanVienFrm.getTable().getValueAt(rowSelected, 6).toString();
		try {
			// .setValueAt() : set giá trị mới cho dòng rowSelected
			NhanVienFrm.getTable().setValueAt(nhanVienModel.getHo(), rowSelected, 1);
			NhanVienFrm.getTable().setValueAt(nhanVienModel.getTen(), rowSelected, 2);
			NhanVienFrm.getTable().setValueAt(nhanVienModel.getSoCMND(), rowSelected, 3);
			NhanVienFrm.getTable().setValueAt(nhanVienModel.getDiaChi(), rowSelected, 4);
			NhanVienFrm.getTable().setValueAt(Formatter.formatterDate(nhanVienModel.getNgaySinh()), rowSelected, 5);
			NhanVienFrm.getTable().setValueAt(Formatter.formatObjecttoMoney(nhanVienModel.getLuong()), rowSelected, 6);
			NhanVienFrm.getDao().update(nhanVienModel);
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Lỗi cập nhật nhân viên!! \n "+e.getMessage(), "THông Báo", JOptionPane.WARNING_MESSAGE);
            refreshData();
            NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
            return;
		}
		
		// .getSelectionModel().setSelectionInterval() : chọn dòng rowSelected trong bảng
		NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
		String sqlUndo = "Update NhanVien set Ho = N'" + Ho + "', Ten = N'" + Ten + "', SoCMND = '" + CMND
				+ "', DiaChi = N'" + DiaChi + "', NgaySinh = '" + NgaySinh + "', Luong = '"
				+ Formatter.formatMoneyToFloat(Luong) + "' where MaNV = '" + nhanVienModel.getManv().toString() + "'";
		undoList.push(sqlUndo);
		NhanVienFrm.getBtnHoanTac().setEnabled(true);
		JOptionPane.showConfirmDialog(null, "Ghi Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
	}

	private void refreshData() {
		NhanVienFrm.getTable().getSelectionModel().removeListSelectionListener(NhanVienFrm.getSelectionListener());
		// .setRowCount(0) : xóa hết dữ liệu
		NhanVienFrm.getModel().setRowCount(0);
		NhanVienFrm.loadDataIntoTable();
		NhanVienFrm.getTable().getSelectionModel().addListSelectionListener(NhanVienFrm.getSelectionListener());
		NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	private void undoData() {
		if (isSelectThem) { // && NhanVienFrm.getBtnThem().isEnabled() == false
			NhanVienFrm.getBtnThem().setEnabled(true);
			NhanVienFrm.getBtnXoa().setEnabled(true);
			NhanVienFrm.getBtnLamMoi().setEnabled(true);
			NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(true);
			NhanVienFrm.getBtnThoat().setEnabled(true);
			NhanVienFrm.getTable().setEnabled(true);
			isSelectThem = false;
			if (undoList.isEmpty()) {
				NhanVienFrm.getBtnHoanTac().setEnabled(false);
			}
			// trờ về dòng đang chọn trước đó
			NhanVienFrm.getTable().getSelectionModel().addListSelectionListener(NhanVienFrm.getSelectionListener());
			NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
			return;
		}
		if (undoList.isEmpty()) {
			JOptionPane.showConfirmDialog(null, "Hết thao tác để khôi phục", "Thông Báo", JOptionPane.CLOSED_OPTION);
			NhanVienFrm.getBtnHoanTac().setEnabled(false);
			return;
		}
		String queryUndo = undoList.pop().toString();
		if (queryUndo.contains("sp_ChuyenChiNhanh")) {
			String CurrentBranch = Program.servername;
			String ForwardBranch = Program.servernameLeft;
			
			Program.servername = ForwardBranch;
            Program.mlogin = Program.remotelogin;
            Program.password = Program.remotepassword;
			if (Program.Connect() == 0) {
				System.out.println("Kết nối không thành công");
				return;
			}

			if (Program.ExecSqlNonQuery(queryUndo) == -1) {
				JOptionPane.showConfirmDialog(null, "Khôi phục nhân viên chuyển chi nhánh thất bại!", "Thông Báo", JOptionPane.CLOSED_OPTION);
				return;
			}
			else {
				JOptionPane.showConfirmDialog(null, "Khôi phục nhân viên chuyển chi nhánh thành công", "Thông Báo",
						JOptionPane.CLOSED_OPTION);
			}	
			
			Program.servername = CurrentBranch;
			Program.mlogin = Program.mloginDN;
			Program.password = Program.passwordDN;
			if (Program.Connect() == 0) {
				System.out.println("Kết nối lại không thành công");
				return;
			}
			
		} else {
			if (Program.ExecSqlNonQuery(queryUndo) == -1) {
				JOptionPane.showConfirmDialog(null, "Khôi phục thất bại", "Thông Báo", JOptionPane.CLOSED_OPTION);
				return;
			}
		}
		refreshData();
		if (rowSelected <= NhanVienFrm.getTable().getRowCount() - 1) {
			NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
		} else {
			NhanVienFrm.getTable().getSelectionModel().setSelectionInterval(0, 0);
			rowSelected = 0;
		}
		if (undoList.isEmpty()) {
			NhanVienFrm.getBtnHoanTac().setEnabled(false);
			return;
		}
	}

	private boolean checkDatHang(String MaNV) {
		String sql = "SELECT CASE WHEN ? IN (SELECT MANV FROM DatHang) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaNV);
		try {
			Program.myReader.next();
			return Program.myReader.getBoolean(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean checkPhieuNhap(String MaNV) {
		String sql = "SELECT CASE WHEN ? IN (SELECT MANV FROM PhieuNhap) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaNV);
		try {
			Program.myReader.next();
			return Program.myReader.getBoolean(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean checkPhieuXuat(String MaNV) {

		String sql = "SELECT CASE WHEN ? IN (SELECT MANV FROM PhieuXuat) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaNV);
		try {
			Program.myReader.next();
			return Program.myReader.getBoolean(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean CheckMaNV(String MaNV) {
		String sql = "SELECT CASE WHEN ? IN (SELECT MaNV FROM Link2.QLVT_DATHANG.dbo.NhanVien) THEN 'true' ELSE 'false' END";
		Program.myReader = Program.ExecSqlDataReader(sql, MaNV);
		try {
			Program.myReader.next();
			return Program.myReader.getBoolean(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void deleteNhanVien() {
		if (NhanVienFrm.getTable().getRowCount() == 0)
			NhanVienFrm.getBtnXoa().setEnabled(false);
		// .getSelectedRow() : lấy dòng đang chọn trong table
		rowSelected = NhanVienFrm.getTable().getSelectedRow();
		if (rowSelected == -1) {
			JOptionPane.showMessageDialog(null, "Hãy chọn nhân viên cần xóa", "Thông Báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		NhanVienModel.setManv(Integer.parseInt(NhanVienFrm.getTable().getValueAt(rowSelected, 0).toString()));
		NhanVienModel.setHo(NhanVienFrm.getTable().getValueAt(rowSelected, 1).toString());
		NhanVienModel.setTen(NhanVienFrm.getTable().getValueAt(rowSelected, 2).toString());
		NhanVienModel.setSoCMND(NhanVienFrm.getTable().getValueAt(rowSelected, 3).toString());
		NhanVienModel.setDiaChi(NhanVienFrm.getTable().getValueAt(rowSelected, 4).toString());
		try {
			NhanVienModel.setNgaySinh(new java.sql.Date(new SimpleDateFormat("dd-MM-yyyy").parse((String) NhanVienFrm.getTable().getValueAt(NhanVienFrm.getTable().getSelectedRow(), 5)).getTime()));
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		NhanVienModel.setLuong(Float.valueOf(Formatter.formatMoneyToFloat(NhanVienFrm.getTable().getValueAt(rowSelected, 6))));
		NhanVienModel.setMacn(NhanVienFrm.getTable().getValueAt(rowSelected, 7).toString());
		NhanVienModel.setTrangThaiXoa((Boolean) NhanVienFrm.getTable().getValueAt(rowSelected, 8));
		if (NhanVienModel.getTrangThaiXoa() == true) {
			JOptionPane.showMessageDialog(null, "Nhân viên này đã bị xóa", "Thông Báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa nhân viên này không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (reply == JOptionPane.YES_OPTION) {
			
			// Nếu nhân viên đã làm việc thì không xóa mà chỉ đổi trạng thái xóa
			if (checkPhieuXuat(NhanVienModel.getManv().toString()) || checkPhieuNhap(NhanVienModel.getManv().toString())
					|| checkDatHang(NhanVienModel.getManv().toString())) {
				String sql = "Update NhanVien set TrangThaiXoa = 1 where MaNV = "+ NhanVienModel.getManv().toString();
				if (Program.ExecSqlNonQuery(sql) == -1) {
					JOptionPane.showMessageDialog(null, "Xóa thất bại", "Thông Báo", JOptionPane.WARNING_MESSAGE);
					return;
				}
				NhanVienFrm.getTable().setValueAt(true, rowSelected, 8);
				NhanVienFrm.getTrangThaiXoa().setSelected(true);
				undoList.push("Update NhanVien set TrangThaiXoa = 0 where MaNV = '" + NhanVienModel.getManv().toString()
						+ "'");
				NhanVienFrm.getBtnHoanTac().setEnabled(true);
				JOptionPane.showMessageDialog(null, "Đã đổi trạng thái xóa nhân viên thành công!", "Thông Báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			} else {
				String sql = "Delete from NhanVien where MaNV = "+ NhanVienModel.getManv().toString();
				if (Program.ExecSqlNonQuery(sql) == -1) {
					JOptionPane.showMessageDialog(null, "Xóa thất bại", "Thông Báo", JOptionPane.WARNING_MESSAGE);
					return;
				}

				undoList.push(
						"Insert into NhanVien(MaNV, Ho, Ten, SoCMND, DiaChi, NgaySinh, Luong, MaCN, TrangThaiXoa) values('"
								+ NhanVienModel.getManv() + "', N'" + NhanVienModel.getHo() + "', N'"
								+ NhanVienModel.getTen() + "', '" + NhanVienModel.getSoCMND() + "', N'"
								+ NhanVienModel.getDiaChi() + "', '" + NhanVienModel.getNgaySinh() + "', '"
								+ NhanVienModel.getLuong() + "', '" + NhanVienModel.getMacn() + "', '"
								+ NhanVienModel.getTrangThaiXoa() + "')");
				refreshData();
				NhanVienFrm.getBtnHoanTac().setEnabled(true);
				JOptionPane.showConfirmDialog(null, "Xóa Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
				return;
			}

		} else {
			return;
		}
	}

	private void chuyenChiNhanh() {
		if (NhanVienFrm.getTable().getRowCount() == 0)
			NhanVienFrm.getBtnChuyenChiNhanh().setEnabled(false);
		// .getSelectedRow() : lấy dòng đang chọn trong table
		rowSelected = NhanVienFrm.getTable().getSelectedRow();
		if (rowSelected == -1) {
			JOptionPane.showMessageDialog(null, "Hãy chọn nhân viên cần chuyển chi nhánh", "Thông Báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		NhanVienModel.setManv(Integer.parseInt(NhanVienFrm.getTable().getValueAt(rowSelected, 0).toString()));
		if (Program.username.equals(NhanVienModel.getManv().toString())) {
			JOptionPane.showMessageDialog(null, "Không thể chuyển chi nhánh cho chính mình", "Thông Báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		NhanVienModel.setHo(NhanVienFrm.getTable().getValueAt(rowSelected, 1).toString());
		NhanVienModel.setTen(NhanVienFrm.getTable().getValueAt(rowSelected, 2).toString());
		NhanVienModel.setSoCMND(NhanVienFrm.getTable().getValueAt(rowSelected, 3).toString());
		NhanVienModel.setDiaChi(NhanVienFrm.getTable().getValueAt(rowSelected, 4).toString());
		try {
			NhanVienModel.setNgaySinh(new java.sql.Date(new SimpleDateFormat("dd-MM-yyyy").parse((String) NhanVienFrm.getTable().getValueAt(NhanVienFrm.getTable().getSelectedRow(), 5)).getTime()));
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		NhanVienModel.setLuong(Float.valueOf(Formatter.formatMoneyToFloat(NhanVienFrm.getTable().getValueAt(rowSelected, 6))));
		NhanVienModel.setMacn(NhanVienFrm.getTable().getValueAt(rowSelected, 7).toString());
		NhanVienModel.setTrangThaiXoa((Boolean) NhanVienFrm.getTable().getValueAt(rowSelected, 8));
		
		if (NhanVienModel.getTrangThaiXoa() == true) {
			JOptionPane.showMessageDialog(null, "Nhân viên này đã bị xóa", "Thông Báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		ChuyenChiNhanhForm CCNFrm = new ChuyenChiNhanhForm();
		CCNFrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		for (String key : Program.servers.keySet()) {
			CCNFrm.getCBBoxChuyenChiNhanh().addItem(key);
		}
		
		// Xóa chi nhánh hiện tại đang đứng
		CCNFrm.getCBBoxChuyenChiNhanh().removeItemAt(Program.mChinhanh);
		CCNFrm.setVisible(true);
		
		CCNFrm.getBtnXacNhan().addActionListener(e -> {
			String sql_CCN = "sp_ChuyenChiNhanh ?, ?, ?, ?";
			Map<String, String> ChiNhanh = new HashMap<>();
			int index = 0;
			for (String key : Program.servers.keySet()) {
				ChiNhanh.put(key, Program.macn.get(index++));
				System.out.println(key + " : " + Program.macn.get(index - 1));
			}
			// Mã Nhân Viên Chi Nhánh Khác
			int MaNVMoi = -1;
			// Kiểm tra xem nhân viên đã tồn tại bên site bên kia chưa
			boolean isDeleteNVNew = false;
			String sql_CheckMaNV = "select isnull(MaNV,-1) from link0.QLVT_DATHANG.dbo.NhanVien where ? = SOCMND and ?  = MACN ";
			Program.myReader = Program.ExecSqlDataReader(sql_CheckMaNV, NhanVienModel.getSoCMND(), ChiNhanh.get(CCNFrm.getCBBoxChuyenChiNhanh().getSelectedItem()));
			try {
				Program.myReader.next();
				MaNVMoi = Program.myReader.getInt(1);
			} catch (Exception e1) {
			}
			// Nếu nhân viên chưa tồn tại 
			if (MaNVMoi == -1) {
				isDeleteNVNew = true;
				String sql_MaMVMoi = "select max(MaNV)+1 from link2.QLVT_DATHANG.dbo.NhanVien";
				Program.myReader = Program.ExecSqlDataReader(sql_MaMVMoi);
				try {
					Program.myReader.next();
					MaNVMoi = Program.myReader.getInt(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			// Tên sever chuyển chi nhánh
			Program.servernameLeft = Program.servers.get(CCNFrm.getCBBoxChuyenChiNhanh().getSelectedItem());
			// Kiểm tra xem Nhân Viên đó đã làm gì hay chưa
			
			if (!checkPhieuXuat(NhanVienModel.getManv().toString()) && !checkPhieuNhap(NhanVienModel.getManv().toString())
					&& !checkDatHang(NhanVienModel.getManv().toString())) {
				// Nếu Nhân viên này bên site kia chưa tồn tại. Xóa nhân viên bên này thì lấy mã nhân viên chuyển sang bên kia
				if(isDeleteNVNew)
					MaNVMoi = NhanVienModel.getManv();
				try {
					Program.ExecSqlDML(sql_CCN, NhanVienModel.getManv(), ChiNhanh.get(CCNFrm.getCBBoxChuyenChiNhanh().getSelectedItem()),MaNVMoi, true);
					String sqlUndo = "exec sp_ChuyenChiNhanh "+ MaNVMoi+ " ,'" + NhanVienModel.getMacn() + "'," + NhanVienModel.getManv()+ ", "+ isDeleteNVNew;
					undoList.push(sqlUndo);
					System.out.println(sqlUndo);
					NhanVienFrm.getBtnHoanTac().setEnabled(true);
					JOptionPane.showMessageDialog(null, "Chuyển chi nhánh thành công", "Thông Báo",
							JOptionPane.WARNING_MESSAGE);
					refreshData();
					CCNFrm.dispose();
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Chuyển chi nhánh thất bại\n  "+e1.getMessage(), "Thông Báo",
							JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					return;
                }
			}
			// Nếu không thì xóa nhân viên đó đi và thêm nhân viên đó sang chi nhánh mới
			else {
				try {
					Program.ExecSqlDML(sql_CCN, NhanVienModel.getManv(), ChiNhanh.get(CCNFrm.getCBBoxChuyenChiNhanh().getSelectedItem()),MaNVMoi, false);
					NhanVienFrm.getBtnHoanTac().setEnabled(true);
					String sqlUndo = "exec sp_ChuyenChiNhanh "+ MaNVMoi+ " ,'" + NhanVienModel.getMacn() + "'," + NhanVienModel.getManv()+ ", "+ isDeleteNVNew;
					undoList.push(sqlUndo);
					refreshData();
					JOptionPane.showMessageDialog(null, "Chuyển chi nhánh thành công", "Thông Báo",
							JOptionPane.WARNING_MESSAGE);
					CCNFrm.dispose();
				}
				catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Chuyển chi nhánh thất bại\n "+e1.getMessage(), "Thông Báo",
                            JOptionPane.WARNING_MESSAGE);
                    e1.printStackTrace();
                    return;
                }
			}
		});
		
		CCNFrm.getBtnThoat().addActionListener(e -> {
			// .dispose() : đóng frame
			CCNFrm.dispose();
		});
		
		
	}

	

}
