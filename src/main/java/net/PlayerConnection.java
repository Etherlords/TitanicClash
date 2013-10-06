package net;

import logic.UserIDManager;
import net.events.SocketDataEventRouter;
import net.packets.BytePacket;
import org.apache.log4j.Logger;
import utils.ByteArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class PlayerConnection {

	static Logger log = Logger.getLogger(PlayerConnection.class.getName());

	private static final int HEADER_SIZE = 8;
	private static final long TIMEOUT = 1000 * 55;
	private static final String POLICY = "<cross-domain-policy><site-control permitted-cross-domain-policies='master-only'/><allow-access-from domain='*' to-ports='*' /></cross-domain-policy>\u0000";
	private static final String POLICY_REQUEST = "<policy-file-request/>\u0000";

	private Server parent;
    private Socket socket;

	private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private boolean removed = false;

	public long lastDataTime = 0;

    public int id;

	public IReciver reciver;

	private ByteArray inputBuffer;

	private Boolean justConnected = true;

	private SocketDataEventRouter eventRouter;
	public DataReader dataReader;

	protected PlayerConnection(Server parent, Socket socket) throws IOException
	{
		create(parent, socket);
    }

	public PlayerConnection(Server parent, Socket socket, DataReader dataReader, SocketDataEventRouter eventRouter) throws IOException
	{
		this.dataReader = dataReader;
		this.eventRouter = eventRouter;
		create(parent, socket);
	}

	private void create(Server parent, Socket socket) throws IOException
	{
		try {
			socket.setSoTimeout(0);
			socket.setKeepAlive(true);
		} catch (SocketException e) {e.printStackTrace();}

		id = UserIDManager.getInstance().getId();
		//id = socket.getInetAddress().toString();
		this.parent = parent;
		this.socket = socket;

		inputBuffer = new ByteArray();

		outputStream = new DataOutputStream(socket.getOutputStream());
		inputStream = new DataInputStream(socket.getInputStream());

		lastDataTime = System.currentTimeMillis();
	}

	public void sendPolicy()  throws IOException
    {
        //send(POLICY);
    }

	private void sendString(String message)
	{
		try
		{
			outputStream.writeBytes(message);
			outputStream.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

    public void send(BytePacket packet) //throws IOException
    {
        try
        {
	        ByteArray buffer = new ByteArray();

	        packet.source = buffer;
	        packet.write();

	        outputStream.write(buffer.buffer, 0, buffer.length);
	        outputStream.flush();

        }catch (IOException e)
        {
            internalClose();
        }
    }

    private void internalClose()
    {

        if(removed)
            return;

        removed = true;

	    try
	    {
		    this.socket.close();
	    } catch (IOException e)
	    {
		    e.printStackTrace();
	    }
	    parent.closed(this);
    }

    public void listenSocket()
    {
	    long deltaTime = System.currentTimeMillis() - lastDataTime;
	    if(!this.socket.isClosed() && deltaTime<TIMEOUT)
        {
	        int currentBytesAvailable = 0;

	        try {
		        currentBytesAvailable = inputStream.available();
	        } catch (IOException e) {
		        e.printStackTrace();
	        }

	        if(currentBytesAvailable > 0)
            {
	            log.debug("recive " + currentBytesAvailable + " bytes");
				read();
            }
        }
	    else
        {
	        System.out.println("client is closed");

            internalClose();
            System.out.println("client out");
        }
    }

	private int bytesNeeded = 0;
	private int bufferLength = 0;

	public void read()
	{

		lastDataTime = System.currentTimeMillis();



		byte[] readBuffer = inputBuffer.buffer;
		int bytesAvalible = 0;

		try {
			bytesAvalible = inputStream.read(readBuffer);
			inputBuffer.length += bytesAvalible;
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(bytesAvalible <= 0)
			return;

		if(justConnected)
		{
			try
			{
				String s = ByteArray.readUTF(inputBuffer, 0, inputBuffer.length);

				log.debug("str req: " + s);
				if(s.equals(POLICY_REQUEST))
				{
					sendString(POLICY);
					inputBuffer.position = 0;
					return;
				}

			} catch (IOException e)
			{
				log.debug("try to read cross domain request");
			}
		}


		log.debug("read from socket: " + bytesAvalible + " bytes");

		bufferLength += bytesAvalible;

		processBuffer();
	}

	private void processBuffer()// throws EOFException
	{
		if(bufferLength < HEADER_SIZE)
			return;

		justConnected = false;
		Boolean isPacketRecived = bytesNeeded <= bufferLength;

		if (!isPacketRecived)
			return;

		inputBuffer.position = 0;
		bytesNeeded = inputBuffer.readInt();

		isPacketRecived = bytesNeeded <= bufferLength;

		if (!isPacketRecived)
			return;

		int type = inputBuffer.readInt();

		BytePacket reader = dataReader.getReader(type);

		if(reader != null)
		{
			reader.source = inputBuffer;
			inputBuffer.position = 0;

			reader.read();

			log.debug("READ: " + reader);

			eventRouter.routData(reader, this);
		}

			//if(type == PacketTypes.PING)
		//{
			//пинг можно игнорировать но как и при любом другом пакете нужно очистить буфер от этого пакета
		//	System.out.println("recive ping, ignore actions " + inputBuffer.length);
		//	return;
		//}

		log.debug("recive message type: " + type + ", packet size: " + bytesNeeded);
	}

}