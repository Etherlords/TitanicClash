package net;

import net.packets.AddPlayerPacket;
import net.packets.Header;
import utils.ByteArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class PlayerConnection {

	private static final long TIMEOUT = 1000 * 15;
	private static final String POLICY = "<cross-domain-policy><site-control permitted-cross-domain-policies='all'/><allow-access-from domain='*' to-ports='*' /></cross-domain-policy>\0";

    private Server parent;

    private Socket socket;

	private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private boolean removed = false;

	public long lastDataTime = 0;

    public String id;

	public IReciver reciver;

	private ByteArray inputBuffer;

	protected PlayerConnection(Server parent, Socket socket) throws IOException {
        try {
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
        } catch (SocketException e) {e.printStackTrace();}

        id = socket.getInetAddress().toString();
        this.parent = parent;
        this.socket = socket;

		inputBuffer = new ByteArray();

	    outputStream = new DataOutputStream(socket.getOutputStream());
	    inputStream = new DataInputStream(socket.getInputStream());

	    lastDataTime = System.currentTimeMillis();
    }

    public void sendPolicy()  throws IOException
    {
        send(POLICY);
    }

    public void send(String command) throws IOException {

        try
        {
	        AddPlayerPacket pack = new AddPlayerPacket();

	        ByteArray buffer = new ByteArray();

	        pack.header.packetType = 1;
	        pack.header.serverTime = System.currentTimeMillis();
	        pack.header.packetSize = 1;

	        pack.playerInfo.playerID = 1;
	        pack.playerInfo.playerName = "Asfel";

	        pack.write(buffer);


	        buffer.position = 0;
	        buffer.writeInt(buffer.length);


	        outputStream.write(buffer.buffer, 0, buffer.length);
	        //outputStream.writeInt(outputStream.size(););

	        outputStream.flush();

        }catch (IOException e)
        {
            internalClose();
        }

    }

    private void internalClose() throws IOException {

        if(removed)
            return;

        removed = true;

        this.socket.close();
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
	            try {
		            readSocketData();
	            } catch (EOFException e) {
		            e.printStackTrace();
	            }
            }
        }
	    else
        {
	        System.out.println("client is closed");
	        try {
	            internalClose();
	        } catch (IOException e)
	        {
	            System.out.println("client out");
	        }
        }
    }

	private int bytesNeeded = 0;

	private void readSocketData() throws EOFException
	{
		lastDataTime = System.currentTimeMillis();
		byte[] readBuffer = inputBuffer.buffer;
		int currentDataLength = 0;

		try {
			currentDataLength = inputStream.read(readBuffer);
			inputBuffer.length += currentDataLength;
		} catch (IOException e) {
			e.printStackTrace();
		}

		Boolean isPacketRecived = bytesNeeded <= currentDataLength;

		if (!isPacketRecived)
			return;

		if(inputBuffer.length < Header.SIZE)
			return;

		inputBuffer.position = 0;
		bytesNeeded = inputBuffer.readInt();

		isPacketRecived = bytesNeeded <= currentDataLength;

		if (!isPacketRecived)
			return;

		int type = inputBuffer.readInt();

		System.out.println("recive message " + bytesNeeded+", "+type);
	}

}