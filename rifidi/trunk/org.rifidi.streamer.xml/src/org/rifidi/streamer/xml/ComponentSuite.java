/*
 *  ComponentSuite.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.streamer.xml.components.ReaderComponent;

/**
 * The ComponentSuite is a collection of all Components in a MetaFile
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class ComponentSuite {

	/**
	 * List of components in this suite
	 */
	private List<ReaderComponent> readerComponents;

	/**
	 * @return the readerComponents
	 */
	public List<ReaderComponent> getReaderComponents() {
		return readerComponents;
	}

	/**
	 * @param readerComponents
	 *            the readerComponents to set
	 */
	public void setReaderComponents(List<ReaderComponent> readerComponents) {
		this.readerComponents = readerComponents;
	}

}
