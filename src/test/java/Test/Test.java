package Test;

import java.sql.SQLException;

import Constrants.Status;
import Model.Log;
import Model.Staging;
import Task2.UploadStaging;

public class Test {

	public static void main(String[] args) throws SQLException {
		Log log = new Log();
//		log.getLog(Status.ER);
//		
//		String sinhVien = "sinhvien";
//		UploadStaging.loadToStaging(log, sinhVien, 1);
		
		UploadStaging.uploadStaging(log);

	}

}
