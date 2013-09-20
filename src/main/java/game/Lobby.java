package game;

import net.Session;

import java.util.Observable;
import java.util.Observer;

public class Lobby implements Observer
{

	private Session session;

	public Lobby()
	{
		initilize();
	}

	private void initilize()
	{
		session = new Session();
	}

	@Override
	public void update(Observable o, Object arg)
	{

	}
}
