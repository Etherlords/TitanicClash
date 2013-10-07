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

	private static Logger log = Logger.getLogger(PlayerConnection.class.getName());

	private static final int HEADER_SIZE = 8;
	private static final long TIMEOUT = 1000 * 55;
	private static final String POLICY = "<cross-domain-policy><site-control permitted-cross-domain-policies='master-only'/><allow-access-from domain='*' to-ports='*' /></cross-domain-policy>\u0000";
	private static final String POLICY_REQUEST = "<policy-file-request/>\u0000";

	private Server parent;
    private Socket socket;

	private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private boolean removed = false;

	private long lastDataTime = 0;

    public int id;

	public IReceiver receiver;

	private ByteArray inputBuffer;

	private Boolean justConnected = true;

	private SocketDataEventRouter eventRouter;
	private DataReader dataReader;

	private PlayerConnection(Server parent, Socket socket) throws IOException
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

	public void sendPolicy()
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
	            //log.debug("receive " + currentBytesAvailable + " bytes");
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

	void read()
	{

		lastDataTime = System.currentTimeMillis();



		byte[] readBuffer = inputBuffer.buffer;
		int bytesAvailable = 0;

		try {
			bytesAvailable = inputStream.read(readBuffer, inputBuffer.length, readBuffer.length-inputBuffer.length);
			inputBuffer.length += bytesAvailable;
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(bytesAvailable <= 0)
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


		log.debug("read from socket: " + bytesAvailable + " bytes");

		bufferLength += bytesAvailable;

		processBuffer();
	}

	private void processBuffer()// throws EOFException
	{
		if(bufferLength < HEADER_SIZE)
			return;

		justConnected = false;
		Boolean isPacketReceived = bytesNeeded <= bufferLength;

		if (!isPacketReceived)
		{
			log.debug("packet is not received yet " + bufferLength+", " + bytesNeeded);
			return;
		}


		inputBuffer.position = 0;
		bytesNeeded = inputBuffer.readInt();

		isPacketReceived = bytesNeeded <= bufferLength;

		if (!isPacketReceived)
		{
			log.debug("packet is not received yet " + bufferLength+", " + bytesNeeded);
			return;
		}

		int type = inputBuffer.readInt();

		BytePacket reader = dataReader.getReader(type);

		if(reader != null)
		{
			reader.source = inputBuffer;
			inputBuffer.position = 0;

			reader.read();

			log.debug("READ: " + reader);
			log.debug("receive message type: " + type + ", packet size: " + bytesNeeded);

			eventRouter.routData(reader, this);
			inputBuffer.cut(bytesNeeded);

			bufferLength -= bytesNeeded;

			bytesNeeded = 0;
		}

	}

}