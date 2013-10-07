package utils.io;

import utils.ByteArray;

public class IntOperator implements ISerializer, IDeserializer
{
	private Object _value;

	private static final int _size = TypesSize.INT_SIZE;

	public IntOperator(int value)
	{
		_value = value;
	}

	public IntOperator()
	{
	}

	@Override
	public void setValue(Object value)
	{
		_value = value;
	}

	@Override
	public int deserialize(ByteArray source)
	{
		source.writeInt((Integer) _value);
		return _size;
	}

	@Override
	public int serialize(ByteArray source)
	{
		_value = source.readInt();
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
		return "IntOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
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