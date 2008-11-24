/**
 * 
 */
package org.rifidi.emulator.reader.properties.xml;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that holds many readerProperties
 * 
 * @author kyle
 */
public class ReaderXMLPropertyHolder {
	
	private ArrayList<ReaderXMLProperty> readerXMLPropertyList;
	
	/**
	 * Adds a property to the reader xml propety list.  
	 * 
	 * @param property
	 */
	public void addReaderXMLProperty(ReaderXMLProperty property) {
		readerXMLPropertyList.add(property);
	}
	
	/**
	 * Returns a list of readerXMLProperties
	 * 
	 * @return
	 */
	public Collection<ReaderXMLProperty> getReaderXMLPropertyList() {
		return readerXMLPropertyList;
	}
}
