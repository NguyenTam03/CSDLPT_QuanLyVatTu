package views;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Program;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class ChangePasswordForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldLoginName;
	private JPasswordField passwordFieldOld;
	private JPasswordField passwordFieldNew;
	public static boolean isVisible = false;
	/**
	 * Create the frame.
	 */
	public ChangePasswordForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 520, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelHeader = new JPanel();
		contentPane.add(panelHeader, BorderLayout.NORTH);
		panelHeader.setLayout(new BorderLayout(0, 0));

		JLabel lblIcon = new JLabel("");
		lblIcon.setVerticalAlignment(SwingConstants.TOP);
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setIcon(new ImageIcon(ChangePasswordForm.class.getResource("/imgs/reset-password.png")));
		panelHeader.add(lblIcon, BorderLayout.NORTH);

		JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelHeader.add(lblTitle, BorderLayout.CENTER);

		JPanel panelCenter = new JPanel();
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);

		JLabel lblLoginName = new JLabel("Tên Tài Khoản");
		lblLoginName.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLoginName.setBounds(102, 11, 105, 14);
		panelCenter.add(lblLoginName);

		textFieldLoginName = new JTextField();
		textFieldLoginName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldLoginName.setBounds(231, 9, 148, 20);
		textFieldLoginName.setText(Program.mloginDN);
		textFieldLoginName.setEditable(false);
		panelCenter.add(textFieldLoginName);
		textFieldLoginName.setColumns(10);

		JLabel lblMtKhuC = new JLabel("Mật Khẩu Cũ");
		lblMtKhuC.setFont(new Font("Dialog", Font.BOLD, 12));
		lblMtKhuC.setBounds(102, 48, 105, 14);
		panelCenter.add(lblMtKhuC);

		JLabel lblMtKhuMi = new JLabel("Mật Khẩu Mới");
		lblMtKhuMi.setFont(new Font("Dialog", Font.BOLD, 12));
		lblMtKhuMi.setBounds(102, 91, 105, 14);
		panelCenter.add(lblMtKhuMi);

		passwordFieldOld = new JPasswordField();
		passwordFieldOld.setBounds(231, 46, 148, 20);
		panelCenter.add(passwordFieldOld);

		passwordFieldNew = new JPasswordField();
		passwordFieldNew.setBounds(231, 89, 148, 20);
		panelCenter.add(passwordFieldNew);

		JPanel panelFooter = new JPanel();
		contentPane.add(panelFooter, BorderLayout.SOUTH);

		JButton btnAccept = new JButton("Xác Nhận");
		btnAccept.setBackground(SystemColor.textHighlight);
		btnAccept.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAccept.addActionListener(l -> btnAcceptListener());

		JButton btnCancel = new JButton("Hủy");
		btnCancel.setBackground(Color.RED);
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.addActionListener(l -> btnCancelListener());

		GroupLayout gl_panelFooter = new GroupLayout(panelFooter);
		gl_panelFooter
				.setHorizontalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_panelFooter.createSequentialGroup().addGap(89).addComponent(btnAccept)
								.addPreferredGap(ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
								.addGap(89)));
		gl_panelFooter.setVerticalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup().addContainerGap()
						.addGroup(gl_panelFooter.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
								.addComponent(btnAccept, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
						.addContainerGap()));
		panelFooter.setLayout(gl_panelFooter);
		setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isVisible = false;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private boolean validator() {
		if (!passwordFieldOld.getText().equals(Program.passwordDN)) {
			JOptionPane.showMessageDialog(null, "Mật khẩu không đúng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			passwordFieldOld.requestFocusInWindow();
			return false;
		}

		if (passwordFieldOld.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Mật khẩu không được bỏ trống!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			passwordFieldOld.requestFocusInWindow();
			return false;
		}

		if (passwordFieldNew.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Mật khẩu không được bỏ trống!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			passwordFieldNew.requestFocusInWindow();
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private void btnAcceptListener() {
		String loginName = textFieldLoginName.getText();
		String oldPassword = passwordFieldOld.getText();
		String newPassword = passwordFieldNew.getText();

		if (validator()) {
			String sql = "{call dbo.SP_DOI_PASSWORD(?, ?, ?)}";
			try {
				Program.ExecSqlDML(sql, loginName, newPassword, oldPassword);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Đổi mật khẩu không thành công\nError Details: " + e.getMessage(), "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!", "Thành công",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
		}

	}

	private void btnCancelListener() {
		this.dispose();
	}
}
