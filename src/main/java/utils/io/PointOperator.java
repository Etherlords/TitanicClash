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

	@Override
	public void setValue(Object point)
	{
		this._value = point;
	}

	@Override
	public int deserialize(ByteArray source)
	{
		Point p = (Point) _value;
		source.writeDouble(p.x);
		source.writeDouble(p.y);
		return _size;
	}

	@Override
	public int serialize(ByteArray source)
	{
		Point p = (Point) _value;
		p.x = source.readDouble();
		p.y = source.readDouble();
		return _size;
	}

	@Override
	public Object getValue()
	{
		return _value;
	}

	@Override
	public Boolean isStaticSize()
	{
		return true;
	}

	public String toString()
	{
		return "PointOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
	}

	@Override
	public int calculateReadSize()
	{
		return _size;
	}

	@Override
	public int calculateWriteSize()
	{
		return _size;
	}

	@Override
	public int getWriteSize()
	{
		return _size;
	}

	@Override
	public int getReadSize()
	{
		return _size;
	}
}