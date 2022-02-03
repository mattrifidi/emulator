package org.rifidi.ui.ide.configuration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A bean that maps a set of tags to an antenna number
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AntennaTagMap {

	private HashMap<Integer, TagIDList> antennaTagMap = new HashMap<Integer, TagIDList>();

	/**
	 * @return the antennaTagMap
	 */
	public HashMap<Integer, TagIDList> getAntennaTagMap() {
		return antennaTagMap;
	}

	/**
	 * @param antennaTagMap
	 *            the antennaTagMap to set
	 */
	public void setAntennaTagMap(HashMap<Integer, TagIDList> antennaTagMap) {
		this.antennaTagMap = antennaTagMap;
	}

	public void addEntry(Integer anteannaID, ArrayList<Long> tags) {
		TagIDList list = new TagIDList();
		list.setTagIDList(tags);
		antennaTagMap.put(anteannaID, list);
	}

	public ArrayList<Long> getEntry(Integer antennaID) {
		return antennaTagMap.get(antennaID).getTagIDList();
	}

}
