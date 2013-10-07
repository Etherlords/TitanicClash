package utils;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UTFDataFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:byteArrTestConfig.xml")
public class ByteArrayTest {

	static Logger log = Logger.getLogger(ByteArrayTest.class.getName());


	private ByteArray byteArray;

	@Before
	public void setUp()
	{
		byteArray = new ByteArray();
	}

	@Test
	public void readShort() throws Exception
	{
		for(int i = 1; i < 65535; i++)
		{
			byteArray.position = 0;
		    byteArray.writeShort(i);
			byteArray.position = 0;
			int theShorty = byteArray.readShort();

			Assert.assertEquals("check read short", i, theShorty);
		}

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

		while(ByteArray.utfSizeOf(s) < 65535)
		{
			s += "а";
		}

		byteArray.position = 0;
		byteArray.writeUTF(s);
		byteArray.position = 0;

		Assert.assertEquals("check max utf string", s, byteArray.readUTF());

		byteArray.position = 0;
		byteArray.writeUTF(s);
		byteArray.writeUTF(s);
		byteArray.writeUTF(s);
		byteArray.position = 0;

		Assert.assertEquals("check multiply read utf 1", s, byteArray.readUTF());
		Assert.assertEquals("check multiply read utf 2", s, byteArray.readUTF());
		Assert.assertEquals("check multiply read utf 3", s, byteArray.readUTF());

		byteArray.position = 0;
		for(int i = 0; i < 100; i++)
		{
			byteArray.writeUTF("test");
		}

		byteArray.position = 0;
		for(int i = 0; i < 100; i++)
		{
			Assert.assertEquals("check multiply short strings" + i, "test", byteArray.readUTF());
		}

		//log.debug(s);
	}

	@Test
	public void testCut() throws Exception
	{
		byteArray.writeInt(255);
		byteArray.writeInt(344331);
		byteArray.writeInt(123123);
		byteArray.writeInt(4314141);

		byteArray.cut(8);

		byteArray.position = 0;

		Assert.assertEquals("test read int after cut 1", 123123, byteArray.readInt());
		Assert.assertEquals("test read int after cut 2", 4314141, byteArray.readInt());

		byteArray.position = 0;
		byteArray.cut(4);

		Assert.assertEquals("test read int after cut 3", 4314141, byteArray.readInt());
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
	public void testWriteUTF() throws Exception
	{
		String s = "s";

		while(ByteArray.utfSizeOf(s + s) < 65535)
		{
			s += s;
		}

		byteArray.writeUTF(s);
		byteArray.position = 0;

		Assert.assertEquals("check writed utf", s, byteArray.readUTF());

		s += s;

		byteArray.position = 0;

		try
		{
			byteArray.writeUTF(s);
		}catch (UTFDataFormatException e)
		{
		  	//OK
			return;
		}

		throw new Exception("Should catch UTFDataFormatException because try to write too long utf");


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
