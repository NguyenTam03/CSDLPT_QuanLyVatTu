package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import common.method.Formatter;
import controller.DonDHChuaPNController;
import main.Program;
import model.DonDatHangChuaPNModel;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DonDatHangChuaPN extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JComboBox<String> comboBox;
	private JButton btnPreView, btnPrint;
	private List<DonDatHangChuaPNModel> list;
	private DefaultTableModel model;
	private int colCount;
	private List<String> colName;
	public static boolean isVisible = false;
	
	public JTable getTable() {
		return table;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	public JButton getBtnPreView() {
		return btnPreView;
	}

	public JButton getBtnPrint() {
		return btnPrint;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public List<DonDatHangChuaPNModel> getList() {
		return list;
	}

	/**
	 * Create the frame.
	 */
	public DonDatHangChuaPN() {
		setTitle("Đơn đặt hàng chưa có phiếu nhập");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 646, 401);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isVisible = false;
			}
		});

		JPanel panelHeader = new JPanel();
		contentPane.add(panelHeader, BorderLayout.NORTH);
		panelHeader.setLayout(new BorderLayout(0, 0));

		JLabel lblTitle = new JLabel("ĐƠN ĐẶT HÀNG CHƯA CÓ PHIẾU NHẬP");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		panelHeader.add(lblTitle, BorderLayout.NORTH);

		JPanel panelChiNhanh = new JPanel();
		panelHeader.add(panelChiNhanh, BorderLayout.SOUTH);

		JLabel lblChiNhanh = new JLabel("Chi Nhánh");
		lblChiNhanh.setFont(new Font("Tahoma", Font.BOLD, 14));

		comboBox = new JComboBox<>();
		comboBox.setMaximumRowCount(20);
		for (String key : Program.servers.keySet()) {
			comboBox.addItem(key);
		}
		comboBox.setSelectedIndex(Program.mChinhanh);

		if (Program.mGroup.equals("CHINHANH") || Program.mGroup.equals("USER")) {
			comboBox.setEnabled(false);
		}
		comboBox.addItemListener(l -> loadDataOtherServer(l));
		GroupLayout gl_panelChiNhanh = new GroupLayout(panelChiNhanh);
		gl_panelChiNhanh.setHorizontalGroup(gl_panelChiNhanh.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelChiNhanh.createSequentialGroup().addGap(148)
						.addComponent(lblChiNhanh, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE).addGap(49)
						.addComponent(comboBox, 0, 195, Short.MAX_VALUE).addGap(156)));
		gl_panelChiNhanh.setVerticalGroup(gl_panelChiNhanh.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelChiNhanh.createSequentialGroup().addGap(10)
						.addGroup(gl_panelChiNhanh
								.createParallelGroup(Alignment.BASELINE).addComponent(lblChiNhanh,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(comboBox))
						.addContainerGap()));
		panelChiNhanh.setLayout(gl_panelChiNhanh);

		JPanel panelBody = new JPanel();
		contentPane.add(panelBody, BorderLayout.CENTER);
		panelBody.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelBody.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		JPanel panelFooter = new JPanel();
		contentPane.add(panelFooter, BorderLayout.SOUTH);

		btnPreView = new JButton("Xem trước");
		btnPreView.setFont(new Font("Tahoma", Font.PLAIN, 14));

		btnPrint = new JButton("Xuất");
		btnPrint.setFont(new Font("Tahoma", Font.PLAIN, 14));

		GroupLayout gl_panelFooter = new GroupLayout(panelFooter);
		gl_panelFooter.setHorizontalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup().addGap(117)
						.addComponent(btnPreView, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
						.addGap(176).addComponent(btnPrint, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
						.addGap(126)));
		gl_panelFooter.setVerticalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup()
						.addGroup(gl_panelFooter.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnPreView, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
								.addComponent(btnPrint, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
						.addContainerGap()));
		panelFooter.setLayout(gl_panelFooter);

		setLocationRelativeTo(null);

		// không cho phép chỉnh sửa nội dung trực tiếp trên row
		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);
		model = (DefaultTableModel) table.getModel();	
		loadDataIntoTable();
		DonDHChuaPNController ac = new DonDHChuaPNController(this);
		ac.initController();
	}

	public List<DonDatHangChuaPNModel> getDataFromDB() {
		String sql = "EXEC DBO.SP_DS_DDH_CHUACO_PN";
		Program.myReader = Program.ExecSqlDataReader(sql);
		setColCountAndColName();
		model.setColumnIdentifiers(colName.toArray());
		List<DonDatHangChuaPNModel> temp = new ArrayList<DonDatHangChuaPNModel>();
		try {
			while (Program.myReader.next()) {
				DonDatHangChuaPNModel model = new DonDatHangChuaPNModel();
				model.setMaDDH(Program.myReader.getString(1));
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String ngayLap = formatter.format(Program.myReader.getDate(2));
				model.setNgayLap(ngayLap);
				model.setNhaCC(Program.myReader.getString(3));
				model.setHoTen(Program.myReader.getString(4));
				model.setTenVT(Program.myReader.getString(5));
				model.setSoLuong(Program.myReader.getInt(6));
				model.setDonGia(Program.myReader.getFloat(7));
				temp.add(model);
			}
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setColCountAndColName() {
		try {
			colName = new ArrayList<String>();
			colCount = Program.myReader.getMetaData().getColumnCount();
			for (int i = 0; i < colCount; i++) {
				String name = Program.myReader.getMetaData().getColumnName(i + 1);
				colName.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadDataIntoTable() {
		list = getDataFromDB();
		
		for (DonDatHangChuaPNModel obj : list) {
			Object[] row = { obj.getMaDDH(), obj.getNgayLap(), obj.getNhaCC(), obj.getHoTen(), obj.getTenVT(),
					obj.getSoLuong(), Formatter.formatObjecttoMoney(obj.getDonGia()) };
			model.addRow(row);
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
			model.setRowCount(0);
			loadDataIntoTable();
		}
	}

}
