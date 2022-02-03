package org.rifidi.ui.ide.configuration;

import java.util.HashMap;

/**
 * A bean class that maps an AntennaTagMap to a readerName
 * 
 * @author Kyle Neumeier- kyle@prmari.com
 * 
 */
public class ReaderAntennaTagMap {
	private HashMap<String, AntennaTagMap> readerAntennaTagMap = new HashMap<String, AntennaTagMap>();

	/**
	 * @return the antennaTagMap
	 */
	public HashMap<String, AntennaTagMap> getAntennaTagMap() {
		return readerAntennaTagMap;
	}

	/**
	 * @param antennaTagMap
	 *            the antennaTagMap to set
	 */
	public void setAntennaTagMap(HashMap<String, AntennaTagMap> antennaTagMap) {
		this.readerAntennaTagMap = antennaTagMap;
	}

	public void addEntry(String readerName, AntennaTagMap antennaTagMap) {
		this.readerAntennaTagMap.put(readerName, antennaTagMap);
	}

	public AntennaTagMap getEntry(String readerName) {
		return readerAntennaTagMap.get(readerName);
	}

}
