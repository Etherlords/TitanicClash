package net;

import logic.SessionManager;
import net.events.SocketDataEventRouter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static Logger log = Logger.getLogger(Server.class.getName());

	private ServerSocket socket;

	private Session mainSession;
	private DataReader dataReader;
	private SocketDataEventRouter dataRouter;
	private SessionManager sessionManager;

	public Server(DataReader dataReader, SocketDataEventRouter dataRouter, SessionManager sessionManager)
	{
		this.dataReader = dataReader;
		this.dataRouter = dataRouter;
		this.sessionManager = sessionManager;
		initilize();
		log.debug("socket created");
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

		mainSession = new Session(SessionManager.MAIN_SESSION);
		sessionManager.addSession(mainSession);


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
        PlayerConnection newPlayer = new PlayerConnection(this, client, dataReader, dataRouter);

        mainSession.add(newPlayer);
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
        playerConnection.reciver.remove(playerConnection);
    }
}