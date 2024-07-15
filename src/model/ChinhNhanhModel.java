package model;

public class ChinhNhanhModel {
	private String macn;
	private String chiNhanh;
	private String diaChi;
	private String soDT;
	
	public ChinhNhanhModel(String macn, String chiNhanh, String diaChi, String soDT) {
		this.macn = macn;
		this.chiNhanh = chiNhanh;
		this.diaChi = diaChi;
		this.soDT = soDT;
	}
	public String getMacn() {
		return macn;
	}
	public void setMacn(String macn) {
		this.macn = macn;
	}
	public String getChiNhanh() {
		return chiNhanh;
	}
	public void setChiNhanh(String chiNhanh) {
		this.chiNhanh = chiNhanh;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getSoDT() {
		return soDT;
	}
	public void setSoDT(String soDT) {
		this.soDT = soDT;
	}
	
	
	
	
}
