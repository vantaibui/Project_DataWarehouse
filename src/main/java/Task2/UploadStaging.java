package Task2;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Constrants.Status;
import Model.ConnectDatabase;
import Model.DangKy;
import Model.Log;
import Model.Lop;
import Model.MonHoc;
import Model.SendMail;
import Reader.XLSXReader;
import Model.SinhVien;

public class UploadStaging {
	ConnectDatabase cdb;
	PreparedStatement pst;
	ResultSet rs;

	public UploadStaging() {
		cdb = new ConnectDatabase();
	}

//	Lấy url local trong configuration với config_id tương ứng
	public String getUrlLocal(int config_id) throws SQLException {
		String result = "";
		Connection connection = cdb.connectDBControl();
		String sql = "select import_dir from controldb.configuration where config_id = " + config_id;
		pst = connection.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			String urlLocal = rs.getString(1);
			result = urlLocal;
		}
		return result;
	}

//	Lấy column_list và variable_list trong configuration với config_id tương ứng
	public String getColumnList(int config_id) throws SQLException {
		String result = "";
		Connection connection = cdb.connectDBControl();
		String col = "select column_list, variable_list from controldb.configuration where config_id = " + config_id;
		pst = connection.prepareStatement(col);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			String[] column_list = rs.getString(1).split(",");
			String[] variable_list = rs.getString(2).split(",");
			for (int i = 0; i < column_list.length; i++) {
				result += column_list[i] + " " + variable_list[i] + ",";
			}
			result += "primary key (`" + column_list[0] + "`)";
		}
		return result;
	}

//	Tạo table trong stagingdb
	public void createTableInStaging(String tableName, int config_id) throws SQLException {
		String result = "";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("create table if not exists stagingdb." + tableName + "(");

		stringBuilder.append(getColumnList(config_id));
		stringBuilder.append(");");
//		System.out.println(stringBuilder.toString());
		result = stringBuilder.toString();

		Connection connection = cdb.connectDBStaging();
		PreparedStatement preparedStatement = connection.prepareStatement(result);
		preparedStatement.executeUpdate();
	}

//	import dữ liệu vào table sinhvien
	public boolean loadStudent(SinhVien sinhvien) {
		String sql = "insert into stagingdb.sinhvien(id, first_name, last_name, dob, id_class, class_name, number_phone, email, address, note) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = cdb.connectDBStaging().prepareStatement(sql);
			pst.setString(1, sinhvien.getId());
			pst.setString(2, sinhvien.getFirst_name());
			pst.setString(3, sinhvien.getLast_name());
			pst.setString(4, sinhvien.getDob());
			pst.setString(5, sinhvien.getId_class());
			pst.setString(6, sinhvien.getClass_name());
			pst.setString(7, sinhvien.getNumber_phone());
			pst.setString(8, sinhvien.getEmail());
			pst.setString(9, sinhvien.getAddress());
			pst.setString(10, sinhvien.getNote());

			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

//	import dữ liệu vào table lophoc
	public boolean loadLop(Lop lop) {
		String sql = "insert into stagingdb.lop(ma_lh, ma_mh, nam_hoc) values(?, ?, ?);";
		try {
			pst = cdb.connectDBStaging().prepareStatement(sql);
			pst.setString(1, lop.getMaLH());
			pst.setString(2, lop.getMaMH());
			pst.setString(3, lop.getNamHoc());

			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

//	import dữ liệu vào table dangky
	public boolean loadDangKy(DangKy dangky) {
		String sql = "insert into stagingdb.dangky(ma_dk, ma_hv, ma_lh, tgdk) values(?, ?, ?, ?);";
		try {
			pst = cdb.connectDBStaging().prepareStatement(sql);
			pst.setString(1, dangky.getMaDK());
			pst.setString(2, dangky.getMaSV());
			pst.setString(3, dangky.getMaLH());
			pst.setString(4, dangky.getTGDK());

			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

//	import dữ liệu vào table monhoc
	public boolean loadMonHoc(MonHoc monhoc) {
		String sql = "insert into stagingdb.monhoc(ma_mh, ten_mh, tin_chi, khoa_BMQuanLi, khoa_BMDangSuDung, ghi_chu) values(?, ?, ?, ?, ?, ?)";
		try {
			pst = cdb.connectDBStaging().prepareStatement(sql);
			pst.setString(1, monhoc.getMaMH());
			pst.setString(2, monhoc.getTenMH());
			pst.setInt(3, monhoc.getTinChi());
			pst.setString(4, monhoc.getKhoa_BMQuanLi());
			pst.setString(5, monhoc.getKhoa_BMDangSuDung());
			pst.setString(6, monhoc.getGhiChu());

			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static boolean loadToStaging(Log log, String tableName, int config_id) {
		UploadStaging uploadStaging = new UploadStaging();
		XLSXReader xlsxReader = new XLSXReader();
		log.getLog(Status.ER);
		int countFile = 0;
		try {
			File file = new File("Data" + File.separator + "Student" + File.separator + log.getFile_name());
			System.out.println(file);
//			List<SinhVien> data = uploadStaging.readData(file);
			List<SinhVien> data = xlsxReader.readDataSV(file);
//			uploadStaging.createTable(config_id);
			uploadStaging.createTableInStaging(tableName, config_id);
			for (SinhVien sinhvien : data) {
				System.out.println(sinhvien);
				uploadStaging.loadStudent(sinhvien);
			}
			log.updateLog(Status.TR, countFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean uploadStaging(Log log) throws SQLException {
		File file;

		int countFile = 0;

		UploadStaging uploadStaging = new UploadStaging();

		XLSXReader xlsxReader = new XLSXReader();

//		Lấy status của file trong log
		log.getLog(Status.ER);

//		Lấy name file trong log
		String fileName = log.getFile_name();

		boolean loadSuccess = false;

		if (fileName.contains("sinhvien") || fileName.contains("Sinhvien")) {
			file = new File(uploadStaging.getUrlLocal(1) + File.separator + fileName);

			if (file.exists()) {
				try {
					List<SinhVien> dssv = xlsxReader.readDataSV(file);
					uploadStaging.createTableInStaging("sinhvien", 1);

					for (SinhVien sinhVien : dssv) {
						loadSuccess = uploadStaging.loadStudent(sinhVien);
						countFile++;
					}

				} catch (IOException e) {
					System.out.println("Lỗi");
					e.printStackTrace();
				}
			}
			System.out.println("Đường dẫn không tồn tại!");
//			Load lớp học
		} else if (fileName.contains("lophoc") || fileName.contains("LopHoc")) {
			file = new File(uploadStaging.getUrlLocal(2) + File.separator + fileName);

			if (file.exists()) {
				try {
					List<Lop> dslop = xlsxReader.readDataLop(file);
					uploadStaging.createTableInStaging("lop", 2);

					for (Lop lop : dslop) {
						loadSuccess = uploadStaging.loadLop(lop);
						countFile++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Đường dẫn không tồn tại!");

//			Load đăng ký
		} else if (fileName.contains("dangky") || fileName.contains("Dangky")) {
			file = new File(uploadStaging.getUrlLocal(3) + File.separator + fileName);

			if (file.exists()) {
				try {
					List<DangKy> dsdk = xlsxReader.readDataDK(file);
					uploadStaging.createTableInStaging("dangky", 3);

					for (DangKy dangKy : dsdk) {
						loadSuccess = uploadStaging.loadDangKy(dangKy);
						countFile++;
					}
				} catch (IOException e) {
					System.out.println("Lỗi");
					e.printStackTrace();
				}
			}
			System.out.println("Đường dẫn không tồn tại!");

//			Load môn học
		} else if (fileName.contains("Monhoc") || fileName.contains("monhoc")) {
			System.out.println(fileName);
			file = new File(uploadStaging.getUrlLocal(4) + File.separator + fileName);
			System.out.println(file);
			if (file.exists()) {
				try {
					List<MonHoc> dsmh = xlsxReader.readDataMH(file);
					uploadStaging.createTableInStaging("monhoc", 4);

					for (MonHoc monHoc : dsmh) {
						loadSuccess = uploadStaging.loadMonHoc(monHoc);
						countFile++;
					}
				} catch (IOException e) {
					System.out.println("Lỗi");
					e.printStackTrace();
				}
			}
			System.out.println("Đường dẫn không tồn tại!");
		} else {
			System.out.println("Không có file yêu cầu");
		}
		System.out.println(loadSuccess);
		if (loadSuccess == true) {
			log.updateLog(Status.TR, countFile);
			SendMail.sendMail("vantaibui92@gmail.com", "Upload File To Staging", "Tải lên staging thành công.");
			System.out.println(loadSuccess);
			return true;
		} else {
			log.updateLog(Status.ERROR, countFile);
			SendMail.sendMail("vantaibui92@gmail.com", "Upload File To Staging", "Tải file thất bại.");
			return false;
		}
	}

	public static void main(String[] args) throws SQLException, IOException {
		UploadStaging uploadStaging = new UploadStaging();
//		File dir = new File("Data\\Student\\sinhvien_chieu_nhom12.xlsx");
//		List<Student> data = uploadStaging.readData(dir);
//		uploadStaging.createTable();
//		for (Student student : data) {
//			System.out.println(student);
//			uploadStaging.loadStudent(student);
//		}
		Log log = new Log();
		String sinhvien = "sinhvien";
//		uploadStaging.uploadStaging(log);
//		uploadStaging.createTableInStaging("dangky", 3);
//		System.out.println(uploadStaging.loadToStaging(log, "sinhvien", 1));
	}

}
