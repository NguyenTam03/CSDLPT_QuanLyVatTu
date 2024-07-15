package model;

import java.sql.Date;
import javax.swing.JOptionPane;
import common.method.IValidation;

public class DatHangModel implements IValidation {
	private String maSoDDH;
	private Date ngay;
	private String nhaCC;
	private Integer manv;
	private String makho;

	public static enum ValidateError {
		ERROR_MASODDH, ERROR_NGAY, ERROR_NHACC, ERROR_MANV, ERROR_MAKHO, NO_ERROR
	}

	public static ValidateError validateError;

	public DatHangModel() {

	}

	public DatHangModel(String maSoDDH, Date ngay, String nhaCC, Integer manv, String makho) {
		super();
		this.maSoDDH = maSoDDH;
		this.ngay = ngay;
		this.nhaCC = nhaCC;
		this.manv = manv;
		this.makho = makho;
	}

	public String getMaSoDDH() {
		return maSoDDH;
	}

	public void setMaSoDDH(String maSoDDH) {
		this.maSoDDH = maSoDDH;
	}

	public Date getNgay() {
		return ngay;
	}

	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}

	public String getNhaCC() {
		return nhaCC;
	}

	public void setNhaCC(String nhaCC) {
		this.nhaCC = nhaCC;
	}

	public Integer getManv() {
		return manv;
	}

	public void setManv(Integer manv) {
		this.manv = manv;
	}

	public String getMakho() {
		return makho;
	}

	public void setMakho(String makho) {
		this.makho = makho;
	}


	@Override
	public boolean validate() {
		if (maSoDDH.equals("")) {
			JOptionPane.showMessageDialog(null, "Mã đơn đặt hàng không được bỏ trống.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_MASODDH;
			return false;
		}

		if (!maSoDDH.matches("^[a-zA-Z0-9\\s]+$")) {
			JOptionPane.showMessageDialog(null, "Mã đơn đặt hàng chỉ chứa chữ, số và khoảng trắng.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_MASODDH;
			return false;
		}

		if (ngay.toString() == null) {
			JOptionPane.showMessageDialog(null, "Ngày lập đơn không được bỏ trống.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_NGAY;
			return false;
		}

		if (nhaCC.equals("")) {
			JOptionPane.showMessageDialog(null, "Nhà cung cấp không được bỏ trống.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_NHACC;
			return false;
		}

		if (makho.equals("")) {
			JOptionPane.showMessageDialog(null, "Mã kho không được bỏ trống.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_MAKHO;
			return false;
		}
		validateError = ValidateError.NO_ERROR;
		return true;
	}

}
