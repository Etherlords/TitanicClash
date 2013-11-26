package net;

import net.packets.BytePacket;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Session extends Thread implements IReceiver
{
	final Lock lock = new ReentrantLock();
	private Condition await = lock.newCondition();

	private static Logger log = Logger.getLogger(Session.class.getName());

	public int id;

	private ConcurrentHashMap<Integer, PlayerConnection> clients = new ConcurrentHashMap<Integer, PlayerConnection>();

	private Boolean isExit = false;

	public Session(int id)
	{
	 	this.id = id;
	}

	public Session(String name, int id)
	{
		super(name);
		this.id = id;
	}

	public ConcurrentHashMap<Integer, PlayerConnection> getClients()
	{
		return clients;
	}

	public void add(PlayerConnection client)
	{
		log.debug("add to session: " + this.id + ", " + client.id);
		clients.put(client.id, client);
		client.setReceiver(this);
	}

	public void remove(int id)
	{
		log.debug("remove from session: " + this.id + ", " + id + ", " + clients.get(id));
		clients.remove(id).setReceiver(null);
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
			lock.lock();

			for(Map.Entry<Integer, PlayerConnection> entry : clients.entrySet())
			{
				PlayerConnection connection = entry.getValue();
				connection.listenSocket();
			}
			try
			{
				await.awaitNanos(20);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		lock.unlock();
	}

	@Override
	public void handlePacket() {

	}

	public void broadcast(BytePacket packet, PlayerConnection from)
	{
		for(Map.Entry<Integer, PlayerConnection> entry : clients.entrySet())
		{
			PlayerConnection connection = entry.getValue();
			if(connection == from) continue;
			connection.send(packet);
		}
	}
}
