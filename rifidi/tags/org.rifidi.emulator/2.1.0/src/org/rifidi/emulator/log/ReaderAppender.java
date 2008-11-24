/*
 *  ReaderFileAppender.java
 *
 *  Created:	Mar 20, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * This Log4j appender is used to store the logs for the consoleView. For each
 * reader a separate file is created in which only the log statements with the
 * INFO level are written.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderAppender extends AppenderSkeleton {

	private int maxCacheLines = 2000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append(LoggingEvent event) {
		// strip "console." from the string, leaving only the reader name
		String readerName = event.getLoggerName().substring(8);

		// we are only interested on INFO-level messages
		if (event.getLevel().equals(Level.INFO)) {
			ReaderLogCacheSingleton log = ReaderLogCacheSingleton.getInstance();
			log.setMaxCacheLines(maxCacheLines);
			String type = "";
			for (String line : event.getRenderedMessage().split("\n")) {
				if (isSpecialTypeStart(line)) {
					type = line.split(">")[0] + ">";
					log.addMessage(readerName, line + "\n");
				}else if(isSpecialTypeEnd(line)){
					type="";
					log.addMessage(readerName, "\n");
				}else{
					log.addMessage(readerName, type + line + "\n");
				}
			}
		}
	}
	
	private boolean isSpecialTypeStart(String line){
		if(line.startsWith("<INPUT>",0)){
			return true;
		}else if(line.startsWith("<OUTPUT>", 0)){
			return true;
		}else if(line.startsWith("<STARTUP>", 0)){
			return true;
		}else return false;
	}
	
	private boolean isSpecialTypeEnd(String line){
		if(line.startsWith("</INPUT>",0)){
			return true;
		}else if(line.startsWith("</OUTPUT>", 0)){
			return true;
		}else if(line.startsWith("</STARTUP>", 0)){
			return true;
		}else return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.AppenderSkeleton#close()
	 */
	@Override
	public void close() {
		// not needed anymore because log is only in memory
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
	 */
	@Override
	public boolean requiresLayout() {
		return false;
	}

	/**
	 * @return get the maxCacheLines
	 */
	public int getMaxCacheLines() {
		return maxCacheLines;
	}

	/**
	 * @param maxCacheLines
	 *            to maxCacheLines set
	 */
	public void setMaxCacheLines(int maxCacheLines) {
		this.maxCacheLines = maxCacheLines;
	}

}
