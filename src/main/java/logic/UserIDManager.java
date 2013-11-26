package logic;

import org.apache.log4j.Logger;

/**
 * User: Asfel
 */
public class UserIDManager
{
	private static Logger log = Logger.getLogger(UserIDManager.class.getName());

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
		log.debug("GetUSERID " + id);
		return id++;
	}
}
