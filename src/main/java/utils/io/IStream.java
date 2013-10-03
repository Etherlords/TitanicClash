package utils.io;

/**
 * ...
 * @author Nikro
 */
public interface IStream
{
	void addSerializer(ISerializer serializer);
	void addDeserializer(IDeserializer deserializer);
}