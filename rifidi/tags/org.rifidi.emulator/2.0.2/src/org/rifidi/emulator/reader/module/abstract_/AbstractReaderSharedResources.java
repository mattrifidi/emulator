/*
 *  @(#)AbstractReaderSharedResources.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.module.abstract_;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.reader.source.ReaderSource;
import org.rifidi.emulator.rmi.client.ClientCallbackInterface;

/**
 * A container for resources which are unique to a single reader, but shared by
 * multiple systems within the reader. These shared resources are generally
 * independent from one another, and can represent various subsystems of the
 * reader.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AbstractReaderSharedResources {

	/**
	 * Message logger
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(AbstractReaderSharedResources.class);

	/**
	 * The map that will store the shared properties for this reader.
	 */
	private Map<String, ReaderProperty> propertyMap;

	/**
	 * The shared radio that multiple components may use.
	 */
	private GenericRadio radio;

	/**
	 * The power signal which controls the entire reader operation. This is
	 * equivalent to the power switch or plug on a physical reader.
	 */
	private ControlSignal<Boolean> readerPowerSignal;

	/**
	 * The shared tag memory for this reader.
	 */
	private TagMemory tagMemory;

	/**
	 * The name of the reader
	 */
	private String readerName;

	/**
	 * The exception handler
	 */
	private GenericExceptionHandler exc;

	/**
	 * The digester
	 */
	private CommandDigester dig;

	/**
	 * 
	 */
	private HashMap<String, ReaderSource> sourceMap;

	/**
	 * The GPIOController for the reader. If this reader does not support GPIO,
	 * the number of GPIO ports will be 0
	 */
	private GPIOController gpioController;

	private int numAntennas;
	
	private ClientCallbackInterface callbackManager;

	public ClientCallbackInterface getCallbackManager() {
		return callbackManager;
	}

	public void setCallbackManager(ClientCallbackInterface callbackManager) {
		this.callbackManager = callbackManager;
		this.gpioController.setCallbackInterface(callbackManager);
	}

	/**
	 * 
	 * @param aRadio
	 *            The radio
	 * @param aTagMemory
	 *            The tag memory
	 * @param readerPowerSignal
	 *            The global power switch for the reader
	 * @param readerName
	 *            The reader name
	 * @param exc
	 *            The exception Handler
	 * @param dig
	 *            The command digester
	 * @param numAntennas
	 *            the number of antennas
	 */
	public AbstractReaderSharedResources(GenericRadio aRadio, TagMemory aTagMemory,
			ControlSignal<Boolean> readerPowerSignal, String readerName,
			GenericExceptionHandler exc, CommandDigester dig, int numAntennas) {
		/* Set corresponding field values to their passed values */
		this.radio = aRadio;
		this.tagMemory = aTagMemory;
		this.readerPowerSignal = readerPowerSignal;
		this.readerName = readerName;
		this.exc = exc;
		this.dig = dig;
		this.numAntennas = numAntennas;

		/* Create a new empty map for the properties */
		this.propertyMap = new HashMap<String, ReaderProperty>();
		this.sourceMap = new HashMap<String, ReaderSource>();
		this.gpioController = new GPIOController(readerName);
	}

	/**
	 * Returns the propertyMap.
	 * 
	 * @return Returns the propertyMap.
	 */
	public final Map<String, ReaderProperty> getPropertyMap() {
		return this.propertyMap;
	}

	/**
	 * Returns the radio.
	 * 
	 * @return Returns the radio.
	 */
	public final GenericRadio getRadio() {
		return this.radio;
	}

	/**
	 * Returns the readerPowerSignal.
	 * 
	 * @return Returns the readerPowerSignal.
	 */
	public final ControlSignal<Boolean> getReaderPowerSignal() {
		return this.readerPowerSignal;
	}

	/**
	 * Returns the tagMemory.
	 * 
	 * @return Returns the tagMemory.
	 */
	public final TagMemory getTagMemory() {
		return this.tagMemory;
	}

	/**
	 * Resets all shared properties' values to their default values. This simply
	 * calls reset() on each ReaderProperty in the property map.
	 */
	public final void resetAllSharedProperties() {
		/* Reset all shared property values to defaults */
		Iterator<String> iter = this.getPropertyMap().keySet().iterator();

		while (iter.hasNext()) {
			String curKey = iter.next();
			ReaderProperty curProperty = this.getPropertyMap().get(curKey);
			curProperty.reset();
		}

	}

	/**
	 * Saves all shared properties' values as their default values. This simply
	 * calls save() on each ReaderProperty in the property map.
	 */
	public final void saveAllSharedProperties() {
		/* Save all shared property values as defaults */
		Iterator<String> iter = this.getPropertyMap().keySet().iterator();

		while (iter.hasNext()) {
			String curKey = iter.next();
			ReaderProperty curProperty = this.getPropertyMap().get(curKey);
			curProperty.save();
		}

	}

	/**
	 * Get the name of this reader.
	 * 
	 * @return The name of the reader.
	 */
	public String getReaderName() {
		return readerName;
	}

	/**
	 * @return the exc
	 */
	public GenericExceptionHandler getExceptionHandler() {
		return exc;
	}

	/**
	 * @return the dig
	 */
	public CommandDigester getDigester() {
		return dig;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public HashMap<String, CommandObject> getCommandsByState(String arg) {
		return dig.getQueryCommandsByState(arg);
	}

	/**
	 * 
	 * @return the sourceMap
	 */
	public HashMap<String, ReaderSource> getSourceMap() {
		return sourceMap;
	}

	/**
	 * 
	 * @param source
	 */
	public void addSource(ReaderSource source) {
		this.sourceMap.put(source.getName(), source);
	}

	public GPIOController getGpioController() {
		return gpioController;
	}

	public int getNumAntennas() {
		return numAntennas;
	}

}
