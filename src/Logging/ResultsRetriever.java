/*
 * ResultsRetriever.java displays the results housed in the mysql database as well as in Errors.txt to the 
 * 		command line
 * 
 * What will be shown is -
 * 		-version number - check Changelong.txt for a list of what happened in each version
 * 		-Success - the # of successful runs
 * 		-Total Runs - the total number of runs ran for that version number (# of Success + # of Failure +
 * 			# of Errors)
 * 		-Percentage of Success - Success / Total Runs
 * 		-Avg Score - the average score obtained during successful runs (An Avg Score of 0.0 dictates that the
 * 			version was before we had rewards implemented)
 * 
 */

package Logging;
public class ResultsRetriever {

	MySQLConnector c;
	public static void main(String[] args)
	{
		String username = "testuser";
		String password = "testpassword";
		int versionNumber = 16;
		try
		{
		MySQLConnector c = new MySQLConnector(username,password);
		c.getResults(versionNumber);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error, please try again");
		}
		
	}
}
