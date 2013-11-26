package net;

import logic.SessionManager;
import net.events.SocketDataEventRouter;
import net.packets.BytePacket;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread
{
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	final Lock lock = new ReentrantLock();
	private Condition await = lock.newCondition();

	private static Logger log = Logger.getLogger(Server.class.getName());

	private ServerSocket socket;

	private Session mainSession;
	private DataReader dataReader;
	private SocketDataEventRouter dataRouter;
	private SessionManager sessionManager;
	private BytePacket greetPacket;

	public Server(DataReader dataReader, SocketDataEventRouter dataRouter, SessionManager sessionManager)
	{
		this.dataReader = dataReader;
		this.dataRouter = dataRouter;
		this.sessionManager = sessionManager;
		initialize();
		log.debug("socket listen");
	}

	private void initialize()
	{
		try {
			socket = new ServerSocket(8881);
			log.debug("socket managed");
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}

		mainSession = new Session(SessionManager.MAIN_SESSION);
		sessionManager.addSession(mainSession);

		executor.execute(this);
	}

	@Override
	public void run()
	{
		while (true)
		{
			lock.lock();

			Socket client;

			try
			{
				client = socket.accept();
				log.debug("new client: " +  client.getInetAddress().toString());
				handleConnection(client);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				await.awaitNanos(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			ArrayList<Object> input = new ArrayList<Object>();
			input.add("teset test test");
			greetPacket.input = input;

			mainSession.broadcast(greetPacket, mainSession.getClients().get(0));
		}
	}

	public void handleConnection(Socket client) throws IOException
    {
        PlayerConnection newPlayer = new PlayerConnection(this, client, dataReader, dataRouter);

        mainSession.add(newPlayer);
	    ArrayList<Object> input = new ArrayList<Object>();
	    input.add("tesettestest");
	    greetPacket.input = input;
	    newPlayer.send(greetPacket);
    }

   /* public void notifyClients(PlayerConnection playerConnection, BaseCommand command) throws IOException
    {
        command.serverTime = System.currentTimeMillis();
    }

    public void handleCommand(PlayerConnection playerConnection, BaseCommand command) throws IOException
    {
        //System.out.println("client: " + playerConnection.id + " >> " + command);
    }     */

    public void closed(PlayerConnection playerConnection)
    {
	    System.out.println("Player left: " + playerConnection.id);
		playerConnection.getReceiver().remove(playerConnection);
    }

	public void setGreetPacket(BytePacket greetPacket)
	{
		this.greetPacket = greetPacket;
	}

	public BytePacket getGreetPacket()
	{
		return greetPacket;
	}
}