package game;

import logic.SessionManager;
import net.PlayerConnection;
import net.Session;
import net.packets.BytePacket;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby
{

	private static Logger log = Logger.getLogger(Lobby.class.getName());


	private SessionManager sessionManager;
	private Session session;

	private BytePacket chatMessage;
	private BytePacket chatJoinPacket;

	public Lobby()
	{

	}

	public void setChatJoinPacket(BytePacket chatJoinPacket)
	{
		this.chatJoinPacket = chatJoinPacket;
	}

	public void setChatMessage(BytePacket chatMessage)
	{
		this.chatMessage = chatMessage;
	}

	public void setSessionManager(SessionManager sessionManager)
	{
		this.sessionManager = sessionManager;
	}

	private void initialize()
	{
		session = new Session("lobby", SessionManager.LOBBY_SESSION);
		sessionManager.addSession(session);
	}

	public void add(PlayerConnection connection)
	{
		log.debug("add to lobby " + connection.id);
		ArrayList<Object> input = new ArrayList<Object>();

		input.add(connection.id);
		input.add(connection.data.name);
		input.add(SessionManager.LOBBY_SESSION);

		chatJoinPacket.input = input;
		session.broadcast(chatJoinPacket, connection);

		ConcurrentHashMap<Integer,PlayerConnection> clients = session.getClients();

		for(Map.Entry<Integer, PlayerConnection> entry : clients.entrySet())
		{
			PlayerConnection player = entry.getValue();

			if(player == connection)
				continue;

			input.set(0, player.id);
			input.set(1, player.data.name);
			connection.send(chatJoinPacket);
		}

		input.add(0, -1);
		input.add(1, SessionManager.LOBBY_SESSION);
		input.add(2, "Hello");

		chatMessage.input = input;

		connection.send(chatMessage);
	}

	public void sendMessage(String message, int from, int group)
	{
		ArrayList<Object> input = new ArrayList<Object>();
		input.add(from);
		input.add(group);
		input.add(message);

		chatMessage.input = input;
	}
}
