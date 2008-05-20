package org.rifidi.emulator.reader.llrp.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageServer {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		ServerSocket srvr;
		InetAddress my_addr = InetAddress.getByName(args[0]);
		
		srvr = new ServerSocket();
		srvr.bind(new InetSocketAddress(
				my_addr, Integer.parseInt(args[1])));
		
		Socket skt = srvr.accept();
		BufferedReader bf = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		
		while(true){
			int ch = bf.read();
			if(ch!=-1){
				System.out.print((byte)ch);
			}
		}

	}

}
