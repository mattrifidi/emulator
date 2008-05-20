/*
 *  @(#)SerialCommunication.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.comm.buffered.BufferedCommunication;
import org.rifidi.emulator.io.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.io.comm.CommunicationException;

/**
 * An extension of the BufferedCommunication class which uses a serial port to
 * communicate through. <br>
 * 
 * 
 * @author Mike Graupner - mike@pramari.com
 * @author John Olender - john@pramari.com
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class SerialCommunication extends BufferedCommunication {

	
	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(SerialCommunicationIncomingMessageHandler.class);
	

	
	/**
	 * The serial port that sends and recieves data.  
	 */
	private SerialPort theSerialPort;

	/**
	 * The commPortIdentifier for the 
	 */
	private CommPortIdentifier theCommPortID;
	
	private OutputStream serialOut;
	
	private InputStream serialIn;
	
	private SerialIOBean serialBean;
	
	/**
	 * The logger class for the console
	 */
	private Log consoleLogger = null;
	
	/**
	 * This constructor creates a serial communication which communicates
	 * through a given port at the baud passed using the specified
	 * data/parity/stop pattern (ex: COM1,9600,8N1). The data, parity, and stop
	 * bits passed should generally be constant values from the
	 * gnu.io.SerialPort class.
	 * 
	 * @param port
	 * @param baud
	 * @param data
	 * @param parity
	 * @param stop
	 * @param flowControl
	 * @param maxMessageLength
	 */
	public SerialCommunication(Protocol prot, ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal,
			CommunicationPowerState initialPowerState,
			CommunicationConnectionState initialConnectionState, 
			SerialIOBean serialBean, String readerName) throws CommunicationException {
		super(initialPowerState, initialConnectionState, prot, powerControlSignal, connectionControlSignal);
		
		this.consoleLogger = LogFactory.getLog("console."+readerName);
				
		this.serialBean = serialBean;
		
		try {
			theCommPortID = CommPortIdentifier.getPortIdentifier(SerialCommunication.
					this.getSerialBean().getThePort());
			
			//create serial port
			theSerialPort = (SerialPort) theCommPortID.open(
					SerialCommunication.class.getSimpleName(), 
					SerialCommunication.this.getSerialBean().getTimeoutValue());
						
			serialOut = theSerialPort.getOutputStream();
			serialIn = theSerialPort.getInputStream();
			
		} catch(NoSuchPortException e) {
			this.getConsoleLogger().info("No such port: " + e.getMessage());
			throw new CommunicationException("No Such Serial Port");
		} catch ( PortInUseException e ) {
			logger.error("Port in use: " + e.getMessage());
			throw new CommunicationException("Serial Port in use");
		} catch( IOException e ) {
			logger.error( "IOException occured: " + e.getMessage() );
			throw new CommunicationException("Error creating serial port");
		}
	}


	
	/**
	 * Allows the use of the BufferedCommunication method by Serial-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getReceiveBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getReceiveBuffer() {
		return super.getReceiveBuffer();
	}

	/**
	 * Allows the use of the BufferedCommunication method by Serial-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getSendBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getSendBuffer() {
		return super.getSendBuffer();
	}

	/**
	 * Allows the use of the AbstractCommunication method by Serial-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#changePowerState(org.rifidi.emulator.io.comm.CommunicationPowerState)
	 */
	@Override
	protected void changePowerState(CommunicationPowerState anotherPowerState) {
		/* Invoke the super-class implementation. */
		super.changePowerState(anotherPowerState);
	}



	/**
	 * Returns the CommPortIdentifier for the serial module.  
	 * 
	 * @return the theCommPortID
	 */
	public CommPortIdentifier getCommPortIdentifier() {
		return theCommPortID;
	}



	/**
	 * Returns the SerialPort for the serial module.  
	 * 
	 * @return the theSerialPort
	 */
	public SerialPort getSerialPort() {
		return theSerialPort;
	}



	/**
	 * Get the inputStream for this serial module.  
	 * 
	 * @return the serialIn
	 */
	public InputStream getSerialInputStream() {
		return serialIn;
	}


	/**
	 * Get the outputStream for this serial module.  
	 * 
	 * @return the serialOut
	 */
	public OutputStream getSerialOutputStream() {
		return serialOut;
	}


	/**
	 * Returns the SerialIOBean to give access to data such as the port, 
	 * the baud, the flow control, etc.  
	 * 
	 * @return the serialBean
	 */
	public SerialIOBean getSerialBean() {
		return serialBean;
	}



	/**
	 * @return the consoleLogger
	 */
	public Log getConsoleLogger() {
		return consoleLogger;
	}

}
