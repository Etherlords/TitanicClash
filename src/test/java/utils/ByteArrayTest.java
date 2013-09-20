package utils;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: Asfel
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:byteArrTestConfig.xml")
public class ByteArrayTest {

	@Autowired
	private ByteArray byteArray;


	@Test
	public void testWriteDouble() throws Exception {

	}

	@Test
	public void testWriteLong() throws Exception {

	}


	@Test
	public void testWriteInt() throws Exception {
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



	}
}
