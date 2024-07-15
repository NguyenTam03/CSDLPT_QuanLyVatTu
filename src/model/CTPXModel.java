package model;

public class CTPXModel extends CTAbstractModel {
	private String mapx;


	public CTPXModel() {
		
	}

	public CTPXModel(String mapx, String mavt, Integer soLuong, Float donGia) {
		super(mavt, soLuong, donGia);
		this.mapx = mapx;
	}

	public String getMapx() {
		return mapx;
	}

	public void setMapx(String mapx) {
		this.mapx = mapx;
	}

	
	
	
}
