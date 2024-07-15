package model;

import java.sql.Date;



public class NhanVienModel {
	private Integer manv;
	private String ho;
	private String ten;
	private String soCMND;
	private String diaChi;
	private Date ngaySinh;
	private Float luong;
	private String macn;
	private Boolean trangThaiXoa;

	public NhanVienModel() {
		super();
	}
	public NhanVienModel(Integer manv, String ho, String ten, String soCMND, String diaChi, Date ngaySinh, Float luong,

			String macn, Boolean trangThaiXoa) {
		super();
		this.manv = manv;
		this.ho = ho;
		this.ten = ten;
		this.soCMND = soCMND;
		this.diaChi = diaChi;
		this.ngaySinh = ngaySinh;
		this.luong = luong;
		this.macn = macn;
		this.trangThaiXoa = trangThaiXoa;
	}
	public Integer getManv() {
		return manv;
	}
	public void setManv(Integer manv) {
		this.manv = manv;
	}
	public String getHo() {
		return ho;
	}
	public void setHo(String ho) {
		this.ho = ho;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getSoCMND() {
		return soCMND;
	}
	public void setSoCMND(String soCMND) {
		this.soCMND = soCMND;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public Date getNgaySinh() {
		return ngaySinh;
	}
	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}
	public Float getLuong() {
		return luong;
	}
	public void setLuong(Float luong) {
		this.luong = luong;
	}
	public String getMacn() {
		return macn;
	}
	public void setMacn(String macn) {
		this.macn = macn;
	}
	public Boolean getTrangThaiXoa() {
		return trangThaiXoa;
	}
	public void setTrangThaiXoa(Boolean trangThaiXoa) {
		this.trangThaiXoa = trangThaiXoa;
	}
	
	
}
