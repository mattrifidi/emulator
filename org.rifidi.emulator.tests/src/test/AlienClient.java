/**
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class AlienClient {

	public static final int PORT = 4321;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket ss = null;
		try {
			System.out.println("Starting a new ServerSocket on port " + PORT);
			ss = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			Socket clientSocket = null;
			try {
				System.out.println("Trying to accept a socket...");
				clientSocket = ss.accept();
				System.out.println("Accepted a socket sucessfully.");
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while (true) {
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					System.out.println("********************************");
					String str;
					while ((str = rd.readLine()) != null) {
						System.out.println(str);
					}
					System.out.println("********************************");
				}
			} catch (IOException e) {
				System.err.println("Connection broken");
			}
		}
	}

}
