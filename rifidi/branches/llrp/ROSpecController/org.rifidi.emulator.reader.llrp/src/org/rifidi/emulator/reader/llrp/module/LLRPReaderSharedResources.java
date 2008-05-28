package org.rifidi.emulator.reader.llrp.module;

import java.util.ArrayList;
import java.util.HashMap;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.llrp.accessspec.AccessSpecList;
import org.rifidi.emulator.reader.llrp.keepalive.KeepAliveController;
import org.rifidi.emulator.reader.llrp.properties.Properties;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;

import edu.uark.csce.llrp.ROSpec;
import edu.uark.csce.llrp.TagReportData;

public class LLRPReaderSharedResources extends AbstractReaderSharedResources {

	/**
	 * The connection signal for the reader's InteractiveCommandProcessor and
	 * its underlying communication.
	 */
	private ControlSignal<Boolean> interactiveConnectionSignal;

	/**
	 * The power signal for the reader's InteractiveCommandProcessor and its
	 * underlying communication.
	 */
	private ControlSignal<Boolean> interactivePowerSignal;

	/**
	 * Properties map for the LLRP reader.
	 */
	private HashMap<String, String> propertiesMap;

	private Properties properties;

	/**
	 * This hashmap contains the ROSpecs that the client has added. The rospec
	 * data structure used for this hashmap is the llrp-tk one, because this is
	 * used to send back the added_Rospecs that the client has added in a
	 * GET_ROSPECS Message response
	 */
	private HashMap<Integer, ROSpec> added_Rospecs;

	/**
	 * This is a list of tags that have been seen but have not yet been reported
	 */
	private ArrayList<TagReportData> tagReportDataEntries;

	/**
	 * this is a variable that records the time that the reader was started for
	 * the purpose of calculating the reader's uptime
	 */
	private long startTimeOfReader;

	/**
	 * This is the controller for sending out keep alive messages
	 */
	private KeepAliveController keepAliveController;

	/**
	 * A list of accessSpecs that have been added
	 */
	public AccessSpecList accessSpecs;

	/**
	 * This variable holds the last time in ms that the reader was suspended. It
	 * is used to calculate the total time of reader suspension so that
	 * time-based triggers can be resumed properly
	 */
	private long lastSuspendTime = 0;

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Shared Resources constructor
	 * 
	 * @param aRadio
	 *            Radio to use
	 * @param aTagMemory
	 *            TagMemory to use
	 * @param gpioController
	 *            The Controller of GPIO ports
	 * @param readerPowerSignal
	 *            Power signal of reader
	 * @param readerName
	 *            Reader Name
	 * @param exc
	 *            Exception Handler
	 * @param dig
	 *            Command Digester
	 * @param interactiveConnectionSignal
	 *            Connection Signal of Interactive Communication
	 * @param interactivePowerSignal
	 *            Power signal of Interactive Communication
	 */
	public LLRPReaderSharedResources(GenericRadio aRadio, TagMemory aTagMemory,
			ControlSignal<Boolean> readerPowerSignal, String readerName,
			GenericExceptionHandler exc, CommandDigester dig,
			ControlSignal<Boolean> interactiveConnectionSignal,
			ControlSignal<Boolean> interactivePowerSignal, int numAntennas) {
		super(aRadio, aTagMemory, readerPowerSignal, readerName, exc, dig,
				numAntennas);
		this.interactiveConnectionSignal = interactiveConnectionSignal;
		this.interactivePowerSignal = interactivePowerSignal;
		this.propertiesMap = new HashMap<String, String>();
		properties = new Properties();
		added_Rospecs = new HashMap<Integer, ROSpec>();
		this.tagReportDataEntries = new ArrayList<TagReportData>();
		this.keepAliveController = new KeepAliveController(readerName);
		this.accessSpecs = new AccessSpecList();

		/*
		 * TODO: to calculate more correctly, this call should be moved to the
		 * admin console
		 */
		this.startTimeOfReader = System.currentTimeMillis();
	}

	/**
	 * @return the interactiveConnectionSignal
	 */
	public ControlSignal<Boolean> getInteractiveConnectionSignal() {
		return interactiveConnectionSignal;
	}

	/**
	 * @return the interactivePowerSignal
	 */
	public ControlSignal<Boolean> getInteractivePowerSignal() {
		return interactivePowerSignal;
	}

	/**
	 * @return the propertiesMap
	 */
	public HashMap<String, String> getPropertiesMap() {
		return propertiesMap;
	}

	public HashMap<Integer, ROSpec> getAdded_Rospecs() {
		return added_Rospecs;
	}

	/**
	 * This method retuns the uptime from an offset
	 * 
	 * @param timeInMillis
	 *            An offset to calculate the uptime from.
	 * @return
	 */
	public long getUptime(long offsetInMillis) {
		return (offsetInMillis - startTimeOfReader);
	}

	/**
	 * The Tag Report Data Entries are the tags that have been seen but not yet
	 * reported.
	 * 
	 * @return
	 */
	public ArrayList<TagReportData> getTagReportDataEntries() {
		return tagReportDataEntries;
	}

	public KeepAliveController getKeepAliveController() {
		return keepAliveController;
	}

	public long getLastSuspendTime() {
		return lastSuspendTime;
	}

	public void setLastSuspendTime(long lastSuspendTime) {
		this.lastSuspendTime = lastSuspendTime;
	}

}
