package model;


public class DonDatHangChuaPNModel {
	private String maDDH;
	private String ngayLap;
	private String nhaCC;
	private String hoTen;
	private String tenVT;
	private int soLuong;
	private float donGia;

	public DonDatHangChuaPNModel(String maDDH, String ngayLap, String nhaCC, String hoTen, String tenVT, int soLuong,
			float donGia) {
		this.maDDH = maDDH;
		this.ngayLap = ngayLap;
		this.nhaCC = nhaCC;
		this.hoTen = hoTen;
		this.tenVT = tenVT;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}

	public DonDatHangChuaPNModel() {
	}

	public String getMaDDH() {
		return maDDH;
	}

	public String getNgayLap() {
		return ngayLap;
	}

	public String getNhaCC() {
		return nhaCC;
	}

	public String getHoTen() {
		return hoTen;
	}

	public String getTenVT() {
		return tenVT;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public float getDonGia() {
		return donGia;
	}

	public void setMaDDH(String maDDH) {
		this.maDDH = maDDH;
	}

	public void setNgayLap(String ngayLap) {
		this.ngayLap = ngayLap;
	}

	public void setNhaCC(String nhaCC) {
		this.nhaCC = nhaCC;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public void setTenVT(String tenVT) {
		this.tenVT = tenVT;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public void setDonGia(float donGia) {
		this.donGia = donGia;
	}

}
