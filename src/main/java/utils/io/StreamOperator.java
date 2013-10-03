package utils.io;

import org.apache.log4j.Logger;
import utils.ByteArray;

import java.util.Vector;

public class StreamOperator implements ISerializer, IDeserializer, IStream
{

	static Logger log = Logger.getLogger(StreamOperator.class.getName());

	private int serializersCount = 0;
	private int deserializersCount = 0;

	public Vector output = new Vector();
	public Vector input = new Vector();

	private int _writeSize = 0;
	private int _readSize = 0;

	private Vector<ISerializer> serializers = new Vector<ISerializer>();
	private Vector<IDeserializer> deserializers = new Vector<IDeserializer>();

	private Boolean _isStaticSize = true;

	public StreamOperator()
	{

	}

	public void setDeserializers(IDeserializer[] value)
	{
		for(IDeserializer deserializer : value)
			addDeserializer(deserializer);
	}

	public void setSerializers(ISerializer[] value)
	{
		for(ISerializer serializer : value)
			addSerializer(serializer);
	}


	public int deserialize(ByteArray source)
	{
		int size = 0;
		IDeserializer currentDeserializer;
		for (int i = 0; i < deserializersCount; i++)
		{
			currentDeserializer = deserializers.get(i);
			currentDeserializer.setValue(input.get(i));
			size += currentDeserializer.deserialize(source);
			//trace(currentdeserializer);
		}

		return size;
	}

	public int serialize(ByteArray source)
	{
		int size = 0;
		ISerializer currentSerializer;
		for (int i = 0; i < serializersCount; i++)
		{
			currentSerializer = serializers.get(i);
			size += currentSerializer.serialize(source);
			output.add(currentSerializer.getValue());
			//trace(currentSerializer);
		}

		return size;
	}

	public void addSerializer(ISerializer serializer)
	{
		log.debug("addSerializer" + serializer);
		serializersCount++;
		serializers.add(serializer);

		if (!serializer.isStaticSize())
			_isStaticSize = false;
		else
			_readSize += serializer.getReadSize();
	}

	public void addDeserializer(IDeserializer deserializer)
	{
		deserializersCount++;
		deserializers.add(deserializer);

		if (!deserializer.isStaticSize())
			_isStaticSize = false;
		else
			_writeSize += deserializer.getWriteSize();
	}

	public Object getValue()
	{
		return output;
	}

	public void setValue(Object value)
	{
		input = (Vector) value;
	}

	public Boolean isStaticSize()
	{
		return _isStaticSize;
	}

	public int calculateReadSize()
	{
		if (_isStaticSize)
		{
			return _readSize;
		}

		_readSize = 0;
		ISerializer currentSerializer;
		for (int i = 0; i < serializersCount; i++)
		{
			currentSerializer = serializers.get(i);
			currentSerializer.setValue(output.get(i));

			if(currentSerializer.isStaticSize())
				_readSize += currentSerializer.getReadSize();
			else
				_readSize += currentSerializer.calculateReadSize();
		}

		return _readSize;
	}

	public int calculateWriteSize()
	{
		if (_isStaticSize)
		{
			return _writeSize;
		}

		_writeSize = 0;
		IDeserializer currentDesirealizer;
		for (int i = 0; i < deserializersCount; i++)
		{
			currentDesirealizer = deserializers.get(i);
			currentDesirealizer.setValue(input.get(i));

			if(currentDesirealizer.isStaticSize())
				_writeSize += currentDesirealizer.getWriteSize();
			else
				_writeSize += currentDesirealizer.calculateWriteSize();
		}

		return _writeSize;
	}

	public int getReadSize()
	{
		return _readSize;
	}

	public int getWriteSize()
	{
		return _writeSize;
	}
}