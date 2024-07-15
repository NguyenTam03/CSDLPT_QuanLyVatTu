package views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.CreateLoginController;
import main.Program;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class CreateLoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ButtonGroup bg;
	private JPanel panelHeader;
	private JLabel lblIcon;
	private JLabel lblTaoTK;
	private JPanel panelCenter;
	private JPanel panelFooter;
	private static JTextField textFieldMaNV;
	private JTextField textFieldLogin;
	private JPasswordField passwordField;
	private JLabel lblPasswordConfirm;
	private JPasswordField passwordFieldAccept;
	private JLabel lblGroup;
	private JRadioButton rdbtnCT;
	private JRadioButton rdbtnCN, rdbtnUser;
	private JButton btnAccept;
	private JButton btnExit;
	private JButton btnNVOption;
	private static JTextField textFieldName;
	public static boolean isVisible = false;
	
	public CreateLoginForm() {
		setTitle("Tạo tài khoản");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 643, 439);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panelHeader = new JPanel();
		contentPane.add(panelHeader, BorderLayout.NORTH);
		panelHeader.setLayout(new BorderLayout(0, 0));

		lblIcon = new JLabel("");
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setIcon(new ImageIcon(CreateLoginForm.class.getResource("/imgs/login.png")));
		panelHeader.add(lblIcon, BorderLayout.NORTH);

		lblTaoTK = new JLabel("TẠO TÀI KHOẢN");
		lblTaoTK.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTaoTK.setHorizontalAlignment(SwingConstants.CENTER);
		panelHeader.add(lblTaoTK);

		panelCenter = new JPanel();
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);

		JLabel lblMaNV = new JLabel("Mã Nhân Viên:");
		lblMaNV.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMaNV.setBounds(92, 24, 115, 14);
		panelCenter.add(lblMaNV);

		textFieldMaNV = new JTextField();
		textFieldMaNV.setEditable(false);
		textFieldMaNV.setBounds(233, 21, 120, 20);
		panelCenter.add(textFieldMaNV);
		textFieldMaNV.setColumns(10);

		btnNVOption = new JButton("Chọn nhân viên");
		btnNVOption.setBounds(424, 20, 125, 23);
		panelCenter.add(btnNVOption);

		JLabel lblLogin = new JLabel("Tên Tài Khoản:");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblLogin.setBounds(92, 101, 115, 14);
		panelCenter.add(lblLogin);

		textFieldLogin = new JTextField();
		textFieldLogin.setBounds(233, 98, 171, 20);
		panelCenter.add(textFieldLogin);
		textFieldLogin.setColumns(10);

		JLabel lblPassword = new JLabel("Mật Khẩu:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPassword.setBounds(92, 137, 115, 14);
		panelCenter.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(233, 134, 171, 20);
		panelCenter.add(passwordField);

		lblPasswordConfirm = new JLabel("Xác Nhận Mật Khẩu:");
		lblPasswordConfirm.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPasswordConfirm.setBounds(92, 177, 131, 14);
		panelCenter.add(lblPasswordConfirm);

		passwordFieldAccept = new JPasswordField();
		passwordFieldAccept.setBounds(233, 174, 171, 20);
		panelCenter.add(passwordFieldAccept);

		lblGroup = new JLabel("Vai Trò:");
		lblGroup.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGroup.setBounds(92, 225, 115, 14);
		panelCenter.add(lblGroup);

		JPanel panelGroup = new JPanel();
		panelGroup.setBounds(230, 206, 251, 33);
		panelCenter.add(panelGroup);
		
		JLabel lblName = new JLabel("Họ và tên:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(92, 61, 115, 14);
		panelCenter.add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setEditable(false);
		textFieldName.setBounds(233, 58, 171, 20);
		panelCenter.add(textFieldName);
		textFieldName.setColumns(10);
		
		bg = new ButtonGroup();
		if (Program.mGroup.equals("CONGTY")) {
			rdbtnCT = new JRadioButton("CONGTY");
			rdbtnCT.setSelected(true);
			panelGroup.add(rdbtnCT);
			bg.add(rdbtnCT);
		} else {
			rdbtnCN = new JRadioButton("CHINHANH");
			panelGroup.add(rdbtnCN);

			rdbtnUser = new JRadioButton("USER");
			panelGroup.add(rdbtnUser);
			bg.add(rdbtnCN);
			bg.add(rdbtnUser);
		}

		panelFooter = new JPanel();
		contentPane.add(panelFooter, BorderLayout.SOUTH);

		btnAccept = new JButton("Xác nhận");

		btnExit = new JButton("Thoát");
		GroupLayout gl_panelFooter = new GroupLayout(panelFooter);
		gl_panelFooter.setHorizontalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup().addGap(95)
						.addComponent(btnAccept, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
						.addGap(195).addComponent(btnExit, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(100, Short.MAX_VALUE)));
		gl_panelFooter.setVerticalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelFooter.createSequentialGroup().addContainerGap()
						.addGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
								.addComponent(btnExit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnAccept, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 31,
										Short.MAX_VALUE))
						.addContainerGap()));
		panelFooter.setLayout(gl_panelFooter);

		setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isVisible = false;
			}
		});
		CreateLoginController ac = new CreateLoginController(this);
		ac.initController();
	}

	public static JTextField getTextFieldMaNV() {
		return textFieldMaNV;
	}

	public JTextField getTextFieldLogin() {
		return textFieldLogin;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JPasswordField getPasswordFieldAccept() {
		return passwordFieldAccept;
	}

	public JRadioButton getRdbtnCT() {
		return rdbtnCT;
	}

	public JRadioButton getRdbtnCN() {
		return rdbtnCN;
	}

	public JButton getBtnAccept() {
		return btnAccept;
	}

	public JButton getBtnExit() {
		return btnExit;
	}

	public JButton getBtnNVOption() {
		return btnNVOption;
	}
	
	

	public static JTextField getTextFieldName() {
		return textFieldName;
	}

	public ButtonGroup getBg() {
		return bg;
	}
}
