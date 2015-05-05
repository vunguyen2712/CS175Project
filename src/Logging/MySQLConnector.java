package Logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class MySQLConnector {

	
	Connection connection;
	public MySQLConnector(String username, String password) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		// Incorporate mySQL driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql:///cs175",username, password);
	}
	
	public void insertResult(String version, String date, String result) throws SQLException
	{
		Statement update = connection.createStatement();
			String sqlCommand = "INSERT INTO results VALUES ('"
					+ version + "','" 
					+ result + "','"
					+ date + "')";
			update.executeUpdate(sqlCommand);
	}
}
