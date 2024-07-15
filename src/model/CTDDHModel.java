package model;


public class CTDDHModel extends CTAbstractModel {
	private String maSoDDH;
	
	public CTDDHModel() {
		super();
	}
	
	public CTDDHModel(String maSoDDH, String mavt, Integer soLuong, Float donGia) {
		super(mavt, soLuong, donGia);
		this.maSoDDH = maSoDDH;
	}

	public String getMaSoDDH() {
		return maSoDDH;
	}

	public void setMaSoDDH(String maSoDDH) {
		this.maSoDDH = maSoDDH;
	}
	
}
