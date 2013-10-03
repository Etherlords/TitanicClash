package utils.io;

import utils.ByteArray;

public interface IDeserializer extends IStreamOperator
{

	/**
	 * Write data to stream as raw bytes
	 * @param	source an output stream implementation
	 * @return number of bytes writed into stream
	 */
	int deserialize(ByteArray source);

	int calculateWriteSize();

	int getWriteSize();

}