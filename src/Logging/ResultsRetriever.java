package src.Logging;

public class ResultsRetriever {

	MySQLConnector c;
	public static void main(String[] args)
	{
		String username = "testuser";
		String password = "testpassword";
		int versionNumber = 7;
		try
		{
		MySQLConnector c = new MySQLConnector(username,password);
		c.getResults(versionNumber);
		}
		catch (Exception e)
		{
			System.out.println("Error, please try again");
		}
		
	}
}
