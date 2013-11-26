package utils.io;

import org.apache.log4j.Logger;
import utils.ByteArray;

import java.util.ArrayList;

public class StreamOperator implements ISerializer, IDeserializer, IStream
{

	private static Logger log = Logger.getLogger(StreamOperator.class.getName());

	private int serializersCount = 0;
	private int deserializersCount = 0;

	public ArrayList<Object> output;// = new ArrayList();
	public ArrayList<Object> input;// = new ArrayList();

	private int _writeSize = 0;
	private int _readSize = 0;

	private ArrayList<ISerializer> serializers = new ArrayList<ISerializer>();
	private ArrayList<IDeserializer> deserializers = new ArrayList<IDeserializer>();

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


	@Override
	public int deserialize(ByteArray source)
	{
		int size = 0;
		IDeserializer currentDeserializer;

		if(deserializersCount == 0)
			log.error("Try to deserialize data but deserialize count is 0");

		for (int i = 0; i < deserializersCount; i++)
		{
			currentDeserializer = deserializers.get(i);
			currentDeserializer.setValue(input.get(i));
			size += currentDeserializer.deserialize(source);
		}

		return size;
	}

	@Override
	public int serialize(ByteArray source)
	{
		int size = 0;
		ISerializer currentSerializer;

		if(serializersCount == 0)
			log.error("Try to serialize data but serialize count is 0");

		for (int i = 0; i < serializersCount; i++)
		{
			currentSerializer = serializers.get(i);
			size += currentSerializer.serialize(source);
			output.add(currentSerializer.getValue());
		}

		return size;
	}

	@Override
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

	@Override
	public void addDeserializer(IDeserializer deserializer)
	{
		log.debug("addDeSerializer" + deserializer);
		deserializersCount++;
		deserializers.add(deserializer);

		if (!deserializer.isStaticSize())
			_isStaticSize = false;
		else
			_writeSize += deserializer.getWriteSize();
	}

	@Override
	public Object getValue()
	{
		return output;
	}

	@Override
	public void setValue(Object value)
	{
		input = (ArrayList) value;
	}

	@Override
	public Boolean isStaticSize()
	{
		return _isStaticSize;
	}

	@Override
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

	@Override
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

	@Override
	public int getReadSize()
	{
		return _readSize;
	}

	@Override
	public int getWriteSize()
	{
		return _writeSize;
	}
}