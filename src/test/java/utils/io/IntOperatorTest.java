package utils.io;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.ByteArray;

/**
 * User: Asfel
 */
public class IntOperatorTest
{
	private ByteArray input;
	private ByteArray output;
	private IntOperator processor;

	@Before
	public void setUp() throws Exception
	{
		input = new ByteArray();
		output = new ByteArray();

		processor = new IntOperator();
	}

	@Test
	public void testSetValue() throws Exception
	{
		processor.setValue(4321);

		int value = (Integer) processor.getValue();

		Assert.assertEquals("check processor value", value, 4321);
	}

	@Test
	public void testDeserialize() throws Exception
	{
		processor.setValue(4321);
		int writeSize = processor.deserialize(output);
		output.position = 0;

		Assert.assertEquals("check buffer size", writeSize, TypesSize.INT_SIZE);
		Assert.assertEquals("check desirialization value", output.readInt(), 4321);
	}

	@Test
	public void testSerialize() throws Exception
	{
		input.writeInt(1234);
		input.position = 0;

		Assert.assertEquals("check buffer size", processor.serialize(input), TypesSize.INT_SIZE);

		int value = (Integer) processor.getValue();

		Assert.assertEquals("check serialization value", value, 1234);
	}

	@Test
	public void testCalculateReadSize() throws Exception
	{
		Assert.assertEquals("check calculateReadSize", processor.calculateReadSize(), TypesSize.INT_SIZE);
	}

	@Test
	public void testCalculateWriteSize() throws Exception
	{
		Assert.assertEquals("check CalculateWriteSize", processor.calculateWriteSize(), TypesSize.INT_SIZE);
	}

	@Test
	public void testGetWriteSize() throws Exception
	{
		Assert.assertEquals("check writeSize", processor.calculateWriteSize(), TypesSize.INT_SIZE);
	}

	@Test
	public void testGetReadSize() throws Exception
	{
		Assert.assertEquals("check readSize", processor.calculateReadSize(), TypesSize.INT_SIZE);
	}
}
