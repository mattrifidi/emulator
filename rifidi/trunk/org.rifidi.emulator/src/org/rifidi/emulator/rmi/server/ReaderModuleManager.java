/**
 * 
 */
package org.rifidi.emulator.rmi.server;

import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.extra.ClientProxy;
import gnu.cajo.utils.extra.TransparentItemProxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.rmi.client.ClientCallbackInterface;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderModuleManager implements ReaderModuleManagerInterface {

	private static final Log logger = LogFactory
			.getLog(ReaderModuleManager.class);

	private ReaderModule reader;

	private ClientProxy clientProxy;

	private ClientCallbackInterface client;

	public ReaderModuleManager(ReaderModule reader) {
		this.reader = reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#addTags(int,
	 *      java.util.Collection)
	 */
	public void addTags(int antennaNum, Collection<RifidiTag> tagsToAdd) {
		logger.info("Adding " + tagsToAdd.size() + " tags to antenna "
				+ antennaNum + " on " + reader.getName());
		GenericRadio r = (GenericRadio) reader.getSharedResources().getRadio();
		r.getAntennas().get(antennaNum).addTags(tagsToAdd);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#getReaderProperties()
	 */
	public GeneralReaderPropertyHolder getReaderProperties() {

		// TODO save ReaderGeneralPropertiesHolder inside the appropriate Reader
		// or at least in the ReaderModuleManager
		GeneralReaderPropertyHolder prop = new GeneralReaderPropertyHolder();
		prop.setNumAntennas(reader.getSharedResources().getNumAntennas());
		prop.setNumGPIs(reader.getSharedResources().getGpioController()
				.getNumGPIPorts());
		prop.setNumGPOs(reader.getSharedResources().getGpioController()
				.getNumGPOPorts());
		// prop.setReaderClass(reader.getClass());
		prop.setReaderName(reader.getName());
		for (String property : reader.getSharedResources().getPropertyMap()
				.keySet()) {
			prop.setProperty(property, reader.getSharedResources()
					.getPropertyMap().get(property).getPropertyStringValue());
		}
		return prop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#getReaderType()
	 */
	public String getReaderType() {
		reader.getClass().toString();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#removeTags(int,
	 *      java.util.Collection)
	 */
	public void removeTags(int antennaNum, Collection<Long> tagIDsToRemove) {
		logger.info("Removing " + tagIDsToRemove.size() + " tags on antenna "
				+ antennaNum + " on " + reader.getName());
		GenericRadio r = (GenericRadio) reader.getSharedResources().getRadio();
		r.getAntennas().get(antennaNum).removeTags(tagIDsToRemove);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#resumeReader()
	 */
	public void resumeReader() {
		reader.resume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#setGPIHigh(int)
	 */
	public void setGPIHigh(int GPIPort) {
		reader.getSharedResources().getGpioController().setGPIHigh(GPIPort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#setGPILow(int)
	 */
	public void setGPILow(int GPIPort) {
		reader.getSharedResources().getGpioController().setGPILow(GPIPort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#setProperties(org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder)
	 */
	public boolean setProperties(GeneralReaderPropertyHolder grph) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#setProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean setProperty(String propertyName, String propertyValue) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#suspendReader()
	 */
	public void suspendReader() {
		reader.suspend();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#turnReaderOff()
	 */
	public void turnReaderOff() {
		// TODO find out how should call this.
		reader.turnOff(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.ReaderModuleInterface#turnReaderOn()
	 */
	public void turnReaderOn() {
		this.reader.turnOn();
	}

	public RemoteInvoke getClientProxy() throws Exception {
		if (clientProxy == null) {
			clientProxy = new ClientProxy();
			client = (ClientCallbackInterface) TransparentItemProxy.getItem(
					clientProxy, new Class[] { ClientCallbackInterface.class });
			reader.getSharedResources().setCallbackManager(client);

		}
		return clientProxy.remoteThis;
	}

	// TODO Design Session about Properties
	public Collection<String> getAllCategories() throws Exception {
		return reader.getAllCategories();
	}

	// TODO Design Session about Properties
	public Map<String, String> getCommandActionTagsByCategory(String category)
			throws Exception {
		Map<String, String> commandList = new HashMap<String, String>();
		for (CommandObject commandObj : reader.getCommandsByCategory(category)) {
			commandList.put(commandObj.getDisplayName(), commandObj
					.getActionTag());
		}
		return commandList;
	}

	// TODO Design Session about Properties
	public String getReaderProperty(String propertyName) {
		ReaderProperty prop = reader.getSharedResources().getPropertyMap().get(
				propertyName);
		return prop.getPropertyStringValue();
	}

	// TODO Design Session about Properties
	public Boolean setReaderProperty(String propertyName, String propertyValue) {
		ReaderProperty prop = reader.getSharedResources().getPropertyMap().get(
				propertyName);
		prop.setPropertyValue(propertyValue);
		return null;
	}

	public List<RifidiTag> getTagList(int antennaNum) {
		try {
			logger.debug("in get tag list ant num is " + antennaNum);
			Collection<RifidiTag> collection = reader.getSharedResources()
					.getRadio().getAntennas().get(antennaNum).getTagList();
			if (collection == null) {
				return new ArrayList<RifidiTag>();
			}
			return new ArrayList<RifidiTag>(collection);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RifidiTag>();
		}
	}

}
