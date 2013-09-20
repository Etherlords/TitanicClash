package net.packets;

import utils.ByteArray;

public interface IPacketPart {
	void write(ByteArray source);
}
