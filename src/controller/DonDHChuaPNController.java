package controller;

import main.Program;
import model.DonDatHangChuaPNModel;
import model.JasperReportModel;
import net.sf.jasperreports.engine.JRException;
import views.DonDatHangChuaPN;

public class DonDHChuaPNController implements IJasperReportController {
	private JasperReportModel<DonDatHangChuaPNModel> reportModel;
	private DonDatHangChuaPN form;

	@Override
	public void preview() {
		String chiNhanh = form.getComboBox().getItemAt(Program.mChinhanh);
		reportModel.getParameters().put("tenChiNhanh", chiNhanh);
		try {
			reportModel.preViewReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Report successfully");
	}

	@Override
	public void print() {
		String chiNhanh = form.getComboBox().getItemAt(Program.mChinhanh);
		reportModel.getParameters().put("tenChiNhanh", chiNhanh);
		try {
			reportModel.saveReport();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Report successfully");
	}

	public DonDHChuaPNController(DonDatHangChuaPN form) {
		this.reportModel = new JasperReportModel<DonDatHangChuaPNModel>();
		this.form = form;
		reportModel.setFilePath("static/reports/DS_DDH_CHUA_PN.jrxml");
		reportModel.setList(form.getList());
	}

	public void initController() {
		form.getBtnPreView().addActionListener(e -> preview());
		form.getBtnPrint().addActionListener(e -> print());
	}

	public JasperReportModel<DonDatHangChuaPNModel> getReportModel() {
		return reportModel;
	}

	public DonDatHangChuaPN getForm() {
		return form;
	}

	public void setReportModel(JasperReportModel<DonDatHangChuaPNModel> reportModel) {
		this.reportModel = reportModel;
	}

	public void setForm(DonDatHangChuaPN form) {
		this.form = form;
	}

}
