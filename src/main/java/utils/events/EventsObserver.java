package utils.events;

import java.util.Observable;
import java.util.Observer;

public class EventsObserver implements Observer
{

	@Override
	public void update(Observable o, Object arg)
	{
		 Event evt = (Event) arg;

	}
}
