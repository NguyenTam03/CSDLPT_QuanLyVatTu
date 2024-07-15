package model;

public class CTPNModel extends CTAbstractModel {
	private String mapn;

	public CTPNModel(String mavt, Integer soLuong, Float donGia, String mapn) {
		super(mavt, soLuong, donGia);
		this.mapn = mapn;
	}

	public String getMapn() {
		return mapn;
	}

	public void setMapn(String mapn) {
		this.mapn = mapn;
	}

	
}
