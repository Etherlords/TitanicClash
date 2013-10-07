package utils.io;

import utils.ByteArray;

import java.io.IOException;
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

	@Override
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

	@Override
	public int serialize(ByteArray source)
	{
		try
		{
			_value = source.readUTF();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		_size = ByteArray.utfSizeOf((String) _value) + 2;
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
		return false;
	}

	public String toString()
	{
		return "UTFOperator";//formateToString(this, 'value', 'writeSize', 'readSize');
	}

	@Override
	public int calculateReadSize()
	{
		return _size;
	}

	@Override
	public int calculateWriteSize()
	{
		return ByteArray.utfSizeOf((String) _value) + 2;
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
