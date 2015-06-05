/*
 * The log package is responsible for logging the results of the maze solver each time its run
 * 
 * 
 * The dependencies are - 
 * 		-mysql must be installed
 * 		-there must be a cs175 data base with the following table 
 * 			-results(version VARCHAR(20), result VARCHAR(255), date VARCHAR(255), score INT)
 * 		-JDBC must also be installed
 * 
 * 
 * ALTERNATIVELY - 
 * 		-If the system can't access the database, all results will be placed and fetched from Errors.txt 
 * 			instead
 *
 *	Be aware - the results you see from ResultsRetriever.java may be different from the ones presented in
 *		class due to much of our results being housed in Brian's local mysql database
 */

package Logging;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	//Scema for the database is a database name cs175, 
	// Table is called results(version VARCHAR(200), result VARCHAR(20), date, VARCHAR(200), PRIMARY KEY (date))

	public static void Log(String result, String version, int score)
	{
		//Username/password to the database are admin's of your database, could be root and password
		String username = "test";
		String password = "test";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   //get current date time with Date()
		Date d = new Date();
		String date = dateFormat.format(d);
		//System.out.println(date);
		try
		{
		MySQLConnector c = new MySQLConnector(username,password);
		c.insertResult(version, date, result, score);
		}
		catch (Exception e)
		{
			BufferedWriter out = null;
			try  
			{
			    FileWriter fstream = new FileWriter("Errors.txt", true); //true tells to append data.
			    out = new BufferedWriter(fstream);
			    out.write(version + " " + date + " " + result + " " + score +"\n");
			}
			catch (IOException ee)
			{
			    System.err.println("Error: " + ee.getMessage());
			}
			finally
			{
			    if(out != null) {
			        try {
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
			}
		}
	}
}
