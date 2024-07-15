package controller;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import main.Program;
import model.HoatDongNhanVienModel;
import model.JasperReportModel;
import net.sf.jasperreports.engine.JRException;
import views.ReportHoatDongNhanVien;

public class ReportHoatDongNhanVienController implements IJasperReportController {
	private JasperReportModel<HoatDongNhanVienModel> reportModel;
	private String hoTen, maNV;
	private String tuNgay, denNgay, ngayHienTai;
	private ReportHoatDongNhanVien form;

	public ReportHoatDongNhanVienController(ReportHoatDongNhanVien form) {
		this.form = form;
		reportModel = new JasperReportModel<>();
		reportModel.setFilePath("static/reports/HDNhanVien.jrxml");
	}

	public void initController() {
		form.getBtnXuat().addActionListener((e) -> {
			if (form.getTuNgay().getDate() != null && form.getTuNgay().getDate() != null && ReportHoatDongNhanVien.getLb_MaNV().getText() != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				tuNgay = formatter.format(form.getTuNgay().getDate());
				denNgay = formatter.format(form.getDenNgay().getDate());
				print();
			}
		});

		form.getBtnXemTruoc().addActionListener((e) -> {
			if (form.getTuNgay().getDate() != null && form.getTuNgay().getDate() != null && ReportHoatDongNhanVien.getLb_MaNV().getText() != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				tuNgay = formatter.format(form.getTuNgay().getDate());
				denNgay = formatter.format(form.getDenNgay().getDate());
				preview();
			}
		});
	}
	private void getData() {
		if (!reportModel.getList().isEmpty()) {
			reportModel.getList().clear();
		}
		maNV = ReportHoatDongNhanVien.getLb_MaNV().getText();
		hoTen = ReportHoatDongNhanVien.getLb_NhanVien().getText();
		ngayHienTai = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String sql = "EXEC hdNhanVien ?, ?, ?";
		Program.myReader = Program.ExecSqlDataReader(sql,maNV ,tuNgay, denNgay);
		try {
			while (Program.myReader.next()) {
				HoatDongNhanVienModel model = new HoatDongNhanVienModel();
				model.setNgay(Program.myReader.getString(1));
				model.setMaPhieu(Program.myReader.getString(2));
				model.setLoaiPhieu(Program.myReader.getString(3));
				model.setTenKH(Program.myReader.getString(4));
				model.setTenVt(Program.myReader.getString(5));				
				model.setSoLuong(Program.myReader.getInt(6));
				model.setDonGia(Program.myReader.getInt(7));
				model.setTriGia(Program.myReader.getInt(8));
				reportModel.getList().add(model);
			}
			reportModel.getParameters().put("MaNV", maNV);
			reportModel.getParameters().put("HoTen", hoTen);
			reportModel.getParameters().put("NgayBD", tuNgay);
			reportModel.getParameters().put("NgayKT", denNgay);
			reportModel.getParameters().put("NgayHT", ngayHienTai);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void print() {
		getData();
		try {
			reportModel.saveReport();
			JOptionPane.showConfirmDialog(null, "Lưu Thành Công", "Thông Báo", JOptionPane.CLOSED_OPTION);
		    form.dispose();
		} catch (JRException e) {
			JOptionPane.showConfirmDialog(null, "Lưu Thất Bại", "Thông Báo", JOptionPane.CLOSED_OPTION);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Report successfully");
		
	}

	@Override
	public void preview() {
		getData();
		try {
			reportModel.preViewReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Report successfully");
	}

}
