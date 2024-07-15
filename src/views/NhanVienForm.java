package views;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import controller.NhanVienController;
import dao.NhanVienDao;
import main.Program;
import model.NhanVienModel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import com.toedter.calendar.JDateChooser;

import common.method.Formatter;

import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

public class NhanVienForm extends CommonView<NhanVienModel, NhanVienDao> {

	private static final long serialVersionUID = 1L;
	private JTextField TFDiaChi;
	private JTextField TFMaNV;
	private JTextField TFHo;
	private JTextField TFTen;
	private JTextField TFMaCN;
	private JSpinner Luong;
	private JCheckBox CheckBoxTrangThaiXoa;
	private JTextField TFCMND;
	private JDateChooser NgaySinh;

	/**
	 * Create the panel.
	 */
	public NhanVienForm() {
		super();

		JPanel panel_2 = new JPanel();
		panel_2.setOpaque(false);
		panel_2.setForeground(new Color(207, 207, 207));
		add(panel_2, BorderLayout.SOUTH);

		JLabel lbMaNV = new JLabel("Mã nhân viên");
		lbMaNV.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lbHo = new JLabel("Họ");
		lbHo.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lbTen = new JLabel("Tên");
		lbTen.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lbLuong = new JLabel("Lương");
		lbLuong.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Luong = new JSpinner();
		Luong.setInheritsPopupMenu(true);
		Luong.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Luong.setModel(new SpinnerNumberModel(Float.valueOf(4000000), Float.valueOf(4000000), null, Float.valueOf(100000)));

		TFDiaChi = new JTextField();
		TFDiaChi.setColumns(10);
		TFDiaChi.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if ((TFDiaChi.getText().length() == 0 && e.getKeyChar() == KeyEvent.VK_SPACE)
						|| (TFDiaChi.getText().length() > 0
								&& TFDiaChi.getText().charAt(TFDiaChi.getText().length() - 1) == ' '
								&& e.getKeyChar() == KeyEvent.VK_SPACE)
						|| TFDiaChi.getText().length() >= 100) {
					// e.consume() : không cho phép nhập ký tự đó
					e.consume();
				}
			}
		});

		TFMaNV = new JTextField();
		TFMaNV.setEditable(false);
		TFMaNV.setColumns(10);
		TFMaNV.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// Nhập ký tự đâu tiên vào TFHo tự động thành chữ in hoa
				if (!Character.isDigit(e.getKeyChar())) {
					e.consume();
				}
			}
		});

		TFHo = new JTextField();
		TFHo.setColumns(10);
		TFHo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// Nhập ký tự đâu tiên vào TFHo tự động thành chữ in hoa
				if (TFHo.getText().length() == 0) {
					e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
				}

				if (Character.isDigit(e.getKeyChar())
						|| (TFHo.getText().length() == 0 && e.getKeyChar() == KeyEvent.VK_SPACE)
						|| (TFHo.getText().length() > 0 && TFHo.getText().charAt(TFHo.getText().length() - 1) == ' '
								&& e.getKeyChar() == KeyEvent.VK_SPACE)) {
					e.consume();
				}
			}
		});

		TFTen = new JTextField();
		TFTen.setColumns(10);
		TFTen.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (TFTen.getText().length() == 0) {
					e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
				}
				if (Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_SPACE) {
					e.consume();
				}
			}
		});

		JLabel lbNgaySinh = new JLabel("Ngày sinh");
		lbNgaySinh.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lbTrangThaiXoa = new JLabel("Trạng thái xóa");
		lbTrangThaiXoa.setFont(new Font("Tahoma", Font.PLAIN, 14));

		CheckBoxTrangThaiXoa = new JCheckBox("");
		CheckBoxTrangThaiXoa.setEnabled(false);
		CheckBoxTrangThaiXoa.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lbMaCN = new JLabel("Mã chi nhánh");
		lbMaCN.setFont(new Font("Tahoma", Font.PLAIN, 14));

		TFMaCN = new JTextField();
		TFMaCN.setEditable(false);
		TFMaCN.setColumns(10);

		NgaySinh = new JDateChooser();
		NgaySinh.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JLabel lbCMND = new JLabel("CMND");
		lbCMND.setFont(new Font("Tahoma", Font.PLAIN, 14));

		TFCMND = new JTextField();
		TFCMND.setColumns(10);
		TFCMND.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (Character.isLetter(e.getKeyChar()) || TFCMND.getText().length() >= 12
						|| e.getKeyChar() == KeyEvent.VK_SPACE) {
					e.consume();
				}
			}
		});

		JLabel lbDiaChi = new JLabel("Địa chỉ");
		lbDiaChi.setFont(new Font("Tahoma", Font.PLAIN, 14));

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(lbMaNV, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_2.createSequentialGroup().addGap(12).addComponent(lbDiaChi,
										GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(TFDiaChi, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
								.addComponent(TFMaNV, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
						.addGap(4)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup().addGap(22).addComponent(lbHo,
										GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_2.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lbCMND,
												GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_2
								.createSequentialGroup()
								.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_panel_2.createSequentialGroup()
												.addComponent(TFHo, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(lbTen, GroupLayout.PREFERRED_SIZE, 65,
														GroupLayout.PREFERRED_SIZE)
												.addGap(3))
										.addGroup(gl_panel_2.createSequentialGroup()
												.addComponent(TFCMND, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
												.addGap(13).addComponent(lbLuong, GroupLayout.PREFERRED_SIZE, 63,
														GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_2.createSequentialGroup().addGap(7).addComponent(Luong,
												GroupLayout.PREFERRED_SIZE, 173, Short.MAX_VALUE))
										.addGroup(gl_panel_2.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(TFTen, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
										.addComponent(lbMaCN, GroupLayout.PREFERRED_SIZE, 90,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panel_2.createSequentialGroup()
												.addComponent(lbNgaySinh, GroupLayout.PREFERRED_SIZE, 69,
														GroupLayout.PREFERRED_SIZE)
												.addGap(18)))
								.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_2.createSequentialGroup().addGap(18).addComponent(TFMaCN,
												GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
										.addGroup(gl_panel_2.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(NgaySinh,
														GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))))
								.addGroup(Alignment.LEADING, gl_panel_2.createSequentialGroup().addGap(10)
										.addComponent(lbTrangThaiXoa, GroupLayout.PREFERRED_SIZE, 102,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(CheckBoxTrangThaiXoa,
												GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2
				.createSequentialGroup()
				.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_2.createSequentialGroup()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_2
								.createSequentialGroup().addGap(10)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_panel_2.createSequentialGroup().addGap(1)
												.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
														.addComponent(lbMaNV, GroupLayout.DEFAULT_SIZE, 37,
																Short.MAX_VALUE)
														.addComponent(TFMaNV, GroupLayout.DEFAULT_SIZE, 37,
																Short.MAX_VALUE)))
										.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
												.addComponent(lbTen, GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
												.addComponent(TFHo, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
												.addComponent(TFTen, GroupLayout.PREFERRED_SIZE, 36,
														GroupLayout.PREFERRED_SIZE))))
								.addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(lbNgaySinh,
										GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
								.addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(NgaySinh,
										GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
						.addGap(30)).addGroup(
								gl_panel_2.createSequentialGroup().addContainerGap()
										.addComponent(lbHo, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addGap(17)))
				.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup()
						.addGap(30)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE).addGroup(gl_panel_2
								.createSequentialGroup().addGap(2)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
										.addComponent(lbLuong, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
										.addComponent(Luong, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(TFCMND, GroupLayout.PREFERRED_SIZE, 35,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lbCMND, GroupLayout.PREFERRED_SIZE, 55,
												GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel_2.createSequentialGroup().addGap(10)
										.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
												.addComponent(lbDiaChi, GroupLayout.PREFERRED_SIZE, 41,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(TFDiaChi, GroupLayout.PREFERRED_SIZE, 31,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED, 1, Short.MAX_VALUE)))
						.addGap(5))
						.addGroup(gl_panel_2.createSequentialGroup().addGap(33)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
										.addComponent(lbMaCN, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
										.addComponent(TFMaCN, GroupLayout.PREFERRED_SIZE, 36,
												GroupLayout.PREFERRED_SIZE))
								.addGap(3)))
				.addGap(18)
				.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_2.createSequentialGroup()
								.addComponent(lbTrangThaiXoa, GroupLayout.PREFERRED_SIZE, 50,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING,
								gl_panel_2.createSequentialGroup().addComponent(CheckBoxTrangThaiXoa).addGap(24)))));
		panel_2.setLayout(gl_panel_2);
//		load chi nhánh lên combobox
		loadChiNhanh();
//		load dữ liệu từ chi nhánh lên table
		dao = NhanVienDao.getInstance();
		loadDataIntoTable();
//		CONGTY có thể chọn chi nhánh để xem dữ liệu
		comboBox.addItemListener(l -> loadDataOtherServer(l));

//		lắng nghe sự kiện chọn row đồng thời in dữ liệu ra textfield
		selectionListener = e -> {
			TFMaNV.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
			TFHo.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
			TFTen.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
			TFCMND.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
			TFDiaChi.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
			try {
				NgaySinh.setDate(new SimpleDateFormat("dd-MM-yyyy").parse((String) table.getValueAt(table.getSelectedRow(), 5)));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// format money => integer
			Luong.setValue(Formatter.formatMoneyToFloat(table.getValueAt(table.getSelectedRow(), 6)));
			TFMaCN.setText(table.getValueAt(table.getSelectedRow(), 7).toString());
			CheckBoxTrangThaiXoa.setSelected((boolean) table.getValueAt(table.getSelectedRow(), 8));
		};

		// Thêm sự kiện chọn
		table.getSelectionModel().addListSelectionListener(selectionListener);
		// Khi mới vào form thì nó đang ở dòng nhân viên đó đang đăng nhập
		if (Program.username != null) {
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.getValueAt(i, 0).toString().equals(Program.username)) {
					table.setRowSelectionInterval(i, i);
					break;
				}
			}
		}
//		Listener event
		NhanVienController ac = new NhanVienController(this);
		ac.initController();
	}

	public JTextField getTFDiaChi() {
		return TFDiaChi;
	}

	public JTextField getTFMaNV() {
		return TFMaNV;
	}

	public JTextField getTFHo() {
		return TFHo;
	}

	public JTextField getTFTen() {
		return TFTen;
	}

	public JTextField getTFCMND() {
		return TFCMND;
	}

	public JDateChooser getNgaySinh() {
		return NgaySinh;
	}

	public JTextField getTFMaCN() {
		return TFMaCN;
	}

	public JSpinner getLuong() {
		return Luong;
	}

	public JCheckBox getTrangThaiXoa() {
		return CheckBoxTrangThaiXoa;
	}

	public void loadDataIntoTable() {
		loadData();
		for (NhanVienModel NhanVien : list) {
			Object[] rowData = { NhanVien.getManv(), NhanVien.getHo(), NhanVien.getTen(), NhanVien.getSoCMND(),
					NhanVien.getDiaChi(), Formatter.formatterDate(NhanVien.getNgaySinh()) ,
					Formatter.formatObjecttoMoney(NhanVien.getLuong()), NhanVien.getMacn(),
					NhanVien.getTrangThaiXoa() };
			model.addRow(rowData);
		}
	}

	private void loadDataOtherServer(ItemEvent l) {
		if (l.getStateChange() == ItemEvent.SELECTED) {
			if (Program.conn != null) {
				try {
					Program.conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			Program.servername = Program.servers.get(comboBox.getSelectedItem());
			if (!Program.mlogin.equals(Program.mloginDN)) {
				Program.mlogin = Program.mloginDN;
				Program.password = Program.passwordDN;
			} else {
				Program.mlogin = Program.remotelogin;
				Program.password = Program.remotepassword;
			}
			
			if (Program.Connect() == 0)
				return;
			Program.mChinhanh = comboBox.getSelectedIndex();
			TFMaCN.setText(Program.macn.get(Program.mChinhanh));
			table.getSelectionModel().removeListSelectionListener(selectionListener);
			model.setRowCount(0);
			loadDataIntoTable();
			table.getSelectionModel().addListSelectionListener(selectionListener);
		}
	}
}
