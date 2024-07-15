package controller;

import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import main.Program;
import views.CreateLoginForm;
import views.NhanVienOptionForm;

public class CreateLoginController {
	private CreateLoginForm form;

	public CreateLoginController(CreateLoginForm form) {
		this.form = form;
	}
	
	public void initController() {
		form.getBtnExit().addActionListener(l -> exitForm());
		form.getBtnAccept().addActionListener(l -> createLogin());
		form.getBtnNVOption().addActionListener(l -> chooseNhanVien());
	}
	
	private void exitForm() {
		form.dispose();
	}
	
	private boolean validate(String manv, String loginName, String password, String password2, String group) {
		if (manv.equals("") || loginName.equals("") || password.equals("") || password2.equals("") || group == null) {
			JOptionPane.showMessageDialog(null, "Hãy điền đầy đủ tất cả thông tin!", "Thông báo", JOptionPane.OK_OPTION);
			return false;
		}
		
		if (!password.equals(password2)) {
			JOptionPane.showMessageDialog(null, "Mật khẩu không trùng khớp!", "Thông báo", JOptionPane.OK_OPTION);
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void createLogin() {
		String loginName = form.getTextFieldLogin().getText().trim();
		String password = form.getPasswordField().getText();
		String passwordConfirm = form.getPasswordFieldAccept().getText();
		String username = CreateLoginForm.getTextFieldMaNV().getText();
		String group = null;
		
		Enumeration<AbstractButton> rdbtns = form.getBg().getElements();
		while (rdbtns.hasMoreElements()) {
			AbstractButton rdb = rdbtns.nextElement();
			if (rdb.isSelected()) {
				group = rdb.getText();
				break;
			}
		}
		if (validate(username, loginName, password, passwordConfirm, group)) {
			String sql = "{? = call dbo.sp_TaoLogin(?, ?, ?, ?)}";
			int res = 0;
			try {
				res = Program.ExecSqlNoQuery(sql, loginName, password, username, group);
			}catch(SQLException e) {
				JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi tạo tài khoản\nError Details: " + e.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
				System.out.println(res);
				return;
			}
			
			if (res == 0) {
				JOptionPane.showMessageDialog(null, "Tạo thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
				exitForm();
			}
		}
		
	}
	
	private void chooseNhanVien() {
		if (!NhanVienOptionForm.isVisible) {
			NhanVienOptionForm.isVisible = true;
			NhanVienOptionForm form = new NhanVienOptionForm(false);
			form.setVisible(true);
		}
	}
}
