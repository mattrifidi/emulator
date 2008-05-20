/*
 *  LLRPReportControllerFactory.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.report;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Makes a new instance of "LLRPReportController" for every reader that is
 * created.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class LLRPReportControllerFactory {

	/**
	 * The logger for this class.
	 */
	private static Log logger = 
		LogFactory.getLog(LLRPReportControllerFactory.class);
	
	/**
	 * 
	 */
	private static final LLRPReportControllerFactory instance = 
		new LLRPReportControllerFactory();

	/**
	 * A map that has keys of reader names, and values of ReportControllers.  
	 */
	private HashMap<String,LLRPReportController> reportMap;
	
	/**
	 * Private constructor.  
	 */
	private LLRPReportControllerFactory() {
		reportMap = new HashMap<String, LLRPReportController>();
	}
	
	/**
	 * Returns a singleton instance of the ReportControllerFactory.  
	 * 
	 * @return
	 */
	public static LLRPReportControllerFactory getInstance() {
		return instance;
	}

	/**
	 * Creates a new report controller bound to the reader's name.  Replaces 
	 * any other object that is bound to a reader of the same name.  
	 * 
	 * @param name
	 */
	public synchronized void createController(String name) {
		reportMap.put(name, new LLRPReportController());
	}
	
	/**
	 * Gets a report controller tha is bound to the given string.  Returns 
	 * null if there is no such string in the map.  
	 * 
	 * @param name
	 * @return
	 */
	public synchronized LLRPReportController getReportController(String name) {
		return reportMap.get(name);
	}
}
