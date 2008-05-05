/*
 *  TCPAutoTestClient.java
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



import java.io.*;
import java.net.*;

/**
 * @author Kyle
 * This is a TCP client useful for testing out the TCPAutoTestClient to make sure it is working.
 */
class TCPAutoTestClient {
   /**
    * Main Method
    * @param args  1) IP Address 2) Port
    */
	public static void main(String args[]) {
      try {
         Socket skt = new Socket(args[0], Integer.parseInt(args[0]));
         DataOutputStream outToServer = new DataOutputStream(
        	     skt.getOutputStream());
         outToServer.writeBytes("Hello World\n");
         outToServer.writeBytes("Hello World");
      }
      catch(Exception e) {
         System.out.print("Whoops! It didn't work!\n");
      }
   }
}
