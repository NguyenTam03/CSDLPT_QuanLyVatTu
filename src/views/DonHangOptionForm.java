package views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class DonHangOptionForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField TFTim;
	private JButton BtnThoat, BtnChon;
	private JScrollPane scrollPane;
	private JTable tableDH;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DonHangOptionForm frame = new DonHangOptionForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DonHangOptionForm() {
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		TFTim = new JTextField();
		TFTim.setColumns(15);
		TFTim.setText("Search");
		TFTim.setForeground(Color.LIGHT_GRAY);
		TFTim.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (TFTim.getText().isEmpty()) {
					TFTim.setText("Search");
					TFTim.setForeground(Color.LIGHT_GRAY);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if (TFTim.getText().equals("Search")) {
					TFTim.setText("");
					TFTim.setForeground(Color.BLACK);
				}
			}
		});
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(TFTim, BorderLayout.NORTH);
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		BtnThoat = new JButton("Thoát");
		BtnThoat.setBackground(new Color(255, 0, 0));
		BtnThoat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		BtnChon = new JButton("Chọn");
		BtnChon.setBackground(new Color(0, 128, 255));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(59)
					.addComponent(BtnChon, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
					.addComponent(BtnThoat, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addGap(66))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(BtnChon, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(BtnThoat, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		scrollPane = new JScrollPane();
		scrollPane.setRequestFocusEnabled(false);
		scrollPane.setEnabled(false);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		tableDH = new JTable();
		scrollPane.setViewportView(tableDH);
		this.setLocationRelativeTo(null);
		tableDH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public JTextField getTFTim() {
		return TFTim;
	}

	public JTable getTableDH() {
		return tableDH;
	}

	public JButton getBtnThoat() {
		return BtnThoat;
	}

	public JButton getBtnChon() {
		return BtnChon;
	}
	
}
