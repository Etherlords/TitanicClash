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

	@Override
	public void setValue(Object n)
	{
		_value = n;
	}

	@Override
	public int deserialize(ByteArray source)
	{
		source.writeDouble((Double) _value);
		return _size;
	}

	@Override
	public int serialize(ByteArray source)
	{
		_value = source.readDouble();
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
		return "DoubleOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
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