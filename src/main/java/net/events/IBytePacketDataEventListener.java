package net.events;

import net.PlayerConnection;
import net.packets.BytePacket;

/**
 * User: Asfel
 */
public interface IBytePacketDataEventListener
{

	void setType(int type);
	void setTypeFromPacket(BytePacket packet);
	int getType();

	void handle(BytePacket packet, PlayerConnection from);
}
