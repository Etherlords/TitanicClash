package net;

import net.packets.BasePacket;

import java.util.Vector;

public class Session extends Thread implements IReciver
{
	private Vector<PlayerConnection> clients = new Vector<PlayerConnection>();

	public Session()
	{

	}

	public void addPlayer(PlayerConnection client)
	{
		clients.add(client);
		client.reciver = this;
	}

	public void remove(PlayerConnection playerConnection)
	{
		for(int i = 0; i < clients.size(); i++)
		{
			if(clients.get(i) == playerConnection)
			{
				clients.remove(i);
				playerConnection.reciver = null;
				break;
			}
		}
	}

	@Override
	public void run() {

		while (true)
		{
			for(int i = 0; i < clients.size(); i++)
			{
				clients.get(i).listenSocket();
			}
		}
	}

	@Override
	public void handlePacket(BasePacket packet) {

	}
}
