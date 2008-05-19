/**
 * 
 */
package org.rifidi.emulator.reader.epc.sharedrc.memory;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.RifidiTagMap;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * 
 * 
 * @author matt
 */
public class EPCTagMemory implements TagMemory {

	/**
	 * The EmuLogger instance for this class.
	 */
	private static Log logger = LogFactory.getLog(EPCTagMemory.class);

	private RifidiTagMap tagList = new RifidiTagMap();

	private boolean suspended = false;

	public EPCTagMemory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#clear()
	 */
	public void clear() {
		logger.debug("clearing the tag list");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#disable_buffer()
	 */
	public void resume() {
		suspended = false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#enable_buffer()
	 */
	public void suspend() {
		suspended = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#getTagReport()
	 */
	public Collection<RifidiTag> getTagReport() {
		if (!suspended) {
			Collection<RifidiTag> tags = tagList.getTagList();
			tagList.clear();
			return tags;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.NewTagMemory#updateMemory(java.util.Collection)
	 */
	public void updateMemory(Collection<RifidiTag> tagsToAdd) {
		if (!suspended) {
			tagList.addTags(tagsToAdd);
			for (RifidiTag i : tagsToAdd) {
				logger.debug("Tag seen on latest scan: " + i);
			}
		}

	}

}
