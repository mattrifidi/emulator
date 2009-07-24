/**
 * 
 */
package org.rifidi.emulator.scripting.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.scripting.ReaderManager;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class ReaderManagerImpl implements ReaderManager {

	private Set<ReaderModuleFactory> moduleFactoryList = null;

	private HashMap<String, ReaderModule> readerModuleList = new HashMap<String, ReaderModule>();

	/**
	 * Called by spring.
	 * 
	 * @param moduleFactoryList
	 *            the moduleFactoryList to set
	 */
	public void setModuleFactoryList(Set<ReaderModuleFactory> moduleFactoryList) {
		this.moduleFactoryList = moduleFactoryList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.scripting.ReaderManager#getReaderTypes()
	 */
	public Set<String> getReaderTypes() {
		Set<String> retVal = new HashSet<String>();
		for (ReaderModuleFactory rmf : this.moduleFactoryList) {
			retVal.add(rmf.getReaderType());
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#addTags(java.lang.String,
	 * int, java.util.Set)
	 */
	@Override
	public void addTags(String readerID, int antenna, Set<RifidiTag> tagList) {
		ReaderModule rm = this.readerModuleList.get(readerID);
		if (rm == null) {
			return;
		}
		GenericRadio r = (GenericRadio) rm.getSharedResources().getRadio();
		r.getAntennas().get(antenna).addTags(tagList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#createGen1Tag(java.lang.String
	 * )
	 */
	@Override
	public RifidiTag createGen1Tag(String data) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#createGen2Tag(java.lang.String
	 * )
	 */
	@Override
	public RifidiTag createGen2Tag(String data) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#createReader(org.rifidi.emulator
	 * .reader.module.GeneralReaderPropertyHolder)
	 */
	@Override
	public String createReader(GeneralReaderPropertyHolder grph) {
		if (this.readerModuleList.containsKey(grph.getReaderName())) {
			// Reader with this unique ID already created.
			return null;
		}

		for (ReaderModuleFactory rmf : moduleFactoryList) {
			if (rmf.getReaderModuleClassName()
					.equals(grph.getReaderClassName())) {
				ReaderModule newMod = rmf.createReaderModule(grph);

				// Check for problems here, maybe for IP conflicts as well
				this.readerModuleList.put(grph.getReaderName(), newMod);

				return newMod.getName();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#deleteReader(java.lang.String
	 * )
	 */
	@Override
	public void deleteReader(String readerID) {
		ReaderModule rm = this.readerModuleList.get(readerID);
		rm.turnOff(this.getClass());
		this.readerModuleList.remove(readerID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#getDefault(java.lang.String)
	 */
	@Override
	public GeneralReaderPropertyHolder getDefault(String readerType) {
		// FIXME: The more I think about this method the more I doubt its possibility.
		// If we make it we might as well create a new method for every reader,
		// as we can't create a general one for all readers.
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#removeTags(java.lang.String,
	 * int, java.util.Set)
	 */
	@Override
	public void removeTags(String readerID, int antenna, Set<RifidiTag> tagList) {
		ReaderModule rm = this.readerModuleList.get(readerID);
		if (rm == null) {
			return;
		}
		Set<Long> tags = new LinkedHashSet<Long>();
		for (RifidiTag rt : tagList) {
			tags.add(rt.getTagEntitiyID());
		}

		// Whats up with this long removal thing? Should add a RifidiTag
		// convenience method.
		GenericRadio r = (GenericRadio) rm.getSharedResources().getRadio();
		r.getAntennas().get(antenna).removeTags(tags);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#setGPIPortHigh(java.lang.
	 * String, int)
	 */
	@Override
	public void setGPIPortHigh(String readerID, int port) {
		this.readerModuleList.get(readerID).getSharedResources()
				.getGpioController()._setGPIHigh(port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#setGPIPortLow(java.lang.String
	 * , int)
	 */
	@Override
	public void setGPIPortLow(String readerID, int port) {
		this.readerModuleList.get(readerID).getSharedResources()
				.getGpioController()._setGPILow(port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.scripting.ReaderManager#start(java.lang.String)
	 */
	@Override
	public void start(String readerID) {
		this.readerModuleList.get(readerID).turnOn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.scripting.ReaderManager#stop(java.lang.String)
	 */
	@Override
	public void stop(String readerID) {
		this.readerModuleList.get(readerID).turnOff(this.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#testGPOPort(java.lang.String,
	 * int)
	 */
	@Override
	public boolean testGPOPort(String readerID, int port) {
		return this.readerModuleList.get(readerID).getSharedResources()
				.getGpioController().getGPOState(port);
	}
}
