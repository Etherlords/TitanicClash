package logic;

import net.PlayerConnection;
import net.Session;
import net.packets.BytePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionManager
{
	public static final int MAIN_SESSION = 0;
	public static final int LOBBY_SESSION = 1;

	private static ExecutorService executor = Executors.newCachedThreadPool();

	private HashMap<Integer, Session> sessionsPool = new HashMap<Integer, Session>();

	public void addSession(Session session)
	{
		sessionsPool.put(session.id, session);
		executor.execute(session);
	}

	public Session getSession(int sessionId)
	{
		return sessionsPool.get(sessionId);
	}

	public void transfer(PlayerConnection subject, int fromSessionId, int toSessionId)
	{
		Session from = sessionsPool.get(fromSessionId);
		Session to = sessionsPool.get(toSessionId);

		transfer(subject, from, to);
	}

	public void transfer(PlayerConnection subject, Session from, Session to)
	{
		from.remove(subject);
		to.add(subject);
	}

	public void broadcast(BytePacket packet, PlayerConnection from)
	{
		for(Map.Entry<Integer, Session> entry : sessionsPool.entrySet())
		{
			Session session = entry.getValue();
			session.broadcast(packet, from);
		}
	}
}
