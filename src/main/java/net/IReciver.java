package net;

public interface IReciver
{
	public void handlePacket();

	void remove(PlayerConnection playerConnection);
}
