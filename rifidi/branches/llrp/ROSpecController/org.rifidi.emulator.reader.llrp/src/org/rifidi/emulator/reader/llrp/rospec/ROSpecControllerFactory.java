/*
 *  ROSpecControllerFactory.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.rospec;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Matthew Dean - matt@pramari.com
 */
public class ROSpecControllerFactory {
	/**
	 * The logger for this class.
	 */
	private static Log logger = 
		LogFactory.getLog(ROSpecControllerFactory.class);
	
	/**
	 * 
	 */
	private static final ROSpecControllerFactory instance = 
		new ROSpecControllerFactory();

	/**
	 * A map that has keys of reader names, and values of ReportControllers.  
	 */
	private HashMap<String,ROSpecController> reportMap;
	
	/**
	 * Private constructor.  
	 */
	private ROSpecControllerFactory() {
		reportMap = new HashMap<String, ROSpecController>();
	}
	
	/**
	 * Returns a singleton instance of the ROSpecControllerFactory.  
	 * 
	 * @return
	 */
	public static ROSpecControllerFactory getInstance() {
		return instance;
	}

	/**
	 * Creates a new report controller bound to the reader's name.  Replaces 
	 * any other object that is bound to a reader of the same name.  
	 * 
	 * @param name
	 */
	public synchronized void createController(String name) {
		reportMap.put(name, new ROSpecController());
	}
	
	/**
	 * Gets a report controller tha is bound to the given string.  Returns 
	 * null if there is no such string in the map.  
	 * 
	 * @param name
	 * @return
	 */
	public synchronized ROSpecController getReportController(String name) {
		return reportMap.get(name);
	}
}
