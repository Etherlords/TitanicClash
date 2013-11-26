package events;


import game.Lobby;
import logic.SessionManager;
import net.PlayerConnection;
import net.packets.BytePacket;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class LoginEventHandler extends BaseEventHandler
{
	private static Logger log = Logger.getLogger(LoginEventHandler.class.getName());

	private SessionManager sessionManager;
	private BytePacket loginAnswerPacket;

	private Lobby lobby;

	public void setLoginAnswerPacket(BytePacket loginAccess)
	{
		this.loginAnswerPacket = loginAccess;
	}

	public void setLobby(Lobby value)
	{
		lobby = value;
	}

	public void setSessionMManager(SessionManager value)
	{
		this.sessionManager = value;
	}

	@Override
	public void handle(BytePacket packet, PlayerConnection from)
	{

		sessionManager.transfer(from, SessionManager.MAIN_SESSION, SessionManager.LOBBY_SESSION);

		String login = (String) packet.output.get(0);
		String password = (String) packet.output.get(1);

		from.data.name = login;

		ArrayList<Object> input = new ArrayList<Object>();
		input.add(from.id);
		input.add(0);

		loginAnswerPacket.input = input;

		from.send(loginAnswerPacket);


		lobby.add(from);

		log.debug("handle login " + from+", " + login+", " + password);
	}
}
