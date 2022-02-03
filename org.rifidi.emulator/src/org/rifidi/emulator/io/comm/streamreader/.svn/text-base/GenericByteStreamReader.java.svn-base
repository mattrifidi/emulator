package org.rifidi.emulator.io.comm.streamreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to read raw bytes from the socket.
 * 
 * @author kyle
 * 
 */
public class GenericByteStreamReader implements AbstractStreamReader {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(GenericByteStreamReader.class);
	
	/**
	 * The socket to read the bytes from
	 */
	InputStream in = null;

	public GenericByteStreamReader(InputStream in) {
		this.in = in;
	}

	/**
	 * This method reads bytes fromt the socket until it is empty then returns
	 * them as a byte array
	 */
	public byte[] read() throws IOException {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		int b = in.read();
		while (in.available() != 0) {
			bytes.add((byte) b);
			b = in.read();
		}
		bytes.add((byte)b);
		/* If there is nothing on the buffer, return null */
		if (bytes.isEmpty() || bytes.get(0)==-1) {
			logger.debug("returning bytes is null");
			return null;
		}
		/* Else return the bytes */
		else {
			byte[] retVal = new byte[bytes.size()];

			logger.debug("returning bytes");
			for (int i = 0; i < bytes.size(); i++) {
				retVal[i] = bytes.get(i);
			}
			return retVal;
		}

	}

}
