package utils.io;

import geom.Point;
import utils.ByteArray;

public class PointOperator implements ISerializer, IDeserializer
{
	private Object _value;

	private static final int _size = TypesSize.POINT_SIZE;

	public PointOperator(Point value)
	{
		_value = value;
	}

	public PointOperator()
	{
	}

	public void setValue(Object point)
	{
		this._value = point;
	}

	public int deserialize(ByteArray source)
	{
		Point p = (Point) _value;
		source.writeDouble(p.x);
		source.writeDouble(p.y);
		return _size;
	}

	public int serialize(ByteArray source)
	{
		Point p = (Point) _value;
		p.x = source.readDouble();
		p.y = source.readDouble();
		return _size;
	}

	public Object getValue()
	{
		return _value;
	}

	public Boolean isStaticSize()
	{
		return true;
	}

	public String toString()
	{
		return "PointOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
	}

	public int calculateReadSize()
	{
		return _size;
	}

	public int calculateWriteSize()
	{
		return _size;
	}

	public int getWriteSize()
	{
		return _size;
	}

	public int getReadSize()
	{
		return _size;
	}
}