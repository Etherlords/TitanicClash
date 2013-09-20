package net.packets;

import utils.ByteArray;

import java.util.ArrayList;

public class BasePacket {

	private ArrayList<IPacketPart> parts = new ArrayList<IPacketPart>();
	private int partsCount = 0;

	public Header header = new Header();

	public BasePacket()
	{
		initilize();
	}

	protected void initilize()
	{
		addPart(header);
	}

	public void addPart(IPacketPart part)
	{
		partsCount++;
		parts.add(part);
	}

	public void write(ByteArray source)
	{
		for (int i = 0; i < partsCount; i++)
		{
			parts.get(i).write(source);
		}
	}


}
