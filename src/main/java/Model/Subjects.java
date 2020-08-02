package Model;

public class Subjects {
	private int maMH;
	private String tenMH;
	private int tinChi;
	private String khoa;
	
	public Subjects() {
		super();
	}

	public Subjects(int maMH, String tenMH, int tinChi, String khoa) {
		super();
		this.maMH = maMH;
		this.tenMH = tenMH;
		this.tinChi = tinChi;
		this.khoa = khoa;
	}

	public int getMaMH() {
		return maMH;
	}

	public void setMaMH(int maMH) {
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

	public String getKhoa() {
		return khoa;
	}

	public void setKhoa(String khoa) {
		this.khoa = khoa;
	}

	@Override
	public String toString() {
		return "Subjects [maMH=" + maMH + ", tenMH=" + tenMH + ", tinChi=" + tinChi + ", khoa=" + khoa + "]";
	}
	
}
