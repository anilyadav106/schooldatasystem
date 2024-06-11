package com.nagarro.nagp.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectivity {

	private static final String DB = "anillocalDb";
	private static final String USERNAME = "CMP_Admin";
	private static final String PASSWORD = "CMP@dmin#01";
	private static Connection con = null;

	public static void connectDatabase() {

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://172.20.36.140;databaseName=" + DB
					+ ";trustServerCertificate=true;";

			con = (DriverManager.getConnection(connectionUrl, USERNAME, PASSWORD));
		} catch (SQLException e) {
			System.out.println("There is some error while connecting to database> " + e.getMessage());
			e.printStackTrace();
		}

		catch (ClassNotFoundException e) {
			System.out.println("There is some error while connecting to database> " + e.getMessage());
			e.printStackTrace();

		}

	}

	/*
	 * method to get the connection reference
	 */

	public static Connection getCon() { 
		return con;

	}
}
