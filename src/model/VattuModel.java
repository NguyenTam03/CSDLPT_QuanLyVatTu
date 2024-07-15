package model;

public class VattuModel {
	private String mavt;
	private String tenVT;
	private String dvt;
	private Integer soLuongTon;
	
	public VattuModel(String mavt, String tenVT, String dvt, Integer soLuongTon) {
		super();
		this.mavt = mavt;
		this.tenVT = tenVT;
		this.dvt = dvt;
		this.soLuongTon = soLuongTon;
	}
	
	

	public VattuModel() {
		
	}



	public String getMavt() {
		return mavt;
	}

	public void setMavt(String mavt) {
		this.mavt = mavt;
	}

	public String getTenVT() {
		return tenVT;
	}

	public void setTenVT(String tenVT) {
		this.tenVT = tenVT;
	}

	public String getDvt() {
		return dvt;
	}

	public void setDvt(String dvt) {
		this.dvt = dvt;
	}

	public Integer getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(Integer soLuongTon) {
		this.soLuongTon = soLuongTon;
	}
	
	
}	
