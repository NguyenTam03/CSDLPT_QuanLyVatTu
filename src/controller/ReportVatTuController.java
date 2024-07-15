package controller;

import java.sql.SQLException;

import main.Program;
import model.JasperReportModel;
import model.VattuModel;
import net.sf.jasperreports.engine.JRException;
import views.ReportDanhSachVatTu;

public class ReportVatTuController implements IJasperReportController {
	private ReportDanhSachVatTu danhSachVatTu;
	private JasperReportModel<VattuModel> reportModel;

	public ReportVatTuController(ReportDanhSachVatTu danhSachVatTu) {
		this.danhSachVatTu = danhSachVatTu;
		reportModel = new JasperReportModel<>();
		reportModel.setFilePath("static/reports/ReportDSVT.jrxml");
	}

	public void initController() {
		danhSachVatTu.getBtnXemTruoc().addActionListener(e -> preview());
		danhSachVatTu.getBtnXuatBan().addActionListener(e -> print());
	}

	private void getData() {
		if (!reportModel.getList().isEmpty())
			reportModel.getList().clear();
		String sql = "SELECT MAVT, TENVT, DVT, SOLUONGTON FROM Vattu WITH (INDEX = UK_TENVT)";
		Program.myReader = Program.ExecSqlDataReader(sql);

		try {
			while (Program.myReader.next()) {
				VattuModel model = new VattuModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getString(3), Program.myReader.getInt(4));

				reportModel.getList().add(model);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void preview() {
		getData();
		try {
			reportModel.preViewReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void print() {
		getData();
		try {
			reportModel.saveReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public JasperReportModel<VattuModel> getReportModel() {
		return reportModel;
	}

	public void setReportModel(JasperReportModel<VattuModel> reportModel) {
		this.reportModel = reportModel;
	}

	public void setDanhSachVatTu(ReportDanhSachVatTu danhSachVatTu) {
		this.danhSachVatTu = danhSachVatTu;
	}
	
	public ReportDanhSachVatTu getDanhSachVatTu() {
		return danhSachVatTu;
	}
}
