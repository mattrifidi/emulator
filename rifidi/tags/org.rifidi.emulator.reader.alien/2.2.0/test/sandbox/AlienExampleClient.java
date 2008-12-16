/*
 *  AlienExampleClient.java
 *
 *  Created:	Dec 6, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class shows how to create a program to talk to the alien reader.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class AlienExampleClient {

	public static final String IP_ADDRESS = new String("127.0.0.1");
	public static final int READER_PORT = 20000;
	private static Socket connection = null;

	private static PrintWriter out = null;
	private static BufferedReader in = null;
	
	/**
	 * Initialize the connection and send username/password
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void  init() throws IOException, InterruptedException{
		connection = new Socket("127.0.0.1", READER_PORT);

		in = new BufferedReader(new InputStreamReader(connection
				.getInputStream()));
		out = new PrintWriter(connection.getOutputStream());

		Thread.sleep(500);
		System.out.println(readFromReader(in));
		out.write("alien\n");
		out.flush();
		System.out.println(readFromReader(in));
		Thread.sleep(500);
		out.write("password\n");
		out.flush();
		System.out.println(readFromReader(in));
	}
	
	/**
	 * Tear down the connection
	 * @throws IOException
	 */
	private void tearDown() throws IOException{
		out.write("q");
		out.flush();
		connection.close();
	}
	
	/**
	 * Get tags back from the alien reader
	 * @return
	 * @throws IOException
	 */
	private String getTags() throws IOException{
		String command = "t";
		out.write(command + "\n");
		out.flush();
		String returnVal = readFromReader(in);
		return returnVal;
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		AlienExampleClient client = new AlienExampleClient();
		client.init();
		Thread.sleep(500);
		String tags = client.getTags();
		System.out.println(tags);
		Thread.sleep(500);
		client.tearDown();

	}
	
	/**
	 * Read responses from the socket
	 * @param inBuf
	 * @return
	 * @throws IOException
	 */
	public static String readFromReader(BufferedReader inBuf) throws IOException{
		StringBuffer buf=new StringBuffer();
		int ch=inBuf.read();
		while((char)ch!='\0'){
			buf.append((char)ch);
			ch=inBuf.read();
		}
		return buf.toString();
	}

}
