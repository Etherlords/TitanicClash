package game;

import logic.SessionManager;
import net.PlayerConnection;
import net.Session;
import net.packets.BytePacket;

import java.util.ArrayList;

public class Lobby
{

	private SessionManager sessionManager;
	private Session session;

	private BytePacket chatMessage;

	public Lobby()
	{

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
		session = new Session(SessionManager.LOBBY_SESSION);
		sessionManager.addSession(session);
	}

	public void add(PlayerConnection connection)
	{
		ArrayList<Object> input = new ArrayList<Object>();
		input.add(-1);
		input.add(0);
		input.add("Hello");

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
