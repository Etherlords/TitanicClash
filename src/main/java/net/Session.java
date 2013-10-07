package net;

import net.packets.BytePacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session extends Thread implements IReceiver
{
	public int id;

	private ConcurrentHashMap<Integer, PlayerConnection> clients = new ConcurrentHashMap<Integer, PlayerConnection>();

	private Boolean isExit = false;

	public Session(int id)
	{
	 	this.id = id;
	}

	public void add(PlayerConnection client)
	{
		clients.put(client.id, client);
		client.receiver = this;
	}

	public void remove(int id)
	{
		clients.remove(id).receiver = null;
		//clients.remove(id);
	}

	@Override
	public void remove(PlayerConnection playerConnection)
	{
		remove(playerConnection.id);
	}

	@Override
	public void run() {

		while (!isExit)
		{
			for(Map.Entry<Integer, PlayerConnection> entry : clients.entrySet())
			{
				PlayerConnection connection = entry.getValue();
				connection.listenSocket();
			}
		}
	}

	@Override
	public void handlePacket() {

	}

	public void broadcast(BytePacket packet)
	{
		for(Map.Entry<Integer, PlayerConnection> entry : clients.entrySet())
		{
			PlayerConnection connection = entry.getValue();
			connection.send(packet);
		}
	}
}
