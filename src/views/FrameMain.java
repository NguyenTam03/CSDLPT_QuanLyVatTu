package views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import controller.NavController;
import main.Program;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class FrameMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblInfoNV;
	private JPanel panel_Main;
	private JTabbedPane tabbedPane_Main;
	private JPanel panel_VT, panel_NV, panel_Kho, panel_dathang, panel_phieulap, panel_phieuxuat;
	private JMenu mnNhanVien, mnVatTu, mnKho, mnLapPhieu, mnLogout, mnExit, mnCreateTK;
	private JMenuItem mntmDatHang, mntmPhieuLap, mntmPhieuXuat;
	private JMenu mnDeleteUser;
	private JMenu mnChangePassword;
	private JMenu mnNhanVienList;
	private JMenu mnVatTuList;
	private JMenu mnCTNX;
	private JMenu mnDHKhongPN;
	private JMenu mnHdNV;
	private JMenu mnTongHopNX;

	public FrameMain() {
		setTitle("Quản Lý Vật Tư");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1169, 690);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(4, 4, 3, 4));
		contentPane.add(panel, BorderLayout.NORTH);

		UIManager.put("TabbedPane.selected", Color.WHITE);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 20));

//		------------Nhap Xuat---------------
		JMenuBar menuBarNhapXuat = new JMenuBar();
		tabbedPane.addTab("Nhập Xuất", null, menuBarNhapXuat, null);

		mnNhanVien = new JMenu("Nhân viên");
		mnNhanVien.setHorizontalAlignment(SwingConstants.CENTER);
		mnNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnNhanVien.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/staff.png")));
		menuBarNhapXuat.add(mnNhanVien);

		mnVatTu = new JMenu("Vật tư");
		mnVatTu.setHorizontalAlignment(SwingConstants.CENTER);
		mnVatTu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnVatTu.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/material.png")));
		menuBarNhapXuat.add(mnVatTu);

		mnKho = new JMenu("Kho");
		mnKho.setHorizontalAlignment(SwingConstants.CENTER);
		mnKho.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnKho.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/warehouse.png")));
		menuBarNhapXuat.add(mnKho);

		mnLapPhieu = new JMenu("Lập phiếu");
		mnLapPhieu.setHorizontalAlignment(SwingConstants.CENTER);
		mnLapPhieu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnLapPhieu.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/report.png")));
		menuBarNhapXuat.add(mnLapPhieu);

		mntmDatHang = new JMenuItem("Đặt hàng");
		mntmDatHang.setInheritsPopupMenu(true);
		mntmDatHang.setIgnoreRepaint(true);
		mntmDatHang.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/shopping-cart.png")));
		mnLapPhieu.add(mntmDatHang);

		mntmPhieuLap = new JMenuItem("Phiếu lập");
//		mntmPhieuLap.setInheritsPopupMenu(true);
//		mntmPhieuLap.setIgnoreRepaint(true);
		mntmPhieuLap.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/invoice.png")));
		mnLapPhieu.add(mntmPhieuLap);

		mntmPhieuXuat = new JMenuItem("Phiếu xuất");
		mntmPhieuXuat.setInheritsPopupMenu(true);
		mntmPhieuXuat.setIgnoreRepaint(true);
		mntmPhieuXuat.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/invoice.png")));
		mnLapPhieu.add(mntmPhieuXuat);

		panel.setLayout(new BorderLayout(0, 0));
		panel.add(tabbedPane);

//		------------He Thong---------------
		JMenuBar menuBarHeThong = new JMenuBar();
		tabbedPane.addTab("Hệ Thống", null, menuBarHeThong, null);

		mnLogout = new JMenu("Đăng xuất");
		mnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnLogout.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/log-out.png")));
		menuBarHeThong.add(mnLogout);

		mnExit = new JMenu("Thoát");
		mnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnExit.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/exit.png")));
		menuBarHeThong.add(mnExit);

		mnCreateTK = new JMenu("Tạo tài khoản");
		mnCreateTK.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnCreateTK.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/plus.png")));
		menuBarHeThong.add(mnCreateTK);

		mnDeleteUser = new JMenu("Xóa tài khoản");
		mnDeleteUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		mnDeleteUser.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/delete_user.png")));
		menuBarHeThong.add(mnDeleteUser);

		mnChangePassword = new JMenu("Đổi mật khẩu");
		mnChangePassword.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/reset-password-menu.png")));
		menuBarHeThong.add(mnChangePassword);

		if (Program.mGroup.equals("USER")) {
			mnCreateTK.setEnabled(false);
			mnDeleteUser.setEnabled(false);
		}

//		------------Bao Cao---------------
		JMenuBar menuBarReport = new JMenuBar();
		tabbedPane.addTab("Báo Cáo", null, menuBarReport, null);

		mnNhanVienList = new JMenu("Danh sách nhân viên");
		mnNhanVienList.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/staff.png")));
		menuBarReport.add(mnNhanVienList);

		mnVatTuList = new JMenu("Danh sách vật tư");
		mnVatTuList.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/material.png")));
		menuBarReport.add(mnVatTuList);

		mnCTNX = new JMenu("Chi tiết nhập xuất");
		mnCTNX.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/activity.png")));
		menuBarReport.add(mnCTNX);

		mnDHKhongPN = new JMenu("Đơn hàng không phiếu nhập");
		mnDHKhongPN.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/carts.png")));
		menuBarReport.add(mnDHKhongPN);

		mnHdNV = new JMenu("Hoạt động nhân viên");
		mnHdNV.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/bar-chart.png")));
		menuBarReport.add(mnHdNV);

		mnTongHopNX = new JMenu("Tổng hợp nhập xuất");
		mnTongHopNX.setIcon(new ImageIcon(FrameMain.class.getResource("/imgs/report.png")));
		menuBarReport.add(mnTongHopNX);

		panel_Main = new JPanel();
		contentPane.add(panel_Main, BorderLayout.CENTER);
		panel_Main.setLayout(new BorderLayout(0, 0));

		tabbedPane_Main = new JTabbedPane(JTabbedPane.TOP);
		panel_Main.add(tabbedPane_Main, BorderLayout.CENTER);

		panel_VT = new JPanel();
		panel_VT.setLayout(new BorderLayout(0, 0));

		panel_NV = new JPanel();
		panel_NV.setLayout(new BorderLayout(0, 0));

		panel_Kho = new JPanel();
		panel_Kho.setLayout(new BorderLayout(0, 0));

		panel_dathang = new JPanel();
		panel_dathang.setLayout(new BorderLayout(0, 0));

		panel_phieuxuat = new JPanel();
		panel_phieuxuat.setLayout(new BorderLayout(0, 0));
		panel_phieulap = new JPanel();
		panel_phieulap.setLayout(new BorderLayout(0, 0));

		lblInfoNV = new JLabel(
				"MANV: " + Program.username + " HOTEN: " + Program.mHoten + " VAI TRO: " + Program.mGroup);
		lblInfoNV.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblInfoNV.setBackground(Color.WHITE);
		contentPane.add(lblInfoNV, BorderLayout.SOUTH);

		setLocationRelativeTo(null);

		NavController ac = new NavController(this);
		ac.initController();
	}


	public JMenu getMnNhanVien() {
		return mnNhanVien;
	}

	public JMenu getMnVatTu() {
		return mnVatTu;
	}

	public JMenu getMnKho() {
		return mnKho;
	}

	public JMenu getMnLogout() {
		return mnLogout;
	}

	public JMenu getMnExit() {
		return mnExit;
	}

	public JMenu getMnCreateTK() {
		return mnCreateTK;
	}

	public JMenu getMnChangePassword() {
		return mnChangePassword;
	}

	public JMenuItem getMntmDatHang() {
		return mntmDatHang;
	}

	public JMenuItem getMntmPhieuLap() {
		return mntmPhieuLap;
	}

	public JMenuItem getMntmPhieuXuat() {
		return mntmPhieuXuat;
	}

	public JTabbedPane getTabbedPane_Main() {
		return tabbedPane_Main;
	}

	public JPanel getPanel_VT() {
		return panel_VT;
	}

	public JPanel getPanel_NV() {
		return panel_NV;
	}

	public JPanel getPanel_Kho() {
		return panel_Kho;
	}

	public JPanel getPanel_dathang() {
		return panel_dathang;
	}

	public JPanel getPanel_phieuxuat() {
		return panel_phieuxuat;
	}

	public JPanel getPanel_phieulap() {
		return panel_phieulap;
	}

	public JMenu getMnDeleteUser() {
		return mnDeleteUser;
	}

	public JMenu getMnNhanVienList() {
		return mnNhanVienList;
	}

	public JMenu getMnVatTuList() {
		return mnVatTuList;
	}

	public JMenu getMnCTNX() {
		return mnCTNX;
	}

	public JMenu getMnDHKhongPN() {
		return mnDHKhongPN;
	}

	public JMenu getMnHdNV() {
		return mnHdNV;
	}

	public JMenu getMnTongHopNX() {
		return mnTongHopNX;
	}

}
