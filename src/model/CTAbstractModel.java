package model;

import javax.swing.JOptionPane;

import common.method.IValidation;

public class CTAbstractModel implements IValidation {
	private String mavt;
	private Integer soLuong;
	private Float donGia;
	public static enum ValidateError {
		ERROR_MAVT,
		ERROR_SOLUONG,
		ERROR_DONGIA,
		NO_ERROR;
	}
	public static ValidateError validateError;
	
	public CTAbstractModel() {
		
	}
	
	public CTAbstractModel(String mavt, Integer soLuong, Float donGia) {
		this.mavt = mavt;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

	public String getMavt() {
		return mavt;
	}

	public void setMavt(String mavt) {
		this.mavt = mavt;
	}

	public Integer getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Integer soLuong) {
		this.soLuong = soLuong;
	}

	public Float getDonGia() {
		return donGia;
	}

	public void setDonGia(Float donGia) {
		this.donGia = donGia;
	}

	@Override
	public boolean validate() {
		if (mavt.equals("")) {
			JOptionPane.showMessageDialog(null, "Mã vật tư không được bỏ trống.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_MAVT;
			return false;
		}
		
		if (soLuong <= 0) {
			JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_SOLUONG;
			return false;
		}
		
		if (donGia <= 0) {
			JOptionPane.showMessageDialog(null, "Đơn giá phải lớn hơn 0.", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			validateError = ValidateError.ERROR_DONGIA;
			return false;
		}
		validateError = ValidateError.NO_ERROR;
		return true;
	}
}
