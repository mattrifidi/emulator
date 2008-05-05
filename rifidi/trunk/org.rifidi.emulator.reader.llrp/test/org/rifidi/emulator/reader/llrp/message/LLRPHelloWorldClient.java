package org.rifidi.emulator.reader.llrp.message;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import org.rifidi.emulator.reader.llrp.util.LLRPReadThread;

import edu.uark.csce.llrp.AISpec;
import edu.uark.csce.llrp.AISpecStopTrigger;
import edu.uark.csce.llrp.AccessCommand;
import edu.uark.csce.llrp.AccessReportSpec;
import edu.uark.csce.llrp.AccessSpec;
import edu.uark.csce.llrp.AccessSpecStopTrigger;
import edu.uark.csce.llrp.AddAccessSpec;
import edu.uark.csce.llrp.AddROSpec;
import edu.uark.csce.llrp.C1G2EPCMemorySelector;
import edu.uark.csce.llrp.C1G2TagSpec;
import edu.uark.csce.llrp.C1G2TargetTag;
import edu.uark.csce.llrp.C1G2Write;
import edu.uark.csce.llrp.CloseConnection;
import edu.uark.csce.llrp.DeleteROSpec;
import edu.uark.csce.llrp.DisableROSpec;
import edu.uark.csce.llrp.EnableAccessSpec;
import edu.uark.csce.llrp.EnableROSpec;
import edu.uark.csce.llrp.EventNotificationState;
import edu.uark.csce.llrp.GetAccessSpecs;
import edu.uark.csce.llrp.GetROSpecs;
import edu.uark.csce.llrp.GetReaderCapabilities;
import edu.uark.csce.llrp.GetReaderConfig;
import edu.uark.csce.llrp.InventoryParameterSpec;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.ROBoundarySpec;
import edu.uark.csce.llrp.ROReportSpec;
import edu.uark.csce.llrp.ROSpec;
import edu.uark.csce.llrp.ROSpecStartTrigger;
import edu.uark.csce.llrp.ROSpecStopTrigger;
import edu.uark.csce.llrp.ReaderEventNotification;
import edu.uark.csce.llrp.ReaderEventNotificationData;
import edu.uark.csce.llrp.ReaderEventNotificationSpec;
import edu.uark.csce.llrp.SetReaderConfig;
import edu.uark.csce.llrp.StartROSpec;
import edu.uark.csce.llrp.TagReportContentSelector;

/**
 * HelloWorldClient shows the basic communication with a LLRP Reader. It
 * consists of two Threads. The main Thread is responsible for sending messages
 * to the Reader. The ReaderThread listens for incomming messages, prints them
 * on the System output and stores them in a queue. If the Reader closes the
 * Connection both Threads should shut down.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class LLRPHelloWorldClient {

	/*
	 * Connection settings: - IP_ADDRESS: IP of the reader - READER_PORT: LLRP
	 * Connection Port
	 */
	public static String IP_ADDRESS = null;
	public static int READER_PORT = -1;

	// Class to control the reader through the Administration Interface
	private static Socket connection;
	private static DataOutputStream out;
	
	public static BufferedWriter fileWriter = null;

	// Reader Thread printing to console and store incomming Messages
	private LLRPReadThread rt = null;

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
		try {
			fileWriter = new BufferedWriter(new FileWriter("hello_world_output.txt"));
		} catch (IOException e) {
			System.out.println("Cannot write output to file");
		}
		new LLRPHelloWorldClient();
	}

	public LLRPHelloWorldClient() {

		// Make the Connection and start up the ReaderThread
		initialize();

		// ** Wait for the EventNotificationEvent the Reader sends whenever a
		// connection is made
		Message m = rt.getNextMessage();
		ReaderEventNotification readerEventNotification = (ReaderEventNotification) m;
		ReaderEventNotificationData red = readerEventNotification
				.getReaderEventNotificationDataParam();
		if (red.getConnectionAttemptEventParam() != null)
			System.out.println("++ Success we got a ConnectionAttemptEvent ");

		int ROSPEC_ID = 1;
		int ACCESSSPEC_ID = 2;

		// ** Create a GetReaderCapabilities and sent it to the reader
		GetReaderCapabilities getReaderCapabilities = new GetReaderCapabilities();
		getReaderCapabilities.setRequestedData((byte) 0); // Get all
		// capabilities
		write(getReaderCapabilities, "GetReaderCapabilities");

		// ** Create a SetReaderConfig Message,
		SetReaderConfig setReaderConfig = createSetReaderConfig();
		write(setReaderConfig, "");

		// ** Create a GetReaderConfig Message, set the parameter and sent it to
		// the reader
		GetReaderConfig getReaderConfig = new GetReaderConfig();
		getReaderConfig.setRequestedData((byte) 0); // Get all configuration
		// data
		write(getReaderConfig, "GetReaderConfig");

		// ** Create a AddRospec with a basic AISpec and send it to the reader
		AddROSpec addRoSpec = new AddROSpec();
		// Create a ROSpec by calling the function createROSpec
		ROSpec roSpec = createRoSpec(ROSPEC_ID);
		addRoSpec.setROSpecParam(roSpec);
		write(addRoSpec, "ROSpec");

		// **Create a GetROspec message and send it to the reader
		GetROSpecs getRospecs = new GetROSpecs();
		write(getRospecs, "GetROSpecs");

		// ** Create a EnableRospec to the corresponding Rospec Message and send
		// it to the reader
		EnableROSpec enableROSpec = new EnableROSpec();
		enableROSpec.setROSpecID(ROSPEC_ID);
		write(enableROSpec, "EnableROSpec");

		// ** Create a StartRospec to the corresponding Rospec Message and send
		// it to the reader
		StartROSpec startROSpec = new StartROSpec();
		startROSpec.setROSpecID(ROSPEC_ID);
		write(startROSpec, "StartROSpec");

		giveTheReaderSomeTime(6000, "wait for end of ROSpec");

		// **Create a AccessSpec and send it to the reader
		AddAccessSpec addAccessSpec = new AddAccessSpec();
		AccessSpec accessSpec = createAccessSpec(ACCESSSPEC_ID, ROSPEC_ID);
		addAccessSpec.setAccessSpecParam(accessSpec);
		write(addAccessSpec, "AddAccessSpec");

		// **Create a GetAccessSpecs message and send it to the reader
		GetAccessSpecs getAccessSpecs = new GetAccessSpecs();
		write(getAccessSpecs, "GetAccessSpecs");

		// ** Create an enable accessspec message and send it to the reader
		EnableAccessSpec enableAccessSpec = new EnableAccessSpec();
		enableAccessSpec.setAccessSpecID(ACCESSSPEC_ID);
		write(enableAccessSpec, "EnableAccessSpec");

		// ** Start the previous ROSpec again;
		write(startROSpec, "StartROSpec");

		giveTheReaderSomeTime(6000, "wait for end of ROSpec");

		// **DisableROSpec
		DisableROSpec disableROSpec = new DisableROSpec();
		disableROSpec.setROSpecID(ROSPEC_ID);
		write(disableROSpec, "DisableROSpec");

		// ** Create a DeleteRospec to the corresponding Rospec Message and send
		// it to the reader
		DeleteROSpec deleteRoSpec = new DeleteROSpec();
		deleteRoSpec.setROSpecID(ROSPEC_ID);
		write(deleteRoSpec, "DeleteROSpec");

		giveTheReaderSomeTime(3000, "wait before sending closeConnection");

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
		// Try to establish a connection to the reader
		try {
			connection = new Socket(IP_ADDRESS, READER_PORT);
			out = new DataOutputStream(connection.getOutputStream());
		} catch (IOException e) {
			throw new AssertionError(
					"Couldn't establish a connection to the Reader");
		}
		// Start up the ReaderThread to read messages form socket to Console
		rt = new LLRPReadThread(connection, fileWriter);
		rt.start();
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
			out.write(msg.serialize());
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
		ROReportSpec rrs = new ROReportSpec();
		rrs.setN((short) 0);
		rrs.setROReportTrigger((byte) 2); // report at end of rospec
		TagReportContentSelector trcs = new TagReportContentSelector();
		trcs.setEnableAccessSpecID(true);
		trcs.setEnableAntennaID(true);
		trcs.setEnableChannelIndex(true);
		trcs.setEnableFirstSeenTimestamp(true);
		trcs.setEnableInventoryParameterSpecID(true);
		trcs.setEnableLastSeenTimestamp(true);
		trcs.setEnablePeakRSSI(true);
		trcs.setEnableROSpecID(true);
		trcs.setEnableSpecIndex(true);
		trcs.setEnableTagSeenCount(true);
		C1G2EPCMemorySelector sel = new C1G2EPCMemorySelector();
		sel.setEnableCRC(false);
		sel.setEnablePCBits(false);
		trcs.addAirProtocolSpecificEPCMemorySelectorParam(sel);
		rrs.setTagReportContentSelectorParam(trcs);


		AccessReportSpec ars = new AccessReportSpec();
		// report Access report whenever ro report is generated
		ars.setAccessReportTrigger((byte) 0);

		// Send out Event notifications for ROSpec events and AISpec envents
		ReaderEventNotificationSpec rens = new ReaderEventNotificationSpec();
		EventNotificationState[] ensa = new EventNotificationState[9];
		for (int i = 0; i < 9; i++) {
			ensa[i] = new EventNotificationState();
			ensa[i].setEventType((short) i);

			if (i == 2 || i == 6) {
				ensa[i].setNotificationState(true);
			} else {
				ensa[i].setNotificationState(false);
			}
			rens.addEventNotificationStateParam(ensa[i]);
		}

		src.setROReportSpecParam(rrs);
		src.setAccessReportSpecParam(ars);
		src.setReaderEventNotificationSpecParam(rens);
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
		rsstart.setROSpecStartTriggerType((byte) 0); // Null Stop Trigger
		ROSpecStopTrigger rstop = new ROSpecStopTrigger();
		rstop.setROSpecStopTriggerType((byte) 1); // duration stop trigger
		rstop.setDurationTriggerValue(5000); // stop after 5 seconds
		rbs.setROSpecStartTriggerParam(rsstart);
		rbs.setROSpecStopTriggerParam(rstop);
		rs.setROBoundarySpecParam(rbs);

		AISpec ais = new AISpec();
		AISpecStopTrigger aisst = new AISpecStopTrigger();
		aisst.setAISpecStopTriggerType((byte) 0); // null AI Stop Trigger
		aisst.setDurationTrigger(0);

		ais.setAISpecStopTriggerParam(aisst);

		InventoryParameterSpec isp = new InventoryParameterSpec();
		isp.setInventoryParameterSpecID((byte) 1);
		isp.setProtocolID((byte) 1);
		ais.addInventoryParameterSpecParam(isp);
		ais.addAntennaElement((short) 1);

		rs.addSpecParam(ais);

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
	private AccessSpec createAccessSpec(int accessSpecID, int roSpecID) {
		AccessSpec as = new AccessSpec();
		as.setAccessSpecID(accessSpecID);
		as.setAntennaId((short) 0); // perform on all antennas
		as.setCurrentState(false);
		as.setProtocolId((byte) 1); // protocol is C1G2
		as.setROSpecID(0); // perform on all rospecs

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
		byte[] mask = new byte[0];
		tt.setMask(mask);
		byte[] val = new byte[0];
		tt.setData(val);
		tt.setMB(2);
		tt.setPointer((short) 3);
		ts.setTagPattern1(tt);
		ac.setTagSpecParam(ts);

		// write to epc memory
		C1G2Write c1g2write = new C1G2Write();
		c1g2write.addWriteDataElement((short) 0x0102);
		c1g2write.addWriteDataElement((short) 0x0304);
		c1g2write.addWriteDataElement((short) 0x0506);
		c1g2write.addWriteDataElement((short) 0x0708);
		c1g2write.addWriteDataElement((short) 0x090A);
		c1g2write.addWriteDataElement((short) 0x0B0C);
		c1g2write.setAccessPassword(0);
		c1g2write.setMB(2);
		c1g2write.setOpSpecID((short) 2);
		c1g2write.setWordPointer((short) 2);
		c1g2write.setMB(1);
		ac.addOpSpecParam(c1g2write);
		as.setAccessCommandParam(ac);

		return as;
	}
}