package Test;

import java.sql.SQLException;
import java.util.Scanner;

import Constrants.Status;
import GetData.ChilkatSCP;
import Model.Log;
import Task2.UploadStaging;
import Task3.MoveStagingtoDatabase;

public class Test {

	public static void main(String[] args) throws Exception {
		ChilkatSCP down = new ChilkatSCP();
		Log log = new Log();
		UploadStaging uploadStaging = new UploadStaging();
		MoveStagingtoDatabase moveStagingtoDatabase = new MoveStagingtoDatabase();
		String local_path = "";
//		Log log = new Log();
//		UploadStaging.uploadStaging(log);
		System.out.println("1. Config student");
		System.out.println("2. Config Class");
		System.out.println("3. Config Resgiter");
		System.out.println("4. Config Subject");
		System.out.print("Nhập Config muốn tải");
		
//		Scanner sc = new Scanner(System.in);
//		int key = sc.nextInt();
		int key = Integer.parseInt(args[0]);
		switch (key) {
		case 1:
			down.local_path = "G:\\CodeJava\\DataWarehouse2020\\DW\\Data\\Student";
			down.insertDataLog1(key);
			uploadStaging.uploadStaging(log);
			moveStagingtoDatabase.BuiltDataStudent();
			log.updateLogDW(Status.SU, log);
			break;
		case 2:
			down.local_path = "G:\\CodeJava\\DataWarehouse2020\\DW\\Data\\Class";
			down.insertDataLog1(key);
			uploadStaging.uploadStaging(log);
			moveStagingtoDatabase.BuiltDataLopHoc();
			log.updateLogDW(Status.SU, log);
			break;
		case 3:
			down.local_path = "G:\\CodeJava\\DataWarehouse2020\\DW\\Data\\Register";
			down.insertDataLog1(key);
			uploadStaging.uploadStaging(log);
			moveStagingtoDatabase.BuiltDataDangKi();
			log.updateLogDW(Status.SU, log);
			break;
		case 4:
			down.local_path = "G:\\CodeJava\\DataWarehouse2020\\DW\\Data\\Subject";
			down.insertDataLog1(key);
			uploadStaging.uploadStaging(log);
			moveStagingtoDatabase.BuiltDataMonHoc();
			log.updateLogDW(Status.SU, log);
		default:
			break;
		}

	}

}
