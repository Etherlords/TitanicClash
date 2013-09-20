package utils;

import java.io.EOFException;
import java.io.UTFDataFormatException;

public class ByteArray {

	private static final int SIGNED_INT_BYTE_MAX = 256;

	private static final int INT_SIZE = 4;
	private static final int LONG_SIZE = 8;

	public byte[] buffer = new byte[10000];

	public int position = 0;
	public int length = 0;

	public ByteArray()
	{

	}

	public final void writeDouble(double v) {
		writeLong(Double.doubleToLongBits(v));
	}

	public final void writeLong(long v)
	{
		writeByte((byte) (v >>> 56));
		writeByte((byte)(v >>> 48));
		writeByte((byte)(v >>> 40));
		writeByte((byte)(v >>> 32));
		writeByte((byte)(v >>> 24));
		writeByte((byte)(v >>> 16));
		writeByte((byte)(v >>>  8));
		writeByte((byte)(v));
	}

	public void writeInt(int v)
	{
		writeByte((byte) ((v >>> 24) & 0xFF));
		writeByte((byte) ((v >>> 16) & 0xFF));
		writeByte((byte) ((v >>>  8) & 0xFF));
		writeByte((byte) ((v) & 0xFF));
	}

	public int utfSizeOf(String str)
	{
		int strlen = str.length();
		int utflen = 0;
		int c = 0;

        /* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		return utflen;
	}

	public int writeUTF(String str) throws UTFDataFormatException
	{
		int strlen = str.length();
		int utflen = utfSizeOf(str);
		int c = 0;

		if (utflen > 65535)
			throw new UTFDataFormatException(
					"encoded string too long: " + utflen + " bytes");

		writeByte((byte) ((utflen >>> 8) & 0xFF));
		writeByte((byte) ((utflen) & 0xFF));

		int i;
		for (i=0; i<strlen; i++) {
			c = str.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F))) break;
			writeByte((byte) c);
		}

		for (;i < strlen; i++){
			c = str.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				writeByte((byte) c);

			} else if (c > 0x07FF) {
				writeByte((byte) (0xE0 | ((c >> 12) & 0x0F)));
				writeByte((byte) (0x80 | ((c >>  6) & 0x3F)));
				writeByte((byte) (0x80 | ((c) & 0x3F)));
			} else {
				writeByte((byte) (0xC0 | ((c >>  6) & 0x1F)));
				writeByte((byte) (0x80 | ((c) & 0x3F)));
			}
		}

		return utflen + 2;
	}

	public void writeByte(byte b)
	{
		buffer[position] = b;
		position++;

		if(position>=length)
			length = position;
	}

	public int readUnsignedInt() throws EOFException {

		int ch1 = buffer[position];
		int ch2 = buffer[position+1];
		int ch3 = buffer[position+2];
		int ch4 = buffer[position+3];

		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();

		position += INT_SIZE;

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
	}

	public int readInt() throws EOFException {

		int ch1 = buffer[position];
		int ch2 = buffer[position+1];
		int ch3 = buffer[position+2];
		int ch4 = buffer[position+3];

		if(ch1 < 0)
			ch1 = SIGNED_INT_BYTE_MAX + ch1;

		if(ch2 < 0)
			ch2 = SIGNED_INT_BYTE_MAX + ch2;

		if(ch3 < 0)
			ch3 = SIGNED_INT_BYTE_MAX + ch3;

		if(ch4 < 0)
			ch4 = SIGNED_INT_BYTE_MAX + ch4;

		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();

		position += INT_SIZE;

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
	}
}
