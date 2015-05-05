package Logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	//Scema for the database is a database name cs175, 
	// Table is called results(version VARCHAR(200), result VARCHAR(20), date, VARCHAR(200), PRIMARY KEY (date))
	static String  version = "1.6";
	public static void Log(String result)
	{
		//Username/password to the database are admin's of your database, could be root and password
		String username = "testus";
		String password = "testpassword";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   //get current date time with Date()
		Date d = new Date();
		String date = dateFormat.format(d);
		//System.out.println(date);
		try
		{
		MySQLConnector c = new MySQLConnector(username,password);
		c.insertResult(version, date, result);
		}
		catch (Exception e)
		{
			PrintWriter writer;
			try {
				writer = new PrintWriter("Errors.txt", "UTF-8");
				writer.println(result + " " + date + " " + version);
				writer.close();
			}
			catch(Exception ex)
			{
				System.out.println("multiple errors occured");
			}
		}
	}
}
