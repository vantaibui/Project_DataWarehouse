package Reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.DangKy;
import Model.Lop;
import Model.MonHoc;
import Model.SinhVien;

public class XLSXReader {

	private static final int NUM = 0;
	private static final int ID = 1;
	private static final int FIRSTNAME = 2;
	private static final int LASTNAME = 3;
	private static final int DOB = 4;
	private static final int IDCLASS = 5;
	private static final int CLASSNAME = 6;
	private static final int NUMBERPHONE = 7;
	private static final int EMAIL = 8;
	private static final int ADDRESS = 9;
	private static final int NOTE = 10;

	private static final int NUMLOP = 0;
	private static final int MALH = 1;
	private static final int MAMHLOP = 2;
	private static final int NAMHOC = 3;

	private static final int NUMMH = 0;
	private static final int MAMH = 1;
	private static final int TENMH = 2;
	private static final int TINCHI = 3;
	private static final int KHOA_BMQL = 4;
	private static final int KHOA_BMDSD = 5;
	private static final int GHICHU = 6;
	
	private static final int NUMDK = 0;
	private static final int MADK = 1;
	private static final int MASV = 2;
	private static final int MALHDK = 3;
	private static final int TGDK = 4;


//	Sinh viên
	public List<SinhVien> readDataSV(File file) throws IOException {
		List<SinhVien> students = new ArrayList<SinhVien>();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

//		Lấy ra interator cho tất cả các dòng của sheet hiện tại
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cells = row.cellIterator();
			SinhVien sinhvien = new SinhVien();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				setProps(cell, sinhvien);
			}
			if (!standardizedData(sinhvien)) {
				continue;
			}
			students.add(sinhvien);
		}
		workbook.close();
		inputStream.close();
		return students;
	}

	private static void setProps(Cell cell, SinhVien sinhvien) {
		int propsIndex = cell.getColumnIndex();
		Object value = getValue(cell);

		switch (propsIndex) {

		case NUM:
			sinhvien.setNum(Integer.parseInt(String.valueOf(value)));
			break;
		case ID:
			sinhvien.setId(String.valueOf(value));
			break;
		case FIRSTNAME:
			sinhvien.setFirst_name(String.valueOf(value));
			break;
		case LASTNAME:
			sinhvien.setLast_name(String.valueOf(value));
			break;
		case DOB:
			sinhvien.setDob(String.valueOf(value));
			break;
		case IDCLASS:
			sinhvien.setId_class(String.valueOf(value));
			break;
		case CLASSNAME:
			sinhvien.setClass_name(String.valueOf(value));
			break;
		case NUMBERPHONE:
			sinhvien.setNumber_phone(String.valueOf(value));
			break;
		case EMAIL:
			sinhvien.setEmail(String.valueOf(value));
			break;
		case ADDRESS:
			sinhvien.setAddress(String.valueOf(value));
			break;
		case NOTE:
			sinhvien.setNote(String.valueOf(value));
			break;

		default:
			break;
		}
	}

	// Kiểm tra dữ liệu
	private boolean standardizedData(SinhVien sinhvien) {
		if (sinhvien.getNum() == 0 || sinhvien.getId() == null) {
			return false;
		}
		return true;
	}

//	Lớp học

	public List<Lop> readDataLop(File file) throws IOException {
		List<Lop> lops = new ArrayList<Lop>();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

//		Lấy ra interator cho tất cả các dòng của sheet hiện tại
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cells = row.cellIterator();
			Lop lop = new Lop();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				setProps(cell, lop);
			}
			if (!standardizedData(lop)) {
				continue;
			}
			lops.add(lop);
		}
		workbook.close();
		inputStream.close();
		return lops;
	}

	private void setProps(Cell cell, Lop lop) {
		int propsIndex = cell.getColumnIndex();
		Object value = getValue(cell);
		switch (propsIndex) {
		case NUMLOP:
			lop.setNum(Integer.parseInt(String.valueOf(value)));
			break;
		case MALH:
			lop.setMaLH(String.valueOf(value));
			break;
		case MAMHLOP:
			lop.setMaMH(String.valueOf(value));
			break;
		case NAMHOC:
			lop.setNamHoc(String.valueOf(value));
			break;
		default:
			break;
		}

	}

	private boolean standardizedData(Lop lop) {
		if (lop.getNum() == 0 || lop.getMaLH() == null) {
			return false;
		}
		return true;
	}

//	Đăng ký

	public List<DangKy> readDataDK(File file) throws IOException {
		List<DangKy> dsdk = new ArrayList<DangKy>();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

//		Lấy ra interator cho tất cả các dòng của sheet hiện tại
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cells = row.cellIterator();
			DangKy dk = new DangKy();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				setProps(cell, dk);
			}
			if (!standardizedData(dk)) {
				continue;
			}
			dsdk.add(dk);
		}
		workbook.close();
		inputStream.close();
		return dsdk;
	}

	private void setProps(Cell cell, DangKy dangky) {
		int propsIndex = cell.getColumnIndex();
		Object value = getValue(cell);
		switch (propsIndex) {
		case NUMDK:
			dangky.setNum(Integer.parseInt(String.valueOf(value)));
			break;
		case MADK:
			dangky.setMaDK(String.valueOf(value));
			break;
		case MASV:
			dangky.setMaSV(String.valueOf(value));
			break;
		case MALHDK:
			dangky.setMaLH(String.valueOf(value));
			break;
		case TGDK:
			dangky.setTGDK(String.valueOf(value));
			break;
		default:
			break;
		}

	}

	private boolean standardizedData(DangKy dangky) {
		if (dangky.getNum() == 0 || dangky.getMaDK() == null) {
			return false;
		}
		return true;
	}

	// Môn học
	public List<MonHoc> readDataMH(File file) throws IOException {
		List<MonHoc> monhocs = new ArrayList<MonHoc>();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

//			Lấy ra interator cho tất cả các dòng của sheet hiện tại
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			if (row.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cells = row.cellIterator();
			MonHoc monhoc = new MonHoc();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				setProps(cell, monhoc);
			}
			if (!standardizedData(monhoc)) {
				continue;
			}
			monhocs.add(monhoc);
		}
		workbook.close();
		inputStream.close();
		return monhocs;
	}

	private void setProps(Cell cell, MonHoc monhoc) {
		int propsIndex = cell.getColumnIndex();
		Object value = getValue(cell);
		switch (propsIndex) {
		case NUMMH:
			monhoc.setNum(Integer.parseInt(String.valueOf(value)));
			break;
		case MAMH:
			monhoc.setMaMH(String.valueOf(value));
			break;
		case TENMH: 
			monhoc.setTenMH(String.valueOf(value));
			break;
		case TINCHI:
			monhoc.setTinChi(Integer.parseInt(String.valueOf(value)));
			break;
		case KHOA_BMQL:
			monhoc.setKhoa_BMQuanLi(String.valueOf(value));
			break;
		case KHOA_BMDSD:
			monhoc.setKhoa_BMDangSuDung(String.valueOf(value));
			break;
		case GHICHU:
			monhoc.setGhiChu(String.valueOf(value));
			break;
		default:
			break;
		}

	}

	private boolean standardizedData(MonHoc monhoc) {
		if (monhoc.getNum() == 0 || monhoc.getMaMH() == null) {
			return false;
		}
		return true;
	}

	private static Object getValue(Cell cell) {
		CellType cellType = cell.getCellTypeEnum();
		Object value = new Object();
		switch (cellType) {
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case NUMERIC:
			value = new BigDecimal(cell.getNumericCellValue()).intValue();
			break;
		case STRING:
			value = cell.getStringCellValue();
			break;
		case FORMULA:
			Workbook workbook = cell.getSheet().getWorkbook();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			value = new BigDecimal(evaluator.evaluate(cell).getNumberValue()).intValue();
			break;
		case BLANK:
		case ERROR:
		case _NONE:
			value = 1;
			break;
		default:
			break;
		}
		return value;
	}
}
