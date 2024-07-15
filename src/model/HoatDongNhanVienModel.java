package model;

public class HoatDongNhanVienModel {
	private String ngay, maPhieu, loaiPhieu, tenVt, tenKH;
	private int soLuong, donGia, triGia;

	public HoatDongNhanVienModel() {
	}

	public HoatDongNhanVienModel(String ngay, String maPhieu, String loaiPhieu, String tenVt, int soLuong,
			int donGia, int triGia, String tenKH) {
		this.ngay = ngay;
		this.maPhieu = maPhieu;
		this.loaiPhieu = loaiPhieu;
		this.tenVt = tenVt;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.triGia = triGia;
		this.tenKH = tenKH;
	}

	public String getNgay() {
		return ngay;
	}

	public void setNgay(String ngay) {
		this.ngay = ngay;
	}

	public String getMaPhieu() {
		return maPhieu;
	}

	public void setMaPhieu(String maPhieu) {
		this.maPhieu = maPhieu;
	}

	public String getLoaiPhieu() {
		return loaiPhieu;
	}

	public void setLoaiPhieu(String loaiPhieu) {
		this.loaiPhieu = loaiPhieu;
	}

	public String getTenVt() {
		return tenVt;
	}

	public void setTenVt(String tenVt) {
		this.tenVt = tenVt;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public int getDonGia() {
		return donGia;
	}

	public void setDonGia(int donGia) {
		this.donGia = donGia;
	}

	public int getTriGia() {
		return triGia;
	}

	public void setTriGia(int triGia) {
		this.triGia = triGia;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}
	
}
