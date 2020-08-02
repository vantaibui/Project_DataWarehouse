package Model;

public class CourseRegistration {
	private int maDK;
	private String mssv;
	private String maLH;
	private String thoiGianDangKy;
	public CourseRegistration() {
		super();
	}
	public CourseRegistration(int maDK, String mssv, String maLH, String thoiGianDangKy) {
		super();
		this.maDK = maDK;
		this.mssv = mssv;
		this.maLH = maLH;
		this.thoiGianDangKy = thoiGianDangKy;
	}
	public int getMaDK() {
		return maDK;
	}
	public void setMaDK(int maDK) {
		this.maDK = maDK;
	}
	public String getMssv() {
		return mssv;
	}
	public void setMssv(String mssv) {
		this.mssv = mssv;
	}
	public String getMaLH() {
		return maLH;
	}
	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}
	public String getThoiGianDangKy() {
		return thoiGianDangKy;
	}
	public void setThoiGianDangKy(String thoiGianDangKy) {
		this.thoiGianDangKy = thoiGianDangKy;
	}
	@Override
	public String toString() {
		return "CourseRegistration [maDK=" + maDK + ", mssv=" + mssv + ", maLH=" + maLH + ", thoiGianDangKy="
				+ thoiGianDangKy + "]";
	}
	
}
