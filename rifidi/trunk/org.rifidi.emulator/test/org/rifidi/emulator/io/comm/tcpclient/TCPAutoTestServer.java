/*
 *  TCPAutoTestServer.java
 *
 *  Created:	Jun 5, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.tcpclient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kyle
 * This is a TCPServer that is useful for testing Autonomous mode.  It starts up and listens to a specific port.  When it
 * recieves something it prints it out to the console as well as a file if the file name is supplied
 */
public class TCPAutoTestServer {

	ServerSocket srvr;

	public TCPAutoTestServer(InetSocketAddress addr) throws IOException {
		srvr = new ServerSocket();
		srvr.bind(addr);
	}

	/**
	 * The main method
	 * @param args.  1)Local IP Address 2) Port 3) File to print results out too.  The file prints out some kind of bad character that 
	 * gedit doesn't open, so I've been using vi to delete the character.
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {

		//String ip = args[0];
		//String port = args[1];
		@SuppressWarnings("unused")
		BufferedWriter out = null;
		if (args.length>2) {
			out = new BufferedWriter(new FileWriter(args[2]));
		}

		InetAddress my_addr = InetAddress.getByName(args[0]);
		TCPAutoTestServer s = new TCPAutoTestServer(new InetSocketAddress(
				my_addr, Integer.parseInt(args[1])));

		Socket skt = s.srvr.accept();
		System.out.print("Server has accepted a connection!\n");
		
		while (true) {
			
			int ch = skt.getInputStream().read();
			if(ch!=-1){
				System.out.print((char)ch);
			}
			if( skt.isClosed()){
				System.out.println("is closed");
			}

		}
	}
}
