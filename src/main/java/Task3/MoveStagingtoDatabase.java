package Task3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import Constrants.Status;
import Model.ConnectDatabase;
import Model.DangKy;
import Model.Lop;
import Model.MonHoc;
import Model.SinhVien;


public class MoveStagingtoDatabase {

	static Connection connect;
	static ConnectDatabase cdb;
	static PreparedStatement pst;
	
	static ResultSet rs;


	public MoveStagingtoDatabase() {
		cdb= new ConnectDatabase();
	}
	public static void loadWH(int table) throws Exception {
		 String sql= "select * controldb.log where data_file_id= "+table+"file_status='TR'";
		 int config_id = 0;
		 try {
				PreparedStatement pst1;
				 pst1 = cdb.connectDBControl().prepareStatement(sql);
				 ResultSet rs1= pst1.executeQuery();
				 config_id= rs1.getInt("data_file_config_id");
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 String sql1= "select * controldb.configuration where config_id= "+config_id;
		 String tableStaging="";
		 try {
				PreparedStatement pst1;
				 pst1 = cdb.connectDBControl().prepareStatement(sql1);
				 ResultSet rs1= pst1.executeQuery();
				 tableStaging= rs1.getString("table_taget_staging");
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 String sql2= "select * controldb.configuration_staging	 where NameStaging= "+tableStaging;
		 String table_taget="";
		 try {
				PreparedStatement pst1;
				 pst1 = cdb.connectDBControl().prepareStatement(sql2);
				 ResultSet rs1= pst1.executeQuery();
				 table_taget= rs1.getString("table_Taget");
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 if(table_taget=="studentwh") {
			 BuiltDataStudent();
			 UpdateTimeLogInsertStaging(table);
			  UpdateStatus(table, Status.SU);
		 } else if(table_taget=="monhocwh") {
			 BuiltDataMonHoc();
			 UpdateTimeLogInsertStaging(table);
			 UpdateStatus(table, Status.SU);
			 
		 }else if(table_taget=="lopwh") {
			 BuiltDataLopHoc();
			 UpdateTimeLogInsertStaging(table);
			 UpdateStatus(table, Status.SU);
		 }else if(table_taget=="dangkywh") {
			 BuiltDataDangKi();
			 UpdateTimeLogInsertStaging(table);
			 UpdateStatus(table, Status.SU);
		 }
	}
	 public static boolean checkforexistence(String id_Student) {//Data data
			String sql="SELECT count(id_student) FROM datawarehouse.studentwh Where id_student = ?";//datawarehouse.Student
			 
				try {
					PreparedStatement pst1;
					 int a=0;
					 pst1 = cdb.connectDBStaging().prepareStatement(sql);
					 pst1.setString(1, id_Student);
					 ResultSet rs1= pst1.executeQuery();
					 if(rs1.next()) {
						a= rs1.getInt(1);
						
					 }
					 if(a>0) return true;
					 pst1.close();
					 
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			 return false;
			 
		 }
	 // check trung dk
	 public static boolean checkforexistenceDK(String ma_dk) {
			String sql="SELECT count(MaDK) FROM datawarehouse.dangkiwh Where MaDK = ?";
			 
				try {
					PreparedStatement pst1;
					 int a=0;
					 pst1 = cdb.connectDBStaging().prepareStatement(sql);
					 pst1.setString(1, ma_dk);
					 ResultSet rs1= pst1.executeQuery();
					 if(rs1.next()) {
						a= rs1.getInt(1);
						System.out.println(a);
					 }
					 if(a>0) return true;
					 pst1.close();
					 
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			 return false;
			 
		 }
	 
		public static void UpdateTimeLogInsertStaging(int table) throws SQLException {
			 String sql= "UPDATE controldb.log SET time_stamp_insert_staging = ? WHERE data_file_id = ?";
			 PreparedStatement pst1 = null;
				ResultSet rs1 = null;
				String sql1;
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				String timestamp = dtf.format(now);
				ConnectDatabase cdc1= new ConnectDatabase();
				try {
						pst1 = cdc1.connectDBControl().prepareStatement(sql);
						pst1.setString(1, timestamp);
						pst1.setInt(2, table);
						pst1.executeUpdate();
						pst1.close();
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
			            if (pst1 != null) pst1.close();
			            if (rs1 != null) rs1.close();
			        }
				  
		}
	 
	  public static void UpdateStatus(int table, Status sts) throws SQLException {
			String  sql = "UPDATE controldb.log SET file_status = ? WHERE data_file_id = ?";
				
			PreparedStatement pst1 = null;
			ResultSet rs1 = null;
			String sql1;
			ConnectDatabase cdc1= new ConnectDatabase();
			try {
					pst1 = cdc1.connectDBControl().prepareStatement(sql);
					pst1.setString(1, sts.name());
					pst1.setInt(2, table);
					pst1.executeUpdate();
					pst1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
		            if (pst1 != null) pst1.close();
		            if (rs1 != null) rs1.close();
		        }
			  
		  }
		  ///load mon hoc
		  public static void BuiltDataMonHoc() throws SQLException{
			  int num;
			  String MaMH;
			  String TenMH="";
			  int tinchi;
			  String Khoa="";
			  String KhoaSD="";
			  String ghichu;
				  List<MonHoc> listMH= new ArrayList<MonHoc>();
				  String sql1= "select * from stagingdb.monhoc";
				  String sqldel="";
				  pst=cdb.connectDBStaging().prepareStatement(sql1);
				  ResultSet rs1= pst.executeQuery();
				  while(rs1.next()) {
					  num=rs1.getInt("num");
				  MaMH=rs1.getString("ma_mh");
				  TenMH=rs1.getString("ten_mh");
				  tinchi= rs1.getInt("tin_Chi");
				  Khoa=rs1.getString("khoa_BMQuanLi");
				  KhoaSD= rs1.getString("khoa_BMDangSuDung");
				  ghichu= rs1.getString("ghi_chu");
				  listMH.add(new MonHoc(num,MaMH, TenMH, tinchi, Khoa, KhoaSD, ghichu));
				 
			  }
			String monhoc="create table if not exists datawarehouse.monhocwh(num int(50) Not null AUTO_INCREMENT primary key ,maMH varchar(255),tenMH varchar(255),tinChi varchar(255),Khoa_BMQuanLi varchar(255),Khoa_BMDangSuDung varchar(255),ghiChu varchar(255))";
			Connection connection = cdb.connectDBWarehouse();
			PreparedStatement preparedStatement = connection.prepareStatement(monhoc);
			preparedStatement.executeUpdate();
		
				  for(MonHoc MH:listMH) {
					  String sql2= "insert into datawarehouse.monhocwh(maMH, tenMH,tinChi,Khoa_BMQuanLi,Khoa_BMDangSuDung,ghiChu) values(?,?,?,?,?,?) ";
					  String sql3= "delete from stagingdb.monhoc where num="+MH.getNum();
					 
					  try {
						  pst= cdb.connectDBWarehouse().prepareStatement(sql2);
						
								pst.setString(1, MH.getMaMH());
								pst.setString(2, MH.getTenMH());
								pst.setInt(3, MH.getTinChi());
								pst.setString(4, MH.getKhoa_BMQuanLi());
								pst.setString(5, MH.getKhoa_BMDangSuDung());
								pst.setString(6, MH.getGhiChu());
						pst.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
				        }
					  
					  //xoa
					  try {
						  System.out.println("da xoa "+ MH.getMaMH());
						  pst= cdb.connectDBStaging().prepareStatement(sql3);
						pst.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
				        }
					  }
				  }  
		  
		  // load dang ky
		  public static void BuiltDataDangKi() throws SQLException{	
			  	int num;
				String ma_dk="";
				String ma_hv="";
				String ma_lh="";
				String tgdk="";
				  List<DangKy> listDK= new ArrayList<DangKy>();
				  String sql1= "select * from stagingdb.dangky";
				  pst=cdb.connectDBStaging().prepareStatement(sql1);
				  ResultSet rs1= pst.executeQuery();
				  while(rs1.next()) {
					  num= rs1.getInt("num");
					  ma_dk=rs1.getString("ma_dk");
					  ma_hv=rs1.getString("ma_hv");
					  ma_lh=rs1.getString("ma_lh");
					  tgdk=rs1.getString("tgdk");
				  listDK.add(new DangKy(num,ma_dk, ma_hv, ma_lh, tgdk));
				  
			  }
				  String dangky="create table if not exists datawarehouse.dangkywh(num int(50) Not null AUTO_INCREMENT primary key ,ma_dk varchar(255),ma_hv varchar(255),ma_lh varchar(255),tgdk varchar(255))";
				  Connection connection = cdb.connectDBWarehouse();
					PreparedStatement preparedStatement = connection.prepareStatement(dangky);
					preparedStatement.executeUpdate();
				  for(DangKy dk:listDK) {
					  String sql2= "insert into datawarehouse.dangkywh(ma_dk,ma_hv,ma_lh,tgdk) values(?,?,?,?) ";
					  String sql3= "delete from stagingdb.dangky where num="+dk.getNum();
					  try {
						  pst= cdb.connectDBWarehouse().prepareStatement(sql2);
						 
							pst.setString(1, dk.getMaDK());
							pst.setString(2, dk.getMaSV());
							pst.setString(3, dk.getMaLH());
							pst.setString(4, dk.getTGDK());
						pst.executeUpdate();
						 }catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
				        }
					  
					  //xoa
					  try {
						  System.out.println("da xoa "+ dk.getMaDK());
						  pst= cdb.connectDBStaging().prepareStatement(sql3);
						pst.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
						}
					  }
				  }  
		  
		  
		  //load lop hoc
		  public static void BuiltDataLopHoc() throws SQLException{
			  int num;
			  String MaLH;
				String MaMH;
				String namhoc;
				  List<Lop> listLH= new ArrayList<Lop>();
				  String sql1= "select * from stagingdb.lop";
				  pst=cdb.connectDBStaging().prepareStatement(sql1);
				  ResultSet rs1= pst.executeQuery();
				  while(rs1.next()) {
					  num=rs1.getInt("num");
					  MaLH=rs1.getString("ma_lh");
					  MaMH=rs1.getString("ma_mh");
					  namhoc=rs1.getString("nam_hoc");
				  listLH.add(new Lop(num,MaLH, MaMH, namhoc));
				 
			  }
				  String lop="create table if not exists datawarehouse.lopwh(num int(50) Not null AUTO_INCREMENT primary key,ma_lh varchar(255),ma_mh varchar(255),nam_hoc varchar(255))";
				  Connection connection = cdb.connectDBWarehouse();
					PreparedStatement preparedStatement = connection.prepareStatement(lop);
					preparedStatement.executeUpdate();
				  for(Lop LH:listLH) {
					  String sql2= "insert into datawarehouse.lopwh(ma_lh, ma_mh, nam_hoc) values(?, ?, ?)";
					  String sql3= "delete from stagingdb.lop where num="+LH.getNum();
					 
					  try {
						  pst= cdb.connectDBWarehouse().prepareStatement(sql2);
						
						  pst.setString(1, LH.getMaLH());
							pst.setString(2, LH.getMaMH());
							pst.setString(3, LH.getNamHoc());

						pst.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
				        }
					  
//					  xoa
					  try {
						  System.out.println("da xoa "+ LH.getMaLH());
						  pst= cdb.connectDBStaging().prepareStatement(sql3);
							pst.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
						}
					  }
		  }

		  
		  //load staging to warehouse
		  public static void BuiltDataStudent() throws Exception{
			  	int num;
			     String iD;
			   	 String lastName;
				 String firstName;
				 String dayBorn;
				 String iDClass;
				 String className;
				 String phoneNumber;
				 String email;
				 String address;
				 String note;
				 int sk_date;
				 connect=cdb.connectDBStaging();
				  List<SinhVien> listStudent= new ArrayList<SinhVien>();
				  String sql1= "select * from stagingdb.sinhvien";
				  pst=connect.prepareStatement(sql1);
				  ResultSet rs1= pst.executeQuery();
				  while(rs1.next()) {
					  num=rs1.getInt("num");
					  iD=rs1.getString("id");
					  firstName=rs1.getString("first_name");
					  lastName=rs1.getString("last_name");
					  dayBorn=rs1.getString("dob");
					  iDClass=rs1.getString("id_class");
					  className=rs1.getString("class_name");
					  phoneNumber=rs1.getString("number_phone");
					  email=rs1.getString("email");
					  address=rs1.getString("address");
					  note=rs1.getString("note");
				  listStudent.add(new SinhVien(num,iD,firstName,lastName,dayBorn,iDClass,className,phoneNumber,email,address,note));
			  }
				  
				   connect= cdb.connectDBWarehouse();
				   String create= "create table if not exists datawarehouse.studentwh(num int(50) Not null AUTO_INCREMENT primary key ,id_student varchar(255),first_name varchar(255),last_name "
				   		+ "varchar(255),sk_date int(55),dob varchar(255), id_class varchar(255),class_name varchar(255),email varchar(255),number_phone varchar(255),address varchar(255),note varchar(255))";
				   Connection connection = cdb.connectDBWarehouse();
					PreparedStatement preparedStatement = connection.prepareStatement(create);
					preparedStatement.executeUpdate();
				  for(SinhVien st:listStudent) {
					  String sql2= "insert into datawarehouse.studentwh(id_student,first_name,last_name,dob,id_class,class_name,number_phone,email,address,note) values(?,?,?,?,?,?,?,?,?,?,?) ";
					  String sql3="update datawarehouse.studentwh set first_name=?,last_name=?,dob=?,id_class=?,class_name=?,number_phone=?,email=?,address=?,note=?where id_student=?";
					 String sql4="delete from stagingdb.sinhvien where id= "+st.getId();
					  try {
						  
						  if(checkforexistence(st.getId())) {
							  pst= connect.prepareStatement(sql3);
								pst.setString(1,st.getFirst_name());
								pst.setString(2,st.getLast_name());
								pst.setString(3,st.getDob());
								pst.setString(4, st.getId_class());
								pst.setString(5,st.getClass_name());
								pst.setString(6,st.getNumber_phone());
								pst.setString(7,st.getEmail());
								pst.setString(8,st.getAddress());
								pst.setString(9,st.getNote());
								//pst.setInt(10,InsertDate(st.getDob()));
								pst.setString(10, st.getId());
								pst.executeUpdate();
								System.out.println("Record "+st.getId()+" đã tồn tại đã update");
						  }else {
							  pst= connect.prepareStatement(sql2);
								pst.setString(1, st.getId());
								pst.setString(2,st.getFirst_name());
								pst.setString(3,st.getLast_name());
								pst.setString(4,st.getDob());
								pst.setString(5, st.getId_class());
								pst.setString(6,st.getClass_name());
								pst.setString(7,st.getNumber_phone());
								pst.setString(8,st.getEmail());
								pst.setString(9,st.getAddress());
								pst.setString(10,st.getNote());
								//pst.setInt(11, InsertDate(st.getDob()));
						pst.executeUpdate();
						}} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
				        }
					  
					  //xoa
					  try {
						  System.out.println("da xoa "+ st.getId());
						  pst= cdb.connectDBStaging().prepareStatement(sql4);
						pst.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}finally {
				            if (pst != null) pst.close();
				            if (rs != null) rs.close();
						}
				  }}
				 
				  
		  
		  public static int InsertDate(String dob)  throws Exception{
			 String sql= "select date_sk from datawarehouse.date_dim where full_date=? ";
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			 Date date =formatter.parse(dob);
			 SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
			 String date_sk=formatter2.format(date);
			 PreparedStatement pst1 = null;
				ResultSet rs1 = null;
				String sql1;
				int a=0;
				ConnectDatabase cdc1= new ConnectDatabase();
				try {
						pst1 = cdc1.connectDBWarehouse().prepareStatement(sql);
						pst1.setString(1, date_sk);
						rs1=pst1.executeQuery();
						while(rs1.next()) {
							a=rs1.getInt("date_sk");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
			            if (pst1 != null) pst1.close();
			            if (rs1 != null) rs1.close();
			        }
				  
			  return a;
		  }
	public static void main(String[] args) throws Exception {
		MoveStagingtoDatabase move= new MoveStagingtoDatabase();
		move.BuiltDataDangKi();
	}

}
