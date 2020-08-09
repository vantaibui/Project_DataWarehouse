package Model;

public class DangKy {
	private String maDK;
	private String maSV;
	private String maLH;
	private String TGDK;
	
	public DangKy() {
		super();
	}

	public DangKy(String maDK, String maSV, String maLH, String tGDK) {
		super();
		this.maDK = maDK;
		this.maSV = maSV;
		this.maLH = maLH;
		TGDK = tGDK;
	}

	

	public String getMaDK() {
		return maDK;
	}

	public void setMaDK(String maDK) {
		this.maDK = maDK;
	}

	public String getMaSV() {
		return maSV;
	}

	public void setMaSV(String maSV) {
		this.maSV = maSV;
	}

	public String getMaLH() {
		return maLH;
	}

	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}

	public String getTGDK() {
		return TGDK;
	}

	public void setTGDK(String tGDK) {
		TGDK = tGDK;
	}

	@Override
	public String toString() {
		return "DangKy [num=" + num + ", maDK=" + maDK + ", maSV=" + maSV + ", maLH=" + maLH + ", TGDK=" + TGDK + "]";
	}
	
	
}
