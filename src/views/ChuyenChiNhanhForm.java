package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;

public class ChuyenChiNhanhForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> CBBoxChuyenChiNhanh;
	private JButton btnXacNhan;
	private JButton btnThoat;


	/**
	 * Create the frame.
	 */
	public ChuyenChiNhanhForm() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbChuyenChiNhanh = new JLabel("CHUYỂN CHI NHÁNH");
		lbChuyenChiNhanh.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbChuyenChiNhanh.setBounds(107, 40, 238, 53);
		contentPane.add(lbChuyenChiNhanh);
		
		CBBoxChuyenChiNhanh = new JComboBox<>();
		CBBoxChuyenChiNhanh.setBounds(117, 103, 180, 21);
		contentPane.add(CBBoxChuyenChiNhanh);
		
		btnXacNhan = new JButton("Xác Nhận");
		btnXacNhan.setBackground(new Color(0, 128, 255));
		btnXacNhan.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnXacNhan.setBounds(81, 177, 107, 35);
		contentPane.add(btnXacNhan);
		
		btnThoat = new JButton("Thoát");
		btnThoat.setBackground(new Color(255, 0, 0));
		btnThoat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnThoat.setBounds(236, 177, 94, 35);
		contentPane.add(btnThoat);
		
		setLocationRelativeTo(null);
	}

	public JComboBox<String> getCBBoxChuyenChiNhanh() {
		return CBBoxChuyenChiNhanh;
	}

	public JButton getBtnXacNhan() {
		return btnXacNhan;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}
	
}
