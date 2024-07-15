package model;

import java.sql.Date;

public class PhieuXuatModel {
	private String mapx;
	private Date ngay;
	private String hoTenKH;
	private Integer manv;
	private String maKho;
	public PhieuXuatModel(String mapx, Date ngay, String hoTenKH, Integer manv, String maKho) {
		super();
		this.mapx = mapx;
		this.ngay = ngay;
		this.hoTenKH = hoTenKH;
		this.manv = manv;
		this.maKho = maKho;
	}
	public PhieuXuatModel() {
		// TODO Auto-generated constructor stub
	}
	public String getMapx() {
		return mapx;
	}
	public void setMapx(String mapx) {
		this.mapx = mapx;
	}
	public Date getNgay() {
		return ngay;
	}
	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}
	public String getHoTenKH() {
		return hoTenKH;
	}
	public void setHoTenKH(String hoTenKH) {
		this.hoTenKH = hoTenKH;
	}
	public Integer getManv() {
		return manv;
	}
	public void setManv(Integer manv) {
		this.manv = manv;
	}
	public String getMaKho() {
		return maKho;
	}
	public void setMaKho(String maKho) {
		this.maKho = maKho;
	}
	
	
}
