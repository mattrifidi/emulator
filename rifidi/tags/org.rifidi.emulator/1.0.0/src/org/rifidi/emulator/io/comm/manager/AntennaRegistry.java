/*
 *  AntennaRegistry.java
 *
 *  Created:	Mar 14, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.emulator.tags.utils.RifidiTagFactory;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class AntennaRegistry {

	// singleton pattern
	private static AntennaRegistry instance = new AntennaRegistry();

	// map for the antennas and list of tags an antenna has
	private Map<String, ArrayList<RifidiTag>> antennaMap = new HashMap<String, ArrayList<RifidiTag>>();

	/**
	 * private constructor singleton pattern
	 * 
	 */
	private AntennaRegistry() {
	}

	/**
	 * Singleton pattern
	 * 
	 * @return
	 */
	public static AntennaRegistry getInstance() {
		return instance;
	}

	/**
	 * Get the list of tags visible to a certain antenna
	 * 
	 * @param antennaName
	 *            the name of the antenna
	 * @return a list containing all the tags visible to the antenna
	 */
	public List<RifidiTag> getTagList(String antennaName) {
		List<RifidiTag> list = antennaMap.get(antennaName);
		if (list == null) {
			return new ArrayList<RifidiTag>();
		}
		return list;
	}

	/**
	 * Add a tag to the list of tags an antenna can see
	 * 
	 * @param antennaName
	 *            name of the antenna
	 * @param data
	 *            data of the tag to add
	 * @param type
	 *            the type of the tag (GEN1 or GEN2)
	 */
	public void addTag(String antennaName, String data, String type) {
		// Get the list of tags
		ArrayList<RifidiTag> tags = antennaMap.get(antennaName);

		// if no list add a new one
		if (tags == null) {
			tags = new ArrayList<RifidiTag>();
			antennaMap.put(antennaName, tags);
		}
		// create a gen1 tag
		if ("GEN1".equals(type)) {
			RifidiTag t = RifidiTagFactory.createTag(TagGen.GEN1,
					ByteAndHexConvertingUtility.fromHexString(data));
			tags.add(t);
		}
		// create a gen2 tag
		else {
			RifidiTag t = RifidiTagFactory.createTag(TagGen.GEN2,
					ByteAndHexConvertingUtility.fromHexString(data));
			tags.add(t);
		}
	}

	/**
	 * Remove a tag from the list of visible tags
	 * 
	 * @param antennaName
	 *            the antenna we want to remove the tag from
	 * @param data
	 *            the data the tag we are looking for has
	 */
	public void removeTag(String antennaName, String data) {
		ArrayList<RifidiTag> tags = antennaMap.get(antennaName);
		for (RifidiTag tag : tags) {
			String s = ByteAndHexConvertingUtility.toHexString(tag.getTag().readId());
			if (s.equals(data)) {
				tags.remove(tag);
				break;
			}
		}
	}

	/**
	 * Remove all tags from an antenna
	 * 
	 * @param antennaName
	 *            the antenna to purge
	 */
	public void purgeTags(String antennaName) {
		antennaMap.remove(antennaName);
	}
}
