/*
 *  @(#)Antenna.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.sharedrc.radio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.utils.RifidiTagMap;




/**
 * This is representing an rifid antenna. Basically this stores all the Tags in
 * the field of this antenna.
 * 
 * It is to be used with the new radio, and is an observable. Tags should be
 * directly added to the antenna, and the antenna will notify the radio when the
 * tags on the antenna have changed.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle
 * @author Matt Dean
 * 
 */
public class Antenna extends Observable {

	/**
	 * The log4j logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(Antenna.class);
	
	private static Log eventLogger = LogFactory.getLog("EventLogger");

	/**
	 * This is the Name of the Antenna ( "antenna[index]" e.g. antenna1)
	 */
	private Integer ID;
	
	/**
	 * The name of the reader this antenna is attached to
	 */
	private String readerName;

	/**
	 * Stores the Tags in the field of this antenna
	 */
	private RifidiTagMap tagList = new RifidiTagMap();

	public Antenna(int id, String readerName) {
		this.ID = id;
		this.readerName = readerName;
	}

	/**
	 * Returns the tags seen by the specified antenna
	 * 
	 * @return the list of tags
	 */
	public Collection<RifidiTag> getTagList() {
		return tagList.getTagSet();
	}

	/**
	 * Get the ID of this antenna
	 * 
	 * @return
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Remove a collection of tags from this anntenna. It will remove each tag
	 * individually, then notify the listening radio that the antenna has
	 * changed. It updates the tag radio twice, because an update will signal a
	 * scan to happen. The first scan is necessary, because to properly keep
	 * track of the state of the tags (when thier last scan time was, etc), the
	 * antenna should be scanned right before it removes tags. This represents
	 * the radio as having seen them before they were deleted.
	 * 
	 * @param ids
	 * 
	 * @return returns true if all tags have been removed, false if at least one
	 *         tag failed. Note that the successful tags still will have been
	 *         deleted
	 */
	public boolean removeTags(Collection<Long> ids) {
		// tell the radio to scan before tag removal
		this.setChanged();
		this.notifyObservers();
		
		//remove tags
		boolean success = true;
		for (Long id : ids) {
			if (!tagList.removeTag(id)) {
				success = false;
			}

		}
		eventLogger.info("[TAG EVENT]: "+ids.size() + " tags removed from antenna " + this.ID + " on reader " + readerName);
		// notify radio that tags on radio have changed
		this.setChanged();
		this.notifyObservers();
		return success;
	}

	private boolean addTag(RifidiTag t) {
		return this.tagList.addTag(t);
	}

	/**
	 * Adds a collection of tags to the antenna. It adds each tag, then notifies
	 * the radio that the antenna has changed.
	 * 
	 * @param tagsToAdd
	 * @return true if all tags have been sucessfully added
	 */
	public boolean addTags(Collection<RifidiTag> tagsToAdd) {
		boolean success = true;
		for (RifidiTag t : tagsToAdd) {
			if (!addTag(t))
				success = false;
		}
		eventLogger.info("[TAG EVENT]: "+tagsToAdd.size() + " tags added to antenna " + this.ID + " on reader " + readerName);
		// Tell radio that the tags on the antenna have changed
		this.setChanged();
		this.notifyObservers();
		return success;
	}
	
	public void rehash(){
		ArrayList<RifidiTag> tags = this.tagList.getTagList();
		RifidiTagMap tm = new RifidiTagMap();
		for(RifidiTag t : tags){
			tm.addTag(t);
		}
		this.tagList = tm;
	}

}
