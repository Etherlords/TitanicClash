package net.events;

import net.PlayerConnection;
import net.packets.BytePacket;

import java.util.HashMap;

public class SocketDataEventRouter
{
	private HashMap<Integer, IBytePacketDataEventListener> eventsMap = new HashMap<Integer, IBytePacketDataEventListener>();

	public void setListeners(IBytePacketDataEventListener[] listenersList)
	{
		for(IBytePacketDataEventListener listener : listenersList)
			eventsMap.put(listener.getType(), listener);
	}

	public void routData(BytePacket packet, PlayerConnection from)
	{
		IBytePacketDataEventListener eventListener = eventsMap.get(packet.type);

		if (eventListener == null)
			return;

		eventListener.handle(packet, from);
	}

}
