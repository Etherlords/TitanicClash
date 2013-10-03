package net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	static Logger log = Logger.getLogger(Server.class.getName());

    private static ExecutorService executor = Executors.newCachedThreadPool();

	ServerSocket socket;

	Session mainSession;

	public Server()
	{
		initilize();
	}

	private void initilize()
	{
		try {
			socket = new ServerSocket(8881);
			log.debug("socket managed");
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}

		mainSession = new Session();
		executor.execute(mainSession);

		mainCycle();

	}

	private void mainCycle()
	{
		while (true)
		{
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
		}
	}

	public void handleConnection(Socket client) throws IOException
    {
        PlayerConnection newPlayer = new PlayerConnection(this, client);
        mainSession.addPlayer(newPlayer);
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
        mainSession.remove(playerConnection);
    }
}