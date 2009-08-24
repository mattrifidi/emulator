/**
 * 
 */
package org.rifidi.emulator.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
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

	@Override
	public String getXMLDescription(String readerType) {
		for (ReaderModuleFactory factory : this.moduleFactoryList) {
			if (factory.getReaderType().equals(readerType)) {
				return factory.getReaderXMLDescription();
			}
		}
		logger.warn("No factory found for " + readerType);
		return null;
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

	public List<RifidiTag> getTagList(String readerID, int antennaNum) {
		try {
			ReaderModule reader = this.readerModuleList.get(readerID);
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
		rm.turnOff();
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
		if (this.readerModuleList.containsKey(readerID))
			this.readerModuleList.get(readerID).turnOn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.scripting.ReaderManager#stop(java.lang.String)
	 */
	@Override
	public void stop(String readerID) {
		if (this.readerModuleList.containsKey(readerID))
			this.readerModuleList.get(readerID).turnOff();
	}

	public void suspend(String readerID) {
		if (this.readerModuleList.containsKey(readerID)) {
			this.readerModuleList.get(readerID).suspend();
		}
	}

	public void resume(String readerID) {
		if (readerModuleList.containsKey(readerID)) {
			readerModuleList.get(readerID).resume();
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#getAllCategories(java.lang
	 * .String)
	 */
	@Override
	public Collection<String> getAllCategories(String readerID) {
		if (readerModuleList.containsKey(readerID)) {
			return readerModuleList.get(readerID).getAllCategories();
		}
		return new HashSet<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#getCommandActionTagsByCategory
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getCommandActionTagsByCategory(String readerID,
			String category) {
		if (readerModuleList.containsKey(readerID)) {
			Map<String, String> commandList = new HashMap<String, String>();
			for (CommandObject commandObj : readerModuleList.get(readerID)
					.getCommandsByCategory(category)) {
				commandList.put(commandObj.getDisplayName(), commandObj
						.getActionTag());
			}
			return commandList;
		}
		return new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#getGPIList(java.lang.String)
	 */
	@Override
	public List<Integer> getGPIList(String readerID) {
		if (readerModuleList.containsKey(readerID)) {
			return readerModuleList.get(readerID).getGPIPortNumbers();
		}
		return new ArrayList<Integer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#getGPOList(java.lang.String)
	 */
	@Override
	public List<Integer> getGPOList(String readerID) {
		if (readerModuleList.containsKey(readerID)) {
			return readerModuleList.get(readerID).getGPOPortNumbers();
		}
		return new ArrayList<Integer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#getReaderProperty(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public String getReaderProperty(String readerID, String propertyName) {
		if (readerModuleList.containsKey(readerID)) {
			return readerModuleList.get(readerID).getSharedResources()
					.getPropertyMap().get(propertyName)
					.getPropertyStringValue();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.ReaderManager#setReaderProperty(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean setReaderProperty(String readerID, String propertyName,
			String propertyValue) {
		if (readerModuleList.containsKey(readerID)) {
			readerModuleList.get(readerID).getSharedResources()
					.getPropertyMap().get(propertyName).setPropertyValue(
							propertyValue);
			return true;
		}
		return false;
	}

	@Override
	public void setReaderCallback(String readerID,
			ClientCallbackInterface callback) {
		if (this.readerModuleList.containsKey(readerID)) {
			readerModuleList.get(readerID).getSharedResources()
					.setCallbackManager(callback);
		}
	}

}
