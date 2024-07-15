package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import controller.ChiTietNhaXuatController;
import main.Program;

public class ChiTietNhapXuat extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> comboBox; // combobox này cho loại phiếu
	private JComboBox<String> comboBoxChiNhanh;
	private JDateChooser tuNgay, denNgay;
	private JButton btnXemTruoc, btnXuat;
	public static boolean isVisible = false;
	private String originLogin = Program.mlogin;
	private String originPass = Program.password;

	/**
	 * Create the frame.
	 */
	public ChiTietNhapXuat() {
		setTitle("Chi Tiết Nhập Xuất");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 641, 327);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox.setBounds(146, 67, 122, 22);
		comboBox.setEnabled(true);
		panel.add(comboBox);
		
		comboBox.addItem("XUẤT");
		comboBox.addItem("NHẬP");
		
		comboBoxChiNhanh = new JComboBox<String>();
		for (String key : Program.servers.keySet()) {
			comboBoxChiNhanh.addItem(key);
		}
		comboBoxChiNhanh.setSelectedIndex(Program.mChinhanh);

		JLabel lblNewLabel_1 = new JLabel("Loại Phiếu");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(50, 69, 86, 18);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Từ Ngày");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(50, 140, 86, 18);
		panel.add(lblNewLabel_1_1);

		tuNgay = new JDateChooser();
		tuNgay.setEnabled(true);
		tuNgay.setDateFormatString("dd-MM-yyyy");
		tuNgay.setBounds(146, 140, 122, 18);
		panel.add(tuNgay);

		JLabel lblNewLabel_1_2 = new JLabel("Đến Ngày");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_2.setBounds(334, 140, 86, 18);
		panel.add(lblNewLabel_1_2);

		denNgay = new JDateChooser();
		denNgay.setEnabled(true);
		denNgay.setDateFormatString("dd-MM-yyyy");
		denNgay.setBounds(453, 140, 122, 18);
		panel.add(denNgay);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		btnXemTruoc = new JButton("Xem trước");
		btnXemTruoc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnXemTruoc.setBackground(SystemColor.textHighlight);

		btnXuat = new JButton("Xuất bản");
		btnXuat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnXuat.setBackground(new Color(255, 0, 0));
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addGap(123)
						.addComponent(btnXemTruoc, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE).addGap(146)
						.addComponent(btnXuat, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addGap(123)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnXuat, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
								.addComponent(btnXemTruoc, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
						.addContainerGap()));
		panel_1.setLayout(gl_panel_1);

		JLabel lblHeader = new JLabel("CHI TIẾT NHẬP XUẤT");
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
		contentPane.add(lblHeader, BorderLayout.NORTH);
		setLocationRelativeTo(null);
		setResizable(false);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isVisible = false;
				if (Program.mGroup.equals("CONGTY")) {
					Program.mlogin = originLogin;
					Program.password = originPass;
					Program.Connect();
				}
			}
		});
		ChiTietNhaXuatController ac = null;
//		if (isVisible) {
			ac = new ChiTietNhaXuatController(this);
			ac.initController();
//		}
		
	}

	public JComboBox<String> getComboBoxChiNhanh() {
		return comboBoxChiNhanh;
	}

	public void setComboBoxChiNhanh(JComboBox<String> comboBoxChiNhanh) {
		this.comboBoxChiNhanh = comboBoxChiNhanh;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	public JDateChooser getTuNgay() {
		return tuNgay;
	}

	public JDateChooser getDenNgay() {
		return denNgay;
	}

	public JButton getBtnXemTruoc() {
		return btnXemTruoc;
	}

	public JButton getBtnXuat() {
		return btnXuat;
	}

}
