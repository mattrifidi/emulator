/**
 * 
 */
package org.rifidi.emulator.scripting.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.scripting.ReaderManager;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.tags.impl.C0G1Tag;
import org.rifidi.tags.impl.C1G2Tag;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Matthew Dean - matt@pramari.com
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class ReaderManagerImpl implements ReaderManager {

	/** Logger for this class. */
	private static final Logger logger = LogManager
			.getLogger(ReaderManagerImpl.class);
	/** Reader factories, set by spring. */
	private volatile Set<ReaderModuleFactory> moduleFactoryList = null;
	/** Mapping between reader name and instance. */
	private final HashMap<String, ReaderModule> readerModuleList = new HashMap<String, ReaderModule>();
	/** Counter for tag entitiy ids. */
	private AtomicLong counter = new AtomicLong(0);
	private volatile IRifidiTagService tagService;

	/**
	 * Called by spring.
	 * 
	 * @param tagService
	 *            the tagService to set
	 */
	public void setTagService(IRifidiTagService tagService) {
		this.tagService = tagService;
	}

	/**
	 * Called by spring.
	 * 
	 * @param moduleFactoryList
	 *            the moduleFactoryList to set
	 */
	public void setModuleFactoryList(Set<ReaderModuleFactory> moduleFactoryList) {
		logger.info("Setting reader module factories: " + moduleFactoryList);
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
	 * @see org.rifidi.emulator.scripting.ReaderManager#createGen1Tag(byte[])
	 */
	@Override
	public RifidiTag createGen1Tag(byte[] epcID) {
		C0G1Tag tag = new C0G1Tag();
		try {
			tag.setId(epcID);
		} catch (InvalidMemoryAccessException e) {
			logger.fatal("Couldn't create tag: " + e);
		}
		RifidiTag ret = new RifidiTag(tag);
		ret.setTagEntitiyID(counter.incrementAndGet());
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#createGen2Class1Tag(byte[],
	 * byte[], byte[])
	 */
	@Override
	public RifidiTag createGen2Class1Tag(byte[] epcID, byte[] accessPass,
			byte[] killPass) {
		RifidiTag ret = new RifidiTag(new C1G2Tag(epcID, accessPass, killPass));
		ret.setTagEntitiyID(counter.incrementAndGet());
		return ret;
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
			logger.warn("A reader named " + grph.getReaderName()
					+ " already exists.");
			return null;
		}

		for (ReaderModuleFactory rmf : moduleFactoryList) {
			if (rmf.getReaderModuleClassName()
					.equals(grph.getReaderClassName())) {
				ReaderModule newMod = rmf.createReaderModule(grph);

				// Check for problems here, maybe for IP conflicts as well
				this.readerModuleList.put(grph.getReaderName(), newMod);
				logger.info("Created reader named " + grph.getReaderName());
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
		for (ReaderModuleFactory checkfact : moduleFactoryList) {
			if (checkfact.getReaderType().equals(readerType)) {
				return checkfact.getDefaultProperties();
			}
		}
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
