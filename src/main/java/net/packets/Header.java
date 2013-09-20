package net.packets;

import utils.ByteArray;

public class Header implements IPacketPart{

	public static final int SIZE = 8;

	public int packetSize;
	public int packetType;
	public long serverTime;

	@Override
	public void write(ByteArray source) {
		source.writeInt(packetSize);
		source.writeInt(packetType);
		source.writeDouble(serverTime);
	}

	public void read(ByteArray source)
	{

	}
}
