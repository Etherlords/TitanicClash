package events;

import logic.SessionManager;
import net.PlayerConnection;
import net.packets.BytePacket;

public class ChatMessageEventHandler extends BaseEventHandler
{



	private SessionManager sessionManager;

	public void setSessionManager(SessionManager sessionManager)
	{
		this.sessionManager = sessionManager;
	}

	@Override
	public void handle(BytePacket packet, PlayerConnection from)
	{
		packet.input = packet.output;
		//from.send(packet);sessionManager
		sessionManager.broadcast(packet);
	}
}
