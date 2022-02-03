/**
 * 
 */
package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class UDPEchoServer {
	static final int BUFFERSIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket sock;
		DatagramPacket pack = new DatagramPacket(new byte[BUFFERSIZE],
				BUFFERSIZE);
		try {
			sock = new DatagramSocket(4000);
		} catch (SocketException e) {
			System.out.println(e);
			return;
		}
		// echo back everything
		while (true) {
			try {
				sock.receive(pack);
				sock.send(pack);
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
	}
}
