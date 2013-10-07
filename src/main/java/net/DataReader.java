package net;

import net.packets.BytePacket;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class DataReader
{
	private static Logger log = Logger.getLogger(DataReader.class.getName());

	private HashMap<Integer, BytePacket> readers = new HashMap<Integer, BytePacket>();

	public void setReaders(BytePacket[] value)
	{
		for(BytePacket packet : value)
		{
			if(readers.get(packet.type) != null)
				try
				{
					throw new Exception("try to override reader in DataReader");
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			log.debug("add reader " + packet.type);
			readers.put(packet.type, packet);
		}

	}

	public BytePacket getReader(int type)
	{
		return readers.get(type);
	}

}

