/*
 *  ReaderSource.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.source;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class ReaderSource {
	
	/**
	 * The name of this ReaderSource
	 */
	private String name;
	
	/**
	 * A list of the read points that this source has.  
	 */
	private ArrayList<String> readPoints;
	
	/**
	 * A Map of TagSelectors bound to their names.  
	 */
	private HashMap<String,TagSelector> tagSelectors;
	
	/**
	 * Constructor for the ReaderSource.  
	 * 
	 * @param name
	 */
	public ReaderSource(String name) {
		this.name = name;
		this.readPoints = new ArrayList<String>();
		this.tagSelectors = new HashMap<String, TagSelector>();
	}

	/**
	 * Gets the name of this ReaderSource.  
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this ReaderSource.  
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the list of ReadPoints that this Source has.  
	 * 
	 * @return the readPoints
	 */
	public ArrayList<String> getReadPoints() {
		return readPoints;
	}

	/**
	 * Sets the ReadPoints that this ReaderSource has.  
	 * 
	 * @param readPoints the readPoints to set
	 */
	public void setReadPoints(ArrayList<String> readPoints) {
		this.readPoints = readPoints;
	}
	
	/**
	 * Adds a read point to the Source.  
	 * 
	 * @param arg
	 */
	public void addReadPoint(String arg) {
		this.readPoints.add(arg);
	}
	
	/**
	 * Removes a ReadPoint from the source.  
	 * 
	 * @param arg
	 */
	public void removeReadPoint(String arg) {
		for(int i=0;i<readPoints.size();i++) {
			if(arg.equals(readPoints.get(i))) {
				readPoints.remove(i);
			}
		}
	}
	
	/**
	 * Removes all read points that this ReaderSource has.
	 */
	public void removeAllReadPoints() {
		this.readPoints.clear();
	}

	/**
	 * Returns the Map of TagSelectors
	 * 
	 * @return the tagSelectors
	 */
	public HashMap<String, TagSelector> getTagSelectors() {
		return tagSelectors;
	}
	
	/**
	 * Add a list of selectors to the list of TagSelectors.   
	 */
	public void addTagSelectors(ArrayList<TagSelector> selectorList) {
		for(TagSelector i:selectorList) {
			tagSelectors.put(i.getName(), i);
		}
	}
	
	/**
	 * Gets a selector of a specified name.  Returns null if no selector is specified.  
	 * 
	 * @param name
	 * @return
	 */
	public TagSelector getTagSelector(String name) {
		return tagSelectors.get(name);
	}
	
	
	/**
	 * Remove the tag selectors given in the arguments.
	 * 
	 * @param selectorList
	 */
	public void removeTagSelectors(ArrayList<String> selectorList) {
		for(String i:selectorList) {
			tagSelectors.remove(i);
		}
	}
}
