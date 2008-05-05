/**
 * 
 */
package org.rifidi.emulator.io.comm.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.protocol.RawProtocol;

/**
 * @author matt
 *
 */
public class SerialCommunicationTest2 extends TestCase {

	/**
	 * Message logger
	 */
	private static final Log logger = LogFactory
			.getLog(SerialCommunicationIncomingMessageHandler.class);

	SerialIOBean serialBean;

	SerialCommunication serialComm;

	//private static final String DATA_TO_SEND = "OMG";

	private static final String PORT = "COM2";

	private static final int BAUD = 115200;

	private static final int DATA_BITS = SerialPort.DATABITS_8;

	private static final int STOP_BITS = SerialPort.STOPBITS_1;

	private static final int PARITY_BITS = SerialPort.PARITY_NONE;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		serialBean = new SerialIOBean(PORT, 115200, 8, 0, 1, 0, 1024);
		serialComm = new SerialCommunication(new RawProtocol(), null, null,
				SerialOffCommunicationPowerState.getInstance(), 
				SerialConnectionlessCommunicationConnectionState.getInstance(),
				serialBean,"");
		System.out.println("Got past the start");
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		serialComm.turnOff(this.getClass());
	}


	/**
	 * Test sending out data to a dummy Serial Port
	 *
	 */
	public void testSend() {
		System.out.println("Starting the testSend method");
		
		serialComm.turnOn();
		
		
		/* Allow serial port to fully start */
		synchronized (this) {
			try {
				this.wait(1000);
			} catch (InterruptedException e2) {
				/* Do nothing */
			}
			this.notifyAll();
		}
		
		SerialPort clientPort;
		CommPortIdentifier clientCommID;
		InputStream clientIn = null;
		try {
			clientCommID = CommPortIdentifier.getPortIdentifier("COM3");
			System.out.println("Got past commIDConnection");
			clientPort = (SerialPort)clientCommID.open(SerialCommunicationTest.class.getSimpleName(),
							this.serialBean.getTimeoutValue());
			System.out.println("Got past clientPortConnection");
			clientIn = clientPort.getInputStream();
			System.out.println("Got past clientIn");
			clientPort.setSerialPortParams(BAUD, DATA_BITS, STOP_BITS, PARITY_BITS);
			System.out.println("Got past setSerialPortParams");
			
			ReceiveThread newThread = new ReceiveThread( clientIn );
			
			System.out.println("Got past the run thread, now sending data...");
			this.serialComm.getSendBuffer().addToBuffer("OMG SEND DATA".getBytes());
			
			newThread.run();
		} catch(IOException e) {
			logger.error(e.getMessage());
			fail();
		}
		catch (NoSuchPortException e) {
			logger.error(e.getMessage());
			fail();
		} catch(PortInUseException e) {
			logger.error(e.getMessage());
			fail();
		}catch(UnsupportedCommOperationException e) {
			logger.error(e.getMessage());
			fail();
		} catch(DataBufferInterruptedException e) {
			logger.error(e.getMessage());
			fail();
		}
		
		
		serialComm.turnOff(this.getClass());
	}

}
