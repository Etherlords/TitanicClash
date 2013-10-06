package utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;


public class ByteArray {

	public static final int BYTE_MAX = 256;

	public static final int INT_SIZE = 4;
	public static final int LONG_SIZE = 8;

	public byte[] buffer = new byte[100000];

	public int position = 0;
	public int length = 0;

	public ByteArray()
	{

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

	public void writeByte(byte b)
	{
		buffer[position] = b;
		position++;

		if(position>=length)
			length = position;
	}

	public byte readByte()
	{
		return buffer[position++];
	}

	public void writeInt(int v)
	{
		writeByte((byte) ((v >>> 24) & 0xFF));
		writeByte((byte) ((v >>> 16) & 0xFF));
		writeByte((byte) ((v >>>  8) & 0xFF));
		writeByte((byte) ((v) & 0xFF));
	}

	public int readInt() {

		int ch1 = readByte();
		int ch2 = readByte();
		int ch3 = readByte();
		int ch4 = readByte();

		if(ch1 < 0)
			ch1 = BYTE_MAX + ch1;

		if(ch2 < 0)
			ch2 = BYTE_MAX + ch2;

		if(ch3 < 0)
			ch3 = BYTE_MAX + ch3;

		if(ch4 < 0)
			ch4 = BYTE_MAX + ch4;

		//position += INT_SIZE;

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
	}

	public final void writeShort(short v)
	{
		writeByte((byte) ((v >>> 8) & 0xFF));
		writeByte((byte) ((v) & 0xFF));
	}

	public final int readShort() throws IOException {
		int ch1 = readByte();
		int ch2 = readByte();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (ch1 << 8) + (ch2 << 0);
	}

	public void read(byte[] to, int offset, int length)
	{
		length+=offset;
		int index = 0;
		for(int i = offset; i < length && i < this.length; i++)
		{
			to[index] = buffer[i];
			index++;
		}
	}

	public static final int utfSizeOf(String str)
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

	public String readUTF() throws IOException
	{
		return readUTF(this);
	}

	public final static String readUTF(ByteArray in) throws IOException
	{
		int utflen = in.readShort();
		return readUTF(in, in.position, utflen);
	}

	public final static String readUTF(ByteArray in, int offset, int utflen) throws IOException
	{
		byte[] bytearr = new byte[utflen * 2];
		char[] chararr = new char[utflen * 2];

		int c, char2, char3;
		int count = 0;
		int chararr_count=0;


		in.read(bytearr, offset, utflen);

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127) break;
			count++;
			chararr[chararr_count++]=(char)c;
		}

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
				case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
					count++;
					chararr[chararr_count++]=(char)c;
					break;
				case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
					count += 2;
					if (count > utflen)
						throw new UTFDataFormatException(
								"malformed input: partial character at end");
					char2 = (int) bytearr[count-1];
					if ((char2 & 0xC0) != 0x80)
						throw new UTFDataFormatException(
								"malformed input around byte " + count);
					chararr[chararr_count++]=(char)(((c & 0x1F) << 6) |
							(char2 & 0x3F));
					break;
				case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
					count += 3;
					if (count > utflen)
						throw new UTFDataFormatException(
								"malformed input: partial character at end");
					char2 = (int) bytearr[count-2];
					char3 = (int) bytearr[count-1];
					if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
						throw new UTFDataFormatException(
								"malformed input around byte " + (count-1));
					chararr[chararr_count++]=(char)(((c     & 0x0F) << 12) |
							((char2 & 0x3F) << 6)  |
							((char3 & 0x3F) << 0));
					break;
				default:
                    /* 10xx xxxx,  1111 xxxx */
					throw new UTFDataFormatException(
							"malformed input around byte " + count);
			}
		}
		// The number of chars produced may be less than utflen
		in.position+=utflen;
		return new String(chararr, 0, chararr_count);
	}

	public final void writeDouble(double v) {
		writeLong(Double.doubleToLongBits(v));
	}

	public double readDouble()
	{
		return 0;
	}
}
