package utils.events;

public class Event
{
	public int type;
	public Object target;

	public Event(int type, Object target)
	{
		this.type = type;
		this.target = target;
	}
}
