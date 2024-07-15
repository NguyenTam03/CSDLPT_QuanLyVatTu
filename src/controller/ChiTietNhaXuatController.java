package controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import main.Program;
import model.ChiTietNXModel;
import model.JasperReportModel;
import net.sf.jasperreports.engine.JRException;
import views.ChiTietNhapXuat;

public class ChiTietNhaXuatController implements IJasperReportController {
	private JasperReportModel<ChiTietNXModel> reportModel;
	private ChiTietNhapXuat form;
	private String tuNgay, denNgay;

	public JasperReportModel<ChiTietNXModel> getReportModel() {
		return reportModel;
	}

	public void setReportModel(JasperReportModel<ChiTietNXModel> reportModel) {
		this.reportModel = reportModel;
	}

	public ChiTietNhapXuat getForm() {
		return form;
	}

	public void setForm(ChiTietNhapXuat form) {
		this.form = form;
	}

	public String getTuNgay() {
		return tuNgay;
	}

	public void setTuNgay(String tuNgay) {
		this.tuNgay = tuNgay;
	}

	public String getDenNgay() {
		return denNgay;
	}

	public void setDenNgay(String denNgay) {
		this.denNgay = denNgay;
	}

	public ChiTietNhaXuatController(ChiTietNhapXuat form) {
		this.form = form;
		reportModel = new JasperReportModel<ChiTietNXModel>();
		reportModel.setFilePath("static/reports/ChiTietNhapXuat.jrxml");
	}

	public void initController() {
		form.getBtnXemTruoc().addActionListener(e -> {
			if (form.getTuNgay().getDate() != null && form.getDenNgay().getDate() != null) {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				tuNgay = format.format(form.getTuNgay().getDate());
				denNgay = format.format(form.getDenNgay().getDate());

				preview();
			}
		});

		form.getBtnXuat().addActionListener(e -> {
			if (form.getTuNgay().getDate() != null && form.getDenNgay().getDate() != null) {
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				tuNgay = format.format(form.getTuNgay().getDate());
				denNgay = format.format(form.getDenNgay().getDate());

				print();
			}
		});
	}

	private void getData() {
		if (Program.mGroup.equals("CONGTY")) {
			Program.mlogin = "my_admin";
			Program.password = "12";
			Program.Connect();
		}
		if (!reportModel.getList().isEmpty())
			reportModel.getList().clear();
		String sql = "";
		String chiNhanh = "";
		String loaiPhieu = (form.getComboBox().getSelectedItem().toString() == "NHẬP") ? "NHAP" : "XUAT";

		if (Program.mGroup.equals("CONGTY")) {
			sql = "EXEC sp_ChiTietSoLuongTriGiaHangNhapXuat_SongSong ?, ?, ?";
			Program.myReader = Program.ExecSqlDataReader(sql, loaiPhieu, tuNgay, denNgay);
		} else {
			sql = "EXEC sp_ChiTietSoLuongTriGiaHangNhapXuat ?, ?, ?";
			Program.myReader = Program.ExecSqlDataReader(sql, loaiPhieu, tuNgay, denNgay);
			chiNhanh = form.getComboBoxChiNhanh().getItemAt(Program.mChinhanh);
		}

		try {
			while (Program.myReader.next()) {
				ChiTietNXModel model = new ChiTietNXModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getInt(3), Program.myReader.getInt(4));
				reportModel.getList().add(model);
			}

			reportModel.getParameters().put("tenChiNhanh", chiNhanh);
			reportModel.getParameters().put("loaiPhieu", form.getComboBox().getSelectedItem().toString());
			reportModel.getParameters().put("tuNgay", tuNgay);
			reportModel.getParameters().put("denNgay", denNgay);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void preview() {
		if (form.getDenNgay().getDate().before(form.getTuNgay().getDate())) {
			JOptionPane.showMessageDialog(null, "\"Đến Ngày\" phải lớn hơn \"Từ Ngày\"!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		getData();
		try {
			reportModel.preViewReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void print() {
		if (form.getDenNgay().getDate().before(form.getTuNgay().getDate())) {
			JOptionPane.showMessageDialog(null, "\"Đến Ngày\" phải lớn hơn \"Từ Ngày\"!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		getData();
		try {
			reportModel.saveReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
