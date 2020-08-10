﻿package GetData;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

import Constrants.Status;
import Model.Configuration;
import Model.ConnectDatabase;
import Model.GetConnection;
import Model.Log;
import Model.SendMail;

public class ChilkatSCP {
	SendMail sm = new SendMail();
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private String sql;
	private Connection conn = null;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	LocalDateTime now = LocalDateTime.now();
	String timestamp = dtf.format(now);
	static final String NUMBER_REGEX = "^[0-9]+$";
	String server_path;
	public String local_path = "";
	private String sqlCheck;
	private PreparedStatement pstCheck = null;
	private String sqlUpdate;
	private PreparedStatement pstUpdate = null;
	private ResultSet rsCheck;
	// 1. tải thư viện chilkat
	static {
		try {
			System.loadLibrary("chilkat");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	ConnectDatabase cdb;

	public ChilkatSCP() {
		cdb = new ConnectDatabase();
	}

	public List<Configuration> getSrc() throws SQLException {
		Configuration configuration = new Configuration();
		List<Configuration> list = new ArrayList<Configuration>();
		String sql = "select host_name, port, user_name, password from controldb.configuration";
		Connection connection = cdb.connectDBControl();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			configuration.setHost_name(resultSet.getString(1));
			configuration.setPort(resultSet.getInt(2));
			configuration.setUser_name(resultSet.getString(3));
			configuration.setPassword(resultSet.getString(4));

			list.add(configuration);
		}
		return list;
	}

	// 3. chilkatSCPDownload(Configuration config) download đối tượng config
	public boolean chilkatSCPDownLoad(Configuration config) {
		CkSsh ssh = new CkSsh();
		CkGlobal ck = new CkGlobal();
		ck.UnlockBundle("Hello Team14");
		// 3.1. Kiểm tra kết nối đến address server thành công hay chưa?
		boolean success = ssh.Connect(config.getHost_name(), config.getPort());
		if (success != true) {
			// 3.1.1. sendMail error hostname or port
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Server không tìm thấy...!!");
			return false;
		}
		ssh.put_IdleTimeoutMs(5000);
		// 3.2. Kiểm tra tài khoản và mặt khẩu kết nối đến address server thành công hay
		// chưa?
		success = ssh.AuthenticatePw(config.getUser_name(), config.getPassword());
		if (success != true) {
			// 3.2.1. sendmail error username or password
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Tài khoản hoặc mặt khẩu sai!...");
			return false;
		}
		CkScp scp = new CkScp();
		// 3.3. Kiểm tra sử dụng SSH với tài khoản và mặt khẩu kết nối đến address
		// server thành công hay chưa?
		success = scp.UseSsh(ssh);
		if (success != true) {
			// 3.3.1. sendMail error using ssh
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Không có kết nối!...");
			return false;
		}
		// 3.4. tải những file nào và không tải những file nào đã ghi xuống log
		scp.put_SyncMustMatch(config.getFile_name());
		scp.put_SyncMustNotMatch(config.getNot_file_name());
		success = scp.SyncTreeDownload(config.getServer_dir(), config.getImport_dir(), config.getMode_scp(), false);
		// 3.5. Kiểm tra đã tải được file hay chưa
		if (success != true) {
			// 3.5.1. sendMail error download fail
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE",
					"Thư mực server hoặc địa chỉ local không tìm thấy!...");
			return false;
		}
		// 3.5.2. sendMail download success
		sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Đã tải file thành công!...");
		ssh.Disconnect();
		return true;
	}

	// 2. isDownLoadSCPChilkat(int id) lấy dữ liệu từ database tạo đối tượng.
	// gọi lại phương thức chilkatSCPDownload(Configuration config)để tiến hành
	// download
	public boolean isDownLoadSCPChilkat(int id) {
		boolean result = false;
		sql = "SELECT * FROM configuration where config_id = '" + id + "'";
		try {
			// 1. select fields table SCP in data SCP using download
			pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				int id_scp = rs.getInt("config_id");
				String load_library = rs.getString("load_library");
				String host_name_scp = rs.getString("host_name_scp");
				int port_scp = rs.getInt("port_scp");
				String username_scp = rs.getString("username_scp");
				String password_scp = rs.getString("password_scp");
				String sync_must_math = rs.getString("file_name");
				server_path = rs.getString("server_dir");
				local_path = rs.getString("import_dir");
				int mode_scp = rs.getInt("mode_scp");
				// get file name của config
				String sync_not_must_math = sync_not_mustMath(id);

				Configuration scp_config = new Configuration(id_scp, load_library, host_name_scp, port_scp,
						username_scp, password_scp, sync_must_math, sync_not_must_math, server_path, local_path,
						mode_scp);
				if (chilkatSCPDownLoad(scp_config)) {
					result = true;
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// phương thức sync_not_mustMath(int id) lấy dữ liệu từ database tạo thành chuỗi
	// làm công việc không tải những file này
	public String sync_not_mustMath(int id) {
		String sql = "SELECT file_name FROM log WHERE data_file_config_id=" + id;
		String result = "";
		try {
			pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				result += rs.getString("file_name") + ";";
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	// 4. phương thức insertDatalog(int id) dùng để kiểm tra đã tải thành công hay
	// chưa. Nếu thành công thì
	// insert log những file đã tải.
	public boolean insertDataLog(int id) {
		int rs = 0;
		boolean check = false;
		if (!isDownLoadSCPChilkat(id)) {
			return check;
		} else {
			sql = "INSERT INTO log (file_name,data_file_config_id,file_status, time_stamp_download)"
					+ " values (?,?,?,?)";
			File localPath = new File(local_path);
			File[] listFileLog = localPath.listFiles();

			lable: for (int i = 0; i < listFileLog.length; i++) {
				String sqlCheck = "select * from log";
				try {
					pstCheck = new GetConnection().getConnection("controldb").prepareStatement(sqlCheck);
					rsCheck = pstCheck.executeQuery();
					while (rsCheck.next()) {
						if (rsCheck.getString(2).equals(listFileLog[i].getName())
								&& rsCheck.getLong(3) == listFileLog[i].length()) {
							continue lable;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					return check;
				} finally {
					try {
						if (pstCheck != null)
							pstCheck.close();
						if (this.rsCheck != null)
							this.rsCheck.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				try {
					pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
					pst.setString(1, listFileLog[i].getName());
					System.out.println(listFileLog[i].getName());
					pst.setInt(2, id);
					pst.setString(3, "ER");
					pst.setString(4, timestamp);
					pst.executeUpdate();
					check = true;
				} catch (Exception e) {
					e.printStackTrace();
					return check;
				} finally {
					try {
						if (pst != null)
							pst.close();
						if (this.rs != null)
							this.rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return check;

	}

	public boolean insertDataLog1(int id) {
		int rs = 0;
		sql = "INSERT INTO log (file_name,data_file_config_id,file_status, time_stamp_download)" + " values (?,?,?,?)";
		File localPath = new File(local_path);
		File[] listFileLog = localPath.listFiles();

		lable: for (int i = 0; i < listFileLog.length; i++) {
			String sqlCheck = "select * from log";
			try {
				pstCheck = new GetConnection().getConnection("controldb").prepareStatement(sqlCheck);
				rsCheck = pstCheck.executeQuery();
				while (rsCheck.next()) {
					if (rsCheck.getString(2).equals(listFileLog[i].getName())
							&& rsCheck.getLong(3) == listFileLog[i].length()) {
						continue lable;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstCheck != null)
						pstCheck.close();
					if (this.rsCheck != null)
						this.rsCheck.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
				pst.setString(1, listFileLog[i].getName());
				System.out.println(listFileLog[i].getName());
				pst.setInt(2, id);
				pst.setString(3, "ER");
				pst.setString(4, timestamp);
				pst.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (pst != null)
						pst.close();
					if (this.rs != null)
						this.rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return false;

	}

	// 5. phương thức sendMailInsertLog(int id) dùng để kiểm tra xem ghi log thành
	// công hay chưa. Nếu
	// thành công thì gửi mail thành công còn không thì gửi mail báo thất bại.
	public void sendMailInsertLog(int id) {
		if (insertDataLog(id) == true) {
			// 1.6.1 if success, sendmail with content success
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Ghi log thành công rồi bồ tèo...!");
			System.out.println("Send mail write log success!!!");
		} else {
			// 1.6.2 if fail, sendmail with content fail
			sm.sendMail("17130135@st.hcmuaf.edu.vn", "DATA WAREHOUSE", "Ghi log fail rồi bồ tèo...!");
			System.out.println("Send mail write log fail!!!");

		}
	}

	public List<Log> getLog(Status status) {
		String result = "";
		List<Log> listLog = new ArrayList<Log>();
		sql = "SELECT * FROM controldb.log WHERE file_status = ?";
		try {
			pst = new GetConnection().getConnection("controldb").prepareStatement(sql);
			pst.setString(1, status.name());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				listLog.add(new Log(rs.getString("file_name"), rs.getInt("data_file_config_id"),
						rs.getString("file_status"), rs.getInt("staging_load_count"),
						rs.getString("time_stamp_download"), rs.getLong("time_stamp_insert_staging")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(listLog.toString());
		return listLog;
	}

	public static void main(String[] args) throws SQLException {
		ChilkatSCP test = new ChilkatSCP();
//		test.sendMailInsertLog(1);

		test.getLog(Status.ER);
	}
}
