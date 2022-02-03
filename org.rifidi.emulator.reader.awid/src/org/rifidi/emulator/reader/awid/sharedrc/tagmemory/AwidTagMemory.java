/**
 * 
 */
package org.rifidi.emulator.reader.awid.sharedrc.tagmemory;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.services.tags.utils.RifidiTagMap;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author matt
 * 
 */
public class AwidTagMemory implements TagMemory {

	/**
	 * The EmuLogger instance for this class.
	 */
	private static Log logger = LogFactory.getLog(AwidTagMemory.class);

	private RifidiTagMap tagList = new RifidiTagMap();

	private boolean suspended= false;

	public AwidTagMemory() {

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
		} else
			return null;
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
