package controller;

import javax.swing.JOptionPane;
import main.Program;
import model.KhoModel;
import views.KhoForm;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KhoController {
	private KhoForm khoView;
	private KhoModel khoModel;
	private Stack<String> undoList;
	private int row;

	public KhoController(KhoForm khoView) {
		this.khoView = khoView;
		khoModel = new KhoModel();
		undoList = new Stack<String>();
		row = 0;
	}

	public void initController() {
		khoView.getBtnThem().addActionListener(l -> addKho());
		khoView.getBtnGhi().addActionListener(l -> pushDataToDB());
		khoView.getBtnLamMoi().addActionListener(l -> reFreshData());
		khoView.getBtnXoa().addActionListener(l -> deleteData());
		khoView.getBtnThoat().addActionListener(l -> exitKho());
		khoView.getBtnHoanTac().addActionListener(l -> btnHoanTacClicked());
		autoSearchKho();
	}

	private void autoSearchKho() {
		khoView.getTextFieldTim().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchKho();
			}
		});
	}

	private void searchKho() {
		String input = khoView.getTextFieldTim().getText().trim().toLowerCase();
		khoView.getTable().getSelectionModel().removeListSelectionListener(khoView.getSelectionListener());
		khoView.getModel().setRowCount(0);

		for (KhoModel kho : khoView.getList()) {
			if (kho.getTenKho().toLowerCase().contains(input)) {
				Object[] rowData = { kho.getMaKho(), kho.getTenKho(), kho.getDiaChi(), kho.getMacn() };
				khoView.getModel().addRow(rowData);
			}
		}
		khoView.getTable().getSelectionModel().addListSelectionListener(khoView.getSelectionListener());
		if (khoView.getTable().getRowCount() > 0) {
			khoView.getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	private void exitKho() {
		Program.frmMain.getTabbedPane_Main().removeTabAt(Program.frmMain.getTabbedPane_Main().getSelectedIndex());
		Program.frmMain.getPanel_Kho().remove(khoView);
	}

	private void addKho() {
//		Luu vi tri row dang chon
		row = khoView.getTable().getSelectedRow();
		khoView.getBtnThem().setEnabled(false);
		khoView.getBtnXoa().setEnabled(false);
		khoView.getBtnLamMoi().setEnabled(false);
		khoView.getBtnHoanTac().setEnabled(true);
		khoView.getBtnThoat().setEnabled(false);

		khoView.getTextFieldMaKho().setEditable(true);
		khoView.getTextFieldMaKho().setText("");
		khoView.getTextFieldTenKho().setText("");
		khoView.getTextFieldDiaChi().setText("");
		khoView.getTextFieldTim().setEnabled(false);

		khoView.getTable().getSelectionModel().removeListSelectionListener(khoView.getSelectionListener());
		khoView.getTable().getSelectionModel().clearSelection();
		khoView.getTable().setEnabled(false);
	}

	private void btnHoanTacClicked() {
//		hoan tac su kien click btnthem
		if (!khoView.getBtnThem().isEnabled()) {
			khoView.getBtnThem().setEnabled(true);
			khoView.getBtnXoa().setEnabled(true);
			khoView.getBtnLamMoi().setEnabled(true);
			khoView.getBtnThoat().setEnabled(true);

			khoView.getTextFieldMaKho().setEditable(false);
			khoView.getTextFieldMaKho().setText("");
			khoView.getTextFieldTenKho().setText("");
			khoView.getTextFieldDiaChi().setText("");
			khoView.getTextFieldTim().setEnabled(true);
			khoView.getTable().setEnabled(true);
//			trở về dòng được select hiện tại
			khoView.getTable().getSelectionModel().addListSelectionListener(khoView.getSelectionListener());
			khoView.getTable().getSelectionModel().setSelectionInterval(row, row);

			return;
		}
		
		if (undoList.empty()) {
			khoView.getBtnHoanTac().setEnabled(false);
			return;
		}

		String sqlUndo = undoList.pop();
		try {
			Program.ExecSqlDML(sqlUndo);
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi, undo không thành công!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		reFreshData();
		if (row <= khoView.getTable().getRowCount() - 1) {
			khoView.getTable().getSelectionModel().setSelectionInterval(row, row);
		}

		if (undoList.empty()) {
			khoView.getBtnHoanTac().setEnabled(false);
			return;
		}
	}

	private void pushDataToDB() {
		int reply = JOptionPane.showConfirmDialog(null, "Bạn có muốn ghi dữ liệu vào bảng không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (reply == JOptionPane.YES_OPTION) {
			String maKho = khoView.getTextFieldMaKho().getText().trim();
			String tenKho = khoView.getTextFieldTenKho().getText().trim();
			String diaChi = khoView.getTextFieldDiaChi().getText().trim();
			String macn = khoView.getTextFieldMaCN().getText();
			khoModel.setMaKho(maKho);
			khoModel.setTenKho(tenKho);
			khoModel.setDiaChi(diaChi);
			khoModel.setMacn(macn);
			row = khoView.getTable().getSelectedRow();
			
			if (checkInputData(khoModel)) {
				if (!khoView.getBtnThem().isEnabled()) {
					addDataToDB(khoModel);
				} else {
					upDateDataToDB(khoModel);
				}
				khoView.getTextFieldMaKho().setEditable(false);
			}
		}
	}

	private boolean regexMatch(String text) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\s]+$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

	private boolean checkInputData(KhoModel khoModel) {
		if (khoModel.getMaKho().equals("")) {
			JOptionPane.showMessageDialog(null, "Mã kho không được bỏ trống", "Thông báo", JOptionPane.WARNING_MESSAGE);
			khoView.getTextFieldMaKho().requestFocusInWindow();
			return false;
		}

		if (khoModel.getMaKho().length() > 4) {
			JOptionPane.showMessageDialog(null, "Mã kho không được quá 4 ký tự", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			khoView.getTextFieldMaKho().requestFocusInWindow();
			return false;
		}

		if (!regexMatch(khoModel.getMaKho())) {
			JOptionPane.showMessageDialog(null, "Mã kho chỉ được gồm các chữ cái số và khoảng trắng!", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			khoView.getTextFieldMaKho().requestFocusInWindow();
			return false;
		}

		if (khoModel.getTenKho().equals("")) {
			JOptionPane.showMessageDialog(null, "Tên kho không được bỏ trống", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			khoView.getTextFieldTenKho().requestFocusInWindow();
			return false;
		}

		if (khoModel.getTenKho().length() > 30) {
			JOptionPane.showMessageDialog(null, "Tên kho không được quá 30 ký tự", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			khoView.getTextFieldTenKho().requestFocusInWindow();
			return false;
		}

		if (khoModel.getDiaChi().length() > 100) {
			JOptionPane.showMessageDialog(null, "Địa chỉ không được quá 100 ký tự", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			khoView.getTextFieldDiaChi().requestFocusInWindow();
			return false;
		}

		return true;
	}

	private void addDataToDB(KhoModel khoModel) {
//		Execute query
		String sql = "{? = call dbo.sp_TraCuu_MaKho(?)}";
		int res = 0;
		try {
			res = Program.ExecSqlNoQuery(sql, khoModel.getMaKho());
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Lỗi kiểm tra kho!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
//		execute return 1, dữ liệu đã tồn tại
		if (res == 1) {
			JOptionPane.showMessageDialog(null, "Đã tồn tại mã kho " + khoModel.getMaKho() + " nay, vui lòng nhập lại",
					"Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			try {
				Object[] newRow = { khoModel.getMaKho(), khoModel.getTenKho(), khoModel.getDiaChi(), khoModel.getMacn() };
				khoView.getModel().addRow(newRow);
				khoView.getDao().insert(khoModel);
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Lỗi thêm kho!", "Error", JOptionPane.ERROR_MESSAGE);
				reFreshData();
				khoView.getTable().getSelectionModel().setSelectionInterval(row, row);
				return;
			}

			
			JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

			khoView.getTable().setEnabled(true);
			khoView.getBtnThem().setEnabled(true);
			khoView.getBtnXoa().setEnabled(true);
			khoView.getBtnLamMoi().setEnabled(true);
			khoView.getBtnThoat().setEnabled(true);
			khoView.getBtnHoanTac().setEnabled(true);
			khoView.getTextFieldTim().setEnabled(true);
			khoView.getTable().getSelectionModel().addListSelectionListener(khoView.getSelectionListener());
			khoView.getTable().getSelectionModel().setSelectionInterval(khoView.getTable().getRowCount() - 1,
					khoView.getTable().getRowCount() - 1);
			khoView.getList().add(khoModel);
			row = khoView.getTable().getRowCount() - 1;
			// Luu truy van de hoan tac yeu cau them
			String sqlUndo;
			sqlUndo = "DELETE FROM Kho WHERE MAKHO = '" + khoModel.getMaKho() + "'";
			undoList.push(sqlUndo);
		}

	}

	private void upDateDataToDB(KhoModel khoModel) {
		String tenKho = khoView.getTable().getValueAt(khoView.getTable().getSelectedRow(), 1).toString();
		String diaChi = khoView.getTable().getValueAt(khoView.getTable().getSelectedRow(), 2).toString();

		try {
			khoView.getTable().setValueAt(khoModel.getTenKho(), khoView.getTable().getSelectedRow(), 1);
			khoView.getTable().setValueAt(khoModel.getDiaChi(), khoView.getTable().getSelectedRow(), 2);
			khoView.getDao().update(khoModel);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Lỗi update kho!", "Error", JOptionPane.ERROR_MESSAGE);
			reFreshData();
			khoView.getTable().getSelectionModel().setSelectionInterval(row, row);
			return;
		}
		khoView.getBtnHoanTac().setEnabled(true);
		
		JOptionPane.showMessageDialog(null, "Ghi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		khoView.getTable().getSelectionModel().setSelectionInterval(row, row);
		khoView.getList().set(row, khoModel);
		// Luu truy van de hoan tac yeu cau update
		String sqlUndo;
		sqlUndo = "UPDATE Kho SET TENKHO = '" + tenKho + "'," + "DIACHI = '" + diaChi + "' " + "WHERE MAKHO = '"
				+ khoModel.getMaKho() + "'";
		undoList.push(sqlUndo);

	}

	private void reFreshData() {
		khoView.getTable().getSelectionModel().removeListSelectionListener(khoView.getSelectionListener());
		khoView.getModel().setRowCount(0);
		khoView.loadDataIntoTableKho();
		khoView.getTable().getSelectionModel().addListSelectionListener(khoView.getSelectionListener());
		khoView.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	private boolean checkDatHang(String makho) {
		String sql = "SELECT DISTINCT MAKHO FROM DatHang WHERE MAKHO = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, makho);

		try {
			Program.myReader.next();
			if (Program.myReader.getRow() > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean checkPhieuNhap(String makho) {
		String sql = "SELECT DISTINCT MAKHO FROM PhieuNhap WHERE MAKHO = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, makho);

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

	private boolean checkPhieuXuat(String makho) {
		String sql = "SELECT DISTINCT MAKHO FROM PhieuXuat WHERE MAKHO = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, makho);

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
//		chưa chọn hàng thì không xóa được
		if (khoView.getTable().getSelectedRow() == -1)
			return;
		if (JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa dữ liệu này không?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) {
			return;
		}
//		set dữ liệu của row đó
		khoModel.setMaKho(khoView.getTextFieldMaKho().getText());
		khoModel.setTenKho(khoView.getTextFieldTenKho().getText());
		khoModel.setDiaChi(khoView.getTextFieldDiaChi().getText());
		khoModel.setMacn(khoView.getTextFieldMaCN().getText());
//		check exist
		if (checkDatHang(khoModel.getMaKho())) {
			JOptionPane.showMessageDialog(null, "Không thể xóa kho hàng này vì đã lập đơn đặt hàng.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (checkPhieuNhap(khoModel.getMaKho())) {
			JOptionPane.showMessageDialog(null, "Không thể xóa kho hàng này vì đã lập phiếu nhập.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (checkPhieuXuat(khoModel.getMaKho())) {
			JOptionPane.showMessageDialog(null, "Không thể xóa kho hàng này vì đã lập phiếu xuất.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		row = khoView.getTable().getSelectedRow();

		try {
			khoView.getTable().getSelectionModel().removeListSelectionListener(khoView.getSelectionListener());
			khoView.getModel().removeRow(khoView.getTable().getSelectedRow());
			khoView.getTextFieldMaKho().setText("");
			khoView.getTextFieldTenKho().setText("");
			khoView.getTextFieldDiaChi().setText("");
			khoView.getDao().delete(khoModel);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Lỗi xóa kho!", "Error", JOptionPane.ERROR_MESSAGE);
			reFreshData();
			khoView.getTable().getSelectionModel().setSelectionInterval(row, row);
			return;
		}

		JOptionPane.showMessageDialog(null, "Xóa thành công!", "Thông báo", JOptionPane.OK_OPTION);
		khoView.getTable().getSelectionModel().addListSelectionListener(khoView.getSelectionListener());
		khoView.getBtnHoanTac().setEnabled(true);
		khoView.getList().remove(row);
		if (khoView.getTable().getRowCount() > 0) {
			if (row == 0) {
				khoView.getTable().getSelectionModel().setSelectionInterval(row + 1, row + 1);
			}else {
				khoView.getTable().getSelectionModel().setSelectionInterval(row - 1, row - 1);
			}
		}
		
		String sqlUndo = "INSERT INTO Kho (MAKHO, TENKHO, DIACHI, MACN) VALUES ('" + khoModel.getMaKho() + "', '"
				+ khoModel.getTenKho() + "', '" + khoModel.getDiaChi() + "', '" + khoModel.getMacn() + "')";
		undoList.push(sqlUndo);

		if (khoView.getTable().getRowCount() == 0) {
			khoView.getBtnXoa().setEnabled(false);
		}
	}
}
