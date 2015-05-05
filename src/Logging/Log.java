package Logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	static String  version = "1.6";
	public static void Log(String result)
	{
		String username = "testuser";
		String password = "testpassword";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   //get current date time with Date()
		Date d = new Date();
		String date = dateFormat.format(d);
		System.out.println(date);
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
