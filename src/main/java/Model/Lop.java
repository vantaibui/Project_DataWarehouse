package Model;

public class Lop {
	private int num;
	private String maLH;
	private String maMH;
	private String namHoc;
	
	public Lop() {
		super();
	}

	public Lop(String maLH, String maMH, String namHoc) {
		super();
		this.maLH = maLH;
		this.maMH = maMH;
		this.namHoc = namHoc;
	}

	public Lop(int num, String maLH, String maMH, String namHoc) {
		super();
		this.num = num;
		this.maLH = maLH;
		this.maMH = maMH;
		this.namHoc = namHoc;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getMaLH() {
		return maLH;
	}

	public void setMaLH(String maLH) {
		this.maLH = maLH;
	}

	public String getMaMH() {
		return maMH;
	}

	public void setMaMH(String maMH) {
		this.maMH = maMH;
	}

	public String getNamHoc() {
		return namHoc;
	}

	public void setNamHoc(String namHoc) {
		this.namHoc = namHoc;
	}

	@Override
	public String toString() {
		return "Lop [num=" + num + ", maLH=" + maLH + ", maMH=" + maMH + ", namHoc=" + namHoc + "]";
	}
	
	
}
