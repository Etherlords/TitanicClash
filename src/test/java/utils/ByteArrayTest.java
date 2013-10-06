package utils;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:byteArrTestConfig.xml")
public class ByteArrayTest {

	private ByteArray byteArray;

	@Before
	public void setUp()
	{
		byteArray = new ByteArray();
	}

	@Test
	public void testReadUTF() throws Exception
	{
		String s = "eat that french tea and eat my eyes";
   	    byteArray.writeUTF(s);
   	    byteArray.writeUTF(s);
		byteArray.position = 0;

		Assert.assertEquals("check read utf output", s, byteArray.readUTF());
		Assert.assertEquals("check read utf output2", s, byteArray.readUTF());
	}


	@Test
	public void testWriteDouble() throws Exception {

	}

	@Test
	public void testWriteLong() throws Exception {

	}


	@Test
	public void testWriteInt() throws Exception {
		byteArray.position = 0;
		byteArray.writeInt(1);

		Assert.assertEquals(byteArray.position, byteArray.length);
		Assert.assertEquals(byteArray.length, ByteArray.INT_SIZE);

		byte[] buffer = new byte[ByteArray.INT_SIZE];

		buffer[0] = 0;
		buffer[1] = 0;
		buffer[2] = 0;
		buffer[3] = 1;

		for(int i = 0; i < ByteArray.INT_SIZE; i++)
		{
			Assert.assertEquals(byteArray.buffer[i], buffer[i]);
		}
	}

	@Test
	public void testUtfSizeOf() throws Exception {

	}

	@Test
	public void testWriteUTF() throws Exception {

	}

	@Test
	public void testWriteByte() throws Exception {

	}

	@Test
	public void testReadUnsignedInt() throws Exception {

	}

	@Test
	public void testReadInt() throws Exception {

		for(int i = 0; i < 250; i++)
		{
			byteArray.writeInt(i);
			byteArray.position -= ByteArray.INT_SIZE;
			Assert.assertEquals(i, byteArray.readInt());
		}

		byteArray.position = 0;

		byteArray.writeByte((byte)-1);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);

		byteArray.writeByte((byte)(ByteArray.BYTE_MAX +-1));
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);

		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)-1);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);

		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)(ByteArray.BYTE_MAX +-1));
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);

		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)-1);
		byteArray.writeByte((byte)0);

		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)(ByteArray.BYTE_MAX +-1));
		byteArray.writeByte((byte)0);

		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)-1);

		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)0);
		byteArray.writeByte((byte)(ByteArray.BYTE_MAX +-1));

		byteArray.position = 0;

		Assert.assertEquals(byteArray.readInt(), byteArray.readInt());
		Assert.assertEquals(byteArray.readInt(), byteArray.readInt());
		Assert.assertEquals(byteArray.readInt(), byteArray.readInt());
		Assert.assertEquals(byteArray.readInt(), byteArray.readInt());



	}
}
