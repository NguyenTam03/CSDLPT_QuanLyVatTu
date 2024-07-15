package views;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;

import common.method.Authorization;
import controller.KhoController;
import dao.KhoDao;
import main.Program;
import model.KhoModel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.ItemEvent;
import java.sql.SQLException;

public class KhoForm extends CommonView<KhoModel, KhoDao> {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldMaKho;
	private JTextField textFieldTenKho;
	private JTextField textFieldDiaChi;
	private JTextField textFieldMaCN;

	/**
	 * Create the panel.
	 */
	public KhoForm() {
		super();

		JPanel panel_2 = new JPanel();
		panel_2.setForeground(new Color(207, 207, 207));
		add(panel_2, BorderLayout.SOUTH);

		JLabel lblMaKho = new JLabel("Mã Kho");
		lblMaKho.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldMaKho = new JTextField();
		textFieldMaKho.setEditable(false);
		textFieldMaKho.setColumns(10);

		JLabel lblTenKho = new JLabel("Tên Kho");
		lblTenKho.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldTenKho = new JTextField();
		textFieldTenKho.setColumns(10);

		JLabel lblDiaChi = new JLabel("Địa Chỉ");
		lblDiaChi.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldDiaChi = new JTextField();
		textFieldDiaChi.setColumns(10);

		if (Authorization.valueOf(Program.mGroup) == Authorization.CONGTY) {
			textFieldDiaChi.setEditable(false);
			textFieldTenKho.setEditable(false);
		}

		JLabel lblMaCN = new JLabel("Mã Chi Nhánh");
		lblMaCN.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textFieldMaCN = new JTextField();
		textFieldMaCN.setEditable(false);
		textFieldMaCN.setColumns(10);
		textFieldMaCN.setText(Program.macn.get(Program.mChinhanh));

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2
				.createSequentialGroup().addGap(85)
				.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_2.createSequentialGroup()
						.addComponent(lblDiaChi, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textFieldDiaChi, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE).addGap(14))
						.addGroup(gl_panel_2.createSequentialGroup()
								.addComponent(lblMaKho, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE).addGap(18)
								.addComponent(textFieldMaKho, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
								.addGap(130)))
				.addGap(38)
				.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
								.addComponent(lblMaCN, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldMaCN, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE).addGap(107))
						.addGroup(gl_panel_2.createSequentialGroup()
								.addComponent(lblTenKho, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textFieldTenKho, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
								.addGap(29)))
				.addGap(194)));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addGap(26)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTenKho, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
								.addComponent(lblMaKho, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
								.addComponent(textFieldTenKho).addComponent(textFieldMaKho))
						.addGap(31)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMaCN, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
								.addComponent(lblDiaChi, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
								.addComponent(textFieldDiaChi).addComponent(textFieldMaCN))
						.addGap(52)));
		panel_2.setLayout(gl_panel_2);

		panel_4.remove(getBtnChuyenChiNhanh());

//		load chi nhánh lên combobox
		loadChiNhanh();
//		load dữ liệu từ chi nhánh lên table
		dao = KhoDao.getInstance();
		loadDataIntoTableKho();

		if (table.getRowCount() == 0) {
			getBtnXoa().setEnabled(false);
		}

//		CONGTY có thể chọn chi nhánh để xem dữ liệu
		comboBox.addItemListener(l -> loadDataOtherServer(l));

//		lắng nghe sự kiện chọn row đồng thời in dữ liệu ra textfield
		selectionListener = e -> {
			textFieldMaKho.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
			textFieldTenKho.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
			textFieldDiaChi.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
		};
		table.getSelectionModel().addListSelectionListener(selectionListener);

		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}
//		Listener event
		KhoController ac = new KhoController(this);
		ac.initController();
	}

	public JTextField getTextFieldMaKho() {
		return textFieldMaKho;
	}

	public JTextField getTextFieldTenKho() {
		return textFieldTenKho;
	}

	public JTextField getTextFieldDiaChi() {
		return textFieldDiaChi;
	}

	public JTextField getTextFieldMaCN() {
		return textFieldMaCN;
	}

	public void loadDataIntoTableKho() {
		loadData();
		for (KhoModel kho : list) {
			Object[] rowData = { kho.getMaKho(), kho.getTenKho(), kho.getDiaChi() };
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

			if (!Program.mlogin.equals(Program.mloginDN)) {
				Program.mlogin = Program.mloginDN;
				Program.password = Program.passwordDN;
			} else {
				Program.mlogin = Program.remotelogin;
				Program.password = Program.remotepassword;
			}
			Program.servername = Program.servers.get(comboBox.getSelectedItem());
			if (Program.Connect() == 0) {
				return;
			}
			
			Program.mChinhanh = comboBox.getSelectedIndex();
			textFieldMaCN.setText(Program.macn.get(Program.mChinhanh));
			table.getSelectionModel().removeListSelectionListener(selectionListener);
			model.setRowCount(0);
			loadDataIntoTableKho();
			table.getSelectionModel().addListSelectionListener(selectionListener);
			if (model.getRowCount() > 0) {
				table.getSelectionModel().setSelectionInterval(0, 0);
			} else {
				textFieldMaKho.setText("");
				textFieldTenKho.setText("");
				textFieldDiaChi.setText("");
			}
		}
	}

}
