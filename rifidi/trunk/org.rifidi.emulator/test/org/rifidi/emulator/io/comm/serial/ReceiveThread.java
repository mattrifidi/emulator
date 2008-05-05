/**
 * 
 */
package org.rifidi.emulator.io.comm.serial;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author matt
 *
 */
public class ReceiveThread implements Runnable {
	
	
	DataInputStream dataStream;
	/**
	 * Constructor for the recieveThread
	 */
	public ReceiveThread( InputStream iStream ) {
		dataStream = new DataInputStream(iStream);
	}
	
	/**
	 * The thread that will do the recieving
	 */
	public void run() {
		System.out.println("Starting the read thread");
		
		ArrayList<Byte> receivedData = new ArrayList<Byte>();
		
		boolean eof = false;

		/* Read as fully as we can */
		while (!eof) {
			try {
				/* Attempt to read in a byte */
				receivedData.add(new Byte(dataStream.readByte()));
				if (dataStream.available() <= 0) {
					eof = true;
				}
			} catch (EOFException eofe) {
				/* Indicate end of file */
				eof = true;
			} catch (IOException e) {
				eof = true;
			}
		}
		
		byte[] message = new byte[receivedData.size()];
		
		for (int i = 0; i < receivedData.size(); i++) {
			message[i] = receivedData.get(i).byteValue();
		}
		
		System.out.println( "Message = " + new String(message) );
	}
}
