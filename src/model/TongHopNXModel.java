package model;

public class TongHopNXModel {
	private String ngay;
	private float nhap;
	private float tyLeNhap;
	private float xuat;
	private float tyLeXuat;

	public TongHopNXModel() {
	}

	public TongHopNXModel(String ngay, float nhap, float tyLeNhap, float xuat, float tyLeXuat) {
		this.ngay = ngay;
		this.nhap = nhap;
		this.tyLeNhap = tyLeNhap;
		this.xuat = xuat;
		this.tyLeXuat = tyLeXuat;
	}

	public String getNgay() {
		return ngay;
	}

	public float getNhap() {
		return nhap;
	}

	public float getTyLeNhap() {
		return tyLeNhap;
	}

	public float getXuat() {
		return xuat;
	}

	public float getTyLeXuat() {
		return tyLeXuat;
	}

	public void setNgay(String ngay) {
		this.ngay = ngay;
	}

	public void setNhap(float nhap) {
		this.nhap = nhap;
	}

	public void setTyLeNhap(float tyLeNhap) {
		this.tyLeNhap = tyLeNhap;
	}

	public void setXuat(float xuat) {
		this.xuat = xuat;
	}

	public void setTyLeXuat(float tyLeXuat) {
		this.tyLeXuat = tyLeXuat;
	}

}
