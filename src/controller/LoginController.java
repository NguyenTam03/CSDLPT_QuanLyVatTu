package controller;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Program;
import views.LoginForm;
import views.FrameMain;

public class LoginController {
	private LoginForm loginForm;
	
	public LoginController(LoginForm loginForm) {
		this.loginForm = loginForm;
	}
	
	public void initController() {
		loginForm.getBtnLogin().addActionListener(e -> clickBtnLogin());
		loginForm.getBtnExit().addActionListener(e -> clickBtnExit());
	}
	
	@SuppressWarnings("deprecation")
	private void clickBtnLogin() {
		JComboBox<String> cbChiNhanh = loginForm.getCbChiNhanh();
		JTextField login = loginForm.getTfUsername();
		JPasswordField password = loginForm.getTfPassword();
		login.setText(login.getText().trim());
		
		if (login.getText().equals("") || password.getText().equals("")) {
			JOptionPane.showMessageDialog(
					null, 
					"Điền đầy đủ thông tin tên đăng nhập và mật khẩu!",
					"Thông báo",
					JOptionPane.WARNING_MESSAGE
					);
		}else {
			Program.servername = Program.servers.get(cbChiNhanh.getSelectedItem());
			Program.mlogin = login.getText();
			Program.password = password.getText();
			if (Program.Connect() == 0) return;
			
			Program.mloginDN = Program.mlogin;
			Program.passwordDN = Program.password;
			Program.mChinhanh = cbChiNhanh.getSelectedIndex();
		    String strLenh = "EXEC SP_LayThongTinNhanVien ?";
            Program.myReader = Program.ExecSqlDataReader(strLenh, Program.mlogin);
            if(Program.myReader == null) return;
            try {
            	Program.myReader.next();
            	Program.username =  Program.myReader.getString(1);	
            	Program.mHoten = Program.myReader.getString(2);
            	Program.mGroup = Program.myReader.getString(3);
            	
            	loginForm.dispose();
            	Program.frmMain = new FrameMain();
            	Program.frmMain.setVisible(true);
            }
            catch(Exception e1) {
    			e1.printStackTrace();
            }
		}
	}
	
	private void clickBtnExit() {
		System.exit(0);
		return;
	}
	
}
