package net;

public interface IReceiver
{
	public void handlePacket();

	void remove(PlayerConnection playerConnection);
}
