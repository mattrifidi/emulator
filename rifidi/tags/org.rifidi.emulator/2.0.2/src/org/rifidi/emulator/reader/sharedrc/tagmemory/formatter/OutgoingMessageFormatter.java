/*
 *  @(#)OutgoingMessageFormatter.java
 *
 *  Created:	June 7, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.tagmemory.formatter;

import java.util.Collection;

import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * This interface provides a method with which to format an outgoing tag list.
 * @author Kyle Neumeier
 *
 */
public interface OutgoingMessageFormatter {
	
	/**
	 * 
	 * @param tags A collection of tags to be formatted
	 * @param asr A reference to the shared resources.  Helpful if you need some information to know how to format
	 * @param extraInformation A general placeholder for extra information that the formatter might need
	 * @return The output in the form of a string
	 */
	public String formatMessage(Collection<RifidiTag> tags, AbstractReaderSharedResources asr, String extraInformation);

}
