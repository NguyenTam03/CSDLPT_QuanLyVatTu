package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import common.method.ISearcher;
import common.method.Searcher;
import controller.ReportVatTuController;
import dao.VatTuDao;
import model.VattuModel;

public class ReportDanhSachVatTu extends JFrame implements ISearcher {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextField textFieldSearch;
	private DefaultTableModel model;
	private JButton btnXemTruoc;
	private JButton btnXuatBan;
	private List<VattuModel> vatTuList;
	private VatTuDao vtDao;
	public static boolean isVisible = false;

	/**
	 * Create the frame.
	 */
	public ReportDanhSachVatTu() {
		super("Danh sách thông tin vật tư");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 814, 409);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelHeader = new JPanel();
		contentPane.add(panelHeader, BorderLayout.NORTH);

		JLabel lblHeader = new JLabel("Danh Sách Thông Tin Vật Tư");
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 45));
		GroupLayout gl_panelHeader = new GroupLayout(panelHeader);
		gl_panelHeader.setHorizontalGroup(gl_panelHeader.createParallelGroup(Alignment.LEADING).addComponent(lblHeader,
				GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE));
		gl_panelHeader.setVerticalGroup(gl_panelHeader.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelHeader.createSequentialGroup()
						.addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panelHeader.setLayout(gl_panelHeader);

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

		btnXemTruoc = new JButton("Xem trước");
		btnXemTruoc.setBackground(SystemColor.textHighlight);

		btnXuatBan = new JButton("Xuất bản");
		btnXuatBan.setBackground(new Color(255, 0, 0));

		GroupLayout gl_panelFooter = new GroupLayout(panelFooter);
		gl_panelFooter.setHorizontalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup().addGap(189)
						.addComponent(btnXemTruoc, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
						.addComponent(btnXuatBan, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
						.addGap(134)));
		gl_panelFooter.setVerticalGroup(gl_panelFooter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelFooter.createSequentialGroup().addGap(4)
						.addGroup(gl_panelFooter.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnXemTruoc, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
								.addComponent(btnXuatBan, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		panelFooter.setLayout(gl_panelFooter);
		setLocationRelativeTo(null);

		vtDao = VatTuDao.getInstance();
		loadData();
		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}
		ReportVatTuController ac = new ReportVatTuController(this);
		ac.initController();

	}

	private void loadData() {
		String sql = "SELECT MAVT, TENVT, DVT, SOLUONGTON FROM Vattu WITH (INDEX = UK_TENVT)";
		vatTuList = vtDao.selectByConditionForPX(sql);
		model.setColumnIdentifiers(vtDao.getColName().toArray());
		for (VattuModel vt : vatTuList) {
			Object[] rowData = { vt.getMavt(), vt.getTenVT(), vt.getDvt(), vt.getSoLuongTon() };
			model.addRow(rowData);
		}
	}

	@Override
	public void search() {
		String input = textFieldSearch.getText().trim().toLowerCase();
		model.setRowCount(0);

		for (VattuModel vt : vatTuList) {
			if (vt.getTenVT().toLowerCase().contains(input) || vt.getMavt().toLowerCase().contains(input)
					|| vt.getDvt().toLowerCase().contains(input)
					|| vt.getSoLuongTon().toString().toLowerCase().contains(input)) {
				Object[] rowData = { vt.getMavt(), vt.getTenVT(), vt.getDvt(), vt.getSoLuongTon() };
				model.addRow(rowData);
			}
		}

		if (table.getRowCount() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		}

	}

	public JButton getBtnXemTruoc() {
		return btnXemTruoc;
	}

	public JButton getBtnXuatBan() {
		return btnXuatBan;
	}

}
