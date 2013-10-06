package net.packets;

import utils.ByteArray;
import utils.io.StreamOperator;

import java.util.ArrayList;

public class BytePacket
{
	public ByteArray source;

	public int type = 0;
	public StreamOperator streamOperator;
	public StreamOperator headerOperator;

	public ArrayList<Object> input;
	public ArrayList<Object> output;

	public ArrayList<Object> headerInput = new ArrayList<Object>(2);
	public ArrayList<Object> headerOutput;

	public BytePacket()
	{
		headerInput.add(0);
		headerInput.add(0);
	}

	public void setStreamOperator(StreamOperator value)
	{
		streamOperator = value;
	}

	public void setHeaderOperator(StreamOperator value)
	{
		headerOperator = value;
	}

	public void setType(int value)
	{
		type = value;
	}

	public void read()
	{
		headerOutput = new ArrayList<Object>();

		headerOperator.output = headerOutput;
		headerOperator.serialize(source);

		if (streamOperator != null)
		{
			output = new ArrayList<Object>();

			streamOperator.output = output;
			streamOperator.serialize(source);
		}
	}

	public void write()
	{
		int size = 0;

		if (streamOperator != null)
		{
			streamOperator.input = input;

			if (streamOperator.isStaticSize())
				size += streamOperator.getWriteSize();
			else
				size += streamOperator.calculateWriteSize();
		}

		if (headerOperator.isStaticSize())
			size += headerOperator.getWriteSize();
		else
			size += headerOperator.calculateWriteSize();

		headerInput.set(0, size);
		headerInput.set(1, type);

		headerOperator.input = headerInput;
		headerOperator.deserialize(source);

		if (streamOperator != null)
			streamOperator.deserialize(source);
	}


}