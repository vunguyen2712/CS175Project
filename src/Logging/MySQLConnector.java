package src.Logging;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class MySQLConnector {

	
	Connection connection;
	static int startVersion = 6;
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
	
	public void getResults(int version) throws SQLException
	{
		ResultSet results;
		for(int i = startVersion; i <= version; i++)
		{
			int numberOfSuccess =0;
			int totalRuns =0;
			String versionNumber = "1." + i;
			
			Statement collect = connection.createStatement();
			String sqlCommand = "SELECT result FROM results WHERE version = " + versionNumber;
			
			results = collect.executeQuery(sqlCommand);
			
			while(results.next())
			{
				totalRuns++;
				String res = results.getString(1);
				if (res.equals("Success"))
					numberOfSuccess++;
			}
			
			
			String file = "Errors.txt";
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
			    String line;
			    while ((line = br.readLine()) != null) {
			       String[] split = line.split(" ");
			       String res = split[3];
			       String vers = split[0];
			       if(vers.equals(versionNumber))
			       {
			       totalRuns++;
			       if (res.equals("Success"))
			    	   numberOfSuccess++;
			       }
			    }
			    br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int percent = (numberOfSuccess * 100) / totalRuns;
			System.out.println("Version Number: 1." + i + "   Success: " + numberOfSuccess + "    Total Runs : "
					+ totalRuns + "     Percentage of Success: " + percent + "%");
			System.out.println("--------------------------------");
			
		}
		
	}
}
