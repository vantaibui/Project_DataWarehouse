package Model;

public class MonHoc {
	private int num;
	private String maMH;
	private String tenMH;
	private int tinChi;
	private String khoa_BMQuanLi;
	private String khoa_BMDangSuDung;
	private String ghiChu;
	
	public MonHoc() {
		super();
	}

	public MonHoc( String maMH, String tenMH, int tinChi, String khoa_BMQuanLi, String khoa_BMDangSuDung,
			String ghiChu) {
		super();
		this.maMH = maMH;
		this.tenMH = tenMH;
		this.tinChi = tinChi;
		this.khoa_BMQuanLi = khoa_BMQuanLi;
		this.khoa_BMDangSuDung = khoa_BMDangSuDung;
		this.ghiChu = ghiChu;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getMaMH() {
		return maMH;
	}

	public void setMaMH(String maMH) {
		this.maMH = maMH;
	}

	public String getTenMH() {
		return tenMH;
	}

	public void setTenMH(String tenMH) {
		this.tenMH = tenMH;
	}

	public int getTinChi() {
		return tinChi;
	}

	public void setTinChi(int tinChi) {
		this.tinChi = tinChi;
	}

	public String getKhoa_BMQuanLi() {
		return khoa_BMQuanLi;
	}

	public void setKhoa_BMQuanLi(String khoa_BMQuanLi) {
		this.khoa_BMQuanLi = khoa_BMQuanLi;
	}

	public String getKhoa_BMDangSuDung() {
		return khoa_BMDangSuDung;
	}

	public void setKhoa_BMDangSuDung(String khoa_BMDangSuDung) {
		this.khoa_BMDangSuDung = khoa_BMDangSuDung;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	@Override
	public String toString() {
		return "MonHoc [num=" + num + ", maMH=" + maMH + ", tenMH=" + tenMH + ", tinChi=" + tinChi + ", khoa_BMQuanLi="
				+ khoa_BMQuanLi + ", khoa_BMDangSuDung=" + khoa_BMDangSuDung + ", ghiChu=" + ghiChu + "]";
	}
	
}
