package net.packets;

public class AddPlayerPacket extends BasePacket
{
	public PlayerInfo playerInfo;// = new PlayerInfo();

	public AddPlayerPacket()
	{
		super();
	}

	@Override
	protected void initilize()
	{
		super.initilize();

		playerInfo = new PlayerInfo();

		addPart(playerInfo);
		//addPart(position);
		//addPart(addedObject);
		//addPart(moveInfo);
	}

}
