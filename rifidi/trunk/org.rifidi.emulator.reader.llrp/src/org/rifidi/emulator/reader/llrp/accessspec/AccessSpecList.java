/*
 *  AccessSpecList.java
 *
 *  Created:	Oct 9, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.accessspec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This class is a global list of accessSpecs that have been added
 * 
 * @author kyle
 * 
 */
public class AccessSpecList {

	/**
	 * Internal list to store access specs
	 */
	private ArrayList<_AccessSpec> specs;

	/**
	 * Hasmmap used for efficiency add and remove reasons
	 */
	private HashMap<Integer, _AccessSpec> specHashMap;
	
	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(AccessSpecList.class);



	/**
	 * Construct a new AccessSpec list
	 * 
	 */
	public AccessSpecList() {
		specs = new ArrayList<_AccessSpec>();
		specHashMap = new HashMap<Integer, _AccessSpec>();
	}

	/**
	 * 
	 * @param i
	 *            index
	 * @return the Access spec at index i
	 */
	public _AccessSpec getAccessSpecAt(int i) {
		return specs.get(i);
	}

	/**
	 * 
	 * @return the current size of the list
	 */
	public int numAccessSpecs() {
		return specs.size();
	}

	/**
	 * Adds an access spec to the list
	 * 
	 * @param specToAdd
	 *            the spec to add
	 * @param ltkAccessSpec
	 *            the incoming accessspec. Save it for a
	 *            GET_ACCESSSPECS_RESPONSE message
	 * @return true if operation succeeded, false otherwise
	 */
	public boolean addAccessSpec(_AccessSpec specToAdd) {
		logger.debug("in addAccessSpec method ");
		int specID = specToAdd.getSpecID();
		if (!specHashMap.containsKey(specID)) {
			if (this.specs.add(specToAdd)) {
				logger.debug("adding access spec");
				specHashMap.put(specID, specToAdd);
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove a spec from the list
	 * 
	 * @param IDofSpecToRemove
	 *            The SpecID of the access spec to remove. Note that it is not
	 *            the same thing as the index of the spec.
	 * @return True if the spec was removed, false otherwise.
	 */
	public boolean removeAccessSpec(int IDofSpecToRemove) {
		if (specHashMap.containsKey(IDofSpecToRemove)) {
			_AccessSpec spec = specHashMap.get(IDofSpecToRemove);
			if (this.specs.remove(spec)) {
				logger.debug("AccessSpec delted");
				specHashMap.remove(IDofSpecToRemove);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param tag
	 *            The tag to match
	 * @return The first accessspec in the list that matches the tag
	 */
	public _AccessSpec getFirstAccessSpecthatMatches(RifidiTag tag) {
		Iterator<_AccessSpec> iter = specs.iterator();

		while (iter.hasNext()) {
			_AccessSpec current = iter.next();
			if (current.shouldPerformOperation(tag)) {
				return current;
			}
		}

		return null;
	}

	/**
	 * Returns an access spec with a certain ID
	 * 
	 * @param accessSpecID
	 * @return
	 */
	public _AccessSpec getAccessSpec(int accessSpecID) {
		return this.specHashMap.get(accessSpecID);
	}
	
	public void clear(){
		this.specs.clear();
		this.specHashMap.clear();
	}

}
