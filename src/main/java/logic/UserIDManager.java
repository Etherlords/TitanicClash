package logic;

/**
 * User: Asfel
 */
public class UserIDManager
{
	private static UserIDManager ourInstance = new UserIDManager();
	private int id = 0;

	public static UserIDManager getInstance()
	{
		return ourInstance;
	}

	private UserIDManager()
	{
	}

	public int getId()
	{
		return id++;
	}
}
