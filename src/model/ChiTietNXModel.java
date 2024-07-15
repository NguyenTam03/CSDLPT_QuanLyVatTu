package model;

public class ChiTietNXModel {
	private String thangNam;
	private String tenVT;
	private int tongSoLuong;
	private int tongTriGia;
	public ChiTietNXModel(String thangNam, String tenVT, int tongSoLuong, int tongTriGia) {
		this.thangNam = thangNam;
		this.tenVT = tenVT;
		this.tongSoLuong = tongSoLuong;
		this.tongTriGia = tongTriGia;
	}
	public ChiTietNXModel() {
	
	}
	public String getThangNam() {
		return thangNam;
	}
	public void setThangNam(String thangNam) {
		this.thangNam = thangNam;
	}
	public String getTenVT() {
		return tenVT;
	}
	public void setTenVT(String tenVT) {
		this.tenVT = tenVT;
	}
	public int getTongSoLuong() {
		return tongSoLuong;
	}
	public void setTongSoLuong(int tongSoLuong) {
		this.tongSoLuong = tongSoLuong;
	}
	public int getTongTriGia() {
		return tongTriGia;
	}
	public void setTongTriGia(int tongTriGia) {
		this.tongTriGia = tongTriGia;
	}
	
	
}
