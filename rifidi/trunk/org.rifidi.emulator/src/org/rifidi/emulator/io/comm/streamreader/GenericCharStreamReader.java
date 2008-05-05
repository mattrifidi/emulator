package org.rifidi.emulator.io.comm.streamreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is used to read bytes as characters from the socket.
 * @author kyle
 *
 */
public class GenericCharStreamReader implements AbstractStreamReader {

	/**
	 * The bufferedReader that reads the socket
	 */
	BufferedReader bf = null;
	
	public GenericCharStreamReader(InputStream in){
		bf = new BufferedReader(new InputStreamReader(in));
	}
	
	/**
	 *This method reads the input stream and returns bytes
	 */
	public byte[] read() throws IOException {
		String s = bf.readLine();
		if(s!=null){
			return s.getBytes();
		}
		else return null;
	}

}
