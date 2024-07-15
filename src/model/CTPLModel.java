package model;

public class CTPLModel extends CTAbstractModel {
	private String mapn;

	public CTPLModel() {
		super();
	}
	public CTPLModel(String mapn, String mavt, Integer soLuong, Float donGia) {
		super(mavt, soLuong, donGia);
		this.mapn = mapn;
	}

	public String getMaPN() {
		return mapn;
	}

	public void setMaPN(String mapn) {
		this.mapn = mapn;
	}

	
}
