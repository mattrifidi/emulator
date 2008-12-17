/*
 *  TagEvent.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.antennafield;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.services.core.events.WorldEvent;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.tags.impl.RifidiTag;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 26, 2008
 * 
 */
public class TagEvent extends WorldEvent {
	/**
	 * Empty default constructor.
	 */
	public TagEvent() {
		this.color = Colors.GREEN;
	}

	/**
	 * Constructor.
	 * 
	 * @param rifidiTag
	 * @param readerIface
	 * @param antennaNum
	 * @param appear
	 */
	public TagEvent(RifidiTag rifidiTag,
			ReaderModuleManagerInterface readerIface, int antennaNum,
			boolean appear) {
		this.color = Colors.GREEN;
		this.rifidiTag = rifidiTag;
		this.readerIface = readerIface;
		this.antennaNum = antennaNum;
		this.appear = appear;
	}

	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(TagEvent.class);
	/**
	 * The tag that appeared.
	 */
	private RifidiTag rifidiTag;
	/**
	 * The RMI interface of the reader.
	 */
	private ReaderModuleManagerInterface readerIface;
	/**
	 * The antenna the event happened on.
	 */
	private int antennaNum;
	/**
	 * true if the tag appeared, false if it disappeared.
	 */
	private boolean appear = true;

	/**
	 * @return the rifidiTag
	 */
	public RifidiTag getRifidiTag() {
		return this.rifidiTag;
	}

	/**
	 * @param rifidiTag
	 *            the rifidiTag to set
	 */
	public void setRifidiTag(RifidiTag rifidiTag) {
		this.rifidiTag = rifidiTag;
	}

	/**
	 * @return the readerIface
	 */
	public ReaderModuleManagerInterface getReaderIface() {
		return this.readerIface;
	}

	/**
	 * @param readerIface
	 *            the readerIface to set
	 */
	public void setReaderIface(ReaderModuleManagerInterface readerIface) {
		this.readerIface = readerIface;
	}

	/**
	 * @return the antennaNum
	 */
	public int getAntennaNum() {
		return this.antennaNum;
	}

	/**
	 * @param antennaNum
	 *            the antennaNum to set
	 */
	public void setAntennaNum(int antennaNum) {
		this.antennaNum = antennaNum;
	}

	/**
	 * @return the appear
	 */
	public boolean isAppear() {
		return this.appear;
	}

	/**
	 * @param appear
	 *            the appear to set
	 */
	public void setAppear(boolean appear) {
		this.appear = appear;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.events.WorldEvent#toString()
	 */
	@Override
	public String toString() {
		if (isAppear()) {
			try {
				return "Tag " + rifidiTag + " has appeared on reader "
						+ readerIface.getReaderProperties().getReaderName()
						+ " on antenna " + antennaNum;
			} catch (Exception e) {
				logger.error(e);
				return "Unable to retrive data: " + e;
			}
		}
		try {
			return "Tag " + rifidiTag + " has disappeared from reader "
					+ readerIface.getReaderProperties().getReaderName()
					+ " from antenna " + antennaNum;
		} catch (Exception e) {
			logger.error(e);
			return "Unable to retrive data: " + e;
		}
	}

}
