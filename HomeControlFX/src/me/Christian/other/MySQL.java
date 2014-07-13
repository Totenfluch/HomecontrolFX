package me.Christian.other;
import java.sql.*;

public class MySQL {
	public static String MySQL_IP, MySQL_User, MySQL_Pass, MySQL_dir;
	public static boolean MySQL_enabled, MySQL_hashing;
	public static boolean Login(String username, String password){
		String url = "jdbc:mysql://" + MySQL_IP + "/" + MySQL_dir;

		Connection conn;

		String createString = "Select username,password from Accounts WHERE username = '"+username+"' AND password = '"+password+"'";

		Statement stmt;

		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(java.lang.ClassNotFoundException cnfe){
			System.out.println("Class Not Found - " + cnfe.getMessage());
		}      

		try{
			conn = DriverManager.getConnection(url, MySQL_User, MySQL_Pass);

			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(createString);

			String usercheck = "";

			while (rs.next()) {
				usercheck = rs.getString("username");
			}

			if(usercheck.equals("")){
				return false;
			}

			stmt.close();
			conn.close();
		}
		catch(SQLException sqle){
			System.out.println("SQL Exception: " + sqle.getMessage());
		}

		return true;
	}

	public static boolean Register(String username, String password, String email){
		String url = "jdbc:mysql://" + MySQL_IP + "/" + MySQL_dir;

		Connection conn;

		String createString = "INSERT INTO Accounts (username,password,email) VALUES ('"+username+"','"+password+"','"+email+"')";
		Statement stmt;

		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(java.lang.ClassNotFoundException cnfe){
			System.out.println("Class Not Found - " + cnfe.getMessage());
		}      

		try{
			conn = DriverManager.getConnection(url, MySQL_User, MySQL_Pass);

			stmt = conn.createStatement();

			stmt.executeUpdate(createString);

			stmt.close();
			conn.close();
		}
		catch(SQLException sqle){
			return false;
			//System.out.println("SQL Exception: " + sqle.getMessage());
		}

		//System.out.println("Register");
		return true;
	}

	public static String[][] Userlist(){

		String url = "jdbc:mysql://" + MySQL_IP + "/" + MySQL_dir;
		String[][] userlist = new String[100][4];

		Connection conn;

		Statement stmt;

		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(java.lang.ClassNotFoundException cnfe){
			System.out.println("Class Not Found - " + cnfe.getMessage());
		}      

		try{
			conn = DriverManager.getConnection(url, MySQL_User, MySQL_Pass);

			stmt = conn.createStatement();

			stmt.executeQuery("select * from Accounts");

			ResultSet rs = stmt.getResultSet();
			int i = 0;
			while (rs.next()) {      
				/*System.out.println(rs.getString("username"));
				System.out.println(rs.getString("password"));
				System.out.println(rs.getString("flags"));
				System.out.println(rs.getString("permissions"));
				System.out.println("");*/
				userlist[i][0] = rs.getString("username");
				userlist[i][1] = rs.getString("password");
				userlist[i][2] = rs.getString("flags");
				userlist[i][3] = rs.getString("permissions");
				i++;
			}

			stmt.close();
			conn.close();
		}
		catch(SQLException sqle){
			System.out.println("SQL Exception: " + sqle.getMessage());
		}
		return userlist;

	}

	public static boolean Userdelete(String username){

		String url = "jdbc:mysql://" + MySQL_IP + "/" + MySQL_dir;

		Connection conn;

		Statement stmt;

		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(java.lang.ClassNotFoundException cnfe){
			System.out.println("Class Not Found - " + cnfe.getMessage());
		}      

		try{
			conn = DriverManager.getConnection(url, MySQL_User, MySQL_Pass);

			stmt = conn.createStatement();

			stmt.executeUpdate("Delete from Accounts where username = '"+username+"'");

			stmt.close();
			conn.close();
		}
		catch(SQLException sqle){
			return false;
			//System.out.println("SQL Exception: " + sqle.getMessage());
		}
		return true;

	}

}
