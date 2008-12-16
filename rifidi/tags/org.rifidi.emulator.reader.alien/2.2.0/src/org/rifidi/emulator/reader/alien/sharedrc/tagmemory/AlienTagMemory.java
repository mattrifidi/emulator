/**
 * 
 */
package org.rifidi.emulator.reader.alien.sharedrc.tagmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.IntegerReaderProperty;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.services.tags.utils.RifidiTagMap;

/**
 * @author kyle
 * 
 */
public class AlienTagMemory implements TagMemory {

	/**
	 * The EmuLogger instance for this class.
	 */
	private static Log logger = LogFactory.getLog(AlienTagMemory.class);

	private RifidiTagMap tagList = new RifidiTagMap();

	private boolean suspended = false;

	private HashSet<TagGen> typeFilter;

	/** The PersistTime Property */
	private IntegerReaderProperty persistTimeProperty;

	/**
	 * Constuct an AlienTagMemory
	 * 
	 * @param tagTypes
	 *            an optional list of tag types that define which tag types the
	 *            tag should read
	 */
	public AlienTagMemory(TagGen... tagTypes) {

		typeFilter = new HashSet<TagGen>();
		for (TagGen t : tagTypes) {
			typeFilter.add(t);
		}
	}

	public void setPersistTime(IntegerReaderProperty persistTimeProperty) {
		this.persistTimeProperty = persistTimeProperty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#clear()
	 */
	public void clear() {
		tagList.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#disable_buffer()
	 */
	public void suspend() {
		logger.debug("Disabling Alien Tag Memory");
		suspended = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#enable_buffer()
	 */
	public void resume() {
		logger.debug("Enabling Alien Tag Memory");
		suspended = false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#getTagReport()
	 */
	public ArrayList<RifidiTag> getTagReport() {
		return getTagReport(null);

	}

	//
	public ArrayList<RifidiTag> getTagReport(Integer antennaNum) {
		logger.debug("Getting tag report.  buffer is "
				+ (suspended ? "suspended" : "enabled"));
		if (!suspended) {// iterate through list and remove tags that are too
			// old
			removeOldTags();

			// return a new list
			ArrayList<RifidiTag> returnList = tagList.getTagList();

			// if persistTime==-1, clear taglist
			if (persistTimeProperty.getValue() == -1) {
				this.tagList.clear();
			}
			if (antennaNum != null) {
				for (int i = 0; i < returnList.size(); i++) {
					if (returnList.get(i).getAntennaLastSeen() != antennaNum
							.intValue()) {
						returnList.remove(i);
					}
				}
			}
			return returnList;
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#updateMemory(java.util.Collection)
	 */
	public void updateMemory(Collection<RifidiTag> tagsToAdd) {
		logger.debug("Getting tag report.  buffer is "
				+ (suspended ? "suspended" : "enabled"));
		if (!suspended) {
			// we only need to add the new tags, because the old
			// ones will be removed when their persist time is up
			for (RifidiTag t : tagsToAdd) {
				if (typeFilter.contains(t.getTagGen())) {
					this.tagList.addTag(t);
				}
			}
		}

	}

	/**
	 * This function iterates through the tag list and removes all tags whose
	 * seen time is greater than the persist time of the reader
	 */
	private void removeOldTags() {
		int persistTime = 0;
		logger.debug("persistTime is " + this.persistTimeProperty.getValue());
		if (this.persistTimeProperty.getValue() == -1) {
			return;
		} else if (this.persistTimeProperty.getValue() <= 200) {
			// don't let persistTime be less than 200 ms, else unexpected
			// results
			// may happen (such as people not getting the tags back that are on
			// the antenna)
			persistTime = 200;
		} else {
			persistTime = this.persistTimeProperty.getValue();
		}

		int removedTags = 0;

		Collection<RifidiTag> currentTags = this.tagList.getTagSet();
		long currentTime = System.currentTimeMillis();

		for (RifidiTag t : currentTags) {
			long seenTime = currentTime - t.getLastSeenDate().getTime();
			if (seenTime > persistTime) {
				tagList.removeTag(t.getTagEntitiyID());
				removedTags++;
			}
		}
		logger.debug("Removed " + removedTags + " old tags...");
	}

	/**
	 * This method sets the tag types (eg. gen1, gen2) that we should scan. If
	 * the tag types are not listed, they will not
	 * 
	 * @param tagTypes
	 */
	public void setTagTypeSelection(HashSet<TagGen> types) {
		this.typeFilter = types;
	}

	/**
	 * This static method returns a set of integers that represent the antennas
	 * that should be scanned as defined by the antennalist variable
	 * 
	 * @param asr
	 * @return
	 */
	public static HashSet<Integer> getAntennas(AlienReaderSharedResources asr) {
		String antennaString = asr.getPropertyMap().get("antennasequence")
				.getPropertyStringValue();
		String[] splitString = antennaString.split(" ");

		HashSet<Integer> antennasToScan = new HashSet<Integer>();

		for (String ant : splitString) {
			antennasToScan.add(new Integer(ant));
		}
		return antennasToScan;
	}

}
