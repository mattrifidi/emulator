/*
 *  ReaderComponent.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.components;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

/**
 * This is the container for holding the Information about a reader component.
 * It makes use of the UIReader.class to store the RMI Interface to control the
 * reader
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 */
@XmlRootElement
public class ReaderComponent {

	/**
	 * ID of this Component
	 */
	private int ID;

	/**
	 * the virtual reader of this readerComponent
	 */
	private GeneralReaderPropertyHolder reader;

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param id
	 *            the iD to set
	 */
	@XmlAttribute
	public void setID(int id) {
		ID = id;
	}

	/**
	 * @return the reader
	 */
	public GeneralReaderPropertyHolder getReader() {
		return reader;
	}

	/**
	 * @param reader
	 *            the reader to set
	 */
	@XmlElement
	public void setReader(GeneralReaderPropertyHolder grph) {
		this.reader=grph;
	}
}
