/*
 *  SerialSuspendCommunicationPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.serial;

import java.io.IOException;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This represents a suspended state for a serial port.  The Reader is still connected,
 * but it will be unresponsive.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialSuspendCommunicationPowerState extends
		BufferedSuspendedCommunicationPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(SerialSuspendCommunicationPowerState.class);
	
	/**
	 * The singleton instance for this state.
	 */
	private static final SerialSuspendCommunicationPowerState SINGLETON_INSTANCE 
		= new SerialSuspendCommunicationPowerState();
	
	/**
	 * 
	 */
	private SerialSuspendCommunicationPowerState() {
	}

	/**
	 * Get the instance of a SerialOnCommunicationPowerState
	 * 
	 * @return the SINGLETON_INSTANCE
	 */
	public static SerialSuspendCommunicationPowerState getInstance() {
		return SINGLETON_INSTANCE;
	}
	
	/**
	 * Turn off the serial module.  
	 * 
	 * @param pcObject   The powerControllable object/SeialCommunication object.  
	 */
	@Override
	public void turnOff(PowerControllable pcObject) {
		/* Invoke buffered handlers. */
		super.turnOff(pcObject, this.getClass());
		
		/* Get the SerialCommunication object to close the streams */
		SerialCommunication serialObject = (SerialCommunication)pcObject;
		
		try {
			serialObject.getSerialInputStream().close();
			serialObject.getSerialOutputStream().close();
		} catch(IOException e) {
			logger.debug( e.getMessage() );
		}
		
		serialObject.changePowerState(SerialOffCommunicationPowerState.getInstance());
	}

	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		logger.debug("Resuming...");
		
		/* Turn on the object in the superclass */
		super.resume(pcObject);
		
		/* Cast the serialObject */
		SerialCommunication serialObject = (SerialCommunication)pcObject;
		
		serialObject.changePowerState(SerialOnCommunicationPowerState.getInstance());
	}
}
