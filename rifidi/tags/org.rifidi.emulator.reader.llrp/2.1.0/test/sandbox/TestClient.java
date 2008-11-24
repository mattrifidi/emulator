package sandbox;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;

import edu.uark.csce.llrp.AISpec;
import edu.uark.csce.llrp.AISpecStopTrigger;
import edu.uark.csce.llrp.AccessCommand;
import edu.uark.csce.llrp.AccessReportSpec;
import edu.uark.csce.llrp.AccessSpec;
import edu.uark.csce.llrp.AccessSpecStopTrigger;
import edu.uark.csce.llrp.AddROSpec;
import edu.uark.csce.llrp.AntennaConfiguration;
import edu.uark.csce.llrp.C1G2EPCMemorySelector;
import edu.uark.csce.llrp.C1G2Read;
import edu.uark.csce.llrp.C1G2TagSpec;
import edu.uark.csce.llrp.C1G2TargetTag;
import edu.uark.csce.llrp.CloseConnection;
import edu.uark.csce.llrp.DeleteAccessSpec;
import edu.uark.csce.llrp.DeleteROSpec;
import edu.uark.csce.llrp.EnableROSpec;
import edu.uark.csce.llrp.EventNotificationState;
import edu.uark.csce.llrp.GPITriggerValue;
import edu.uark.csce.llrp.GetReport;
import edu.uark.csce.llrp.InventoryParameterSpec;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.PeriodicTriggerValue;
import edu.uark.csce.llrp.RFReceiver;
import edu.uark.csce.llrp.RFTransmitter;
import edu.uark.csce.llrp.ROBoundarySpec;
import edu.uark.csce.llrp.ROReportSpec;
import edu.uark.csce.llrp.ROSpec;
import edu.uark.csce.llrp.ROSpecStartTrigger;
import edu.uark.csce.llrp.ROSpecStopTrigger;
import edu.uark.csce.llrp.ReaderEventNotificationSpec;
import edu.uark.csce.llrp.SetReaderConfig;
import edu.uark.csce.llrp.StartROSpec;
import edu.uark.csce.llrp.TagObservationTrigger;
import edu.uark.csce.llrp.TagReportContentSelector;

public class TestClient {

	/*
	 * Connection settings: - IP_ADDRESS: IP of the reader - READER_PORT: LLRP
	 * Connection Port
	 */
	public static String IP_ADDRESS = null;
	public static int READER_PORT = -1;

	public static boolean modeClient = true;
	// Class to control the reader through the Administration Interface
	private static Socket connection;
	private static DataOutputStream out;

	@SuppressWarnings("unused")
	private static int ROSPEC_ID = 1;
	@SuppressWarnings("unused")
	private static int ACCESSSPEC_ID = 4;

	// Reader Thread printing to console and store incoming Messages
	private ReadThread rt = null;

	public static void main(String args[]) {
		if (args.length != 2) {
			IP_ADDRESS = "127.0.0.1";
			READER_PORT = 5084;
			System.out.println("No IP address and port were supplied.  Using "
					+ IP_ADDRESS + ":" + READER_PORT);
		} else {
			IP_ADDRESS = args[0];
			READER_PORT = Integer.parseInt(args[1]);
		}
		new TestClient();
	}

	public TestClient() {

		// Make the Connection and start up the ReaderThread
		initialize();
		
		giveTheReaderSomeTime(500, "wait for 500 ms");
		
		int msgID=1;
		SetReaderConfig src = createSetReaderConfig();
		src.setMessageID(msgID++);
		this.write(src, "Set Reader Config");
		
		giveTheReaderSomeTime(500, "wait for 500 ms");
		
		AddROSpec ars = new AddROSpec();
		ars.setMessageID(msgID++);
		ars.setROSpecParam(createRoSpec(11));
		write(ars, "Add ROSpec");
		
		giveTheReaderSomeTime(500, "wait for 500 ms");
		
		EnableROSpec ers = new EnableROSpec();
		ers.setMessageID(msgID++);
		ers.setROSpecID(11);
		write(ers, "enable rospec");
		
		giveTheReaderSomeTime(500, "wait for 500 ms");
		
		StartROSpec srs = new StartROSpec();
		srs.setMessageID(msgID++);
		srs.setROSpecID(11);
		write(srs, "start rospec");
		
		GetReport grs = new GetReport();
		
		for(int i = 0; i<5; i++){
			giveTheReaderSomeTime(2000, "wait for 500 ms");
			write(grs, "GET RROSPEC");
		}
		
		giveTheReaderSomeTime(5000, "waiting for 5 secs");
		
		DeleteAccessSpec das = new DeleteAccessSpec();
		das.setMessageID(msgID++);
		das.setAccessSpecID(12);
		write(das, "delete access spec");
		
		giveTheReaderSomeTime(500, "wait for 500 ms");
		
		DeleteROSpec drs = new DeleteROSpec();
		drs.setROSpecID(11);
		drs.setMessageID(msgID++);
		write(drs, "delete rospec");
		
		giveTheReaderSomeTime(1000, "waiting for 1 sec");
		
		// ** Create a CloseConnection Message and send it to the reader
		CloseConnection cc = new CloseConnection();
		write(cc, "CloseConnection");

		synchronized (rt) {
			try {
				System.out
						.println("Wait for the Reader to close the Connection");
				rt.wait();
			} catch (InterruptedException e) {
				// Quit the Programm
			}
		}

	}

	private void initialize() {
		if (modeClient) {
			System.out.print("Connecting to Reader....");
			// Try to establish a connection to the reader
			try {
				connection = new Socket(IP_ADDRESS, READER_PORT);
				out = new DataOutputStream(connection.getOutputStream());
			} catch (IOException e) {
				throw new AssertionError(
						"Couldn't establish a connection to the Reader");
			}
		} else {
			System.out.print("Waiting for Reader to connect....");
			try {
				ServerSocket serverSocket = new ServerSocket(READER_PORT);
				connection = serverSocket.accept();
				out = new DataOutputStream(connection.getOutputStream());
			} catch (IOException e) {
				throw new AssertionError(
						"Couldn't establish a connection to the Reader");
			}
		}
		// Start up the ReaderThread to read messages form socket to Console
		rt = new ReadThread(connection);
		rt.start();
		System.out.println("connected");
	}

	private boolean giveTheReaderSomeTime(long time, String msg) {
		if (msg != null)
			System.out.println(msg);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// Doesn't matter
		}
		return true;
	}

	/**
	 * Send a llrp message to the reader
	 * 
	 * @param msg
	 *            Message to be send
	 * @param message
	 *            Description for output purposes
	 */
	private void write(Message msg, String message) {
		try {
			byte[] b = msg.serialize();
			write(b, message);
		} catch (IOException e) {
			System.out.println("Couldn't send Command " + message);
			e.printStackTrace();
		}
	}

	/**
	 * Send a llrp message to the reader
	 * 
	 * @param msg
	 *            Message to be send
	 * @param message
	 *            Description for output purposes
	 */
	private void write(byte[] msg, String message) {
		try {
			out.write(msg);
			System.out.println("==> Sending message " + message);
		} catch (IOException e) {
			System.out.println("Couldn't send Command " + message);
			e.printStackTrace();
		}
	}
	/**
	 * Method for easy Set Reader Config creation
	 * 
	 * @author Kyle Neumeier - kyle@pramari.com
	 * @return A SetReaderConfig message
	 */
	private SetReaderConfig createSetReaderConfig() {
		SetReaderConfig src = new SetReaderConfig();

		// set up ro reporting
		/*ROReportSpec rrs = new ROReportSpec();
		rrs.setN((short) 0);
		rrs.setROReportTrigger((byte) 0); // no trigger
		TagReportContentSelector trcs = new TagReportContentSelector();
		trcs.setEnableAccessSpecID(true);
		trcs.setEnableAntennaID(true);
		trcs.setEnableChannelIndex(false);
		trcs.setEnableFirstSeenTimestamp(false);
		trcs.setEnableInventoryParameterSpecID(false);
		trcs.setEnableLastSeenTimestamp(false);
		trcs.setEnablePeakRSSI(false);
		trcs.setEnableROSpecID(false);
		trcs.setEnableSpecIndex(false);
		trcs.setEnableTagSeenCount(true);
		rrs.setTagReportContentSelectorParam(trcs);
		C1G2EPCMemorySelector memsel = new C1G2EPCMemorySelector();
		memsel.setEnableCRC(false);
		memsel.setEnablePCBits(false);
		trcs.addAirProtocolSpecificEPCMemorySelectorParam(memsel);
		src.setROReportSpecParam(rrs);

		AccessReportSpec ars = new AccessReportSpec();
		// report Access report at end of access spec
		ars.setAccessReportTrigger((byte) 1);
		src.setAccessReportSpecParam(ars);*/

		// Send out Event notifications for ROSpec events and AISpec envents
		ReaderEventNotificationSpec rens = new ReaderEventNotificationSpec();
		EventNotificationState[] ensa = new EventNotificationState[9];
		for (int i = 1; i < 2; i++) {
			ensa[i] = new EventNotificationState();
			ensa[i].setEventType((short) i);

			if (i == 1 || i == 2 || i == 6) {
				ensa[i].setNotificationState(true);
			} else {
				ensa[i].setNotificationState(false);
			}
			rens.addEventNotificationStateParam(ensa[i]);
		}

		// src.setROReportSpecParam(rrs);
		// src.setAccessReportSpecParam(ars);
		src.setReaderEventNotificationSpecParam(rens);

		/*
		 * GPIPortCurrentState cs = new GPIPortCurrentState();
		 * cs.setGPIConfig(true); cs.setGPIPortNum((byte)1);
		 * src.addGPIPortCurrentStateParam(cs);
		 * 
		 * GPIPortCurrentState cs1 = new GPIPortCurrentState();
		 * cs1.setGPIConfig(true); cs1.setGPIPortNum((byte)2);
		 * src.addGPIPortCurrentStateParam(cs1);
		 * 
		 * GPIPortCurrentState cs2 = new GPIPortCurrentState();
		 * cs2.setGPIConfig(true); cs2.setGPIPortNum((byte)3);
		 * src.addGPIPortCurrentStateParam(cs2);
		 * 
		 * GPIPortCurrentState cs3 = new GPIPortCurrentState();
		 * cs3.setGPIConfig(true); cs3.setGPIPortNum((byte)4);
		 * src.addGPIPortCurrentStateParam(cs3);
		 */

		return src;

	}

	/**
	 * Method for easy ROSpec creation
	 * 
	 * @author Kyle Neumeier - kyle@pramari.com
	 * @param id
	 *            Message ID of the rospec
	 * @return ROSpec
	 */

	private ROSpec createRoSpec(int id) {
		ROSpec rs = new ROSpec();

		rs.setROSpecID(id);
		rs.setCurrentState((byte) 0);
		rs.setPriority((byte) 0);

		ROBoundarySpec rbs = new ROBoundarySpec();

		ROSpecStartTrigger rsstart = new ROSpecStartTrigger();
		rsstart.setROSpecStartTriggerType((byte) 0); // null trigger
		PeriodicTriggerValue ptv = new PeriodicTriggerValue();
		ptv.setOffset(5000);
		ptv.setPeriod(10000);
		rsstart.setPeriodicTriggerValueParam(ptv);

		ROSpecStopTrigger rstop = new ROSpecStopTrigger();
		rstop.setROSpecStopTriggerType((byte) 0); //null trigger
		rstop.setDurationTriggerValue(5000);
		GPITriggerValue gtv = new GPITriggerValue();
		gtv.setGPIEvent(false);
		gtv.setGPIPortNum((short) 1);
		gtv.setTimeout(5000);
		rstop.setGPITriggerValueParam(gtv);

		rbs.setROSpecStartTriggerParam(rsstart);
		rbs.setROSpecStopTriggerParam(rstop);
		rs.setROBoundarySpecParam(rbs);

		AISpec ais = new AISpec();
		ais.addAntennaElement((short) 1);

		AISpecStopTrigger aisst = new AISpecStopTrigger();
		aisst.setAISpecStopTriggerType((byte) 0); // null AI Stop Trigger
		aisst.setDurationTrigger(1000);
		TagObservationTrigger tot = new TagObservationTrigger();
		tot.setT((short) 5000);
		tot.setTriggerType((byte) 3);
		tot.setTimeout((5000));
		aisst.setTagObservationTriggerParam(tot);
		ais.setAISpecStopTriggerParam(aisst);

		InventoryParameterSpec isp = new InventoryParameterSpec();
		isp.setInventoryParameterSpecID((byte) 0);
		isp.setProtocolID((byte) 0);
		AntennaConfiguration ac = new AntennaConfiguration();
		ac.setAntennaId((short) 0);
		RFReceiver rf = new RFReceiver();
		rf.setReceiverSensitivity((short) 1);
		ac.setRFReceiverParam(rf);
		RFTransmitter rft = new RFTransmitter();
		rft.setChannelIndex((short) 1);
		rft.setHopTableId((short) 1);
		rft.setTransmitPower((short) 1);
		ac.setRFTransmitterParam(rft);
		isp.addAntennaConfigurationParam(ac);
		ais.addInventoryParameterSpecParam(isp);

		rs.addSpecParam(ais);
		
		ROReportSpec rrs = new ROReportSpec();
		rrs.setN((short) 0);
		rrs.setROReportTrigger((byte) 0); // no trigger
		TagReportContentSelector trcs = new TagReportContentSelector();
		trcs.setEnableAccessSpecID(true);
		trcs.setEnableAntennaID(true);
		trcs.setEnableChannelIndex(false);
		trcs.setEnableFirstSeenTimestamp(false);
		trcs.setEnableInventoryParameterSpecID(false);
		trcs.setEnableLastSeenTimestamp(false);
		trcs.setEnablePeakRSSI(false);
		trcs.setEnableROSpecID(false);
		trcs.setEnableSpecIndex(false);
		trcs.setEnableTagSeenCount(true);
		rrs.setTagReportContentSelectorParam(trcs);
		C1G2EPCMemorySelector memsel = new C1G2EPCMemorySelector();
		memsel.setEnableCRC(false);
		memsel.setEnablePCBits(false);
		trcs.addAirProtocolSpecificEPCMemorySelectorParam(memsel);
		rs.setROReportSpecParam(rrs);



		return rs;
	}

	/**
	 * Method for easy AccessSpec creation. It creates an accessspec that writes
	 * to epc memory
	 * 
	 * @param accessSpecID
	 * @param roSpecID
	 * @return
	 * @author kyle Neumeier - kyle@pramari.com
	 */
	public AccessSpec createAccessSpec(int accessSpecID, int roSpecID) {
		AccessSpec as = new AccessSpec();
		as.setAccessSpecID(accessSpecID);
		as.setAntennaId((short) 0); // perform on all antennas
		as.setCurrentState(false);
		as.setProtocolId((byte) 1); // protocol is C1G2
		as.setROSpecID(roSpecID); // perform on all rospecs

		// execute this accessSpec only once
		AccessSpecStopTrigger asst = new AccessSpecStopTrigger();
		asst.setAccessSpecStopTrigger((byte) 1);
		asst.setOperationCountValue((short) 1);
		as.setAccessSpecStopTriggerParam(asst);

		AccessCommand ac = new AccessCommand();

		// match all epc tags
		C1G2TagSpec ts = new C1G2TagSpec();
		C1G2TargetTag tt = new C1G2TargetTag();
		tt.setMatch(true);
		byte[] mask = new byte[1];
		mask[0] = 0;
		tt.setMask(mask);
		byte[] val = new byte[1];
		val[0] = 0;
		tt.setData(val);
		tt.setMB(1);
		tt.setPointer((short) 3);
		ts.setTagPattern1(tt);
		ac.setTagSpecParam(ts);

		// write to epc memory
		C1G2Read c1g2read = new C1G2Read();
		c1g2read.setAccessPassword(0);
		c1g2read.setOpSpecID((short) 20);
		c1g2read.setWordPointer((short) 2);
		c1g2read.setWordCount((short)6);
		c1g2read.setMB(1);
		ac.addOpSpecParam(c1g2read);
		as.setAccessCommandParam(ac);
		
		AccessReportSpec ars = new AccessReportSpec();
		// report Access report at end of access spec
		ars.setAccessReportTrigger((byte) 1);
		as.setAccessReportSpecParam(ars);

		return as;
	}


	/**
	 * Reader Thread to read from socket to System console
	 */
	class ReadThread extends Thread {
		private DataInputStream inStream = null;

		private Socket socket = null;
		private LinkedBlockingQueue<Message> queue = null;

		/**
		 * Thread for constant reading of the stream
		 * 
		 * @param inStream
		 */
		public ReadThread(Socket socket) {
			this.socket = socket;
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
					byte[] first = new byte[6];
					byte[] rest;
					byte[] msg;
					// If -1 is read, the socket was closed
					if (inStream.read(first, 0, 6) == -1) {
						System.out
								.println("SOCKET WAS CLOSED.. CLOSING SOCKET HANDLE");

						// Close socket
						socket.close();

						// skip because no data was read
						break;
					}
					int msgLength = 0;
					try {
						msgLength = calculateLLRPMessageLength(first);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
						System.exit(-1);
					}
					System.out.println("<== INPUT from READER");
					System.out.println("**** Length: " + msgLength + " bytes");

					rest = new byte[msgLength - 6];
					int readBytes = inStream.read(rest, 0, msgLength - 6);
					if ((msgLength - (readBytes + 6)) != 0)
						System.out
								.println("!!!!! Failure happend while reading message");
					msg = new byte[msgLength];
					System.arraycopy(first, 0, msg, 0, 6);
					System.arraycopy(rest, 0, msg, 6, rest.length);
					m = Message.receive(new ByteArrayInputStream(msg));
					queue.offer(m);

					System.out.println("***=== " + m.getClass().getSimpleName()
							+ "===");
					System.out.println(m.toXMLString() + "=== END of INPUT\n");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Send in the first 6 bytes of an LLRP Message
		 * 
		 * @param bytes
		 * @return
		 */
		private int calculateLLRPMessageLength(byte[] bytes)
				throws IllegalArgumentException {
			long msgLength = 0;
			int num1 = 0;
			int num2 = 0;
			int num3 = 0;
			int num4 = 0;

			num1 = ((ByteAndHexConvertingUtility.unsignedByteToInt(bytes[2])));
			num1 = num1 << 32;
			if (num1 > 127) {
				throw new RuntimeException(
						"Cannot construct a message greater than "
								+ "2147483647 bytes (2^31 - 1), due to the fact that there are "
								+ "no unsigned ints in java");
			}

			num2 = ((ByteAndHexConvertingUtility.unsignedByteToInt(bytes[3])));
			num2 = num2 << 16;

			num3 = ((ByteAndHexConvertingUtility.unsignedByteToInt(bytes[4])));
			num3 = num3 << 8;

			num4 = (ByteAndHexConvertingUtility.unsignedByteToInt(bytes[5]));

			msgLength = num1 + num2 + num3 + num4;

			if (msgLength < 0) {
				throw new IllegalArgumentException(
						"LLRP message length is less than 0");
			} else {
				return (int) msgLength;
			}
		}

		/**
		 * Receive the next Message
		 * 
		 * @return returns the Message form the Queue and removes it. It blocks
		 *         if there is no Message.
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
}