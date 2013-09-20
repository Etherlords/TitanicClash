package net.packets;

import utils.ByteArray;

import java.io.UTFDataFormatException;

public class PlayerInfo implements IPacketPart {

	public int playerID;
	public String playerName;

	public PlayerInfo()
	{

	}

	public void write(ByteArray source)
	{
		source.writeInt(playerID);

		try {
			source.writeUTF(playerName);
		} catch (UTFDataFormatException e) {
			e.printStackTrace();
		}
	}


}
