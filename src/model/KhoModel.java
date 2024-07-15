package model;

public class KhoModel {
	private String maKho;
	private String tenKho;
	private String diaChi;
	private String macn;
	
	public KhoModel() {
		super();
	}
	public KhoModel(String maKho, String tenKho, String diaChi, String macn) {
		super();
		this.maKho = maKho;
		this.tenKho = tenKho;
		this.diaChi = diaChi;
		this.macn = macn;
	}
	public String getMaKho() {
		return maKho;
	}
	public void setMaKho(String maKho) {
		this.maKho = maKho;
	}
	public String getTenKho() {
		return tenKho;
	}
	public void setTenKho(String tenKho) {
		this.tenKho = tenKho;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getMacn() {
		return macn;
	}
	public void setMacn(String macn) {
		this.macn = macn;
	}
	
	
}
