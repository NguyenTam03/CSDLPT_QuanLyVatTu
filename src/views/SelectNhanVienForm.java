package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import common.method.ISearcher;
import common.method.Searcher;
import controller.ReportNhanVien;
import dao.NhanVienDao;
import main.Program;
import model.NhanVienModel;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectNhanVienForm extends JFrame implements ISearcher {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField textFieldSearch;
	private DefaultTableModel model;
	private NhanVienDao dao;
	private List<NhanVienModel> list;
	private JButton btnChon;
	/**
	 * Create the frame.
	 */
	public SelectNhanVienForm() {
		super("Danh sách thông tin nhân viên");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 814, 409);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		textFieldSearch = new JTextField();
		textFieldSearch.setHorizontalAlignment(SwingConstants.LEFT);
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				search();
			}
		});

		Searcher.focusInput(textFieldSearch);
		
		panel_1.add(textFieldSearch);
		textFieldSearch.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);
//      Chi duoc select 1 hang khong dc select nhieu hang
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel panelFooter = new JPanel();
		contentPane.add(panelFooter, BorderLayout.SOUTH);
//		btnXemTruoc.addActionListener(l -> chooseNhanVien());

		btnChon = new JButton("Chọn");
		btnChon.setBackground(new Color(255, 0, 0));
		btnChon.addActionListener(e->{
			ReportHoatDongNhanVien.getLb_MaNV().setText(table.getValueAt(table.getSelectedRow(), 0).toString());
			String hoTen  = table.getValueAt(table.getSelectedRow(), 1).toString() + " "+ table.getValueAt(table.getSelectedRow(), 2).toString();
			ReportHoatDongNhanVien.getLb_NhanVien().setText(hoTen);
			this.dispose();
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		GroupLayout gl_panelFooter = new GroupLayout(panelFooter);
		gl_panelFooter.setHorizontalGroup(
			gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup()
					.addGap(314)
					.addComponent(btnChon, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(369, Short.MAX_VALUE))
		);
		gl_panelFooter.setVerticalGroup(
			gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnChon, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelFooter.setLayout(gl_panelFooter);
		setLocationRelativeTo(null);
//		init
		dao = NhanVienDao.getInstance();
		list = new ArrayList<NhanVienModel>();
		loadNhanVien();
	}

	private List<NhanVienModel> loadNhanVien(String sql) {
		List<NhanVienModel> list = new ArrayList<NhanVienModel>();
		Program.myReader = Program.ExecSqlDataReader(sql);
		try {
			while (Program.myReader.next()) {
				NhanVienModel nv = new NhanVienModel(Program.myReader.getInt(1), Program.myReader.getString(2),
						Program.myReader.getString(3), Program.myReader.getString(4), Program.myReader.getString(5),
						Program.myReader.getDate(6), Program.myReader.getFloat(7), Program.myReader.getString(8),
						Program.myReader.getBoolean(9));
				list.add(nv);
			}
			return list;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	private void loadNhanVien() {
		String sql = "select * from NhanVien order by ten, ho";
		list = loadNhanVien(sql);
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(dao.getColName().toArray());

		for (NhanVienModel NhanVien : list) {
			Object[] rowData = { NhanVien.getManv(), NhanVien.getHo(), NhanVien.getTen(), NhanVien.getSoCMND(),
					NhanVien.getDiaChi(), NhanVien.getNgaySinh(), NhanVien.getLuong(), NhanVien.getMacn(),
					NhanVien.getTrangThaiXoa() };
			model.addRow(rowData);
		}
		if (list.size() > 0)
			table.getSelectionModel().setSelectionInterval(0, 0);
	}


	@Override
	public void search() {
		String input = textFieldSearch.getText().trim().toLowerCase();
		model.setRowCount(0);

		for (NhanVienModel NhanVien : list) {
			if (NhanVien.getTen().toLowerCase().contains(input)) {
				Object[] rowData = { NhanVien.getManv(), NhanVien.getHo(), NhanVien.getTen(), NhanVien.getSoCMND(),
						NhanVien.getDiaChi(), NhanVien.getNgaySinh(), NhanVien.getLuong(), NhanVien.getMacn(),
						NhanVien.getTrangThaiXoa() };
				model.addRow(rowData);
			}
		}

		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	public JButton getBtnChon() {
		return btnChon;
	}
}
