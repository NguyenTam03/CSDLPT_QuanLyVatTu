package views;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.LoginController;
import main.Program;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.Font;
public class LoginForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfUsername;
	private JPasswordField tfPassword;
	private JComboBox<String> cbChiNhanh;
	private JButton btnLogin;
	private JButton btnExit;
	
	
	public JTextField getTfUsername() {
		return tfUsername;
	}


	public JPasswordField getTfPassword() {
		return tfPassword;
	}


	public JComboBox<String> getCbChiNhanh() {
		return cbChiNhanh;
	}


	public JButton getBtnLogin() {
		return btnLogin;
	}

	public JButton getBtnExit() {
		return btnExit;
	}


	public LoginForm() {
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 473, 337);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIconLogin = new JLabel("");
		lblIconLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblIconLogin.setIcon(new ImageIcon(LoginForm.class.getResource("/imgs/login.png")));
		lblIconLogin.setBounds(0, 0, 457, 89);
		contentPane.add(lblIconLogin);
		
		JPanel panel = new JPanel();
		panel.setBounds(76, 90, 296, 117);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblChiNhanh = new JLabel("Chi Nhánh:");
		lblChiNhanh.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblChiNhanh.setBounds(22, 11, 61, 14);
		panel.add(lblChiNhanh);
		
		Program.readInfoDBFile();
		
		Program.servers = Program.getServer();
		cbChiNhanh = new JComboBox<String>();
		cbChiNhanh.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cbChiNhanh.setBounds(93, 8, 166, 20);
		panel.add(cbChiNhanh);
		for (String key : Program.servers.keySet()) {
			cbChiNhanh.addItem(key);
		}
		
		JLabel lblTaiKhoan = new JLabel("Tài Khoản:");
		lblTaiKhoan.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTaiKhoan.setBounds(22, 43, 61, 14);
		panel.add(lblTaiKhoan);
		
		tfUsername = new JTextField();
		tfUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfUsername.setBounds(93, 40, 166, 20);
		panel.add(tfUsername);
		tfUsername.setColumns(10);
		tfUsername.requestFocusInWindow();
		
		JLabel lblMatKhau = new JLabel("Mật khẩu:");
		lblMatKhau.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMatKhau.setBounds(22, 76, 61, 14);
		panel.add(lblMatKhau);
		
		tfPassword = new JPasswordField();
		tfPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfPassword.setBounds(93, 73, 166, 20);
		panel.add(tfPassword);
		
		btnLogin = new JButton("Đăng nhập");
		btnLogin.setForeground(SystemColor.text);
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogin.setBackground(SystemColor.textHighlight);
		btnLogin.setBounds(76, 228, 100, 34);
		contentPane.add(btnLogin);
		
		btnExit = new JButton("Thoát");
		btnExit.setForeground(SystemColor.text);
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExit.setBackground(new Color(255, 0, 0));
		btnExit.setBounds(283, 228, 89, 34);
		contentPane.add(btnExit);
		
		LoginController ac = new LoginController(this);
		ac.initController();
		setLocationRelativeTo(null);
	}
}
