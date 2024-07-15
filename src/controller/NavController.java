package controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import main.Program;
import views.ChangePasswordForm;
import views.ChiTietNhapXuat;
import views.CreateLoginForm;
import views.DatHangForm;
import views.DonDatHangChuaPN;
import views.FrameMain;
import views.KhoForm;
import views.LoginForm;
import views.NhanVienForm;
import views.NhanVienOptionForm;
import views.PhieuXuatForm;
import views.TongHopNhapXuat;
import views.ReportDanhSachNhanVien;
import views.ReportDanhSachVatTu;
import views.ReportHoatDongNhanVien;
import views.PhieuLapForm;
import views.VatTuForm;

public class NavController {
	private FrameMain frmMain;

	public NavController(FrameMain frmMain) {
		this.frmMain = frmMain;
	}

	public void initController() {
		frmMain.getMnLogout().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				logout();
			}
		});

		frmMain.getMnExit().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exitFrameMain();
			}
		});

		frmMain.getMnChangePassword().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!ChangePasswordForm.isVisible) {
					ChangePasswordForm.isVisible = true;
					ChangePasswordForm changePasswordForm = new ChangePasswordForm();
					changePasswordForm.setVisible(true);

				}
			}
		});

		frmMain.getMnDHKhongPN().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!DonDatHangChuaPN.isVisible) {
					DonDatHangChuaPN.isVisible = true;
					DonDatHangChuaPN form = new DonDatHangChuaPN();
					form.setVisible(true);
				}
			}
		});

		frmMain.getMnTongHopNX().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TongHopNhapXuat form = null;
				if (!TongHopNhapXuat.isVisible) {
					TongHopNhapXuat.isVisible = true;
					form = new TongHopNhapXuat();
					form.setVisible(true);
				}
			}
		});

		frmMain.getMnNhanVienList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				if (!ReportDanhSachNhanVien.isVisible) {
					ReportDanhSachNhanVien.isVisible = true;
					ReportDanhSachNhanVien reportNhanVien = new ReportDanhSachNhanVien();
					reportNhanVien.setVisible(true);
				}
				
			}
		});
		
		frmMain.getMnHdNV().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!ReportHoatDongNhanVien.isVisible) {
					ReportHoatDongNhanVien reportHoatDongNhanVien = new ReportHoatDongNhanVien();
					reportHoatDongNhanVien.setVisible(true);
				}
			}
		});

		frmMain.getMnVatTuList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!ReportDanhSachVatTu.isVisible) {
					ReportDanhSachVatTu form = new ReportDanhSachVatTu();
					form.setVisible(true);
				}	
			}
		});
		
		frmMain.getMnCTNX().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!ChiTietNhapXuat.isVisible) {
					ChiTietNhapXuat form = new ChiTietNhapXuat();
					form.setVisible(true);
				}
			}
		});
		

		if (!Program.mGroup.equals("USER")) {
			frmMain.getMnCreateTK().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					createLogin();
				}
			});
			frmMain.getMnDeleteUser().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					deleteLogin();
				}
			});
		}

		clickedComponentShowTab("Nhân viên", frmMain.getMnNhanVien(), frmMain.getPanel_NV(), NhanVienForm.class);
		// --------
		// mouse listener vat tu
		clickedComponentShowTab("Vật tư", frmMain.getMnVatTu(), frmMain.getPanel_VT(), VatTuForm.class);

		// ---------------
		// mouse listener kho hang
		clickedComponentShowTab("Kho", frmMain.getMnKho(), frmMain.getPanel_Kho(), KhoForm.class);

		// ---------------------
		clickedMenuItem("Đặt hàng", frmMain.getMntmDatHang(), frmMain.getPanel_dathang(), DatHangForm.class);

		clickedMenuItem("Phiếu xuất", frmMain.getMntmPhieuXuat(), frmMain.getPanel_phieuxuat(), PhieuXuatForm.class);
		clickedMenuItem("Phiếu Lập", frmMain.getMntmPhieuLap(), frmMain.getPanel_phieulap(), PhieuLapForm.class);

	}

	private void logout() {
		frmMain.dispose();
		try {
			Program.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		new LoginForm().setVisible(true);
	}

	private void exitFrameMain() {
		try {
			Program.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void createLogin() {
		if (!CreateLoginForm.isVisible) {
			CreateLoginForm.isVisible = true;
			CreateLoginForm form = new CreateLoginForm();
			form.setVisible(true);

		}
	}

	private void deleteLogin() {
		if (!NhanVienOptionForm.isVisible) {
			NhanVienOptionForm.isVisible = true;
			NhanVienOptionForm form = new NhanVienOptionForm(true);
			form.setVisible(true);
		}
	}

	private void clickedComponentShowTab(String label, JMenu t, JPanel t1, Class<?> formClass) {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Component form;
				try {
					if (t1.getComponents().length == 0) {
						form = (Component) formClass.getDeclaredConstructor().newInstance();
						t1.add(form, BorderLayout.CENTER);
					} else if (t1.getComponents().length > 0 && Program.mGroup.equals("CONGTY")) {
						t1.removeAll();
						form = (Component) formClass.getDeclaredConstructor().newInstance();
						t1.add(form, BorderLayout.CENTER);
					}

					frmMain.getTabbedPane_Main().addTab(label, null, t1);
					frmMain.getTabbedPane_Main().setSelectedComponent(t1);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private void clickedMenuItem(String label, JMenuItem item, JPanel t1, Class<?> formClass) {
		item.addActionListener(l -> {
			Component form;
			try {
				if (t1.getComponents().length == 0) {
					form = (Component) formClass.getDeclaredConstructor().newInstance();
					t1.add(form, BorderLayout.CENTER);
				} else if (t1.getComponents().length > 0 && Program.mGroup.equals("CONGTY")) {
					t1.removeAll();
					form = (Component) formClass.getDeclaredConstructor().newInstance();
					t1.add(form, BorderLayout.CENTER);
				}

				frmMain.getTabbedPane_Main().addTab(label, null, t1);
				frmMain.getTabbedPane_Main().setSelectedComponent(t1);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
	}
}
