/*
 *  SerialOffCommunicationPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.serial;

import gnu.io.UnsupportedCommOperationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState;


/**
 * This represents the reader in the "off" state.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialOffCommunicationPowerState extends
		BufferedOffCommunicationPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(SerialOffCommunicationPowerState.class);
	
	/**
	 * The singleton instance for this state.
	 */
	private static final SerialOffCommunicationPowerState SINGLETON_INSTANCE 
		= new SerialOffCommunicationPowerState();
	
	/**
	 * 
	 */
	private SerialOffCommunicationPowerState() {
	}

	/**
	 * Get the instance of a SerialOnCommunicationPowerState
	 * 
	 * @return the SINGLETON_INSTANCE
	 */
	public static SerialOffCommunicationPowerState getInstance() {
		return SINGLETON_INSTANCE;
	}
	
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOn(PowerControllable pcObject) {
		logger.debug("Turning on...");
		
		/* Turn on the object in the superclass */
		super.turnOn(pcObject);
		
		/* Cast the serialObject */
		SerialCommunication serialObject = (SerialCommunication)pcObject;
		
		SerialIOBean tempBean = serialObject.getSerialBean();
		try {
			serialObject.getSerialPort().setSerialPortParams(tempBean.getBaudRate(), 
				tempBean.getDataBits(), 
				tempBean.getStopBits(),
				tempBean.getParityBits());
			serialObject.getSerialPort().setFlowControlMode(tempBean.getFlowControl());
		} catch( UnsupportedCommOperationException e ) {
			logger.error( e.getMessage() );
		}
		
		new Thread(new SerialCommunicationIncomingMessageHandler(serialObject)).start();
		new Thread(new SerialCommunicationOutgoingMessageHandler(serialObject)).start();
		
		serialObject.changePowerState(SerialOnCommunicationPowerState.getInstance());
	}

}
