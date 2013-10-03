package utils.io;
import utils.ByteArray;

public interface ISerializer extends IStreamOperator
{
	/**
	 * Read data from raw byte stream
	 * @param	source an data input stream
	 * @return number of bytes readed from stream
	 */
	int serialize(ByteArray source);

	int calculateReadSize();

	int getReadSize();
}

