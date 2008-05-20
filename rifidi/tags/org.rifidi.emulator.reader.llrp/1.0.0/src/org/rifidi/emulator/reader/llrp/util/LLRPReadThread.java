package org.rifidi.emulator.reader.llrp.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import edu.uark.csce.llrp.Message;

/**
 * This class is a thread that reads LLRP messages from a TCP socket. It takes
 * special precautions to make sure that even long messages are able to be read,
 * even if the data for the entire message is not available on the socket all at
 * the same time
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class LLRPReadThread extends Thread {

	/**
	 * The socket to read from
	 */
	private Socket socket = null;

	/**
	 * The Input stream from the socket
	 */
	private DataInputStream inStream = null;

	/**
	 * The writer to use to print out debug messages
	 */
	private Writer writer = null;

	/**
	 * The queue to store LLRP messages on that were properly read in
	 */
	private LinkedBlockingQueue<Message> queue = null;

	/**
	 * 
	 * @param socket
	 *            The TCP Socket
	 * @param writer
	 *            The writer to write output on (can write to System.out or a
	 *            file, for example). If you don't want output, pass in null)
	 */
	public LLRPReadThread(Socket socket, Writer writer) {
		this.socket = socket;
		this.writer = writer;
		this.queue = new LinkedBlockingQueue<Message>();
	}

	@Override
	public void run() {
		super.run();
		if (socket.isConnected())
			read();
	}

	/**
	 * Read everything from the stream until the socket is closed
	 */
	public void read() {
		try {
			this.inStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e1) {
			System.out.println("Could not get the InputStream form socket");
			e1.printStackTrace();
		}

		while (!socket.isClosed()) {
			try {
				Message m = null;
				// The message header
				byte[] first = new byte[6];

				// the complete message
				byte[] msg;

				// Read in the message header. If -1 is read, there is no more
				// data available, so close the socket
				if (inStream.read(first, 0, 6) == -1) {
					socket.close();
					break;
				}
				int msgLength = 0;

				try {
					// calculate message length
					msgLength = LLRPUtilities.calculateLLRPMessageLength(first);
				} catch (IllegalArgumentException e) {
					System.out.println("Illegal argument Exception");
					e.printStackTrace();
					System.exit(-1);
				}

				if (writer != null) {
					writer.write("\n<== INPUT from READER\n");
					writer.write("**** Length: " + msgLength + " bytes\n");
					writer.flush();
				}

				/*
				 * the rest of bytes of the message will be stored in here
				 * before they are put in the accumulator. If the message is
				 * short, all messageLength-6 bytes will be read in here at
				 * once. If it is long, the data might not be available on the
				 * socket all at once, so it make take a couple of iterations to
				 * read in all the bytes
				 */
				byte[] temp = new byte[msgLength - 6];

				// all the rest of the bytes will be put into the accumulator
				ArrayList<Byte> accumulator = new ArrayList<Byte>();

				// add the first six bytes to the accumulator so that it will
				// contain all the bytes at the end
				for (byte b : first) {
					accumulator.add(b);
				}

				// the number of bytes read on the last call to read()
				int numBytesRead = 0;

				// read from teh input stream and put bytes into the accumulator
				// while there are still bytes left to read on the socket and
				// the entire message has not been read
				while (((msgLength - accumulator.size()) != 0)
						&& numBytesRead != -1) {

					numBytesRead = inStream.read(temp, 0, msgLength
							- accumulator.size());

					for (int i = 0; i < numBytesRead; i++) {
						accumulator.add(temp[i]);
					}
				}

				if ((msgLength - accumulator.size()) != 0) {
					System.out
							.println("Error: Discrepency between message size"
									+ " in header and actual number of bytes read");
				}

				msg = new byte[msgLength];

				// copy all bytes in the accumulator to the msg byte array
				for (int i = 0; i < accumulator.size(); i++) {
					msg[i] = accumulator.get(i);
				}

				// turn the byte array into an LLRP Message Object
				m = Message.receive(new ByteArrayInputStream(msg));
				queue.offer(m);

				if (writer != null) {
					writer.write("*** " + m.getClass().getSimpleName() + "\n");
					writer.write(m.toXMLString() + "=== END of INPUT\n\n");
					writer.flush();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Receive the next Message
	 * 
	 * @return returns the Message form the Queue and removes it. It blocks if
	 *         there is no Message.
	 */
	public Message getNextMessage() {
		Message m = null;
		try {
			m = queue.take();
		} catch (InterruptedException e) {
			// nothing
		}
		return m;
	}
}
