package model;

import java.sql.Date;

public class PhieuLapModel {
	private String mapn;
	private Date ngay;
	private String maSoDDH;
	private Integer manv;
	private String maKho;

	public PhieuLapModel() {
		super();
	}
	public PhieuLapModel(String mapn, Date ngay, String maSoDDH, Integer manv, String maKho) {
		super();
		this.mapn = mapn;
		this.ngay = ngay;
		this.maSoDDH = maSoDDH;
		this.manv = manv;
		this.maKho = maKho;
	}

	public String getMapn() {
		return mapn;
	}

	public void setMapn(String mapn) {
		this.mapn = mapn;
	}

	public Date getNgay() {
		return ngay;
	}

	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}

	public String getMaSoDDH() {
		return maSoDDH;
	}

	public void setMaSoDDH(String maSoDDH) {
		this.maSoDDH = maSoDDH;
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
