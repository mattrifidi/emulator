package org.rifidi.emulator.io.comm.streamreader;

import java.io.IOException;

/**
 * Because different RFID transmit data differently accross the wire, different
 * ways of reading the streams are needed off the socket are needed. This
 * interface provides a method used to do that.
 * 
 * Make sure to use a constructor that takes a single argument of type
 * InputStream
 * 
 * @author kyle
 * 
 */
public interface AbstractStreamReader {

	/**
	 * Reads directly from the socket and returns a byte array.
	 * 
	 * @return A byte array that contains bytes from the socket. It should
	 *         return null if there is nothing on the buffer
	 * @throws IOException
	 */
	public abstract byte[] read() throws IOException;

}
