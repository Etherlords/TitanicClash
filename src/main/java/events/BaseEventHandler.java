package events;

import net.PlayerConnection;
import net.events.IBytePacketDataEventListener;
import net.packets.BytePacket;


public class BaseEventHandler implements IBytePacketDataEventListener
{
	protected int type;

	@Override
	public void setType(int type)
	{
		this.type = type;
	}

	@Override
	public void setTypeFromPacket(BytePacket packet)
	{
		setType(packet.type);
	}

	@Override
	public int getType()
	{
		return type;
	}

	@Override
	public void handle(BytePacket packet, PlayerConnection from)
	{

	}
}
