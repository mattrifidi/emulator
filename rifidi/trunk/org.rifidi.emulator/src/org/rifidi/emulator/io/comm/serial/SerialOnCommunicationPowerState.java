/*
 *  SerialOnCommunicationPowerState.java
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
import org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This represents the "On" state of the SerialCommunication module.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialOnCommunicationPowerState extends
		BufferedOnCommunicationPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(SerialOnCommunicationPowerState.class);
	
	/**
	 * The singleton instance for this state.
	 */
	private static final SerialOnCommunicationPowerState SINGLETON_INSTANCE 
		= new SerialOnCommunicationPowerState();
	
	/**
	 * 
	 */
	private SerialOnCommunicationPowerState() {		 
	}

	/**
	 * Get the instance of a SerialOnCommunicationPowerState
	 * 
	 * @return the SINGLETON_INSTANCE
	 */
	public static SerialOnCommunicationPowerState getInstance() {
		return SINGLETON_INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		logger.debug("Turned off by " + callingClass);
		
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
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		super.suspend(pcObject);
		
		SerialCommunication serialObject = (SerialCommunication)pcObject;
		
		serialObject.changePowerState(SerialSuspendCommunicationPowerState.getInstance());
	}
	
}
