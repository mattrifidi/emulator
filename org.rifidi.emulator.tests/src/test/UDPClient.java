package test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UDPClient {
	public static void main(String args[]) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket(5000);
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
		byte[] sendData;
		byte[] receiveData = new byte[1024];
		sendData = ("STUPID MESSAGE").getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, 4000);
		System.out.println("Trying to connect");
		clientSocket.connect(IPAddress, 4000);
		System.out.println("Sending packet");
		clientSocket.send(sendPacket);
		System.out.println("Sent packet");
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		clientSocket.setSoTimeout(1000);
		System.out.println("Recieved packet");
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData(), 0,
				receivePacket.getLength());
		System.out.println("FROM SERVER:" + modifiedSentence);
		clientSocket.close();
	}
}
