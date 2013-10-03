package utils.io;

import utils.ByteArray;

import java.io.UTFDataFormatException;

public class UTFStringOperator implements ISerializer, IDeserializer
{
	private Object _value;

	private int _size;

	public UTFStringOperator(Number value)
	{
		_value = value;
	}

	public UTFStringOperator()
	{
	}

	@Override
	public void setValue(Object n)
	{
		_value = n;
	}

	public int deserialize(ByteArray source)
	{
		try
		{
			_size = source.writeUTF((String) _value);
		} catch (UTFDataFormatException e)
		{
			e.printStackTrace();
		}
		return _size;
	}

	public int serialize(ByteArray source)
	{
		_value = source.readUTF();
		_size = ByteArray.utfSizeOf((String) _value) + 2;
		return _size;
	}

	public Object getValue()
	{
		return _value;
	}

	public Boolean isStaticSize()
	{
		return false;
	}

	public String toString()
	{
		return "UTFOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
	}

	public int calculateReadSize()
	{
		return _size;
	}

	public int calculateWriteSize()
	{
		return ByteArray.utfSizeOf((String) _value);
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
