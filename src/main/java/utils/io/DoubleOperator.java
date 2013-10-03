package utils.io;

import utils.ByteArray;

public class DoubleOperator implements ISerializer, IDeserializer
{
	private Object _value;

	private static final int _size = TypesSize.DOUBLE_SIZE;

	public DoubleOperator(Double value)
	{
		_value = value;
	}

	public DoubleOperator()
	{
	}

	public void setValue(Object n)
	{
		_value = n;
	}

	public int deserialize(ByteArray source)
	{
		source.writeDouble((Double) _value);
		return _size;
	}

	public int serialize(ByteArray source)
	{
		_value = source.readDouble();
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
		return "DoubleOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
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